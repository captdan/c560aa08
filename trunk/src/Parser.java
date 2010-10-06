import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList; 
//import java.util.Arrays;
import java.util.StringTokenizer;

public class Parser
{


	public static ArrayList<CodeLine> CodeLineArray = new ArrayList<CodeLine>();
	public static ArrayList<Directive> DirectivesArray = new ArrayList<Directive>();
	public static ArrayList<Instruction> InstructionsArray = new ArrayList<Instruction>();
	public static ArrayList<Error> ErrorArray = new ArrayList<Error>();
	public static SymbolTable SymbTable = new SymbolTable();
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		//System.out.println("test");
		
		fillErrorArray("src/ErrorCodes.txt");
		fillInstructionsArray("MOT_TABBED.txt");
		
		ArrayList<String> linesOfCode = readFileToArrayList("src/asmCode1.txt");
		for(String lineOfCode : linesOfCode)
		{
			//System.out.println(lineOfCode);
			CodeLineArray.add(parseCodeLine(lineOfCode));
		}
	}
	/**
	 * @param lineOfCode takes a string line of code to be parsed
	 * 
	 * This is where we will parse each line of code.
	 * ALL directive testing / checking will be in this function
	*/
	public static CodeLine parseCodeLine(String lineOfCode) 
	{
		//Create an instance of CodeLine Supplying it with original line of code.
		CodeLine cl = new CodeLine(lineOfCode);
		
		//Check to see if the line contains a comment if so parse it.
		String[] result = lineOfCode.split("\\|");
		//Grab anything that is before the comment and set it aside to be parsed.
		String lineOfCodeMinusComment = result[0];
		
		//System.out.println(Arrays.toString(result));

		//Check to see if a comment exists.
		if(result.length > 1)
		{
			result[0] = "";
			cl.comment = joinStringArray(result,""); 
			//If more than one "|" then join them all as one comment
		}
		//To access the code line minus the comment use the string variable lineOfCodeMinusComment
		//this can be were directive checking comes in
		StringTokenizer st = new StringTokenizer(lineOfCodeMinusComment," \t",false);
		
		if (st.hasMoreTokens() == true) 
		{
			if(validInstructionWithOperands(lineOfCodeMinusComment))
			{
				//Extract Valid Features
			}
			else if(validDirective(lineOfCodeMinusComment))
			{
				//Extract Valid Features
			}
			else if(validSymbolInstruction(lineOfCodeMinusComment))
			{
				//Extract Valid Features
			}
			else
			{
				//ERROR, must fall within the three categories
			}

			//else if instruction is in symbol table

		}

		return cl;

	}
	
	public static Boolean validDirective(String instruction)
	{
		Boolean validDirective = false;
		
		//Code to check directives
		
		return validDirective;
	}
	
	public static Boolean validSymbolInstruction(String instruction)
	{
		Boolean validSymbolInstruction = false;
		StringTokenizer st = new StringTokenizer(instruction," \t",false);
		String symbol = st.nextToken();
		
		String commandMinusSymbol = "";
		while (st.hasMoreTokens()) 
		{
			commandMinusSymbol += st.nextToken();
		}
		
		if(validInstructionWithOperands(commandMinusSymbol))
		{
			validSymbolInstruction = true;
		}
		else if(validDirective(commandMinusSymbol))
		{
			validSymbolInstruction = true;
		}
		
		return validSymbolInstruction;
	}
	
	public static Boolean validInstructionWithOperands(String instructionWithOperands)
	{
		Boolean validInstructionWithOperands = true;
		StringTokenizer st = new StringTokenizer(instructionWithOperands," \t",false);
		String instruction = st.nextToken();
		instruction = instruction.toUpperCase();
		if(instructionExists(instruction) == true)
		{
			String operandString = "";
			while (st.hasMoreTokens()) 
			{
				operandString += st.nextToken();
			}
			String[] operands = operandString.split(",");
			if(operands.length == returnInstruction(instruction).numberOfRegisters)
			{
				for(int x=0;x<returnInstruction(instruction).operands.size();x++)
				{
					Instruction.operandTypes operand = returnInstruction(instruction).operands.get(x);
					switch(operand)
					{
					case ADDRESS: /*test for valid ADDRESS*/;break;
					case BIT: /*test for valid BITS*/;break;
					case BITS: /*test for valid BITS*/;break;
					case IMMEDIATE: /*test for valid IMMEDIATE*/;break;
					case REGISTER: validInstructionWithOperands = isRegister(operands[x]);break;
					default: validInstructionWithOperands = false;break;
					}
				}
			}
		}
		else
		{
			validInstructionWithOperands = false;
		}
		return validInstructionWithOperands;
	}
	
	/*
	 	cl.instruction = returnInstruction(instruction);
		
		String operandString = "";
		while (st.hasMoreTokens()) 
		{
			operandString += st.nextToken();
		}
		String[] operands = operandString.split(",");
		
		if(operands.length == cl.instruction.numberOfRegisters)
		{
			//If valid number of operands
			
			//Check if Register or if symbol or number
			System.out.println(cl.originalLineOfCode);
			
			
			//checks each operand against the symbol table.
			//if in table, adds the relevant value for the operand to the codeline.
			for(int j = 0; j < operands.length; j++)
			{
				if(SymbTable.isInTable(operands[j]))
				{
					String[] symbolInfo = SymbTable.getInfoFromSymbol(operands[j]);
					if(symbolInfo[2].equals("equ"))
					{
						cl.operands.add(new Operand(symbolInfo[1]));
					}
					else if(symbolInfo[2].equals("data"))
					{
						//TODO should it refer to a memory address or to the information stored there?
					}
					else
					{
						cl.operands.add(new Operand(symbolInfo[0]));
					}
				}
			}
			
		}
		else
		{
			
			//System.out.println(cl.originalLineOfCode + "  :  " + returnError(0).message );
			cl.errors.add(returnError(0)); //Adds error to line of code
		}
	 */

	/**
	 * Function that joins a String array by a delimiter.
	 * @param stringArray Is passed a string array that is to be joined
	 * @param delimiter This is what string should be placed the elements joined in the stringArray
	 * @return Returns a String that is the joined array plus delimiters
	 */
	public static String joinStringArray(String[] stringArray, String delimiter)
	{
		String totalString = stringArray[0];
		for(int x=1;x<stringArray.length;x++)
		{
			totalString += (delimiter + stringArray[x]);
		}
		return totalString;
	}
	
	/**
	 * @param fileName Is passed the file location of the file to be read and converted into a string array
	 * @return Returns an ArrayList<String> where each string contains one line of code in order.
	 * 
	 * Function that returns an ArrayList of Strings where each
	 * line in the the file is a string.
	 */
	public static ArrayList<String> readFileToArrayList(String fileName)
	{
		ArrayList<String> linesOfCode = new ArrayList<String>();
	    try 
		{
	    FileInputStream fileInputStream = new FileInputStream(fileName);
	    DataInputStream dataInputStream = new DataInputStream(fileInputStream);
	    BufferedReader buffer = new BufferedReader(new InputStreamReader(dataInputStream));
	    
	    String lineOfCode;
	    
	    while (( lineOfCode = buffer.readLine()) != null)   
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
	
	/**
	 * @param fileName This is the file location of the instruction table 
	 * 
	 * This function reads a specified instruction file we have formatted
	 * and it adds the data from the specified instruction file into the
	 * public static InstructionArray. This allows us to easily check to
	 * see if an instruction is in the correct format.
	 */
	public static void fillInstructionsArray(String fileName)
	{
		ArrayList<String> linesOfInstruction = readFileToArrayList(fileName);
		for(String lineOfInstruction : linesOfInstruction)
		{
			String[] variables = lineOfInstruction.split("\t");
			
			if(variables.length > 1)
			{	
				Instruction.instructionTypes instructionType = Instruction.instructionTypes.IMMEDIATE;
				 if (variables[5].equals("I"))
				 {
					 instructionType = Instruction.instructionTypes.IMMEDIATE;
				 }
				 else if(variables[5].equals("R"))
				 {
					 instructionType = Instruction.instructionTypes.REGISTER;
				 }
				 else if(variables[5].equals("S"))
				 {
					 instructionType = Instruction.instructionTypes.SIGNED;
				 }

				variables[4] = variables[4].replaceAll("\"", "");

				//System.out.println(variables[4]);
				String[] operands = variables[4].split(",");
				ArrayList<Instruction.operandTypes> operandArray = new ArrayList<Instruction.operandTypes>();
				for(String operand : operands)
				{
					operand = operand.toUpperCase();
					 if (operand.equals("REG"))
					 {
						 operandArray.add(Instruction.operandTypes.REGISTER);
					 }
					 else if(operand.equals("IMM"))
					 {
						 operandArray.add(Instruction.operandTypes.IMMEDIATE);
					 }
					 else if(operand.equals("BIT"))
					 {
						 operandArray.add(Instruction.operandTypes.BIT);
					 }
					 else if(operand.equals("BITS"))
					 {
						 operandArray.add(Instruction.operandTypes.BITS);
					 }
					 else if(operand.equals("ADDR"))
					 {
						 operandArray.add(Instruction.operandTypes.ADDRESS);
					 }
				}
				Instruction instruction = new Instruction(variables[0],variables[1],variables[3],operandArray,instructionType);
				InstructionsArray.add(instruction);
			}
		}
	}
	
	/**
	 * @param The file location of the error table is passed to fill the error arraylist.
	 * 
	 * This function reads a specified error file we have formatted
	 * and it adds the data from the specified instruction file into the
	 * public static ErrorArray. This allows us to easily check to
	 * see if an error is in the correct format.
	 */
	public static void fillErrorArray(String fileName)
	{
		ArrayList<String> linesOfError = readFileToArrayList(fileName);
		for(String lineOfError : linesOfError)
		{
			//01	23	SUBU	REG,REG,REG U
			String[] variables = lineOfError.split("\t");
			if(variables.length > 1)
			{
				Error error = new Error();
				error.CreateError(Integer.parseInt(variables[0]),variables[1]);
				ErrorArray.add(error);
			}
		}
	}
	
	/**
	 * 
	 * @param ErrorId The error id number associated with the error
	 * @return Returns an instance of the Error class object for the specified error. 
	 */
	public static Error returnError(int ErrorId)
	{
		Error returnError = new Error();
		for(Error error : ErrorArray)
		{	
			if(ErrorId == error.number)
			{
				returnError = error;
			}
		}
		return returnError;
	}
	
	/**
	 * 
	 * @param instructionString The text instruction name that is to be looked up.
	 * @return Returns s boolean value depending on the existance of the instruction.
	 */
	public static boolean instructionExists(String instructionString)
	{
		
		Boolean instructionExists = false;
		for(Instruction instruction : InstructionsArray)
		{	
			if(instructionString.toUpperCase().equals(instruction.instruction.toUpperCase()))
			{
				instructionExists = true;
			}
		}
		return instructionExists;
	}
	/**
	 * 
	 * @param instructionString The text instruction name that is to be returned.
	 * @return Returns an instance of the instruction class for the specified instruction.
	 */
	public static Instruction returnInstruction(String instructionString)
	{
		Instruction returnInstruction = new Instruction();
		
		for(Instruction instruction : InstructionsArray)
		{	
			if(instructionString.toUpperCase().equals(instruction.instruction.toUpperCase()))
			{
				returnInstruction = instruction;
			}
		}
		return returnInstruction;
	}
	
	/**
	 * 
	 * @param operandString The string of the operand of an instruction.
	 * @return Returns is the operand is a valid register or not.
	 */
	public static Boolean isRegister(String operandString)
	{
		Boolean isRegister = false;
		if (operandString.length() == 3)
		{
			int registerNumber = Integer.parseInt(Character.toString(operandString.charAt(3)));
			if(operandString.charAt(0) == '$' && (operandString.charAt(2) == 'r' || operandString.charAt(1) == 'R') &&  registerNumber >= 0 && registerNumber < 8)
			{
				isRegister = true;
			}
		}

		return isRegister;
	}
	
	/**
	 * @param fileName This is the file location of the instruction table 
	 * 
	 * This function reads a specified instruction file we have formatted
	 * and it adds the data from the specified instruction file into the
	 * public static InstructionArray. This allows us to easily check to
	 * see if an instruction is in the correct format.
	 */
	public static void fillDirectivesArray(String fileName)
	{
		ArrayList<String> linesOfInstruction = readFileToArrayList(fileName);
		for(String lineOfInstruction : linesOfInstruction)
		{
			String[] variables = lineOfInstruction.split("\t");
			
			if(variables.length > 1)
			{	
				Instruction.instructionTypes instructionType = Instruction.instructionTypes.IMMEDIATE;
				 if (variables[5].equals("I"))
				 {
					 instructionType = Instruction.instructionTypes.IMMEDIATE;
				 }
				 else if(variables[5].equals("R"))
				 {
					 instructionType = Instruction.instructionTypes.REGISTER;
				 }
				 else if(variables[5].equals("S"))
				 {
					 instructionType = Instruction.instructionTypes.SIGNED;
				 }

				variables[4] = variables[4].replaceAll("\"", "");

				//System.out.println(variables[4]);
				String[] operands = variables[4].split(",");
				ArrayList<Instruction.operandTypes> operandArray = new ArrayList<Instruction.operandTypes>();
				for(String operand : operands)
				{
					operand = operand.toUpperCase();
					 if (operand.equals("REG"))
					 {
						 operandArray.add(Instruction.operandTypes.REGISTER);
					 }
					 else if(operand.equals("IMM"))
					 {
						 operandArray.add(Instruction.operandTypes.IMMEDIATE);
					 }
					 else if(operand.equals("BIT"))
					 {
						 operandArray.add(Instruction.operandTypes.BIT);
					 }
					 else if(operand.equals("BITS"))
					 {
						 operandArray.add(Instruction.operandTypes.BITS);
					 }
					 else if(operand.equals("ADDR"))
					 {
						 operandArray.add(Instruction.operandTypes.ADDRESS);
					 }
				}
				Instruction instruction = new Instruction(variables[0],variables[1],variables[3],operandArray,instructionType);
				InstructionsArray.add(instruction);
			}
		}
	}
}

