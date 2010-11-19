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
	static ArrayList<String> MEM = new ArrayList<String>();
	static ArrayList<String> LoadModule = new ArrayList<String>();
	String opcode = "";
	ArrayList<Error> Errors = new  ArrayList<Error>();
	static boolean debug = false;
	static int PC = 0;
	static String binaryInstruction = "";
	public static ArrayList<String> GSymbTableArray = new ArrayList<String>();
	public static GlobalSymbolTable GSymbTable = new GlobalSymbolTable();
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
		
		// TODO Input our Load Module as .txt and generate memory array OSCAR
		instructionType type;
		DumpArray();
		if(args.length == 2 )
		{
		LoadModule = readFileToArrayList(args[0]);
		GSymbTableArray = readFileToArrayList(args[1]);
		}
		else{
			System.out.println("Unspecified Load File name");
		}
		
		/*
		 * get Symbol table array into GSymbTable 
		 * 
		 */
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
		
		// TODO Handle Linker Header Record as first object in LoadModule Array KERMIT
		
		// TODO Handle Linker End Record as last object in LoadModule Array (Everything else is text records) RAKAAN
		
		// Load any int.data,bin.data,hex.data.str.data values into memory array
		type = null;
		
			
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
		
		DumpArray();
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
		
		for(int i = 0; i < MEM.size() - 1; i++)
		{
			System.out.println(MEM.get(i));
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
}
