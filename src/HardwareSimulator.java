import java.util.ArrayList;

public class HardwareSimulator {

	int[] registers = new int[15];
	static ArrayList<String> MEM = new ArrayList<String>();
	static ArrayList<String> LoadModule = new ArrayList<String>();
	String opcode = "";
	ArrayList<Error> Errors = new  ArrayList<Error>();
	static boolean debug = false;
	static int PC = 0;
	static String binaryInstruction = "";
	public static enum instructionType
	{
		 R, S, J, IO;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		instructionType type;
		DumpArray();
		
		// Input Global Symbol Table from .txt into SymbolTable-type object
		
		// TODO Input our Load Module as .txt and generate memory array OSCAR
		
		// TODO Handle Linker Header Record as first object in LoadModule Array KERMIT
		// TODO Handle Linker End Record as last object in LoadModule Array (Everything else is text records) RAKAAN
		
		// Load any int.data,bin.data,hex.data.str.data values into memory array
		
		
			
		for(int i = 0; i < Integer.MAX_VALUE; i++)
		{
			type = null;
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
}
