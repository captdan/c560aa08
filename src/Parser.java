import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList; //import java.util.Arrays;
//import java.util.Arrays;
import java.util.StringTokenizer;

public class Parser 
{
	public static LiteralTable litTable = new LiteralTable();
	public static int startingLocation = 0;
	public static String programName = "";
	public static Boolean endProgram = false;
	public static Directive.codeLocations codeLocation = null;
	public static ArrayList<CodeLine> CodeLineArray = new ArrayList<CodeLine>();
	public static ArrayList<Directive> DirectivesArray = new ArrayList<Directive>();
	public static ArrayList<Instruction> InstructionsArray = new ArrayList<Instruction>();
	public static ArrayList<Error> ErrorArray = new ArrayList<Error>();
	public static SymbolTable SymbTable = new SymbolTable();
	/**
	 * 
	 * PC is the Program Counter that will be updated as Pass 1 processes each
	 * line of code from the source.
	 */
	public static Integer PC = 0;
	/**
	 * execStart indicates where the execution in the program starts as is
	 * indicated by directive Exec.start. This value is later used in the object
	 * report after pass 2
	 */
	public static int execStart = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		fillDirectivesArray("Directives.txt");
		fillErrorArray("src/ErrorCodes.txt");
		fillInstructionsArray("MOT_TABBED.txt");

		// readFileToArrayList("src/asmCode1.txt");
		ArrayList<String> linesOfCode = readFileToArrayList("src/AddTest.txt");
		for (String lineOfCode : linesOfCode) 
		{
			// System.out.println(lineOfCode);
			if (endProgram == false) 
			{
				CodeLineArray.add(parseCodeLine(lineOfCode));
			}
		}
		
		SymbTable.prettyFerret();
	}

	/**
	 * @param lineOfCode
	 *            takes a string line of code to be parsed
	 * 
	 *            This is where we will parse each line of code. ALL directive
	 *            testing / checking will be in this function
	 */
	public static CodeLine parseCodeLine(String lineOfCode) 
	{
		// Create an instance of CodeLine Supplying it with original line of
		// code.
		CodeLine cl = new CodeLine(lineOfCode);
		// Check to see if the line contains a comment if so parse it.
		String[] result = lineOfCode.split("\\|");
		// System.out.println(result.length);
		// Grab anything that is before the comment and set it aside to be
		// parsed.
		String lineOfCodeMinusComment = "";
		// System.out.println(Arrays.toString(result));

		// Check to see if a comment exists.
		if (result.length > 0) 
		{
			lineOfCodeMinusComment = result[0];
			result[0] = "";
			cl.comment += joinStringArray(result, "|");
			// If more than one "|" then join them all as one comment
		}
		// To access the code line minus the comment use the string variable
		// lineOfCodeMinusComment
		// this can be were directive checking comes in
		StringTokenizer st = new StringTokenizer(lineOfCodeMinusComment, " \t",false);
		System.out.println("Line: " + lineOfCodeMinusComment);

		if (st.hasMoreTokens() == true) 
		{
			if (returnInstruction(lineOfCodeMinusComment) != null) 
			{
				System.out.println("Instruction: " + lineOfCodeMinusComment);
				cl.instruction = returnInstruction(lineOfCodeMinusComment);
				// Extract Valid Features
			} 
			else if (returnDirective(lineOfCodeMinusComment) != null) 
			{
				cl.directive = returnDirective(lineOfCodeMinusComment);
				// Extract Valid Features
				System.out.println("Directive: " + lineOfCodeMinusComment);
			} 
			else if (returnSymbolInstruction(lineOfCodeMinusComment) != null) 
			{
				StringTokenizer st2 = new StringTokenizer(lineOfCodeMinusComment, " \t",false);
				String symbol = st2.nextToken();
				System.out.println("Symbol: " + lineOfCodeMinusComment);
				SymbTable.addSymbol(symbol, PC.toString(), "NONE", SymbolTable.Uses.DATA_LABEL);
				if (returnSymbolInstruction(lineOfCodeMinusComment).getClass() == Directive.class) 
				{
					
					cl.directive = (Directive) returnSymbolInstruction(lineOfCodeMinusComment);
					
				} 
				else if (returnSymbolInstruction(lineOfCodeMinusComment).getClass() == Instruction.class)
				{
					cl.instruction = (Instruction) returnSymbolInstruction(lineOfCodeMinusComment);
					
				}
				// Extract Valid Features
			} 
			else 
			{
				System.out.println("ERROR: " + lineOfCodeMinusComment);
				// ERROR, must fall within the three categories
			}

			// else if instruction is in symbol table

		}
		/**
		 * After each codeLine object is processed, we grab its length and add
		 * it to our global Program Counter.
		 */
		int length = cl.lineLength();
		PC += length;
		return cl;

	}

	public static Directive returnDirective(String codeString) 
	{
		Directive directiveObj = new Directive();

		StringTokenizer st = new StringTokenizer(codeString, " \t", false);
		String possibleDirective = st.nextToken();
		possibleDirective = possibleDirective.toUpperCase();
		for (Directive directive : DirectivesArray) {
			if (directive.directiveName.toUpperCase().equals(possibleDirective.toUpperCase())) 
			{
				directiveObj.directiveName = possibleDirective.toUpperCase();
				String operandString = "";
				while (st.hasMoreTokens()) 
				{
					operandString += st.nextToken();
				}
				String[] operands = operandString.split(",");

				for (int x = 0; x < operands.length; x++) 
				{
					directiveObj.operandArray.add(new Operand(operands[x]));
				}
			}
		}
		String[] specialDirectives = possibleDirective.split(",");
		if (specialDirectives[0].equals(".END")) 
		{
			specialDirectives = removeWhiteSpace(codeString).split(",");
			if (specialDirectives.length == 2) 
			{
				if (programName.equals(specialDirectives[1])) 
				{
					directiveObj.directiveName = ".END";
					endProgram = true;
				}
				else
				{
					//ERROR not same as start code.
				}
			}
		} else if (specialDirectives[0].equals(".START")) 
		{
			specialDirectives = removeWhiteSpace(codeString).split(",");
			if (specialDirectives.length == 3) 
			{
				directiveObj.directiveName = ".START";
				programName = specialDirectives[1];
				startingLocation = Integer.valueOf(specialDirectives[2]);
				PC = startingLocation;
				SymbTable.addSymbol(programName, PC.toString(), "NONE", SymbolTable.Uses.PROGRAM_NAME);
			}
		}
		if (specialDirectives[0].equals("Reset.lc")) 
		{
			directiveObj.directiveName = possibleDirective.toUpperCase();
			int resetValue = Integer.valueOf(st.nextToken());
			if (resetValue > PC) 
			{
				PC = resetValue;
			} 
			else 
			{
				// System.out.println("Error reset value must be larger than PC");
			}
		} 
		else if (specialDirectives[0].equals("Exec.start")) 
		{
			directiveObj.directiveName = possibleDirective.toUpperCase();
			int execValue = Integer.valueOf(st.nextToken());
			execStart = execValue;
		} 
		else if (specialDirectives[0].equals("MEM.SKIP")) 
		{
			directiveObj.directiveName = possibleDirective.toUpperCase();
			int skipValue = Integer.valueOf(st.nextToken());
			if (PC + skipValue < 65536) 
			{
				PC += skipValue;
			} 
			else 
			{
				// System.out.println("Error not enough memory");
			}
		}
		//TODO test this get it working
		/*
		if(symb.hasMoreTokens())
		{
			String directive = symb.nextToken().toUpperCase();
			if(directive.equals("EQU") || directive.equals("EQU.EXT"))
			{
				String EQU = "";
			
				while(symb.hasMoreTokens())
				{
					EQU += symb.nextToken();
				}
				SymbTable.addSymbol(symbol, PC.toString(), EQU, SymbolTable.Uses.EQU);
			}
			else
			{
				SymbTable.addSymbol(symbol, PC.toString(), "NONE", SymbolTable.Uses.DATA_LABEL);
			}
		}
		*/
		if (directiveObj.directiveName == "") 
		{
			directiveObj = null;
		}
		// Code to check special directives e.g. .end and .start
		return directiveObj;
	}

	public static String removeWhiteSpace(String InputString) 
	{
		InputString = InputString.replaceAll(" ", "");
		InputString = InputString.replaceAll("\t", "");
		return InputString;
	}

	public static Object returnSymbolInstruction(String instruction)
	{
		Object symbolObj = null;
		StringTokenizer st = new StringTokenizer(instruction," \t",false);
		st.nextToken();
		String commandMinusSymbol = "";
		
		while (st.hasMoreTokens()) 
		{
			commandMinusSymbol += " " + st.nextToken();
		}
		
		if(returnInstruction(commandMinusSymbol) != null)
		{
			// if the rest of the line is an Instruction, then the symbol must be a label
			
			symbolObj = returnInstruction(commandMinusSymbol);
		}
		else if(returnDirective(commandMinusSymbol) != null)
		{
			// if it's a directive, it could be EQU or DATA
			
			if(returnDirective(commandMinusSymbol).labelType != Directive.labelTypes.NOLABEL)
			{
				symbolObj = returnDirective(commandMinusSymbol);
			}
			// the first thing should be a directive

		}
		else
		{
			symbolObj = null;
		}
		
		return symbolObj;
	}

	public static Instruction returnInstruction(String instructionWithOperands) 
	{
		Instruction instructionObj = new Instruction();
		StringTokenizer st = new StringTokenizer(instructionWithOperands," \t", false);
		String instruction = st.nextToken();
		
		instruction = instruction.toUpperCase();
		if (instructionExists(instruction) == true && returnSymbolInstruction(instructionWithOperands) == null) 
		{
			// System.out.println(instruction);
			String operandString = "";
			while (st.hasMoreTokens()) 
			{
				operandString += st.nextToken();
			}
			String[] operands = operandString.split(",");
			if (operands.length == returnInstructionViaOpcode(instruction).numberOfRegisters) 
			{
				instructionObj.instruction = instruction.toUpperCase();
				/*
				Boolean validOperands = false;
				for (int x = 0; x < returnInstructionViaOpcode(instruction).operands.size(); x++) 
				{
					instructionObj.operandsArray.add(new Operand(operands[x]));
					Instruction.operandTypes operand = returnInstruction(instruction).operands.get(x);
					
					switch (operand)
					{
					case ADDRESS: // test for valid ADDRESS 
						break;
					case BIT: // test for valid BITS 
						break;
					case BITS: // test for valid BITS 
						break;
					case IMMEDIATE: // test for valid IMMEDIATE 
						validOperands = isLiteral(operands[x]);
						break;
					case REGISTER: validOperands = isRegister(operands[x]);
					break;
					default:
						validOperands = false;
						break;
					}
				}
				*/
			}
		} 
		else 
		{
			instructionObj = null;
			// System.out.println(instruction);
		}

		return instructionObj;
	}

	/*
	 * cl.instruction = returnInstruction(instruction); String operandString =
	 * ""; while (st.hasMoreTokens()) { operandString += st.nextToken(); }
	 * String[] operands = operandString.split(","); if(operands.length ==
	 * cl.instruction.numberOfRegisters) { //If valid number of operands //Check
	 * if Register or if symbol or number
	 * System.out.println(cl.originalLineOfCode); //checks each operand against
	 * the symbol table. //if in table, adds the relevant value for the operand
	 * to the codeline. for(int j = 0; j < operands.length; j++) {
	 * if(SymbTable.isInTable(operands[j])) { String[] symbolInfo =
	 * SymbTable.getInfoFromSymbol(operands[j]); if(symbolInfo[2].equals("equ"))
	 * { cl.operands.add(new Operand(symbolInfo[1])); } else
	 * if(symbolInfo[2].equals("data")) {  } else { cl.operands.add(new
	 * Operand(symbolInfo[0])); } } } } else {
	 * //System.out.println(cl.originalLineOfCode + "  :  " +
	 * returnError(0).message ); cl.errors.add(returnError(0)); //Adds error to
	 * line of code }
	 */

	/**
	 * Function that joins a String array by a delimiter.
	 * 
	 * @param stringArray
	 *            Is passed a string array that is to be joined
	 * @param delimiter
	 *            This is what string should be placed the elements joined in
	 *            the stringArray
	 * @return Returns a String that is the joined array plus delimiters
	 */
	public static String joinStringArray(String[] stringArray, String delimiter) 
	{
		String totalString = stringArray[0];
		for (int x = 1; x < stringArray.length; x++) 
		{
			totalString += (delimiter + stringArray[x]);
		}
		return totalString;
	}

	/**
	 * @param fileName
	 *            Is passed the file location of the file to be read and
	 *            converted into a string array
	 * @return Returns an ArrayList<String> where each string contains one line
	 *         of code in order.
	 * 
	 *         Function that returns an ArrayList of Strings where each line in
	 *         the the file is a string.
	 */
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

	/**
	 * @param fileName
	 *            This is the file location of the instruction table
	 * 
	 *            This function reads a specified instruction file we have
	 *            formatted and it adds the data from the specified instruction
	 *            file into the public static InstructionArray. This allows us
	 *            to easily check to see if an instruction is in the correct
	 *            format.
	 */
	public static void fillInstructionsArray(String fileName) 
	{
		ArrayList<String> linesOfInstruction = readFileToArrayList(fileName);
		for (String lineOfInstruction : linesOfInstruction) 
		{
			String[] variables = lineOfInstruction.split("\t");
			if (variables.length > 1) 
			{
				Instruction.instructionTypes instructionType = Instruction.instructionTypes.IMMEDIATE;
				if (variables[5].equals("I")) 
				{
					instructionType = Instruction.instructionTypes.IMMEDIATE;
				} 
				else if (variables[5].equals("R")) 
				{
					instructionType = Instruction.instructionTypes.REGISTER;
				} 
				else if (variables[5].equals("S")) 
				{
					instructionType = Instruction.instructionTypes.SIGNED;
				}

				variables[4] = variables[4].replaceAll("\"", "");

				// System.out.println(variables[4]);
				String[] operands = variables[4].split(",");
				ArrayList<Instruction.operandTypes> operandArray = new ArrayList<Instruction.operandTypes>();
				for (String operand : operands) {
					operand = operand.toUpperCase();
					if (operand.equals("REG")) 
					{
						operandArray.add(Instruction.operandTypes.REGISTER);
					} 
					else if (operand.equals("IMM")) 
					{
						operandArray.add(Instruction.operandTypes.IMMEDIATE);
					} 
					else if (operand.equals("BIT")) 
					{
						operandArray.add(Instruction.operandTypes.BIT);
					} 
					else if (operand.equals("BITS")) 
					{
						operandArray.add(Instruction.operandTypes.BITS);
					} 
					else if (operand.equals("ADDR"))
					{
						operandArray.add(Instruction.operandTypes.ADDRESS);
					}
				}
				Instruction instruction = new Instruction(variables[0],variables[1], variables[3], operandArray,instructionType);
				InstructionsArray.add(instruction);
			}
		}
	}

	/**
	 * @param The
	 *            file location of the error table is passed to fill the error
	 *            arraylist.
	 * 
	 *            This function reads a specified error file we have formatted
	 *            and it adds the data from the specified instruction file into
	 *            the public static ErrorArray. This allows us to easily check
	 *            to see if an error is in the correct format.
	 */
public static void fillErrorArray(String fileName)
{
	ArrayList<String> linesOfError = readFileToArrayList(fileName);
	for(String lineOfError : linesOfError)
	{
	//01
	//23 SUBU
	//REG,REG,REG U
		String[] variables = lineOfError.split("\t");
		if(variables.length == 3)
		{
			Error error = new Error();
			error.CreateError(Integer.parseInt(variables[0]),variables[1], variables[2]);
			ErrorArray.add(error);
		}
	}
}

	/**
	 * 
	 * @param ErrorId
	 *            The error id number associated with the error
	 * @return Returns an instance of the Error class object for the specified
	 *         error.
	 */
	public static Error returnError(int ErrorId) 
	{
		Error returnError = new Error();
		for (Error error : ErrorArray) 
		{
			if (ErrorId == error.number) 
			{
				returnError = error;
			}
		}
		return returnError;
	}

	/**
	 * 
	 * @param instructionString
	 *            The text instruction name that is to be looked up.
	 * @return Returns s boolean value depending on the existance of the
	 *         instruction.
	 */
	public static boolean instructionExists(String instructionString) 
	{
		Boolean instructionExists = false;
		for (Instruction instruction : InstructionsArray) 
		{
			if (instructionString.toUpperCase().equals(instruction.instruction.toUpperCase())) 
			{
				instructionExists = true;
			}
		}
		return instructionExists;
	}

	/**
	 * 
	 * @param instructionString
	 *            The text instruction name that is to be returned.
	 * @return Returns an instance of the instruction class for the specified
	 *         instruction.
	 */
	public static Instruction returnInstructionViaOpcode(String instructionString) 
	{
		Instruction returnInstruction = new Instruction();
		for (Instruction instruction : InstructionsArray) 
		{
			if (instructionString.toUpperCase().equals(instruction.instruction.toUpperCase())) 
			{
				returnInstruction = instruction;
			}
		}
		return returnInstruction;
	}

	/**
	 * 
	 * @param operandString
	 *            The string of the operand of an instruction.
	 * @return Returns is the operand is a valid register or not.
	 */
	public static Boolean isRegister(String operandString) 
	{
		Boolean isRegister = false;
		if (operandString.length() == 3) 
		{
			int registerNumber = Integer.parseInt(Character.toString(operandString.charAt(1)));
			if (operandString.charAt(0) == '$' && registerNumber >= 0 && registerNumber < 8) 
			{
				isRegister = true;
			}
		}

		return isRegister;
	}
	
	/**
	 * Checks to see if an operand is a valid literal. If so, it is added to the literal table.
	 * 
	 * @param operandString
	 * 			The string containing the operand to be checked.
	 * @return Returns true if the operand is a valid literal, otherwise returns false.
	 */
	public static boolean isLiteral(String operandString) 
	{
		boolean isLiteral = false;
		
		//TODO generate errors for incorrect operands.
		
		try 
		{
			if(Integer.parseInt(operandString) >= -32768 && Integer.parseInt(operandString) < 32768) 
			{
				isLiteral = true;
				litTable.addLiteral(operandString, Integer.toString(PC));
			}
		} 
		catch(NumberFormatException err) 
		{
			if(operandString.length() <= 4) 
			{
				isLiteral = true;
				litTable.addLiteral(operandString, Integer.toString(PC));
			}
		}
		return isLiteral;
	}

	/**
	 * @param fileName
	 *            This is the file location of the directives table
	 * 
	 *            This function reads a specified directives file we have
	 *            formatted and it adds the data from the specified directives
	 *            file into the public static DirectivesArray. This allows us to
	 *            easily check to see if an directive is in the correct format.
	 */
	public static void fillDirectivesArray(String fileName) 
	{
		ArrayList<String> linesOfInstruction = readFileToArrayList(fileName);
		for (String lineOfInstruction : linesOfInstruction) 
		{
			String[] variables = lineOfInstruction.split("\t");
			if (variables.length > 1) 
			{
				// System.out.println(Arrays.toString(variables));
				Directive.codeLocations codeLocation = Directive.codeLocations.DATA;
				if (variables[3].toUpperCase().equals(".TEXT")) 
				{
					codeLocation = Directive.codeLocations.TEXT;
				} 
				else if (variables[3].toUpperCase().equals(".DATA")) 
				{
					codeLocation = Directive.codeLocations.DATA;
				} 
				else if (variables[3].toUpperCase().equals(".INFO")) 
				{
					codeLocation = Directive.codeLocations.INFO;
				} 
				else if (variables[3].toUpperCase().equals(".START")) 
				{
					codeLocation = Directive.codeLocations.START;
				} 
				else if (variables[3].toUpperCase().equals(".END")) 
				{
					codeLocation = Directive.codeLocations.END;
				}
				Directive.labelTypes labelType = Directive.labelTypes.OPTIONALLABEL;
				if (variables[2].toUpperCase().equals("NL")) 
				{
					labelType = Directive.labelTypes.NOLABEL;
				} 
				else if (variables[2].toUpperCase().equals("OL")) 
				{
					labelType = Directive.labelTypes.OPTIONALLABEL;
				} 
				else if (variables[2].toUpperCase().equals("RL")) 
				{
					labelType = Directive.labelTypes.REQUIREDLABEL;
				}

				variables[1] = variables[1].replaceAll("\"", "");

				String[] operands = variables[1].split(",");
				ArrayList<Directive.operandTypes> operandArray = new ArrayList<Directive.operandTypes>();
				for (String operand : operands) 
				{
					operand = operand.toUpperCase();
					if (operand.equals("BIN")) 
					{
						operandArray.add(Directive.operandTypes.BINARY);
					} 
					else if (operand.equals("HEX")) 
					{
						operandArray.add(Directive.operandTypes.HEX);
					} 
					else if (operand.equals("STR")) 
					{
						operandArray.add(Directive.operandTypes.STRING);
					} 
					else if (operand.equals("LABEL")) 
					{
						operandArray.add(Directive.operandTypes.LABEL);
					} 
					else if (operand.equals("NUM")) 
					{
						operandArray.add(Directive.operandTypes.NUMBER);
					} 
					else if (operand.equals("LABELREF")) 
					{
						operandArray.add(Directive.operandTypes.NUMBER);
					}
				}
				Directive directive = new Directive(variables[0], labelType,codeLocation, operandArray);
				DirectivesArray.add(directive);
			}
		}
	}
}
