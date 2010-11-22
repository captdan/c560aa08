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
	static String[] status_registers = new String[3];
	//Status Registers
	// Overflow
	// Zero
	// Negative
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
	public static int TargetAddress = 0;
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
		// Input Global Symbol Table from .txt into SymbolTable-type object
		
		initializeMEM();
		initializeREGS();
		instructionType type;
	
			if(args.length == 2 )
		{
		
				LoadModule = readFileToArrayList(args[0]);
		
		
		}
		else{
			System.out.println("Unspecified Load File name");
			System.exit(2);
		}
	
		
		/*
		 * Fill MEM array
		 */
		putToMEM();
	
		type = null;
			
		for(int i = excecStart; i < initialLoadAddr + CompleteModuleLength - 1; i++)
		{
			
			try
			{
				binaryInstruction =	ALU.hexToBin(MEM[i]);
			}
			catch(NumberFormatException e)
			{
				// TODO Invalid Hex instruction....do we already catch this?
			}
			type = SelectInstructionType(binaryInstruction);
			
			
			
			switch (type)
			{
				case S: evaluateSType(binaryInstruction);
					break;
				case R:  evaluateRType(String.valueOf(OPCODE));
					break;
				case J:  
				DumpInfo();

				if(debug)
				{
					DumpArray();
				}
				
				PC++;
				
				System.exit(1);
				
				break;
				case IO: evaluateIOType(binaryInstruction);
					break;
				default: Error();
					break;
			}
			
			
		}
		
		DumpArray();
	}

	/**
	 * Module Name:
	 * Description:
	 * Input Params:
	 * Output Params:
	 * Error Conditions Tested:
	 * Error Messages Generated:
	 * Original Author:
	 * Date of Installation:
	 * Modifications:
	 * @param binaryInstruction2
	 * @return 
	 */
	private static instructionType SelectInstructionType(String binaryInstruction2) {
		instructionType result;
		try
		{
			//System.out.println(binaryInstruction);
			//011010
			OPCODE = Integer.parseInt(binaryInstruction2.substring(0,6),2);
			//System.out.println("OPCODE: " + OPCODE);
		}
		catch(NumberFormatException e)
		{
			// TODO Handle Invalid Opcode
		}
		//011010 
		// At this point, OPCODE is the integer representation of the opcode
		// when you do Integer.ParseInt ... it returns an integer not a hex number, so you need to compare to int not Hex
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
			//System.out.println("Hex Does not contain a valid instruction: Terminating");
			//System.exit(1);
		}
		
		return result;
	}

	/**
	 * Module Name:
	 * Description:
	 * Input Params:
	 * Output Params:
	 * Error Conditions Tested:
	 * Error Messages Generated:
	 * Original Author:
	 * Date of Installation:
	 * Modifications:
	 */
	private static void initializeREGS() {
		for(int i = 0; i < registers.length; i++){
			registers[i] = "00000000";
		}
		
	}

	/**
	 * Module Name:
	 * Description:
	 * Input Params:
	 * Output Params:
	 * Error Conditions Tested:
	 * Error Messages Generated:
	 * Original Author:
	 * Date of Installation:
	 * Modifications:
	 */
	private static void initializeMEM() {
		for(int i = 0; i < MEM.length; i++){
			MEM[i] = "00000000";
		}
		
	}

	/**
	 * Module Name:
	 * Description:
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
	 * Module Name:
	 * Description:
	 * Input Params:
	 * Output Params:
	 * Error Conditions Tested:
	 * Error Messages Generated:
	 * Original Author:
	 * Date of Installation:
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
						if(((isValidHex(loardAddr)&& (loardAddr.length() == 4))) == false);
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
				// Increase PC
				PC++;
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

	private static void evaluateSType(String binaryInstruction2) 
	{
		DumpInfo();
		// Evaluate Instruction
		DumpInfo();
		if(debug)
		{
			DumpArray();
		}
		PC++;
	}

	private static void evaluateRType(String opcode) 
	{
		DumpInfo();
		// TODO Evaluate Instruction KERMIT
		DumpInfo();
		if(debug)
		{
			DumpArray();
		}
		PC++;
	}

	private static void evaluateIOType(String opcode) 
	{
		DumpInfo();
		
		if(opcode.equals("10"))
		{
			Scanner scan = new Scanner(System.in);
			
			int userInput = 0;
			
			System.out.print("Enter the number you want stored in memory:");
			try{
				
				userInput = scan.nextInt();
				
			} catch (Exception e){
				
				//error reading user's input
			}
			
			if (Integer.parseInt(binaryInstruction.substring(6,8))== 0)
			{
				//determine target address when relative to start
			}
			else if (Integer.parseInt(binaryInstruction.substring(6,8))== 1)
			{
				//determine target address when indirect relative to start
			}
			else if (Integer.parseInt(binaryInstruction.substring(6,8))== 2)
			{
				TargetAddress = Integer.parseInt(binaryInstruction.substring(16,32),2);
			}
			else if (Integer.parseInt(binaryInstruction.substring(6,8))== 3)
			{
				//determine target address when literal value is used.
			}
			
			MEM[TargetAddress] = String.valueOf(userInput);
			
			
		}
		else if (opcode.equals("11"))
		{
			System.out.print("Enter the character string you want stored in memory:");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			String userInput = null;
			
			try
			{
				userInput = br.readLine();
				
			} catch(Exception e) {
			
				//error reading user's input				
			}

			if (Integer.parseInt(binaryInstruction.substring(6,8))== 0)
			{
				//determine target address when relative to start
			}
			else if (Integer.parseInt(binaryInstruction.substring(6,8))== 1)
			{
				//determine target address when indirect relative to start
			}
			else if (Integer.parseInt(binaryInstruction.substring(6,8))== 2)
			{
				TargetAddress = Integer.parseInt(binaryInstruction.substring(16,32),2);
			}
			else if (Integer.parseInt(binaryInstruction.substring(6,8))== 3)
			{
				//determine target address when literal value is used.
			}
			
			MEM[TargetAddress] = userInput;
			
		}
		else
		{
			if (Integer.parseInt(binaryInstruction.substring(6,8))== 0)
			{
				//determine target address when relative to start
			}
			else if (Integer.parseInt(binaryInstruction.substring(6,8))== 1)
			{
				//determine target address when indirect relative to start
			}
			else if (Integer.parseInt(binaryInstruction.substring(6,8))== 2)
			{
				TargetAddress = Integer.parseInt(binaryInstruction.substring(16,32),2);
			}
			else if (Integer.parseInt(binaryInstruction.substring(6,8))== 3)
			{
				//determine target address when literal value is used.
			}
			
			//need to convert MEM[TargetAddress] to ASCII equivalent.
			
			//System.out.println(MEM[TargetAddress]);
		}
		
		DumpInfo();
		if(debug)
		{
			DumpArray();
		}
		PC++;
	}
	
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
	
	private static void DumpInfo()
	{
		// TODO Make a method. OSCAR
	}
	private static void Error()
	{
		// TODO Make a method. LATER
	}
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
	
}
