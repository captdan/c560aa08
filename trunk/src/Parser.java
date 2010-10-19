import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList; //import java.util.Arrays;
import java.util.Arrays;
//import java.util.Arrays;
import java.util.StringTokenizer;

//import Directive.labelTypes;

public class Parser 
{
	/**
	 * LiteralTable holds the all occurences of literals in our program.
	 */
	public static LiteralTable litTable = new LiteralTable();
	/**
	 * StartingLocation stores the indicated starting address of the program.
	 */
	public static int startingLocation = 0;
	/**
	 * ProgramName has the name of the program as indicated after the directive .start.
	 */
	public static String programName = "";
	/**
	 * Program ends when variable endProgram is true.
	 */
	public static Boolean endProgram = false;
	/**
	 * Holds the code location of a particular directive.
	 */
	public static Directive.codeLocations codeLocation = null;
	/**
	 * CodeLineArray holds the lines of code retrieved from the source code each line in a separate 
	 * position in the array. 
	 */
	public static ArrayList<CodeLine> CodeLineArray = new ArrayList<CodeLine>();
	/**
	 * Holds the directives as retrieved from the Directives file.
	 */
	public static ArrayList<Directive> DirectivesArray = new ArrayList<Directive>();
	/**
	 * Holds the Instructions as retrieved from the Instructions file.
	 * Each instruction is stored in a separate position in the array.
	 */
	public static ArrayList<Instruction> InstructionsArray = new ArrayList<Instruction>();
	/**
	 * Holds the errors as retrieved by the Error file , each file in the position of the 
	 * array that corresponds to the error's identification number.
	 */
	public static ArrayList<Error> ErrorArray = new ArrayList<Error>();
	/**
	 * Holds all symbols encountered while passing through source code.
	 */
	public static SymbolTable SymbTable = new SymbolTable();
	/**
	 * Contains the errors that are found in the code being assembled.
	 */
	public static ArrayList<Error> currentErrorArray = new ArrayList<Error>();
	/**
	 * maxPC is the maximum value PC can hold without exceeding the memory capacity of the system.
	 */
	public static int maxPC = 65536;
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
	 * Main program of Parser loads directives, ErrorCodes and instructions into arrays.
	 * Also parses the lines of code read from the source code.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		fillDirectivesArray("Directives.txt");
		fillErrorArray("src/ErrorCodes.txt");
		fillInstructionsArray("MOT_TABBED.txt");

		ArrayList<String> linesOfCode = readFileToArrayList("src/alTest1.txt");
		for (String lineOfCode : linesOfCode) 
		{
			// System.out.println(lineOfCode);
			if (endProgram == false) 
			{
				CodeLineArray.add(parseCodeLine(lineOfCode));
			}
			
			currentErrorArray.clear();
		}
		System.out.println(CodeLineArray.get(8).instruction.returnPrintString());
		printIntermediateFile("IntermediateFile.txt");

		SymbTable.prettyFerret();		
	}

	public static void printIntermediateFile(String fileName)
	{		
		try
		{
			FileWriter fileStream = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileStream);
			
			int x = 0;
			for(CodeLine codeline : CodeLineArray)
			{
				bufferedWriter.write("------------------------------------------\n");
				bufferedWriter.write("CodeLine " + x + "\n");
				bufferedWriter.write(codeline.returnPrintString());
				x++;
			}
			bufferedWriter.close();
		}
		catch (Exception e)
		{
			//ERROR
		}
	}
	
	/**
	 * This is where we will parse each line of code. All directive
	 * testing / checking will be in this function.
	 * @param lineOfCode
	 *            Takes a string line of code to be parsed.
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

		StringTokenizer st = new StringTokenizer(lineOfCodeMinusComment, " \t",false);
		//System.out.println("Line: " + lineOfCodeMinusComment);

		if (st.hasMoreTokens() == true) 
		{
			if (returnInstruction(lineOfCodeMinusComment,false) != null) 
			{
				System.out.println("Instruction: " + lineOfCodeMinusComment);
				cl.instruction = returnInstruction(lineOfCodeMinusComment,false);
				// Extract Valid Features
			} 
			else if (returnDirective(lineOfCodeMinusComment,false) != null) 
			{
				cl.directive = returnDirective(lineOfCodeMinusComment,true);
				// Extract Valid Features
				System.out.println("Directive: " + lineOfCodeMinusComment);
			} 
			else if (returnSymbolInstruction(lineOfCodeMinusComment,false) != null) 
			{
				StringTokenizer st2 = new StringTokenizer(lineOfCodeMinusComment, " \t",false);
				String symbol = st2.nextToken();
				System.out.println("Symbol: " + lineOfCodeMinusComment);
				SymbTable.addSymbol(symbol,PC.toString(), "NONE", SymbolTable.Uses.DATA_LABEL);
				if (returnSymbolInstruction(lineOfCodeMinusComment, false).getClass() == Directive.class) 
				{
					cl.directive = (Directive) returnSymbolInstruction(lineOfCodeMinusComment, true);	
				} 
				else if (returnSymbolInstruction(lineOfCodeMinusComment, false).getClass() == Instruction.class)
				{
					cl.instruction = (Instruction) returnSymbolInstruction(lineOfCodeMinusComment, true);
					
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
		checkSymbolsAndSpecialDirectives(cl);
		addToPC(cl.lineLength());
		cl.errors = currentErrorArray;
		return cl;

	}
	/**
	 * Checks code line for special directives MEM.SKIP and RESET.LC and updates the program
	 * counter accordingly.
	 * @param cl CodeLine
	 * 			holds the codeline object that is to be checked for MEM.SKIP and RESET.LC.
	 */
	public static void checkSymbolsAndSpecialDirectives(CodeLine cl)
	{
		if (cl.directive != null)
		{

			if (cl.directive.directiveName.equals("MEM.SKIP"))
			{
				Parser.addToPC(Integer.valueOf(cl.directive.operandArray.get(0).operand));
			}
			else if (cl.directive.directiveName.equals("RESET.LC"))
			{
				if (Integer.valueOf(cl.directive.operandArray.get(0).operand) > Parser.PC) 
				{
					Parser.PC = Integer.valueOf(cl.directive.operandArray.get(0).operand);
				} 
				else 
				{
					//Error
				}
			}
			else if (cl.directive.directiveName.equals("EQU.EXP") || cl.directive.directiveName.equals("ADR.EXP"))
			{
					//Adds the integer value of an expression to the operand array of these special directives.
					cl.directive.operandArray.add(new Operand(String.valueOf(evaluateExpression(cl))));	
				
			}
		}
	}
	/**
	 * Adds the value of the parameter to the value of PC if within acceptable range
	 * of values of PC. If not then returns an error.
	 * @param addValue
	 */
	/**
	 * Goes through an expression and determines the result.
	 * 
	 * @param cl The CodeLine object with the .EXP directive.
	 * @return The integer value of the evaluated expression.
	 */
	public static int evaluateExpression (CodeLine cl)
	{
		int result = 0;
		StringTokenizer st = new StringTokenizer(String.valueOf(cl.directive.operandArray.get(0)),"+-",true);
		String op1 = st.nextToken();
		
		//Check 1st operand
		if (SymbTable.isInTable(op1))
		{
			result += (Integer)SymbTable.getInfoFromSymbol(op1).get(2);
		}
		else if (op1 == "*")
		{
			result += PC;
		}
		else
		{
			result += Integer.valueOf(op1);
		}
		
		//Check for 2nd symbol, if its there, then we have another operand as well
		if(st.hasMoreTokens())
		{
			String symb1 = st.nextToken();
			String op2 = st.nextToken();
			
			if (symb1 == "+")
			{
				if (SymbTable.isInTable(op2))
				{
					result += (Integer)SymbTable.getInfoFromSymbol(op2).get(2);
				}
				else if (op2 == "*")
				{
					result += PC;
				}
				else
				{
					result += Integer.valueOf(op2);
				}
			}
			else
			{
				if (SymbTable.isInTable(op2))
				{
					result -= (Integer)SymbTable.getInfoFromSymbol(op2).get(2);
				}
				else if (op2 == "*")
				{
					result -= PC;
				}
				else
				{
					result -= Integer.valueOf(op2);
				}
			}
			
			
		}
		
		//Check for 3rd symbol, if its there, then we have another operand as well
		if(st.hasMoreTokens())
		{
			String symb2 = st.nextToken();
			String op3 = st.nextToken();
			
			if (symb2 == "+")
			{
				if (SymbTable.isInTable(op3))
				{
					result += (Integer)SymbTable.getInfoFromSymbol(op3).get(2);
				}
				else if (op3 == "*")
				{
					result += PC;
				}
				else
				{
					result += Integer.valueOf(op3);
				}
			}
			else
			{
				if (SymbTable.isInTable(op3))
				{
					result -= (Integer)SymbTable.getInfoFromSymbol(op3).get(2);
				}
				else if (op3 == "*")
				{
					result -= PC;
				}
				else
				{
					result -= Integer.valueOf(op3);
				}
			}
		
		}
		
		//Check for 5th symbol, if its there, then we have another operand as well
		if(st.hasMoreTokens())
		{
			String symb3 = st.nextToken();
			String op4 = st.nextToken();
			
			if (symb3 == "+")
			{
				if (SymbTable.isInTable(op4))
				{
					result += (Integer)SymbTable.getInfoFromSymbol(op4).get(2);
				}
				else if (op4 == "*")
				{
					result += PC;
				}
				else
				{
					result += Integer.valueOf(op4);
				}
			}
			else
			{
				if (SymbTable.isInTable(op4))
				{
					result -= (Integer)SymbTable.getInfoFromSymbol(op4).get(2);
				}
				else if (op4 == "*")
				{
					result -= PC;
				}
				else
				{
					result -= Integer.valueOf(op4);
				}
			}
		
		}
		
		return result;
	}
	public static void addToPC(int addValue)
	{
		if((addValue + PC) <= maxPC && (addValue + PC) > 0)
		{
			PC += addValue;
		}
		else
		{
			currentErrorArray.add(returnError(6));
		}
	}
	/**
	 * @author
	 * Examines the String parameter that is passed in the function and returns a directive if present.
	 * @param codeString
	 * 			Holds the codeString that we check for directives.
	 * @param addErrors
	 * 			Used to indicate when we want to add errors to the currentErrorArray in order 
	 * 			to only add a specific error once. 
	 * @return Directive directiveObj
	 * 			Holds the Directive object extracted from the codeString. 
	 */
	public static Directive returnDirective(String codeString, Boolean addErrors) 
	{
		Directive directiveObj = new Directive();

		StringTokenizer st = new StringTokenizer(codeString, " \t", false);
		String possibleDirective = "";
		if (st.hasMoreTokens())
		{
			possibleDirective = st.nextToken();
		}
		
		possibleDirective = possibleDirective.toUpperCase();
		for (Directive directive : DirectivesArray) 
		{
			if (directive.directiveName.toUpperCase().equals(possibleDirective.toUpperCase())) 
			{
				directiveObj.directiveName = possibleDirective.toUpperCase();
				directiveObj.labelType = directive.labelType;
				directiveObj.codeLocation = directive.codeLocation;
				directiveObj.operands = directive.operands;
				
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

		st = new StringTokenizer(codeString, " \t", false);
		if (st.hasMoreTokens())
		{
			possibleDirective = st.nextToken();
		}
		possibleDirective = possibleDirective.toUpperCase();
		
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
					if(addErrors == true)
					{
						currentErrorArray.add(returnError(1));
					}
				}
			}
			else
			{
				if(addErrors == true)
				{
					currentErrorArray.add(returnError(7));
				}
			}
		}
		else if (specialDirectives[0].equals(".START")) 
		{
			specialDirectives = removeWhiteSpace(codeString).split(",");
			if (specialDirectives.length == 3) 
			{
				try
				{
					directiveObj.directiveName = ".START";
					programName = specialDirectives[1];
					startingLocation = Integer.valueOf(specialDirectives[2]);
					PC = startingLocation;
					SymbTable.addSymbol(programName, PC.toString(), "NONE", SymbolTable.Uses.PROGRAM_NAME);
				}
				catch(Exception e)
				{
					if(addErrors == true)
					{
						currentErrorArray.add(returnError(9));
					}
				}

			}
			else
			{
				if(addErrors == true)
				{
					currentErrorArray.add(returnError(8));
				}
			}
		}
		if (possibleDirective.equals("RESET.LC")) 
		{
			if(st.countTokens()==1)
			{
				try
				{
					directiveObj.directiveName = possibleDirective.toUpperCase();
					
					directiveObj.operandArray.add(new Operand(String.valueOf(Integer.valueOf(removeWhiteSpace(st.nextToken())))));
				}
				catch(Exception e)
				{
					if(addErrors == true)
					{
						currentErrorArray.add(returnError(11));
					}
				}

			}

		} 
		else if (possibleDirective.equals("EXEC.START")) 
		{
			if(st.countTokens() == 1)
			{
				try
				{
					directiveObj.directiveName = possibleDirective.toUpperCase();
					directiveObj.operandArray.add(new Operand(removeWhiteSpace(st.nextToken())));
					//This is requires a label why are we checking a number?
					//execStart = Integer.valueOf(removeWhiteSpace(st.nextToken()));
				}
				catch(Exception e)
				{
					if(addErrors == true)
					{
						currentErrorArray.add(returnError(13));
					}
				}
			}
			else
			{
				if(addErrors == true)
				{
					currentErrorArray.add(returnError(14));
				}
			}


		} 
		else if (possibleDirective.equals("MEM.SKIP")) 
		{
			if(st.countTokens() == 1)
			{
				try
				{
					directiveObj.directiveName = possibleDirective.toUpperCase();
					directiveObj.operandArray.add(new Operand(String.valueOf(Integer.valueOf(removeWhiteSpace(st.nextToken())))));
					//moved add to PC to length function
				}
				catch (Exception e)
				{
					if(addErrors == true)
					{
						currentErrorArray.add(returnError(12));
					}
				}
			}
			else
			{
				if(addErrors == true)
				{
					currentErrorArray.add(returnError(14));
				}
			}

		}
		//TODO test this get it working
		/*
		if(symb.hasMoreTokens())
		{
			String directive = symb.nextToken().toUpperCase();
			if(directive.equals("EQU") || directive.equals("EQU.EXP"))
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
	/**
	 * @author Robert Schmidt
	 * Strips the parameter string from whitespace and tab characters.
	 * @param InputString
	 * 			Contains the String that is to have whitespace and tabs removed.
	 * @return InputString 
	 * 			Contains parameter string void of whitespace and tab characters.
	 */
	public static String removeWhiteSpace(String InputString) 
	{
		InputString = InputString.replaceAll(" ", "");
		InputString = InputString.replaceAll("\t", "");
		return InputString;
	}
	/**
	 * @author Robert Schmidt
	 * returnSymbolInstruction extracts and returns the symbol found in the string parameter.
	 * Additionally, if an error is encountered it adds the error to the currentErrorArray if the 
	 * addErrors parameter is set to true.
	 * @param instruction
	 * 			Carries string to have symbol instruction extracted from.
	 * @param addErrors 
	 * 			Indicates if we should add errors to the currentArrayList.
	 * 			This helps avoid repeating errors for a single line.		
	 * @return Object symbolObj
	 * 			Holds the symbol (directive or instruction or null) found in the instructions String and 
	 * 			updates the currentErrorArray when needed and the addError boolean is set to true.
	 */
	public static Object returnSymbolInstruction(String instruction, Boolean addErrors)
	{
		Object symbolObj = null;
		StringTokenizer st = new StringTokenizer(instruction," \t",false);
		st.nextToken();
		String commandMinusSymbol = "";
		
		while (st.hasMoreTokens()) 
		{
			commandMinusSymbol += " " + st.nextToken();
		}
		
		if(returnInstruction(commandMinusSymbol, false) != null)
		{
			// if the rest of the line is an Instruction, then the symbol must be a label
			
			symbolObj = returnInstruction(commandMinusSymbol, addErrors);
		}
		else if(returnDirective(commandMinusSymbol, false) != null)
		{
			// if it's a directive, it could be EQU or DATA
			
			if(returnDirective(commandMinusSymbol, false).labelType != Directive.labelTypes.NOLABEL)
			{
				symbolObj = returnDirective(commandMinusSymbol, addErrors);
			}
			// the first thing should be a directive
		}
		else
		{
			symbolObj = null;
		}
		
		return symbolObj;
	}
	/**
	 * @author Robert Schmidt
	 * returnInstruction takes a String parameter extracts any instruction if present.
	 * @param instructionWithOperands
	 * 			Holds a string with a possible instruction and its operands to be checked and returned to caller
	 * 			if present.
	 * @param addErrors
	 * 			Boolean that contains permission to add to currentErrorArray.
	 * @return instructionObj
	 * 			Holds an instruction object that is constructed from information gathered from incoming parameters
	 * 			or null if instruction were not present in parameters.
	 * 			
	 */	

	public static Instruction returnInstruction(String instructionWithOperands, Boolean addErrors) 
	{
		Instruction instructionObj = new Instruction();
		StringTokenizer st = new StringTokenizer(instructionWithOperands," \t", false);
		String instruction = "";
		if (st.hasMoreTokens())
		{
			instruction = st.nextToken();
		}
		
		instruction = instruction.toUpperCase();

		if (instructionExists(instruction) == true && returnSymbolInstruction(instructionWithOperands, false) == null) 
		{
			// System.out.println(instruction);
			String operandString = "";
			while (st.hasMoreTokens()) 
			{
				operandString += st.nextToken();
			}
			String[] operands = operandString.split(",");
			System.out.println("\n\n" +instructionWithOperands + "\n" + Arrays.toString(operands));
			System.out.println(returnInstructionViaOpcode(instruction).numberOfRegisters);
			if (operands.length == returnInstructionViaOpcode(instruction).numberOfRegisters) 
			{
				System.out.println("WORKS");
				instructionObj.instruction = returnInstructionViaOpcode(instruction).instruction;
				instructionObj.instructionBinary = returnInstructionViaOpcode(instruction).instructionBinary;
				instructionObj.instructionExtended = returnInstructionViaOpcode(instruction).instructionExtended;
				instructionObj.instructionExtendedBinary = returnInstructionViaOpcode(instruction).instructionExtendedBinary;
				instructionObj.instructionExtendedHex = returnInstructionViaOpcode(instruction).instructionExtendedHex;
				instructionObj.instructionHex = returnInstructionViaOpcode(instruction).instructionHex;
				instructionObj.instructionType = returnInstructionViaOpcode(instruction).instructionType;
				instructionObj.numberOfRegisters = returnInstructionViaOpcode(instruction).numberOfRegisters;
				instructionObj.operands = returnInstructionViaOpcode(instruction).operands;

				//Boolean validOperands = false;
				for (int x = 0; x < returnInstructionViaOpcode(instruction).operands.size(); x++) 
				{
					instructionObj.operandsArray.add(new Operand(operands[x]));
					/*
					Instruction.operandTypes operand = returnInstructionViaOpcode(instruction).operands.get(x);
					
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
					*/
				}
	
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
	 * @author 
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
	 * @author 
	 * readFileToArrayList reads a file and stores its contents in an array with 
	 * each line in the file occupying a specific position in the array.
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
	 * @author 
	 * This function reads a specified instruction file we have
	 * formatted and it adds the data from the specified instruction
	 * file into the public static InstructionArray. This allows us
	 * to easily check to see if an instruction is in the correct
	 * format.
	 * @param fileName
	 *            This is the file location of the instruction table
	 */
	public static void fillInstructionsArray(String fileName) 
	{
		ArrayList<String> linesOfInstruction = readFileToArrayList(fileName);
		for (String lineOfInstruction : linesOfInstruction) 
		{
			String[] variables = lineOfInstruction.split("\t");
			System.out.println(Arrays.toString(variables) + "\n");
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
				System.out.println(Arrays.toString(operands) + "\n");
				ArrayList<Instruction.operandTypes> operandArray = new ArrayList<Instruction.operandTypes>();
				for (String operand : operands) 
				{
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
					else if (operand.equals("NUM"))
					{
						operandArray.add(Instruction.operandTypes.NUMBER);
					}
				}
				System.out.println(operandArray.size());
				Instruction instruction = new Instruction(variables[0],variables[1], variables[3], operandArray,instructionType);
				InstructionsArray.add(instruction);
			}
		}
	}

	/**
	 * @author
	 * fillErrorArray is a function that reads a specified error file we have formatted
	 * and it adds the data from the specified instruction file into
	 * the public static ErrorArray. This allows us to easily check
	 * to see if an error is in the correct format.
	 * @param fileName
	 *          Is a String that represents the name of the Error file that is to be read
	 *          into the ErrorArray.
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
	 * @author 
	 * The function returnError returns an error whose position in the ErrorArray
	 * corresponds to the number that we pass in as a parameter.
	 * @param ErrorId
	 *            The error id number associated with the error.
	 *            
	 * @return returnError
	 * 			Returns an instance of the Error class object for the specified
	 *         	error.
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
	 * @author
	 * the function instructionExists checks the String parameter we pass in to 
	 * see if it holds an instruction and returns a boolean accordingly.
	 * @param instructionString
	 *            The text instruction name that is to be looked up.
	 * @return instructionExists
	 * 		   Returns a boolean value depending on the existence of the
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
	 * @author 
	 * The function returnInstructionViaOpcode returns the instruction extracted 
	 * from the String parameter instructionString.
	 * @param instructionString
	 *            The text instruction name that is to be returned.
	 * @return returnInstruction
	 * 			Returns an instance of the instruction class for the specified
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
	 * @author 
	 * The function isRegister checks to see if the parameter String represents a valid register
	 * and returns a boolean accordingly.
	 * @param operandString
	 *            The string of the operand of an instruction.
	 * @return isRegister
	 * 			 Returns is the operand is a valid register or not.
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
	 * @return Returns isLiteral
	 * 			true if the operand is a valid literal, otherwise returns false.
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
	 * This function reads a specified directives file we have
	 * formatted and it adds the data from the specified directives
	 * file into the public static DirectivesArray. This allows us to
	 * easily check to see if an directive is in the correct format.
	 * @param fileName
	 *            This is the file location of the directives table.
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
