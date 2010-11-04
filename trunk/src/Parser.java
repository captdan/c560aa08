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

//import Instruction.instructionTypes;

/**
 * @authors Robert Schmidt, Kermit Stearns, Daniel Burnett, Oscar Flores, Rakaan Kayali.
 * Contains the code for parsing the source code read from the source. Code is segmented 
 * into numerous functions and is interdependent on other classes.
 * Modified to include startinglocation global variable and added removeWhiteSpace function.
 * Date Modified: 10/10/2010 by all authors.
 */


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
	 * @authors Robert Schmidt, Daniel Burnett, Kermit Stearns, Oscar Flores and Rakaan Kayali.
	 * 
	 * Main program of Parser loads directives, ErrorCodes and instructions into arrays.
	 * Also parses the lines of code read from the source code.
	 * @param args
	 */
	public static void main(String[] args) 
	{
	
		ArrayList<String> linesOfCode = new ArrayList<String>();
		if(args.length > 0)
		{
			linesOfCode = readFileToArrayList(args[0]);
		}
		else
		{
			linesOfCode = readFileToArrayList("TestCode/altest01.txt");
		}
		
		fillDirectivesArray("Directives.txt");
		fillErrorArray("ErrorCodes.txt");
		fillInstructionsArray("MOT_TABBED.txt");

		//Check to see if there is a valid start operation else end program.
		if (linesOfCode.size() > 0 && (parseCodeLine(linesOfCode.get(0)).directive == null || parseCodeLine(linesOfCode.get(0)).directive.directiveName.toUpperCase().equals(".START") == false))
		{
			currentErrorArray.clear();
			currentErrorArray.add(returnError(20));
			CodeLineArray.add(parseCodeLine(linesOfCode.get(0)));
			endProgram = true;
		}
		
		for (String lineOfCode : linesOfCode) 
		{
			// System.out.println(lineOfCode);
				
			if (endProgram == false) 
			{
				CodeLineArray.add(parseCodeLine(lineOfCode));
			}
			
			currentErrorArray.clear();
		}
		//System.out.println(CodeLineArray.get(8).instruction.returnPrintString());
		printIntermediateFile("IntermediateFile.txt");
		
		for (CodeLine codeLine : CodeLineArray) 
		{
			if(codeLine.instruction != null && codeLine.instruction.instructionType == Instruction.instructionTypes.REGISTER)
			{
				System.out.println(codeLine.instruction.instruction + "  :  " + codeLine.instruction.returnHexCodeLine());
			}
		}
			
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
				bufferedWriter.write("PC: " + codeline.PC + "\n");
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
	 * @author Robert Schmidt add 10/2/2010.
	 * This is where we will parse each line of code. All directive
	 * testing / checking will be in this function.
	 * Modified to include extra functionality to check for instructions and directives.
	 * Also Modified module to print out instructions , directives, symbols or errors found.
	 * Added test to see if line of code was neither an instruction, directive or symbol we printed an error.
	 * Date Modified: 10/10/2010
	 * Modified Fixed Mem.skip code added various error checks.
	 * Date Modified 10/17/2010 by Robert Schmidt
	 * 
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
			if (returnInstruction(lineOfCodeMinusComment,true) != null) 
			{
				System.out.println("Instruction: " + lineOfCodeMinusComment);
				cl.instruction = returnInstruction(lineOfCodeMinusComment,false);
				// Extract Valid Features
			} 
			else if (returnDirective(lineOfCodeMinusComment,true) != null) 
			{
				cl.directive = returnDirective(lineOfCodeMinusComment,false);
				// Extract Valid Features
				System.out.println("Directive: " + lineOfCodeMinusComment);
			} 
			else if (returnSymbolInstruction(lineOfCodeMinusComment,true) != null) 
			{
				StringTokenizer st2 = new StringTokenizer(lineOfCodeMinusComment, " \t",false);
				String symbol = st2.nextToken();
				System.out.println("Symbol: " + lineOfCodeMinusComment);
				SymbTable.addSymbol(symbol,PC.toString(), "NONE", SymbolTable.Uses.DATA_LABEL);
				if (returnSymbolInstruction(lineOfCodeMinusComment, false).getClass() == Directive.class) 
				{
					cl.directive = (Directive) returnSymbolInstruction(lineOfCodeMinusComment, false);	
				} 
				else if (returnSymbolInstruction(lineOfCodeMinusComment, false).getClass() == Instruction.class)
				{
					cl.instruction = (Instruction) returnSymbolInstruction(lineOfCodeMinusComment, false);
					
				}
				// Extract Valid Features
			} 
			else 
			{
				
				currentErrorArray.add(returnError(99));
				System.out.println("ERROR: " + lineOfCodeMinusComment);
				//cl.errors.add(returnError(99));
				//System.out.println(cl.errors.size());
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
		cl.PC = PC;
		
		for(Error error : currentErrorArray)
		{
			cl.errors.add(error);
		}
		
		System.out.println(currentErrorArray.size());
		return cl;

	}
	/**
	 * @author Robert Schmidt added 10/2/2010
	 * Checks code line for special directives MEM.SKIP and RESET.LC and EQU.EXP updates the program
	 * counter accordingly.
	 * Modified to include checking and dealing with directives (MEM.SKIP and RESET.LC)
	 * Date modified: 10/11/2010 by Robert Schmidt.
	 * @param cl CodeLine
	 * 			holds the codeline object that is to be checked for MEM.SKIP and RESET.LC.
	 */
	public static void checkSymbolsAndSpecialDirectives(CodeLine cl)
	{
		if (cl.directive != null)
		{
			if (cl.directive.directiveName.equals("RESET.LC"))
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
				//cl.directive.operandArray.add(new Operand(String.valueOf(cl.directive.operandArray.get(0).operand)));	
				//cl.directive.operandArray.add(new Operand(String.valueOf(evaluateExpression(cl))));	
			}
			else if (cl.directive.directiveName.equals("EXT"))
			{
				int x = 0;
				for(Operand operand : cl.directive.operandArray)
				{
					if(x<4)
					{
						SymbTable.addSymbol(operand.operand, "99999","NONE", SymbolTable.Uses.EXTERNAL);
					}
					x++;
				}
				
			}
		}
	}
	
	/**
	 * @author Robert Schmidt added 10/17/2010
	 * 
	 * addToPC is a method used to update the PC counter. 
	 * @param addValue 
	 * 			Is the integer that is to be added to the PC.
	 */
	public static void addToPC(int addValue)
	{
		if((addValue + PC) <= maxPC && (addValue + PC) >= 0)
		{
			PC += addValue;
		}
		else
		{
			currentErrorArray.add(returnError(6));
		}
	}
	/**
	 * @author Robert Schmidt.
	 * Examines the String parameter that is passed in the function and returns a directive if present.
	 * Modified to accept a boolean parameter weather or not we
	 * want to added errors.
	 * Date Modified: 10/17/2010 by Robert Schmidt.
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
				String operandString = "";
				while (st.hasMoreTokens()) 
				{
					operandString += st.nextToken();
				}
				operandString = removeWhiteSpace(operandString);
				String[] operands = operandString.split(",");
				

				if(operands[0].equals(""))
				{
					operands = new String[0];
				}
				
				//System.out.println("QWER:"+operands.length + "  " + directive.operands.size());
				//System.out.println(Arrays.toString(operands));
				if (directive.operands.size() == operands.length || directive.directiveName.equals("EXT") || directive.directiveName.equals("ENT"))
				{
					//Checks to see if directive is ENT and necessitates changing previously declared data_label to ENT usage
					if(directive.directiveName.equals("ENT"))
					{
						for(int i = operands.length-1; i >= 0; i--)
						{
							SymbolTable.changeToENT(operands[i]);
						}
					}
					directiveObj.directiveName = directive.directiveName;
					directiveObj.labelType = directive.labelType;
					directiveObj.codeLocation = directive.codeLocation;
					directiveObj.operands.clear();
				
					for (int x = 0; x < operands.length; x++) 
					{
						/*
						if(directiveObj.operands.get(x) == Directive.operandTypes.STRING)
						{
							
						}
						*/
						directiveObj.operands.add(Directive.operandTypes.LABELREF);
						directiveObj.operandArray.add(new Operand(operands[x]));
					}
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
					directiveObj.codeLocation = Directive.codeLocations.START;
					directiveObj.labelType = Directive.labelTypes.NOLABEL;
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
					
					//directiveObj.operandArray.add(new Operand(String.valueOf(Integer.valueOf(removeWhiteSpace(st.nextToken())))));
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
					//directiveObj.operandArray.add(new Operand(removeWhiteSpace(st.nextToken())));
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
					//directiveObj.operandArray.add(new Operand(String.valueOf(Integer.valueOf(removeWhiteSpace(st.nextToken())))));
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
		else if (possibleDirective.equals("STR.DATA")) 
		{
			String[] tempString = codeString.split("'");
			if(tempString.length < 4)
			{
				directiveObj.operandArray.clear();
				directiveObj.operandArray.add(new Operand("'"+tempString[1]+"'"));
				//System.out.println("QWER+: " + tempString[1]);
			}
		}
		else if (possibleDirective.equals("ADR.EXP") && possibleDirective.equals("EQU.EXP")) 
		{
			String[] tempString = codeString.split("'");
			
			
			
			if(tempString.length < 4)
			{
				directiveObj.operandArray.clear();
				tempString[1] = tempString[1].replaceAll("*", Integer.toString(PC));
				directiveObj.operandArray.add(new Operand("'"+tempString[1]+"'"));
				//System.out.println("QWER+: " + tempString[1]);
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
	 * Modified to include adding symbols found to the symbol table.
	 * Date Modified: 10/11/2010 by Oscar Flores.
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
	 * Modified to accept a boolean parameter to determine if we
	 * want to added errors or not.
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
			//System.out.println("\n\n" +instructionWithOperands + "\n" + Arrays.toString(operands));
			//System.out.println(returnInstructionViaOpcode(instruction).numberOfRegisters);
			if (operands.length == returnInstructionViaOpcode(instruction).numberOfRegisters) 
			{
				//System.out.println("WORKS");
				instructionObj.instruction = returnInstructionViaOpcode(instruction).instruction;
				instructionObj.instructionBinary = returnInstructionViaOpcode(instruction).instructionBinary;
				instructionObj.instructionExtended = returnInstructionViaOpcode(instruction).instructionExtended;
				instructionObj.instructionExtendedBinary = returnInstructionViaOpcode(instruction).instructionExtendedBinary;
				instructionObj.instructionExtendedHex = returnInstructionViaOpcode(instruction).instructionExtendedHex;
				instructionObj.instructionHex = returnInstructionViaOpcode(instruction).instructionHex;
				instructionObj.instructionType = returnInstructionViaOpcode(instruction).instructionType;
				instructionObj.numberOfRegisters = returnInstructionViaOpcode(instruction).numberOfRegisters;
				instructionObj.operands = returnInstructionViaOpcode(instruction).operands;

				
				for (int x = 0; x < returnInstructionViaOpcode(instruction).operands.size(); x++) 
				{
						Instruction.operandTypes operand = returnInstructionViaOpcode(instruction).operands.get(x);
						Boolean validOperands = false;
						//REGISTER, IMMEDIATE, ADDRESS, BIT, BITS, NUMBER, IO;
						switch (operand)
						{
						case ADDRESS:  validOperands = Operand.isValidInstructionAddress(operands[x]);
							break;
						case BIT:  validOperands = Operand.isValidInstructionBit(operands[x]);
							break;
						case BITS: validOperands = Operand.isValidInstructionBits(operands[x]);
							break;
						case IMMEDIATE: validOperands = true;
							break;
						case NUMBER: validOperands = Operand.isValidInstructionNumber(operands[x]);
							break;
						case IO: validOperands = Operand.isValidInstructionImmediate(operands[x]);
							break;
						case REGISTER: validOperands = isRegister(operands[x]);
							break;
						default: validOperands = false;
							break;
						}
						
						if(validOperands == true)
						{
							instructionObj.operandsArray.add(new Operand(operands[x]));
						}
						else
						{
							if(addErrors == true)
							{
								currentErrorArray.add(new Error(19,"The operand " + x + ": " + operands[x].toString() + " did not match the operand type "+operand.toString()+".","Change Operand"));
							}
						}
				}
	
			}
			else
			{
				if(addErrors == true)
				{
					currentErrorArray.add(new Error(0,"Expected " + returnInstructionViaOpcode(instruction).numberOfRegisters + " operands and received " + operands.length + " operands.","Add or Remove Operands accordingly."));
					//currentErrorArray.add(returnError(0));
				}
				instructionObj = null;
			}
		} 
		else 
		{
			instructionObj = null;
			// System.out.println(instruction);
		}
		
		if(currentErrorArray.size() > 0)
		{
			instructionObj = null;
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
	 * @author Robert Schmidt added 10/12/2010.
	 * 
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
	 * @author Robert Schmidt added 10/4/2010.
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
	 * @author Robert Schmidt added 10/4/2010.
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
			//System.out.println(Arrays.toString(variables) + "\n");
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
				else if (variables[5].equals("J"))
				{
					instructionType = Instruction.instructionTypes.J;
				}

				variables[4] = variables[4].replaceAll("\"", "");

				// System.out.println(variables[4]);
				String[] operands = variables[4].split(",");
				//System.out.println(Arrays.toString(operands) + "\n");
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
				//System.out.print(operandArray.size());
				Instruction instruction = new Instruction(variables[0],variables[1], variables[3], operandArray,instructionType);
				InstructionsArray.add(instruction);
			}
		}
	}

	/**
	 * @author Robert Schmidt added 10/4/2010.
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
 * @author Oscar Flores added 10/4/2010.
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
	 * @author Daniel Burnett add 10/6/2010.
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
	 * @author Rakaan Kayali added 10/11/2010.
	 * 
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
	 * @author Kermit Stearns added 10/11/2010.
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
		if (operandString.length() == 2) 
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
	 * @author Daniel Burnett 10/11/2010.
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
	 * Module Name: fillDirectivesArray.
	 * Description:	This function reads a specified directives file we have
	 * formatted and it adds the data from the specified directives
	 * file into the public static DirectivesArray. This allows us to
	 * easily check to see if an directive is in the correct format.
	 * Input Params: fileName
	 *            This is the file location of the directives table.
	 * Output Params: Array of Directives.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/5/2010.	
	 * Modifications: None.
	 * @param fileName: fileName
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
					//operand = removeWhiteSpace(operand);
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
					else if (operand.equals("EXP")) 
					{
						operandArray.add(Directive.operandTypes.EXP);
					}
				}
				Directive directive = new Directive(variables[0].toUpperCase(), labelType,codeLocation, operandArray);
				DirectivesArray.add(directive);
			}
		}
	}
}
