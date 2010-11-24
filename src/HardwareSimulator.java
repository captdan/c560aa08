import java.io.BufferedReader;
import java.io.DataInputStream;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class HardwareSimulator {

	static String[] registers = new String[15];
	static String[] MEM = new String[65536];
	static ArrayList<String> LoadModule = new ArrayList<String>();
	static int OPCODE = 0;
	ArrayList<Error> Errors = new  ArrayList<Error>();
	static boolean debug = false;
	static int PC = 0;
	static int NPIC = 0;
	static String binaryInstruction = "";
	public static ArrayList<String> GSymbTableArray = new ArrayList<String>();
	public static GlobalSymbolTable GSymbTable = new GlobalSymbolTable();
	public static int excecStart = 0;
	public static int initialLoadAddr = 0;
	public static int CompleteModuleLength = 0;
	public static String LHfirstModuleName;
	public static ALU ALU = new ALU();
	public static int EFFADDR = 0;
 	public static enum instructionType
	{
		 R, S, J, IO, I, NOTINSTRUCTION;
	}
	/**
	 * Holds all symbols encountered while passing through source code.
	 */

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		initializeMEM();
		initializeREGS();
		FillGlobalSymbTable();
		instructionType type;
	
			if(args.length == 1 )
		{
		
				LoadModule = readFileToArrayList(args[0]);
		
		
		}
		else{
			System.out.println("Unspecified Load File name");
			System.exit(2);
		}
	
		
		 // Fill MEM array
		 
		putToMEM();
		NPIC = PC + 1;
		
		type = null;
		
		for(PC = excecStart; PC < initialLoadAddr + CompleteModuleLength - 1; PC++)
		{
			
			try
			{
				binaryInstruction =	hexToBin(MEM[PC]);
				type = SelectInstructionType(binaryInstruction);
				try
				{
					
					OPCODE = Integer.parseInt(binaryInstruction.substring(0,6),2);
					
				}
				catch(NumberFormatException e)
				{
					// Handle Invalid Opcode
					System.err.println("Invalid OPCODE....proceeding to next instruction.");
					return;
				}
			}
			catch(NumberFormatException e)
			{
				
				System.err.println("Invalid Machine Hex....Proceeding to next instruction.");
				type = instructionType.NOTINSTRUCTION;
				
			}
			
			switch (type)
			{
				case S: evaluateSType(String.valueOf(OPCODE),binaryInstruction);
					break;
				case R:  evaluateRType(String.valueOf(OPCODE),binaryInstruction);
					break;
				case J:  
				DumpInfo();

				if(debug)
				{
					DumpArray();
				}
				
				System.exit(1);
				
				break;
				case IO: evaluateIOType(String.valueOf(OPCODE),binaryInstruction);
					break;
				case NOTINSTRUCTION: ;
					break;
			}
			
			NPIC++;
			
		}
		
		DumpArray();
	}

	/**
	 * Module Name: SelectInstructionType
	 * Description: Looks at a Binary Instruction and determines the Instruction Type 
	 * Input Params: Valid Binary Instruction
	 * Output Params: instructionType Enum
	 * Error Conditions Tested: 
	 * Error Messages Generated: Invalid OPCODE, Invalid Instruction
	 * Original Author: Kermit Stearns
	 * Date of Installation: 11/21/2010
	 * Modifications:
	 * @param binaryInstruction2 Valid Binary Instruction 
	 * @return An instructionType Enum.
	 */
	private static instructionType SelectInstructionType(String binaryInstruction2) {
		instructionType result;
		try
		{
			
			OPCODE = Integer.parseInt(binaryInstruction2.substring(0,6),2);
			
		}
		catch(NumberFormatException e)
		{
			// Handle Invalid Opcode
			System.err.println("Invalid OPCODE....proceeding to next instruction.");
			return instructionType.NOTINSTRUCTION;
		}
		
		// At this point, OPCODE is the Hex representation of the opcode
		
		if((16 <= OPCODE) && (OPCODE <= 23) || (OPCODE >= 52) && (OPCODE <= 55) || (OPCODE == 61) || (OPCODE == 30) || (OPCODE == 31) )
		{
			result = instructionType.I;
		}
		else if ((OPCODE >= 10) && (OPCODE <= 13))
		{
			result = instructionType.IO;
		}
		else if (OPCODE == 8)
		{
			result = instructionType.J;
		}
		else if (((OPCODE >= 1) && (OPCODE <= 3)) || ((OPCODE == 63)))
		{
			result = instructionType.R;
		}
		else if(((OPCODE >= 32) && (OPCODE <= 36)) || (OPCODE == 39)|| (OPCODE == 48) || (OPCODE == 49) || ((OPCODE >= 56) && (OPCODE <= 59)) || (OPCODE == 6) || (OPCODE <= 7) || ((OPCODE >= 26) && (OPCODE <= 29)))
		{
			result = instructionType.S;
		}
		else{
			result = instructionType.NOTINSTRUCTION;
			System.err.println("Not A Valid Instruction...Proceeding to Next Instruction.");
			
		}
		
		return result;
	}

	/**
	 * Module Name: InitializeREGS
	 * Description: Sets all registers to 0
	 * Input Params: N/A
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Oscar Flores
	 * Date of Installation: 11/22/2010
	 * Modifications:
	 */
	private static void initializeREGS() {
		for(int i = 0; i < registers.length; i++){
			registers[i] = "00000000";
		}
		
	}

	/**
	 * Module Name: initializeMEM
	 * Description: loads entire MEM array with HALT instruction followed by integer value of the MEM location
	 * Input Params: N/A
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Oscar Flores
	 * Date of Installation: 11/23
	 * Modifications: Modified to load with HALTs instead of 0s
	 */
	private static void initializeMEM() {
		for(int i = 0; i < MEM.length; i++){
			//These are always the leading 4 hex digits of a Halt instruction
			MEM[i] = "2000";
			//This uses the integer value of the MEM location to calculate the last 4 hex digits of the HALT instruction for each MEM
			MEM[i] += SixteenBitBinaryToFourHexDigits(padZeros16Bits(Integer.toBinaryString(i)));
			 
		}
		
	}

	/**
	 * Module Name: FillGlobalSmbTable
	 * Description: Creates our Global Symbol Table
	 * Input Params:
	 * Output Params:
	 * Error Conditions Tested:
	 * Error Messages Generated:
	 * Original Author:
	 * Date of Installation:
	 * Modifications:
	 */
	private static void FillGlobalSymbTable() {
		for(int i =0; i<GSymbTableArray.size(); i++){
			String Line = GSymbTableArray.get(i);
			StringTokenizer st = new StringTokenizer(Line, " \t |");
			if(st.countTokens() == 4){
				String label = st.nextToken();
				String assemblerAddr = st.nextToken(); 
				String LinkerAddr = st.nextToken();
				String LengthOfModule = st.nextToken();
				GSymbTable.addSymbol( label,  assemblerAddr,  LinkerAddr,  LengthOfModule);
			}
			else{
				System.out.println("Error: Missing element in global symbol table");
			}
			
		}
		
	}

	/**
	 * Module Name: putToMEM
	 * Description: Inputs the LoadModule from Linker into our Memory Array (MEM)
	 * Input Params: The .txt name from the LoadModule specified from the command line
	 * Output Params: N/A
	 * Error Conditions Tested: Invalid Load Module (both file specified and interna structure of load module)
	 * Error Messages Generated: Invalid LoadModule specified, inproperly formatted header,text, or end records in LoadModule
	 * Original Author: Oscar Flores
	 * Date of Installation: 11/21/2010
	 * Modifications:
	 * @param loadModule2
	 */
	private static void putToMEM() {
		try{
			
		
			String LH = LoadModule.get(0);
			StringTokenizer LHTokenizer = new StringTokenizer(LH, "|");
			
			if(LHTokenizer.countTokens() == 11)
				{
				
					String H = LHTokenizer.nextToken();
					String exStart = LHTokenizer.nextToken();
					excecStart = Integer.parseInt(exStart);
					//System.out.println(exectStart);
					LHfirstModuleName = LHTokenizer.nextToken();
					String LengthOfModule = LHTokenizer.nextToken();
					
					CompleteModuleLength = Integer.parseInt(LengthOfModule);
					//System.out.println(CompleteModuleLength);
					String IniLodAddr = LHTokenizer.nextToken();
					initialLoadAddr = Integer.parseInt(IniLodAddr);
					PC = initialLoadAddr;
					String Date = LHTokenizer.nextToken();
					String Time = LHTokenizer.nextToken();
					String SAL = LHTokenizer.nextToken();
					String version = LHTokenizer.nextToken();
					String revision = LHTokenizer.nextToken();
					String firstModuleName2 = LHTokenizer.nextToken();
				}
			else
				{
				
					System.err.println("Unable to load Load Module:");
				}
			
			String LT = LoadModule.get(1);
			//System.out.println(LT);
			StringTokenizer st = new StringTokenizer(LT, "|");
			for(int i = 2; st.nextToken().equals("LT") ; i++)
			{
				
				if(st.countTokens() == 4)
				{
					String loardAddr = st.nextToken();
						if(((isValidHex(loardAddr)&& (loardAddr.length() == 4))) == false)
						{
							System.err.println("Unrecoverable problem with Load Module:");
						}
					// get Load address for MEM	
					int loadAddsDecimal = Integer.parseInt(loardAddr, 16);
					
					String debug = st.nextToken();
						if((debug.equals("Y") || (debug.equals("N"))) == false)
						{
							// TODO Generate error .. invalid debug flag
						}
					String Hex = st.nextToken();
						if(((isValidHex(Hex)&& (Hex.length() == 8))) == false);
						{
						// TODO Generate Error .. invalid hex
						}
					String moduleName = st.nextToken();
					// add the Hex code to MEM
					try
					{
					MEM[loadAddsDecimal] = Hex;	
					}
					catch(Exception e)
					{
						// Generate Error ... MEM out of bounds
					}
					
				}
				else
				{
					// TODO GEnerate Error .. missing argument in Load File
 				}
				
				LT = LoadModule.get(i);
				st = new StringTokenizer(LT, "|");
			}
			/*
			 * Handle LE
			 */
			String LE = LoadModule.get(LoadModule.size()-1);
			StringTokenizer LETokenizer = new StringTokenizer(LE, "|");
			
			
			if(LHTokenizer.countTokens() == 3)
				{
				
					String endRecord = LETokenizer.nextToken();
					String totalNumberOfMudules = LETokenizer.nextToken();
					String LEfirstModuleName = LETokenizer.nextToken();
					if(LEfirstModuleName.equals(LHfirstModuleName) == false){
						//TODO Generate Error ... miss-matching LH and LH firstModuleName 
					}
					
				}
			else
			{
				// TODO generate Error .... invalid number of arguments in LE 
			}
		}
		
		catch(Exception e){
			
		}
	}

	/**
	 * 
	 * Module Name: evaluateSType
	 * Description: Evaluates SAL560 S Type instructions.
	 * Input Params: 32 bit binary String
	 * Output Params: N/A
	 * Error Conditions Tested: 
	 * Error Messages Generated: 
	 * Original Author: Kermit Stearns
	 * Date of Installation: 11/18/2010
	 * Modifications:
	 * @param binaryInstruction Binary string (32bits) presumed to be valid instruction.
	 */
	private static void evaluateSType(String opcode, String binaryInstruction) 
	{
		DumpInfo();
		// Evaluate Instruction
		DumpInfo();
		if(debug)
		{
			DumpArray();
		}
		
	}

	/**
	 * 
	 * Module Name: evaluateRType	
	 * Description: Evaluates SAL560 R Type instructions.
	 * Input Params: 32bit Binary String of Instruction to be evaluated
	 * Output Params: N/A
	 * Error Conditions Tested:
	 * Error Messages Generated: 
	 * Original Author: Kermit Stearns
	 * Date of Installation: 
	 * Modifications:
	 * @param binaryInstruction
	 */
	private static void evaluateRType(String opcode, String binaryInstruction) 
	{
		
		DumpInfo();
		// TODO Evaluate Instruction KERMIT
		
		System.out.println("Error");
		DumpInfo();
		if(debug)
		{
			DumpArray();
			return;
		}
		
		
		
	}

	/**
	 * 
	 * Module Name: evaluateIOType
	 * Description: Evaluates SAL560 IO Instruction
	 * Input Params: 
	 * Output Params: N/A
	 * Error Conditions Tested:
	 * Error Messages Generated: 
	 * Original Author: Rakaan Kayali
	 * Date of Installation: 11/18/2010
	 * Modifications:
	 * @param binaryInstruction
	 */
	private static void evaluateIOType(String opcode, String binaryInstruction) 
	{
		DumpInfo();
		
		EFFADDR = Integer.parseInt(binaryInstruction.substring(16),2)+Integer.parseInt(binaryInstruction.substring(8,11));
		
		int displayNumber = Integer.parseInt(binaryInstruction.substring(11,16),2);
		
		if (EFFADDR > 65536)
		{
			//print error, address is out of bounds.
		}
		
		else if(opcode.equals("10"))
		{
			Scanner scan = new Scanner(System.in);
			
			int userInput = 0;
			
			try{
				
				userInput = scan.nextInt();
				
			} catch (Exception e){
				
				//error reading user's input
			}
			
			MEM[EFFADDR] = String.valueOf(Integer.toHexString(userInput));	
		}
		else if (opcode.equals("11"))
		{
			//System.out.print("Enter the character string you want stored in memory:");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			String userInput = null;
			
			try
			{
				userInput = br.readLine();
				
			} catch(Exception e) {
			
				//error reading user's input				
			}
			
			//convert string value entered by user into ASCII equivalent
			if (userInput.length()<=4)
			{
			String userInputHex = "";
			
			for (int i = 0; i<userInput.length(); i++)
			{
				char c = userInput.charAt(i);
				
				int j = (int)c;
				
				String append = String.valueOf(j);
				
				userInputHex = userInputHex + append; 
				
			}
			
			MEM[EFFADDR] = userInputHex;
			}
			else
			{
				//print error, input is too long.
			}
		}
		else if (opcode.equals("12"))
		{
			int displayAddress = 0;
			
			for (int i = 0; i < displayNumber; i++ )
			{
				displayAddress = EFFADDR + i;
				
				System.out.println(Integer.parseInt(MEM[displayAddress],16));	
			}
						
		}
		else
		{
			int displayAddress = 0;
			
			for (int i = 0; i<displayNumber; i++)
			{
				displayAddress = EFFADDR + i;
				
				String output = "";
				
				for (int cutter = 0; cutter < MEM[displayAddress].length(); cutter+=2)
				{
					int letter = Integer.parseInt(MEM[displayAddress].substring(cutter, cutter+2),16);
					
					char appendment = (char)letter;
					
					output = output + appendment;
				}
				
				System.out.println(output);
			}
			
		}
		DumpInfo();
		if(debug)
		{
			DumpArray();
		}
		
	}
	
	/**
	 * 
	 * Module Name: DumpArray
	 * Description: Dumps the current MEM array.
	 * Input Params: N/A
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Oscar Flores
	 * Date of Installation: 11/22/2010
	 * Modifications:
	 */
	private static void DumpArray()
	{
		
		System.out.println("DUMP MEM");
		int last = CompleteModuleLength + initialLoadAddr +34;
		
		int printcount = 0;
		System.out.println("Regs\t" + registers[0] + "  " + registers[1] + "  " + registers[2] + "  " + registers[3] + "  " + registers[4] + "  " + registers[5] + "  " + registers[6] + "  " + registers[7]);
		for(int i = initialLoadAddr+1 ; i < last-1 ; i++){
		
				if(printcount % 8 == 0)
				{
					if(printcount != 0){
					System.out.println();}
					System.out.print(i  + "\t");
				}
				System.out.print(MEM[i] + "  ");
				printcount++;
			
		}
	}
	
	/**
	 * 
	 * Module Name: DumpInfo
	 * Description: Information concerning the instruction being executed, displayed before and after each instruction.
	 * Input Params: N/A
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns, Oscar Flores, Rakaan Kayali
	 * Date of Installation: 11/22/2010
	 * Modifications:
	 */
	private static void DumpInfo()
	{
		// TODO
	}
	
	/**
	 * 
	 * Module Name: readFileToArrayList
	 * Description: Inputs Load Module into an Array to be processed.
	 * Input Params: fileName is the .txt LoadModule
	 * Output Params: ArrayList<String> with each line of LoadModule
	 * Error Conditions Tested: Invalid fileName, Unable to read from file
	 * Error Messages Generated: "",""
	 * Original Author: Oscar Flores
	 * Date of Installation: 11/21/2010
	 * Modifications:
	 * @param fileName LoadModule name in .txt format
	 * @return ArrayList<String> with each line from LoadModule
	 */
	public static ArrayList<String> readFileToArrayList(String fileName)

	{
		ArrayList<String> linesOfCode = new ArrayList<String>();
		try 
		{
			//System.out.println(fileName);
			FileInputStream fileInputStream = new FileInputStream(fileName);
			DataInputStream dataInputStream = new DataInputStream(
					fileInputStream);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					dataInputStream));

			String lineOfCode;

			while ((lineOfCode = buffer.readLine()) != null) 
			{
				linesOfCode.add(lineOfCode);
			}
			dataInputStream.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File Not Found");
			
		} 
		catch (IOException e) 
		{
			System.out.println("Error Reading File");
			e.printStackTrace();
		}
		return linesOfCode;
	}
	
	/**
	 * 
	 * Module Name: isValidHex
	 * Description: Checks for valid Hex String
	 * Input Params: a String
	 * Output Params: TRUE iff the input String "hex" is a valid Hex String, FALSE otherwise
	 * Error Conditions Tested: Hex digits, length
	 * Error Messages Generated: N/A
	 * Original Author: Oscar Flores
	 * Date of Installation: 11/20/2010
	 * Modifications:
	 * @param hex String to be evaluated
	 * @return
	 */
	
	static boolean isValidHex (String hex) 
	{
		
		boolean result = true;
		for(int i =0; i <hex.length();i++){
			if (!(Character.isDigit(hex.charAt(i))|| hex.charAt(i)== 'A' || hex.charAt(i)== 'B'|| hex.charAt(i)== 'C' || hex.charAt(i)== 'D' || hex.charAt(i)== 'E' || hex.charAt(i)== 'F')){
				result = false;
			}
		}
		return (result);
	}
	
	/**
	 * Module Name: padZeros16Bits
	 * Description: pads a binary value out to 16 bits (with zeros)
	 * Input Params: valid binary string
	 * Output Params: zero padded binary string of 16 bits
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Oscar Flores
	 * Date of Installation: 11/23/2010
	 * Modifications: Original function padded to 32 bits....lol
	 * @param binary Binary String to be padded with zeros in order to form 16 bit binary value
	 * @return
	 */
	public static String padZeros16Bits(String binary){
		String result = "";
		int zeroes = 16- binary.length();
		while (zeroes >0){
			result += '0';
			zeroes--;
		}
		result += binary;
		return result;
	}

	/**
	 * 
	 * Module Name: SixteenBitBinaryToFourHexDigits
	 * Description: takes 16 bits of binary to 4 Hex
	 * Input Params: valid 16 bit binary value
	 * Output Params: String of 4 hex digits
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Oscar Flores
	 * Date of Installation: 11/23/2010
	 * Modifications: Kermit changed this to 4 hex digits from 8 hex digits
	 * @param bin
	 * @return
	 */
	
	public static String SixteenBitBinaryToFourHexDigits (String bin){
		
		String binaryString = padZeros16Bits(bin);
		
		String hex = "";
		int b = 4;
		for(int i = 0; b <= 16 ; i+=4){
			
			String singlehexinbin = binaryString.substring(i, b);
			
			if(singlehexinbin.equals("0000")){
				hex+='0';
			}
			else if(singlehexinbin.equals("0001")){
				hex+='1';
			}
			else if(singlehexinbin.equals("0010")){
				hex+='2';
			}
			else if(singlehexinbin.equals("0011")){
				hex+='3';
			}
			else if(singlehexinbin.equals("0100")){
				hex+='4';
			}
			else if(singlehexinbin.equals("0101")){
				hex+='5';
			}
			else if(singlehexinbin.equals("0110")){
				hex+='6';
			}
			else if(singlehexinbin.equals("0111")){
				hex+='7';
			}
			else if(singlehexinbin.equals("1000")){
				hex+='8';
			}
			else if(singlehexinbin.equals("1001")){
				hex+='9';
			}
			else if(singlehexinbin.equals("1010")){
				hex+='A';
			}
			else if(singlehexinbin.equals("1011")){
				hex+='B';
			}
			else if(singlehexinbin.equals("1100")){
				hex+='C';
			}
			else if(singlehexinbin.equals("1101")){
				hex+='D';
			}
			else if(singlehexinbin.equals("1110")){
				hex+='E';
			}
			else if(singlehexinbin.equals("1111")){
				hex+='F';
			}
			b+=4;
		}
		//System.out.println(hex);
		return hex;
	}

	/**
	 * 
	 * Module Name: hexToBin.
	 * Description: converts a hex number to a binary string.
	 * Input Params: hex: a hex String.
	 * Output Params: a binary string.
	 * Error Conditions Tested: None.
	 * Error Messages Generated:  None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public static String hexToBin(String hex){
		String result = "";
		for(int i = 0; i <hex.length(); i++){
			if(hex.charAt(i) == '0'){
				result += "0000";
			}
			else if (hex.charAt(i) == '1'){
				result+="0001";
			}
			else if (hex.charAt(i) == '2'){
				result+="0010";
			}
			else if (hex.charAt(i) == '3'){
				result+="0011";
			}
			else if (hex.charAt(i) == '4'){
				result+="0100";
			}
			else if (hex.charAt(i) == '5'){
				result+="0101";
			}
			else if (hex.charAt(i) == '6'){
				result+="0110";
			}
			else if (hex.charAt(i) == '7'){
				result+="0111"; 
			}
			else if (hex.charAt(i) == '8'){
				result+="1000";
			}
			else if (hex.charAt(i) == '9'){
				result+="1001";
			}
			else if (hex.charAt(i) == 'A' || hex.charAt(i) == 'a'){
				result+="1010";
			}
			else if (hex.charAt(i) == 'B' || hex.charAt(i) == 'b'){
				result+="1011";
			}
			else if (hex.charAt(i) == 'C' || hex.charAt(i) == 'c'){
				result+="1100";
			}
			else if (hex.charAt(i) == 'D' || hex.charAt(i) == 'd'){
				result+="1101";
			}
			else if (hex.charAt(i) == 'E' || hex.charAt(i) == 'e'){
				result+="1110";
			}
			else if (hex.charAt(i) == 'F' || hex.charAt(i) == 'f'){
				result+="1111";
			}
		}
		return result;
	}
}
