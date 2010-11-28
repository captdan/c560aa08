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
	static boolean[] debug = new boolean[65536];
	static int PC = 0;
	static int NPIC = 0;
	static String binaryInstruction = "";
	public static ArrayList<String> GSymbTableArray = new ArrayList<String>();
	public static GlobalSymbolTable GSymbTable = new GlobalSymbolTable();
	public static int excecStart = 0;
	public static int initialLoadAddr = 0;
	public static int moduleonelength = 0;
	public static int moduletwolength = 0;
	public static int modulethreelength = 0;
	public static String moduleonename;
	public static String moduletwoname ;
	public static String modulethreename;
	public static int CompleteModuleLength = 0;
	public static String LHfirstModuleName;
	public static ALU ALU = new ALU();
	public static int EFFADDR = 0;
 	public static enum instructionType
	{
		 R, S, J, IO, I, NOTINSTRUCTION;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		initializeMEM();
		initializeREGS();
		FillGlobalSymbTable();
		instructionType type;
		putToMEM();
		LoadModule = readFileToArrayList(args[0]);
		putToMEM();
		 // Fill MEM array
		 
		putToMEM();
		NPIC = PC + 1;
		DumpArray();
		type = null;
		
		for(PC = excecStart; PC <= initialLoadAddr + CompleteModuleLength - 1; PC++)
		{
			//System.out.println(ALU.hexToBin(MEM[PC]));
			try
			{
				binaryInstruction =	ALU.hexToBin(MEM[PC]);
				type = SelectInstructionType(binaryInstruction);
				try
				{		
					OPCODE = Integer.parseInt(binaryInstruction.substring(0,6),2);	
				}
				catch(NumberFormatException e)
				{
					
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
					
				case R:
					//System.out.println(binaryInstruction);
					evaluateRType(String.valueOf(OPCODE),binaryInstruction);
					break;
					
				case J:  
						
						DumpArray();
						DumpInstructionInfo(String.valueOf(OPCODE), binaryInstruction, instructionType.J);
						
					
						System.exit(1);
				
						break;
						
				case IO: evaluateIOType(String.valueOf(OPCODE),binaryInstruction);
					break;
					
				case I: evaluateIType(String.valueOf(OPCODE),binaryInstruction);
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
			//System.out.println("OPCODE SELECT " + (binaryInstruction2.substring(0,6)));
			OPCODE = Integer.parseInt(binaryInstruction2.substring(0,6),2);
			//System.out.println(OPCODE);
			
		}
		catch(NumberFormatException e)
		{
			// Handle Invalid Opcode
			System.err.println("Invalid OPCODE....proceeding to next instruction.");
			return instructionType.NOTINSTRUCTION;
		}
		
		// At this point, OPCODE is the Decimal representation of the opcode
		
		if((16 <= OPCODE) && (OPCODE <= 23) || (OPCODE >= 50) && (OPCODE <= 55) || (OPCODE == 61) || (OPCODE == 30) || (OPCODE == 31) )
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
			debug[i] = false;
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
					
					CompleteModuleLength = Integer.parseInt(LengthOfModule,16);
					//System.out.println(CompleteModuleLength);
					String IniLodAddr = LHTokenizer.nextToken();
					initialLoadAddr = Integer.parseInt(IniLodAddr,16);
					//System.out.println(initialLoadAddr);
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
					System.exit(1);
				}
			
			String LT = LoadModule.get(1);
			//System.out.println(LT);
			StringTokenizer st = new StringTokenizer(LT, "|");
			for(int i = 2; st.nextToken().equals("LT") ; i++)
			{
				
				if(st.countTokens() == 4)
				{
					String loardAddr = st.nextToken();
					//System.out.println(loardAddr);
						if(((isValidHex(loardAddr)&& (loardAddr.length() == 4))) == false)
						{
							System.err.println("Unrecoverable problem with Load Module:");
							System.exit(1);
						}
					// get Load address for MEM	
					int loadAddsDecimal = Integer.parseInt(loardAddr, 16);
					
					String debug1 = st.nextToken();
					//System.out.println(debug);
					if((debug1.equals("Y") || (debug1.equals("N"))) == false)
						{
							System.err.println("Invalud Dubug Flag");
							System.exit(1);
						}
					else if(debug1.equals("Y") ){
						debug[loadAddsDecimal] = true;
					}
					String Hex = st.nextToken();
					//System.out.println(Hex);
						if(((isValidHex(Hex)&& (Hex.length() == 8))) == false)
						{
							System.err.println("Invalid Hex");
							System.exit(1);
					
						}
					String moduleName = st.nextToken();
					//System.out.println(moduleName);
					// add the Hex code to MEM
					try
					{
					MEM[loadAddsDecimal] = Hex;	
					}
					catch(Exception e)
					{
						System.err.println("Memory out of bounds");
						System.exit(1);
					}
					
				}
				else
				{
					System.err.println("Unrecoverable problem with Load Module: missing element in Load File ");
					System.exit(1);
 				}
				
				LT = LoadModule.get(i);
				st = new StringTokenizer(LT, "|");
			}
			/*
			 * Handle LE
			 */
			String LE = LoadModule.get(LoadModule.size()-1);
			
			StringTokenizer LETokenizer = new StringTokenizer(LE, "|");
			
			//System.out.println(LHTokenizer.countTokens());
			if(LETokenizer.countTokens() == 3)
				{
					
					String endRecord = LETokenizer.nextToken();
					String totalNumberOfMudules = LETokenizer.nextToken();
					String LEfirstModuleName = LETokenizer.nextToken();
					if(LEfirstModuleName.equals(LHfirstModuleName) == false){
						System.err.println("Unrecoverable problem with Load Module: Unmatching Module names");
						System.exit(1);
					
					}
					
				}
			else
			{
			
				System.err.println("Unrecoverable problem with Load Module: invalid number of arguments in LE record ");
				System.exit(1);
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
		//System.out.println(opcode);
		//System.out.println(binaryInstruction);
		
		int debugnow = PC;
		int registerOne = Integer.parseInt(binaryInstruction.substring(8,11),2);
			
		int registerTwo = Integer.parseInt(binaryInstruction.substring(11,14),2);
		
		int registerOneValue = ALU.GetIntegerFromTwosComplementSigned(ALU.hexToBin(registers[registerOne]));
		
		int registerTwoValue = ALU.GetIntegerFromTwosComplementSigned(ALU.hexToBin(registers[registerTwo]));
		
		int lastSixteen = ALU.GetIntegerFromTwosComplementSigned(binaryInstruction.substring(16));
		//System.out.println(lastSixteen);
		//System.out.println(binaryInstruction.substring(16));
		if (!(registerOne>=0 && registerOne<=7 && registerTwo >=0 && registerTwo <=7))
		{
			System.err.println("specified register is not between 0 and 7");
		}
		else if (opcode.equals("32")) // handle Jump on equal
		{
				//System.out.println(Integer.parseInt(binaryInstruction.substring(16),2));
				if(registerOneValue==registerTwoValue)
				{
					PC = lastSixteen-1;
					//System.out.println("PC= " + PC);
					NPIC = PC;
				}
		}
		else if (opcode.equals("33")) //handles Jump on not equal
		{
			if(registerOneValue!=registerTwoValue)
			{
				PC = lastSixteen-1;
			
				NPIC = PC;
			}
		}
		else if (opcode.equals("34")) //handles Jump greater than
		{
			if(registerOneValue>registerTwoValue)
			{
				PC = lastSixteen-1;
			
				NPIC = PC;
			}
		}
		else if (opcode.equals("35")) //handles Jump less than
		{
			if(registerOneValue<registerTwoValue)
			{
				PC = lastSixteen-1;
			
				NPIC = PC;
			}
		}
		else if (opcode.equals("36")) //handles Jump less than or equal to
		{
			if(registerOneValue<=registerTwoValue)
			{
				PC = lastSixteen-1;
			
				NPIC = PC;
			}
		}
		else if (opcode.equals("39")) // store word address
		{
			EFFADDR = registerTwoValue + lastSixteen;
			
			if (EFFADDR < 65535)
			{
			MEM[EFFADDR] = registers[registerOne];
			
			}
			else
			{
				System.err.println("address out of bounds");
			}
		}
		else if (opcode.equals("48")) // load word address
		{
			EFFADDR = registerTwoValue + lastSixteen;
			if (EFFADDR < 65535)
			{
			registers[registerOne] = MEM[lastSixteen];
			}
			else
			{
				System.err.println("address out of bounds");
			}
		}
		else if (opcode.equals("49")) // load negative address
		{
			EFFADDR = registerTwoValue + lastSixteen;
			if (EFFADDR < 65535)
			{
			int loadedValue = Integer.parseInt(MEM[registerTwoValue + lastSixteen],16)*(-1);
			
			registers[registerOne] =  Integer.toBinaryString(loadedValue);
			}
			else
			{
				System.err.println("address out of bounds");
			}
		}
		else if (opcode.equals("56"))//load address of word into register //ask to make sure interpretation is correct
		{
			EFFADDR = lastSixteen + registerTwoValue;
			
			if(EFFADDR < 65535)
			{
			int decimal = Integer.parseInt(MEM[EFFADDR],16);
			
			registers[registerOne]= Integer.toBinaryString(decimal);
			}
			else
			{
				System.err.println("address out of bounds");
			}
		}
		else if (opcode.equals("57"))//store address in word
		{
			EFFADDR = registerTwoValue + lastSixteen;
			
			if (EFFADDR < 65535)
			{
				
			MEM[EFFADDR] = Integer.toHexString(registerOneValue);
			
			}
			else
			{
				System.err.println("address out of bounds");
			}
		}
		else if (opcode.equals("58"))//and register to storage
		{	
			EFFADDR = registerTwoValue + lastSixteen;
			
			if (EFFADDR <65536)
			{
			int decimal = Integer.parseInt(MEM[EFFADDR],16);
			
			String binary = Integer.toBinaryString(decimal);
			
			String andResult = "";
			
			for (int i = 0; i <binary.length(); i++){
			
				if (registers[registerOne].charAt(i)==binary.charAt(i))
				{
					andResult = andResult + "1";
				}
				else 
				{
					andResult = andResult + "0";
				}
			}
			
			registers[registerOne] = andResult;
			}
			else
			{
				
			System.err.println("address out of bounds");
			
			}
		}
		else if (opcode.equals("59"))
		{
			EFFADDR = registerTwoValue + lastSixteen;
		
			if (EFFADDR < 65536)
			{
			int decimal = Integer.parseInt(MEM[EFFADDR],16);
			
			String binary = Integer.toBinaryString(decimal);
			
			String andResult = "";
			
			for (int i = 0; i <binary.length(); i++){
			
				if (registers[registerOne].charAt(i)!=binary.charAt(i))
				{
					andResult = andResult + "1";
				}
				else 
				{
					andResult = andResult + "0";
				}
			}
			
			registers[registerOne] = andResult;
			}
			else
			{
				
			System.err.println("address out of bounds");
			
			}
		}
		else if (opcode.equals("6"))//Jump
		{
			if(lastSixteen <65536)
			{
				PC = lastSixteen-1;
			
				NPIC = PC;
			}
		}
		else if (opcode.equals("7"))//Jump and link
		{
			//
			PC = lastSixteen;
		}
		else if (opcode.equals("26"))
		{
			
		}
		else if (opcode.equals("27"))
		{
			
		}
		else if (opcode.equals("28"))
		{
			
		}
		else if (opcode.equals("29"))
		{
			
		}
		//System.out.println(opcode);
		if(debug[debugnow])
		{
			DumpArray();
			DumpInstructionInfo(opcode,binaryInstruction,instructionType.S);
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
		int debugnow = PC;
		//System.out.println("OPCODE "+ opcode);
		int op = Integer.valueOf(opcode);
		int r1,r2,r3,shift,function;
		try
		{
			r1 = Integer.parseInt(binaryInstruction.substring(8,11),2);
		//	System.out.println(binaryInstruction.substring(9,11));
		//	System.out.println("$"+r1);
			r2 = Integer.parseInt(binaryInstruction.substring(11,14),2);
		//	System.out.println("$"+r2);
			r3 = Integer.parseInt(binaryInstruction.substring(14,17),2);
		//	System.out.println("$"+r3);
			shift = Integer.parseInt(binaryInstruction.substring(17,23),2);
		//	System.out.println(binaryInstruction.substring(17,23));
		//	System.out.println("Shift "+shift);
			if(shift > 24)
			{
				System.err.println("Excessive bit shift...proceeding to next instruction.");
				return;
			}
			if((r1 > 7) || (r2 > 7) || (r3 > 7))
			{
				System.err.println("Invalid Register Specified...proceeding to next instruction.");
				return;
			}
			function = Integer.parseInt((binaryInstruction.substring(26)),2);
		//	System.out.println("Function = " + function);
			
			
		}
		catch (NumberFormatException e)
		{
			System.err.println("Wow...how the hell did you find this error???");
			return;
		}
		
		
		if(op == 1)
		{
			
				if(function == 32)
				{
					registers[r1] = ALU.intToHex(ALU.ADD(ALU.hexToBin(registers[r2]), ALU.hexToBin(registers[r3])));
				}
				else if(function == 33)
				{
					registers[r1] = ALU.intToHex(ALU.ADDU(ALU.hexToBin(registers[r2]), ALU.hexToBin(registers[r3])));
				}
				else if(function == 34)
				{
					registers[r1] = ALU.intToHex(ALU.SUB(ALU.hexToBin(registers[r2]), ALU.hexToBin(registers[r3])));
				}
				else if(function == 35)
				{
					registers[r1] = ALU.intToHex(ALU.SUBU(ALU.hexToBin(registers[r2]), ALU.hexToBin(registers[r3])));
				}
				else if(function == 24)
				{
					registers[r1] = ALU.intToHex(ALU.MUL(ALU.hexToBin(registers[r2]), ALU.hexToBin(registers[r3])));
				}
				else if(function == 25)
				{
					registers[r1] = ALU.intToHex(ALU.MULU(ALU.hexToBin(registers[r2]), ALU.hexToBin(registers[r3])));
				}
				else if(function == 26)
				{
					registers[r1] = ALU.intToHex(ALU.DIV(ALU.hexToBin(registers[r2]), ALU.hexToBin(registers[r3])));
				}
				else if(function == 27)
				{
					registers[r1] = ALU.intToHex(ALU.DIVU(ALU.hexToBin(registers[r2]), ALU.hexToBin(registers[r3])));
				}
				else if(function == 28)
				{
					registers[r1] = ALU.intToHex(ALU.PWR(ALU.hexToBin(registers[r2]), ALU.hexToBin(registers[r3])));
				}
				else
				{
						System.err.println("Invalide Function Code with R-Type Instruction");
						return;
				}
			
		
		}
		else if(op == 2)
		{
			if(function == 0)
			{
				registers[r1] = ALU.SLL(ALU.hexToBin(registers[r2]), shift);
			}
			else if(function == 2)
			{
				registers[r1] = ALU.SRL(ALU.hexToBin(registers[r2]), shift);
			}
			else if(function == 3)
			{
				registers[r1] = ALU.SRA(ALU.hexToBin(registers[r2]), shift);
			}
			else if(function == 36)
			{
				registers[r1] = ALU.AND(ALU.hexToBin(registers[r2]), registers[r3]);
			}
			else if(function == 37)
			{
				registers[r1] = ALU.OR(ALU.hexToBin(registers[r2]), registers[r3]);
			}
			else if(function == 38)
			{
				registers[r1] = ALU.XOR(ALU.hexToBin(registers[r2]), registers[r3]);
			}
			else if(function == 39)
			{
				registers[r1] = ALU.NOR(ALU.hexToBin(registers[r2]), registers[r3]);
			}
			else 
			{
				System.err.println("Invalide Function Code with R-Type Instruction");
				return;
			}
		}
		else if (op == 3)
		{
			registers[r2] = ALU.intToBin(0);
			registers[r3] = ALU.intToBin(0);
			int jump = Integer.parseInt(registers[r1],2);
			if(jump <= 65535)
			{
				PC = jump -1;
				NPIC = PC;
			}
			else
			{
				System.err.println("Jump address is out of bounds!");
				return;
			}
			
		}
		else if (op == 63)
		{
			if((r1 <= 1) && (r2 <= 1) && r3 <= 1)
			{
				if(r1 == 1)
				{
					System.out.println("Regs\t" + registers[0] + "  " + registers[1] + "  " + registers[2] + "  " + registers[3] + "  " + registers[4] + "  " + registers[5] + "  " + registers[6] + "  " + registers[7]);
				}
				if(r2 == 1)
				{
					DumpArray();
				}
				if(r3 == 1)
				{
					System.out.println("LC: " + PC);
				}
			}
			else
			{
				System.err.println("Please specify a correct Dump.");
				return;
			}
		}
		
	
		if(debug[debugnow])
		{
			DumpArray();
			DumpInstructionInfo(opcode,binaryInstruction,instructionType.R);
		}
		
		
	}

	/**
	 * 
	 * Module Name: evaluateIType	
	 * Description: Evaluates SAL560 I Type instructions.
	 * Input Params: 32bit Binary String of Instruction to be evaluated
	 * Output Params: N/A
	 * Error Conditions Tested:
	 * Error Messages Generated: 
	 * Original Author: Kermit Stearns
	 * Date of Installation: 
	 * Modifications:
	 * @param binaryInstruction
	 */
	private static void evaluateIType(String opcode, String binaryInstruction) 
	{
		int debugnow = PC;
		//System.out.println("OPCODE "+ opcode + " WTF");
		int op = Integer.valueOf(opcode);
		//System.out.println("OP  " + op);
		int r1,r2;
		String opc = binaryInstruction.substring(0, 6);
		String unused = binaryInstruction.substring(6, 8);
		String R1 = binaryInstruction.substring(8, 11);
		String R2 = binaryInstruction.substring(11, 14);
		String unused2 = binaryInstruction.substring(14, 16);
		String imm = binaryInstruction.substring(16);
		//System.out.println("IMMEDIATE " + imm);
		try
		{
			//r1 = Integer.parseInt(binaryInstruction.substring(arg0, arg1), 2);
			
			r1 = Integer.parseInt(R1,2);
			r2 = Integer.parseInt(R2,2);
			
		}
		catch (NumberFormatException e)
		{
			System.err.println("Wow...how the hell did you find this error???");
			return;
		}
		
		int result = 0;
		if(op == 16){
			registers[r1] = ALU.intToHex(ALU.ADDI(ALU.hexToBin(registers[r2]), imm));
		}
		if(op == 17){
			registers[r1] = ALU.intToHex(ALU.ADDIU(ALU.hexToBin(registers[r2]), imm));
		}
		if(op == 18){
			registers[r1] = ALU.intToHex(ALU.SUBI(ALU.hexToBin(registers[r2]), imm));
		}
		if(op == 19){
			registers[r1] = ALU.intToHex(ALU.SUBIU(ALU.hexToBin(registers[r2]), imm));
		}
		if(op == 20){
			registers[r1] = ALU.intToHex(ALU.MULI(ALU.hexToBin(registers[r2]), imm));
		}
		if(op == 21){
			registers[r1] = ALU.intToHex(ALU.MULIU(ALU.hexToBin(registers[r2]), imm));
		}
		if(op == 22){
			registers[r1] = ALU.intToHex(ALU.DIVI(ALU.hexToBin(registers[r2]), imm));
		}
		if(op == 23){
			registers[r1] = ALU.intToHex(ALU.DIVIU(ALU.hexToBin(registers[r2]), imm));
		}
		if(op == 50){
			// LWI
			registers[r1] = ALU.binToHex32bits(imm);
		}
		if(op == 51){
			// LUI
			registers[r1] = ALU.binToHex32bits(ALU.intToBin((ALU.GetIntegerFromTwosComplementUnsigned(imm))));
		}
		if(op == 52){
			// ori
			registers[r1] = ALU.binToHex32bits(ALU.ORI(registers[r2], imm));
		}
		if(op == 53){
			// xori
			registers[r1] = ALU.binToHex32bits(ALU.XORI(registers[r2], imm));
		}
		if(op == 54){
			// nori
			registers[r1] = ALU.binToHex32bits(ALU.NORI(registers[r2], imm));
		}
		if(op == 55){
			// andi
			registers[r1] = ALU.binToHex32bits(ALU.ANDI(registers[r2], imm));
		}
		if(op == 61){
			// SRV
			registers[r1] = ALU.binToHex32bits(imm);
			registers[r2] = ALU.binToHex32bits(imm);
		}
		if(op == 30){
			
			if(imm.startsWith("0"))
			{
				//Binary Immediate is a positive number, parseInt and print
				try
				{
					System.out.println(Integer.parseInt(imm, 2));
				}
				catch (NumberFormatException e)
				{
					System.err.println("Invalid Immediate Value");
					return;
				}
			}
			else
			{
				//Must start with 1, so it's a negative number in 2's Complement Form, convert and print
				try
				{
					System.out.println(-1*(1+Integer.parseInt(ALU.XOR(imm, "1111111111111111"), 2)));
				}
				catch (NumberFormatException e)
				{
					System.err.println("Invalid Immediate Value");
					return;
				}
				
			}
		}
		if(op == 31){
		
			try
			{
				//1st Character
				System.out.print((char)Integer.parseInt(imm.substring(0,7),2));
				
				//2nd Character
				System.out.println((char)Integer.parseInt(imm.substring(8,15),2));
				
			}
			catch (NumberFormatException e)
			{
				System.err.println("Invalid Character String!");
				return;
			}
		}
		
		
		if(debug[debugnow])
		{
			DumpArray();
			DumpInstructionInfo(opcode,binaryInstruction,instructionType.I);
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
		int debugnow = PC;
		
		String opCode = binaryInstruction.substring(0,6);
		String addrCode = binaryInstruction.substring(6,8);
		
		int r1 = Integer.parseInt(binaryInstruction.substring(8,11));
		int r1Contents = ALU.GetIntegerFromTwosComplementSigned(registers[r1]);
		
		int words =  Integer.parseInt(binaryInstruction.substring(11,16),2);
		
		String imm =  binaryInstruction.substring(16);
		int MemStart =  Integer.parseInt(imm,2);
		
		
		if(addrCode.equals("00") || addrCode.equals("10"))
		{
			//Direct Addressing Modes
			EFFADDR = r1Contents + ALU.GetIntegerFromTwosComplementSigned(imm);
			System.out.println("EFFADRR: " +EFFADDR );
		}
		else
		{
			//Indirect Addressing Mode
			EFFADDR = r1Contents + ALU.GetIntegerFromTwosComplementSigned(ALU.hexToBin(MEM[Integer.parseInt(imm, 2)]));
		}
		
		
		int displayNumber = Integer.parseInt(binaryInstruction.substring(11,16),2);
		
		if (EFFADDR > 65536)
		{
			System.err.println("address is out of bounds");
			return;
		}
		
		else if(opcode.equals("10"))
		{
			//System.out.println("?????");
			Scanner scan = new Scanner(System.in);
			
			int userInput = 0;
			int addretowrite = EFFADDR;
			for(int i = 0; i < words; i++)
			{
				System.out.print("Enter the integer you want stored in memory:");
				addretowrite += i;
			try{
				
				userInput = scan.nextInt();

				
				
			} catch (Exception e){
			
				System.err.println("couldn't read user's input");
				return;
			}
			
			MEM[addretowrite] = ALU.intToHex(userInput);	
			
			}
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
				
				System.err.println("Couldn't read user's input");
				return;
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
				if(debug[debugnow])
				{
					DumpArray();
					DumpInstructionInfo(opcode,binaryInstruction,instructionType.S);
				}
				
			}
			
			MEM[EFFADDR] = userInputHex;
			}
			else
			{
				System.err.println("input is too long");
				return;
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
		else if (opcode.equals("13"))
		{
			String hbin = ALU.hexToBin(MEM[EFFADDR]);
			//System.out.println(hbin);
			//System.out.println((char)Integer.parseInt(hbin.substring(0,8),2));
			try
			{
				//1st Character
				System.out.print((char)Integer.parseInt(hbin.substring(0,8),2));				//2nd Character
				System.out.print((char)Integer.parseInt(hbin.substring(8,16),2));				//3rd Character
				System.out.print((char)Integer.parseInt(hbin.substring(16,24),2));				//4th Character
				System.out.print((char)Integer.parseInt(hbin.substring(24),2));
				System.out.println();
			}
				catch (NumberFormatException e)
			{
				System.err.println("Invalid Character String!");
				return;
			}
						
		}
		else if (opcode.equals("30"))
		{
			//OUTNI
			if(imm.startsWith("0"))
			{
				//Binary Immediate is a positive number, parseInt and print
				try
				{
					System.out.println(Integer.parseInt(imm, 2));
				}
				catch (NumberFormatException e)
				{
					System.err.println("Invalid Immediate Value");
					return;
				}
			}
			else
			{
				//Must start with 1, so it's a negative number in 2's Complement Form, convert and print
				try
				{
					System.out.println(-1*(1+Integer.parseInt(ALU.XOR(imm, "1111111111111111"), 2)));
				}
				catch (NumberFormatException e)
				{
					System.err.println("Invalid Immediate Value");
					return;
				}
				
			}
			
			
		}
		else if (opcode.equals("31"))
		{
			//OUTCI
			try
			{
				//1st Character
				System.out.print((char)Integer.parseInt(imm.substring(0,7),2));
				
				//2nd Character
				System.out.println((char)Integer.parseInt(imm.substring(8,15),2));
				
			}
			catch (NumberFormatException e)
			{
				System.err.println("Invalid Character String!");
				return;
			}
			
		}
		
		if(debug[debugnow])
		{
			DumpArray();
			DumpInstructionInfo(opcode,binaryInstruction,instructionType.S);
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
		int last = CompleteModuleLength + initialLoadAddr;
		
		int printcount = 0;
		System.out.println("Regs\t" + registers[0] + "  " + registers[1] + "  " + registers[2] + "  " + registers[3] + "  " + registers[4] + "  " + registers[5] + "  " + registers[6] + "  " + registers[7]);
		for(int i = initialLoadAddr ; i < last ; i++){
		
				if(printcount % 8 == 0)
				{
					if(printcount != 0){
					System.out.println();}
					System.out.print(i  + "\t");
				}
				System.out.print(MEM[i] + "  ");
				printcount++;
			
		}
		System.out.println();
		
		
	}
	
	/**
	 * 
	 * Module Name: DumpInstructionInfo
	 * Description: Information concerning the instruction being executed, displayed before and after each instruction.
	 * Input Params: N/A
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns, Oscar Flores, Rakaan Kayali
	 * Date of Installation: 11/22/2010
	 * Modifications:
	 */
	private static void DumpInstructionInfo(String op,String instruction, instructionType i)
	{
		//Current PC/LC Value
		System.out.print("LC: " + String.valueOf(PC) + '\t');
		
		//Current values in Registers
		//System.out.println("Regs\t" + registers[0] + "  " + registers[1] + "  " + registers[2] + "  " + registers[3] + "  " + registers[4] + "  " + registers[5] + "  " + registers[6] + "  " + registers[7]);
		
		//Binary Instruction
		System.out.print("Binary Instruction: " + instruction);
		
		//Opcode of Instruction
		System.out.println("\tOpcode (HEX): " + Integer.toHexString(Integer.parseInt(instruction.substring(0, 6),2)).toUpperCase() + '\t');
		
		//EFFADDR if S-type or IO-type
		if(i.equals(instructionType.S) || i.equals(instructionType.IO))
		{
			System.out.println("EFFADDR: " + String.valueOf(EFFADDR));
		}
		Scanner scan = new Scanner(System.in);
		scan.nextLine();
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
			System.exit(0);
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
		
		String binaryString = ALU.padZeros("0",bin);
		binaryString = binaryString.substring(16);
		
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

	/**
	 * Module Name: opcodeInHex
	 * Description: Takes 6 binary digits of Instruction opcode and converts to hex
	 * Input Params: 6 digit binary string
	 * Output Params: 2 digits valid hex
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns
	 * Date of Installation: 11/27/2010
	 * Modifications:
	 * @param binary 6 digits to be converted to hex
	 * @return 2 valid hex digits
	 */
	public static String opcodeInHex(String binary)
	{
		String hex = "";
		//System.out.println(binary);
		//System.out.println((binary.substring(0,1)));
		ArrayList<Integer> digits = new ArrayList<Integer>();
		try
		{
			digits.add(Integer.parseInt(binary.substring(0,1),2));
			digits.add(Integer.parseInt(binary.substring(1,6),2));
		}
		catch (NumberFormatException e)
		{
			//Won't Happen
		}
		
		for(int i = 0; i < 2; i++)
		{
			if(digits.get(i) < 10)
			{
				hex += String.valueOf(digits.get(i));
			}
			else if(digits.get(i) == 10)
			{
				hex += "A";
			}
			else if(digits.get(i) == 11)
			{
				hex += "B";
			}
			else if(digits.get(i) == 12)
			{
				hex += "C";
			}
			else if(digits.get(i) == 13)
			{
				hex += "D";
			}
			else if(digits.get(i) == 14)
			{
				hex += "E";
			}
			else if(digits.get(i) == 15)
			{
				hex += "F";
			}
		}
		
		return hex;
	}

}
