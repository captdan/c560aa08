import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class HardwareSimulator {

	int[] registers = new int[15];
	static String[] MEM = new String[65536];
	static ArrayList<String> LoadModule = new ArrayList<String>();
	String opcode = "";
	ArrayList<Error> Errors = new  ArrayList<Error>();
	static boolean debug = false;
	static int PC = 0;
	static String binaryInstruction = "";
	public static ArrayList<String> GSymbTableArray = new ArrayList<String>();
	public static GlobalSymbolTable GSymbTable = new GlobalSymbolTable();
	public static int exectStart = 0;
	public static int initialLoadAddr = 0;
	public static int CompleteModuleLength = 0;
	public static String LHfirstModuleName;
	public static enum instructionType
	{
		 R, S, J, IO;
	}
	/**
	 * Holds all symbols encountered while passing through source code.
	 */

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Input Global Symbol Table from .txt into SymbolTable-type object
		
		inisializeMEM();
		instructionType type;
	//	DumpArray();
	//	if(args.length == 2 )
	//	{
		LoadModule = readFileToArrayList(args[0]);
		//System.out.println(LoadModule.size());
		
	//	GSymbTableArray = readFileToArrayList(args[1]);
	//	}
	//	else{
	//		System.out.println("Unspecified Load File name");
	//		System.exit(2);
	//	}
		
		/*
		 * get Symbol table array into GSymbTable 
		 * 
		 */
//		FillGlobalSymbTable();
	
		
		/*
		 * Fill MEM array
		 */
		putToMEM();
		
		
		
		// TODO Handle Linker End Record as last object in LoadModule Array (Everything else is text records) RAKAAN
		
		// Load any int.data,bin.data,hex.data.str.data values into memory array
		type = null;
		
	/*		
		for(int i = 0; i < Integer.MAX_VALUE; i++)
		{
			
			System.out.println(LoadModule.get(i));
			
			// TODO Look at OpCode and separate by Instruction Type  KERMIT
						// returns Enum type of Instruction
			switch (type)
			{
				case S: evaluateSType();
					break;
				case R:  evaluateRType();
					break;
				case J:  evaluateJType();
					break;
				case IO: evaluateIOType();
					break;
				default: Error();
					break;
			}
			
			
		}
		*/
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
	 */
	private static void inisializeMEM() {
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
			// TODO Handle Linker Header Record as first object in LoadModule Array KERMIT
			// TODO Input our Load Module as .txt and generate memory array OSCAR//
			String LH = LoadModule.get(0);
			StringTokenizer LHTokenizer = new StringTokenizer(LH, "|");
			
			if(LHTokenizer.countTokens() == 11)
				{
				
					String H = LHTokenizer.nextToken();
					String exStart = LHTokenizer.nextToken();
					exectStart = Integer.parseInt(exStart);
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
					//TODO Generate Error .. missing arguments in LH
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
								// TODO Generate Error .. invalid hex
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

	private static void evaluateSType() 
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

	private static void evaluateRType() 
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

	private static void evaluateJType() 
	{
		DumpInfo();
		// TODO Evaluate Instruction RAKAAN
		DumpInfo();
		if(debug)
		{
			DumpArray();
		}
		PC++;
	}

	private static void evaluateIOType() 
	{
		DumpInfo();
		// TODO Evaluate Instruction RAKAAN
		DumpInfo();
		if(debug)
		{
			DumpArray();
		}
		PC++;
	}
	
	private static void DumpArray()
	{
		// TODO Format Array OSCAR
		System.out.println("Program MEM");
		int last = CompleteModuleLength + initialLoadAddr +34;
		
		int printcount = 0;
		
		for(int i = initialLoadAddr+1 ; i < last-1 ; i++){
		
				if(printcount % 8 == 0)
				{
					System.out.println();
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
			e.printStackTrace();
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
		//need to flesh this out, but each method to check operands should just return a boolean 
		//max of 8 hex digits, fill in with signed value in front (either 0 or F)
		boolean result = true;
		for(int i =0; i <hex.length();i++){
			if (!(Character.isDigit(hex.charAt(i))|| hex.charAt(i)== 'A' || hex.charAt(i)== 'B'|| hex.charAt(i)== 'C' || hex.charAt(i)== 'D' || hex.charAt(i)== 'E' || hex.charAt(i)== 'F')){
				result = false;
			}
		}
		return (result);
	}
	
}
