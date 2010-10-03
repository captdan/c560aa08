import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList; 
import java.util.Arrays;
import java.util.StringTokenizer;

public class main
{

	/**
	 * @param args
	 */
	public static ArrayList<CodeLine> CodeLineArray = new ArrayList<CodeLine>();
	public static ArrayList<Instruction> InstructionsArray = new ArrayList<Instruction>();
	public static ArrayList<Error> ErrorArray = new ArrayList<Error>();
	
	public static void main(String[] args)
	{
		//System.out.println("test");
		
		fillInstructionsArray("src/Instructions.txt");
		
		ArrayList<String> linesOfCode = readFileToArrayList("src/asmCode1.txt");
		for(String lineOfCode : linesOfCode)
		{
			//System.out.println(lineOfCode);
			CodeLineArray.add(parseCodeLine(lineOfCode));
		}
	}
	
	//This is where we will parse each line of code.
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
		
		String tempstring = lineOfCodeMinusComment;
		StringTokenizer st = new StringTokenizer(lineOfCodeMinusComment," \t",false);
		
		if (st.hasMoreTokens() == true) 
		{
			String instruction = "";
			//String instruction = st.nextToken();
			//instruction = instruction.toUpperCase();
			
			System.out.println(tempstring);
			
			/*
			while (st.hasMoreTokens()) 
			{
				System.out.println(st.nextToken());
			}
			*/
			
			if(instructionExists(instruction) == true)
			{
				cl.instruction = returnInstruction(instruction);
				String operandString = "";
				while (st.hasMoreTokens()) 
				{
					operandString += st.nextToken();
				}
				String[] operands = operandString.split(",");
				
				if(operands.length == cl.instruction.numberOfRegisters )
				{
					System.out.println(cl.originalLineOfCode);
						
					
					
				}
				else
				{
					cl.errors.add(returnError(0));
				}
			}
			//else if instruction is in symbol table
			
			
		}

		return cl;

	}

	//Function that joins a String array by a delimiter.
	public static String joinStringArray(String[] stringArray, String delimiter)
	{
		String totalString = stringArray[0];
		for(int x=1;x<stringArray.length;x++)
		{
			totalString += (delimiter + stringArray[x]);
		}
		return totalString;
	}
	
	/*
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
	
	/*
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
			//01	23	SUBU	REG,REG,REG U
			String[] variables = lineOfInstruction.split("\t");
			Boolean signed = false;
			
			if(variables.length > 1)
			{
				//System.out.println(lineOfInstruction);
				if(variables[4]=="S")
				{
					signed = true;
				}
				
				String[] operands = variables[3].split(",");
				ArrayList<Instruction.operandTypes> operandArray = new ArrayList<Instruction.operandTypes>();
				for(String operand : operands)
				{
					operand = operand.toUpperCase();
					 if (operand == "REG")
					 {
						 operandArray.add(Instruction.operandTypes.REGISTER);
					 }
					 else if(operand == "IM")
					 {
						 operandArray.add(Instruction.operandTypes.IMMEDIATE);
					 }
					 else if(operand == "AMT")
					 {
						 operandArray.add(Instruction.operandTypes.AMOUNT);
					 }
					 else if(operand == "NUM")
					 {
						 operandArray.add(Instruction.operandTypes.NUMBER);
					 }
					 else if(operand == "ADD")
					 {
						 operandArray.add(Instruction.operandTypes.ADDRESS);
					 }
				}
				Instruction instruction = new Instruction(variables[2],variables[0],variables[1],operandArray,signed);
				InstructionsArray.add(instruction);
			}
		}
	}
	
	/*
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
	
	public static boolean instructionExists(String instructionString)
	{
		Boolean instructionExists = false;
		for(Instruction instruction : InstructionsArray)
		{	
			if(instructionString.toUpperCase() == instruction.instruction.toUpperCase())
			{
				instructionExists = true;
			}
		}
		return instructionExists;
	}
	
	public static Instruction returnInstruction(String instructionString)
	{
		Instruction returnInstruction = new Instruction();
		for(Instruction instruction : InstructionsArray)
		{	
			if(instructionString.toUpperCase() == instruction.instruction.toUpperCase())
			{
				returnInstruction = instruction;
			}
		}
		return returnInstruction;
	}
	
	public static Boolean isRegister(String operandString)
	{
		Boolean isRegister = false;
		if (operandString.length() == 3)
		{
			int registerNumber = Integer.parseInt(Character.toString(operandString.charAt(3)));
			if(operandString.charAt(0) == '$' && (operandString.charAt(2) == 'r' || operandString.charAt(1) == 'R') &&  registerNumber > 0 && registerNumber < 9)
			{
				isRegister = true;
			}
		}

		return isRegister;
	}
}

