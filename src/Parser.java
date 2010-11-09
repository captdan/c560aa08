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
	
	public static boolean debugMode = false;
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
			linesOfCode = readFileToArrayList("TestCode/SimpleInstructionTest.txt");
		}
		
		/** This fills all the start ArrayLists with their corresponding values **/
		fillDirectivesArray("Directives.txt");
		fillErrorArray("ErrorCodes.txt");
		fillInstructionsArray("MOT_TABBED.txt");

		/** This parses the .START directive and verifies that the program is started correctly **/
		parseStartDirective(linesOfCode);
		
		/** This code processes each line of the code from the source assembly file **/
		for (String lineOfCode : linesOfCode) 
		{
			//System.out.println(lineOfCode);	
			if (endProgram == false) 
			{
				CodeLineArray.add(parseCodeLine(lineOfCode));
			}
			currentErrorArray.clear();
		}
		
		/** This finds all errors in the operands and verifies all the input **/
		checkOperands(CodeLineArray);
		
		/** This adds an A/R/E scope variable to each operand **/
		selectCodeLineScope(CodeLineArray);
		
		/** This gets the EXEC.START location  **/
		setExecStart(CodeLineArray);
		
		/** This print out the intermediate file which we use for debugging and for SP1 **/
		printIntermediateFile("IntermediateFile.txt");
		
		/**
		 //This is to test that all the instructions are converted properly to the correct hex / binary.
		for (CodeLine codeLine : CodeLineArray) 
		{
			if(codeLine.instruction != null)
			{
				System.out.println(codeLine.instruction.instruction + ":  \t\t" + codeLine.instruction.returnHexCodeLine());
			}
		}
		**/
		
		//System.out.println(SymbTable.size);
		SymbTable.prettyFerret();	
		
		Program newProgram = new Program(programName, startingLocation, execStart, debugMode,PC, CodeLineArray, SymbTable, litTable);
		ObjectFile newObjectFile = new ObjectFile(newProgram);
		
		newObjectFile.prettyFerret2();
		
		Program newProgram2 = new Program(programName, startingLocation, execStart,debugMode,PC, CodeLineArray, SymbTable, litTable);
		AssemblyListing newAssemblyListing = new AssemblyListing(newProgram2);
		newAssemblyListing.prettyFerret3();
	}
	
	/**
	 * 
	 * Module Name: setExecStart
	 * Description: Sets the address for starting execution. Determines
	 * 				if there is a valid EXEC.START directive, otherwise starts at the beginning of the program.
	 * Input Params: CodeLineArrayValue
	 * 					The array of all the lines of code. To be checked for valid EXEC.START directive.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11/6/2010
	 * Modifications: 
	 */
	public static void setExecStart(ArrayList<CodeLine> CodeLineArrayValue)
	{
		boolean foundDirective = false;
		for (CodeLine codeLine : CodeLineArrayValue) 
		{
			if(codeLine.directive != null && codeLine.directive.directiveName.equals("EXEC.START"))
			{
				foundDirective = true;
				try
				{
					execStart = Integer.valueOf(codeLine.directive.operandArray.get(0).operand);
				}
				catch(Exception e)
				{
					if(SymbTable.isInTable(codeLine.directive.operandArray.get(0).operand))
					{
						execStart = Integer.valueOf(SymbTable.getInfoFromSymbol(codeLine.directive.operandArray.get(0).operand).get(0).toString());
					}
					else if(codeLine.directive.operandArray.get(0).operand.equals("*"))
					{
						execStart = codeLine.PC;
					}
					else
					{
						execStart = startingLocation;
						System.out.println("No valid EXEC.START Location");
					}
				}

			}
			if(codeLine.directive != null && codeLine.directive.directiveName.equals("DEBUG"))
			{
				debugMode = Boolean.valueOf(codeLine.directive.operandArray.get(0).operand);
			}
		}
		if (foundDirective == false)
		{
			execStart = startingLocation;
		}
	}
	
	/**
	 * 
	 * Module Name: selectCodeLineScope
	 * Description: This sets the A/R/E types on each codeline
	 * Input Params: CodeLineArrayValue 
	 * 						The array of the CodeLine objects who's scope is to be selected.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010 2:00AM
	 * Modifications:
	 */
	public static void selectCodeLineScope(ArrayList<CodeLine> CodeLineArrayValue)
	{
		for (CodeLine codeLine : CodeLineArrayValue) 
		{
			if(codeLine.errors.size() == 0)
			{
				if (codeLine.instruction != null)
				{
					checkOperandScope(codeLine.instruction.operandsArray);
				}
				if (codeLine.directive != null)
				{
					checkOperandScope(codeLine.directive.operandArray);
				}
			}
		}
	}
	
	/**
	 * 
	 * Module Name: checkOperandScope
	 * Description: This checks the operands scope for an array list of operands.
	 * Input Params: operandsArray 
	 * 						This is the ArrayList of the operands that is to be parsed.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010 2:40 AM
	 * Modifications: Added ability to search complex addresses for external or relative operands
	 */
	public static void checkOperandScope(ArrayList<Operand> operandsArray)
	{
		for(Operand operandValue: operandsArray)
		{
			String tempOperandValue = operandValue.operand;
			try
			{
				if(operandValue.operandType == Instruction.operandTypes.COMPLEXADDRESS)
				{
					if(tempOperandValue.contains("("))
					{
						tempOperandValue = tempOperandValue.substring(0, tempOperandValue.indexOf('('));
					}
					if(tempOperandValue.equals("*"))
					{
						operandValue.relocationType = Operand.relocationTypes.R;
					}
					
				}
			}
			catch(Exception e){}
			try
			{
				if(operandValue.operandType == Directive.operandTypes.EXP)
				{
					for(String equOperand : returnEquOperands(tempOperandValue))
					{
						if(SymbTable.isInTable(equOperand))
						{
							if((SymbolTable.Uses)SymbTable.getInfoFromSymbol(equOperand).get(2) == SymbolTable.Uses.EXTERNAL)
							{
								operandValue.relocationType = Operand.relocationTypes.E;
							}
							else if ((SymbolTable.Uses)SymbTable.getInfoFromSymbol(equOperand).get(2) == SymbolTable.Uses.DATA_LABEL)
							{
								if(operandValue.relocationType !=operandValue.relocationType.E)
								{
									operandValue.relocationType = Operand.relocationTypes.R;
								}
							}
							else if ((SymbolTable.Uses)SymbTable.getInfoFromSymbol(equOperand).get(2) == SymbolTable.Uses.ENT)
							{
								operandValue.relocationType = Operand.relocationTypes.E;
							}
						}
					}
				}
			}
			catch(Exception e){}

			if(SymbTable.isInTable(tempOperandValue))
			{
				if((SymbolTable.Uses)SymbTable.getInfoFromSymbol(tempOperandValue).get(2) == SymbolTable.Uses.EXTERNAL)
				{
					
					operandValue.relocationType = Operand.relocationTypes.E;
				}
				else if ((SymbolTable.Uses)SymbTable.getInfoFromSymbol(tempOperandValue).get(2) == SymbolTable.Uses.DATA_LABEL)
				{
					if(operandValue.relocationType !=operandValue.relocationType.E)
					{
						operandValue.relocationType = Operand.relocationTypes.R;
					}
				}
				else if ((SymbolTable.Uses)SymbTable.getInfoFromSymbol(tempOperandValue).get(2) == SymbolTable.Uses.ENT)
				{
					operandValue.relocationType = Operand.relocationTypes.E;
				}
			}
		}
	}
	
	/**
	 * 
	 * Module Name: returnEquOperands
	 * Description: This function returns the operands of an EQU statment in an ArrayList<String>.
	 * Input Params: equString 
	 * 					This is the input EQU String.
	 * Output Params: ArrayList<String>
	 * 					This function returns the operands of the EQU input string in an ArrayList<String>
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010 2:20 AM
	 * Modifications: 
	 */
	public static ArrayList<String> returnEquOperands (String equString)
	{
		ArrayList<String> equOperands = new ArrayList<String>();
		StringTokenizer expressionTokenizer = new StringTokenizer(equString,"+-",false);
		
		while(expressionTokenizer.hasMoreTokens())
		{
			equOperands.add(expressionTokenizer.nextToken());
		}
		
		return equOperands;
	}
	
	/**
	 * 
	 * Module Name: checkOperands
	 * Description: This checks every codeLine object in a CodeLineArray for syntax errors in the operand fields
	 * Input Params: CodeLineArrayValue
	 * 					The array of the codeLine objects who's operands are to be tested.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010 12:30 AM
	 * Modifications: Added support for both directives and instructions
	 */
	public static void checkOperands (ArrayList<CodeLine> CodeLineArrayValue)
	{
		for (CodeLine codeLine : CodeLineArrayValue) 
		{
			if (codeLine.instruction != null)
			{
				for (int x = 0; x < codeLine.instruction.operands.size(); x++) 
				{
					Instruction.operandTypes operand = codeLine.instruction.operands.get(x);
					Boolean validOperands = false;

					switch (operand)
					{
					case ADDRESS:  validOperands = Operand.isValidInstructionSimpleAddress(codeLine.instruction.operandsArray.get(x).operand);
						break;
					case COMPLEXADDRESS:  validOperands = Operand.isValidInstructionComplexAddress(codeLine.instruction.operandsArray.get(x).operand);
						break;
					case BIT:  validOperands = Operand.isValidInstructionBit(codeLine.instruction.operandsArray.get(x).operand);
						break;
					case BITS: validOperands = Operand.isValidInstructionBits(codeLine.instruction.operandsArray.get(x).operand);
						break;
					case SIGNEDIMMEDIATE: validOperands = Operand.isValidInstructionSignedImmediate(codeLine.instruction.operandsArray.get(x).operand);
						break;
					case UNSIGNEDIMMEDIATE: validOperands = Operand.isValidInstructionUnSignedImmediate(codeLine.instruction.operandsArray.get(x).operand);
						break;
					case NUMBER: validOperands = Operand.isValidInstructionNumber(codeLine.instruction.operandsArray.get(x).operand);
						break;
					case REGISTER: validOperands = Operand.isValidInstructionRegister(codeLine.instruction.operandsArray.get(x).operand);
						break;
					default: validOperands = false;
						break;
					}
					
					if(validOperands == false)
					{
						codeLine.errors.add(new Error(19,"Operand [" + x + "]: " + codeLine.instruction.operandsArray.get(x).operand + " is not a valid "+operand.toString()+".","Change Operand"));
					}
				}
			}
			else if(codeLine.directive != null)
			{
				for (int x = 0; x < codeLine.directive.operands.size(); x++) 
				{
					Directive.operandTypes operand = codeLine.directive.operands.get(x);
					Boolean validOperands = false;

					switch (operand)
					{
					case BINARY:  validOperands = Operand.isValidDirectiveBinary(codeLine.directive.operandArray.get(x).operand);
						break;
					case BOOLEAN:  validOperands = Operand.isValidDirectiveBoolean(codeLine.directive.operandArray.get(x).operand);
						break;
					case EXP: validOperands = Operand.isValidDirectiveExp(codeLine.directive.operandArray.get(x).operand);
						break;
					case HEX: validOperands = Operand.isValidDirectiveHex(codeLine.directive.operandArray.get(x).operand);
						break;
					case NUMBER: validOperands = Operand.isValidDirectiveNumber(codeLine.directive.operandArray.get(x).operand);
						break;
					case LABEL: validOperands = Operand.isValidDirectiveLabel(codeLine.directive.operandArray.get(x).operand);
						break;
					case LABELREF: validOperands = Operand.isValidDirectiveLabel(codeLine.directive.operandArray.get(x).operand);
						break;
					case STRING: validOperands = Operand.isValidDirectiveString(codeLine.directive.operandArray.get(x).operand);
						break;
					case CHARSTR: validOperands = Operand.isValidDirectiveCharacterString(codeLine.directive.operandArray.get(x).operand);
						break;
					default: validOperands = false;
						break;
					}
					
					if(validOperands == false)
					{
						codeLine.errors.add(new Error(19,"The operand " + x + ": " + codeLine.directive.operandArray.get(x).operand + " did not match the operand type "+operand.toString()+".","Change Operand"));
					}
				}
			}

		}
	}
	
	/**
	 * 
	 * Module Name: copyStringArray
	 * Description: This copies a ArrayList of type String to an output ArrayList of type String.
	 * Input Params: stringArray
	 * 					An ArrayList of String to be copied.
	 * Output Params: ArrayList<String> 
	 * 					Copy of the input ArrayList
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010 3:23 AM
	 * Modifications:
	 */
	public static ArrayList<String> copyStringArray(ArrayList<String> stringArray)
	{
		ArrayList<String> newStringArray = new ArrayList<String>();
		for(String stringValue : stringArray)
		{
			newStringArray.add(stringValue);
		}
		return newStringArray;
	}
	
	/**
	 * 
	 * Module Name: parseStartDirective
	 * Description: This code reads the first line of an ArrayList of CodeLine and it verifies that the .START directive is properly formatted.
	 * Input Params: ArrayList<String> linesOfCode 
	 * 					The ArrayList that is to be parsed.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010
	 * Modifications:
	 */
	public static void parseStartDirective(ArrayList<String> linesOfCode)
	{
		//Check to see if there is a valid start operation else end program.
		if(linesOfCode.size() > 1)
		{
			Directive tempDirective = returnDirective(linesOfCode.get(0),false);
			if(tempDirective != null && currentErrorArray.size() == 0 && tempDirective.directiveName.equals(".START"))
			{
				StringTokenizer st = new StringTokenizer(linesOfCode.get(0), " \t", false);
				String possibleDirective = "";
				if (st.hasMoreTokens())
				{
					possibleDirective = st.nextToken();
				}
				
				String[] specialDirectives = possibleDirective.split(",");
				
				programName = specialDirectives[1];
				startingLocation = Integer.valueOf(specialDirectives[2]);
				PC = startingLocation;
				SymbTable.addSymbol(programName, PC.toString(), "NONE", SymbolTable.Uses.PROGRAM_NAME);
				endProgram = false;
			}
			else
			{
				currentErrorArray.clear();
				currentErrorArray.add(returnError(20));
				endProgram = true;
			}
			CodeLineArray.add(parseCodeLine(linesOfCode.get(0)));
			linesOfCode.remove(0);
		}
	}

	/**
	 * 
	 * Module Name: printIntermediateFile
	 * Description: Outputs an Intermediate file for use in debugging and for SP1.
	 * Input Params: String fileName
	 * 					This is the file name of the file you want to output.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10-15-2010
	 * Modifications: Added A\R\E output 11-6-2010 Robert Schmidt
	 */
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
				//bufferedWriter.write("A\\R\\E: " + codeline.scope.toString() + "\n");
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
	 * 
	 * Module Name: parseCodeLine
	 * Description: This is where we will parse each line of code. 
	 * All directive testing / checking will be in this function.
	 * Input Params: lineOfCode
	 * 				a string of code to be parsed.
	 * Output Params: CodeLine 
	 * 				the parsed code line.
	 * Error Conditions Tested: General syntax checked
	 * Error Messages Generated: Error 99, General Error check line syntax
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/2/2010
	 * Modifications: 10/10/2010: Modified to include extra functionality to check for instructions and directives.
	 * 					Also modified module to print out instructions, directives, symbols or errors found.
	 * 					Added test to see if line of code was neither an instruction, directive or symbol we printed an error.
	 * 				  10/17/2010: Fixed Mem.skip code to add various error checks.
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
				//System.out.println("Instruction: " + lineOfCodeMinusComment);
				cl.instruction = returnInstruction(lineOfCodeMinusComment,false);
				// Extract Valid Features
			} 
			else if (returnDirective(lineOfCodeMinusComment,true) != null) 
			{
				cl.directive = returnDirective(lineOfCodeMinusComment,false);
				// Extract Valid Features
				//System.out.println("Directive: " + lineOfCodeMinusComment);
			} 
			else if (returnSymbolInstruction(lineOfCodeMinusComment,true) != null) 
			{
				StringTokenizer st2 = new StringTokenizer(lineOfCodeMinusComment, " \t",false);
				String symbol = st2.nextToken();

				if (returnSymbolInstruction(lineOfCodeMinusComment,true) != null) 
				{
					if (returnSymbolInstruction(lineOfCodeMinusComment, false).getClass() == Directive.class) 
					{
						cl.directive = (Directive) returnSymbolInstruction(lineOfCodeMinusComment, false);	
					} 
					else if (returnSymbolInstruction(lineOfCodeMinusComment, false).getClass() == Instruction.class)
					{
						cl.instruction = (Instruction) returnSymbolInstruction(lineOfCodeMinusComment, false);		
					}
				}
				if (cl.directive != null)
				{
					if(cl.directive.directiveName.equals("EQU") || cl.directive.directiveName.equals("EQU.EXP"))
					{
						SymbTable.addSymbol(symbol,PC.toString(), cl.directive.operandArray.get(0).operand, SymbolTable.Uses.EQU);
						symbol = "";
					}
				}
				if(symbol != "")
				{
					SymbTable.addSymbol(symbol,PC.toString(), "NONE", SymbolTable.Uses.DATA_LABEL);
					symbol = "";
				}
			} 
			else 
			{
				currentErrorArray.add(returnError(99));
			}
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
		
		//System.out.println(currentErrorArray.size());
		return cl;

	}
	
	/**
	 * 
	 * Module Name: checkSymbolsAndSpecialDirectives
	 * Description: Checks code line for special directives mem.skip and reset.lc and equ.exp and 
	 * updates the program counter accordingly.
	 * Input Params: cl
	 * 			holds the codeline object that is to be checked for mem.skip or reset.lc or equ.exp
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/2/2010
	 * Modifications: 10/11/2010: Modified to include checking and dealing with directives (mem.skip and reset.lc)
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
	 * 
	 * Module Name: addToPC
	 * Description: Used to update PC counter.
	 * Input Params: addValue
	 * 			Integer that is to be added to PC.
	 * Output Params: N/A
	 * Error Conditions Tested: Maximum PC value.
	 * Error Messages Generated: Error 6, memory out of bounds.
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/17/2010
	 * Modifications: N/A
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
	 * 
	 * Module Name: returnDirective
	 * Description: Checks the string passed in for a directive.
	 * Input Params: codeString
	 * 					Holds the codeString to be checked for directives
	 * 				 addErrors
	 * 					Indicates whether or not we ant to add errors so that an error will
	 * 					only be added once.
	 * Output Params: Directive
	 * 					Holds the directive object extracted from the codeString.
	 * Error Conditions Tested: 	Mismatch name between start and end.
	 * 								No program name at end.
	 * 								Missing parameters at start.
	 * 								Improper start directive.
	 * 								Error with reset directive.
	 * 								Error with mem.skip directive.
	 * 								Error with exec.start.
	 * 								Too many parameters on mem.skip.
	 * Error Messages Generated: 	Error 1,  End name not same as the start name.
	 * 								Error 7,  .END code missing program name.
	 * 								Error 8,  .START missing parameters.
	 * 								Error 9,  Error Processing .START.
	 * 								Error 11, Error Processing Reset.
	 * 								Error 12, Error Processing Mem.skip.
	 * 								Error 13, Error Processing Exec.start.
	 * 								Error 14, Mem.skip has too many parameters.
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/12/2010
	 * Modifications: 10/17/2010: Modified to accept a boolean parameter to denote
	 * 							whether or not we want to add errors.		
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
					directiveObj.directiveName = directive.directiveName;
					directiveObj.labelType = directive.labelType;
					directiveObj.codeLocation = directive.codeLocation;
					//directiveObj.operands.clear();
				
					for (int x = 0; x < operands.length; x++) 
					{
						directiveObj.operands.add(directive.operands.get(x));
						directiveObj.operandArray.add(new Operand(operands[x],directive.operands.get(x)));
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
					//programName = specialDirectives[1];
					//startingLocation = Integer.valueOf(specialDirectives[2]);
					//PC = startingLocation;
					//SymbTable.addSymbol(programName, PC.toString(), "NONE", SymbolTable.Uses.PROGRAM_NAME);
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
			String[] tempString = Operand.splitByCharacter(codeString, '\'');
			if(tempString.length < 4)
			{
				directiveObj.operandArray.clear();
				directiveObj.operandArray.add(new Operand("'"+tempString[1]+"'",Directive.operandTypes.STRING));
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
				directiveObj.operandArray.add(new Operand("'"+tempString[1]+"'",Directive.operandTypes.EXP));
				//System.out.println("QWER+: " + tempString[1]);
			}
		}
		
		if (directiveObj.directiveName == "") 
		{
			directiveObj = null;
		}
		// Code to check special directives e.g. .end and .start
		return directiveObj;
	}
	
	/**
	 * 
	 * Module Name: removeWhiteSpace
	 * Description: Removes whitespace and tab characters from the parameter string.
	 * Input Params: InputString
	 * 					Contains the String that is to have whitespace and tabs removed.
	 * Output Params: String
	 * 					Contains parameter string void of whitespace and tab characters.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/2/2010
	 * Modifications: N/A		
	 */
	public static String removeWhiteSpace(String InputString) 
	{
		InputString = InputString.replaceAll(" ", "");
		InputString = InputString.replaceAll("\t", "");
		return InputString;
	}
	
	/**
	 * 
	 * Module Name: returnSymbolInstruction
	 * Description: Extracts and returns the symbol found in the string parameter.
	 * Additionally, if an error is encountered it adds the error to the currentErrorArray if the 
	 * addErrors parameter is set to true.
	 * Input Params: instruction
	 * 					Carries the string to extract the symbol instruction from.
	 * 				 addErrors
	 * 					Indicates whether or not we ant to add errors so that an error will
	 * 					only be added once.
	 * Output Params: Object
	 * 					Holds the symbol (directive or instruction or null) found in the instruction parameter.
	 * 				Updates the currentErrorArray when needed and the addError boolean is set to true.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/4/2010
	 * Modifications: 10/11/2010: Can now add symbols to the symbol table.		
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
	 * 
	 * Module Name: returnInstruction
	 * Description: Returns an instruction from the parameter string, if present.
	 * Input Params: instructionWithOperands
	 * 					Holds a string with a possible instruction and its operands to be checked and returned
	 * 					the caller if present.
	 * 				 addErrors
	 * 					Indicates whether or not we ant to add errors so that an error will
	 * 					only be added once.
	 * Output Params: Instruction
	 * 					Holds an instruction object that is constructed from information gathered from
	 * 				incoming parameters, or null if there is no instruction present.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/12/2010
	 * Modifications: 10/17/2010: Accepts a boolean parameter to determine if we wand to add errors.
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
						instructionObj.operandsArray.add(new Operand(operands[x],operand));
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

	/**
	 * 
	 * Module Name: joinStringArray.
	 * Description: Function that joins a String array by a delimiter.
	 * Input Params: stringArray
	 *            		Is passed a string array that is to be joined.
	 *               delimiter
	 *            		This is what string should be placed the elements joined in the stringArray.
	 * Output Params: Returns a String that is the joined array plus delimiters.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt.
	 * Date of Installation: 10/12/2010.
	 * Modifications:
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
	 * 
	 * Module Name: readFileToArrayList
	 * Description: readFileToArrayList reads a file and stores its contents in an array with 
	 * each line in the file occupying a specific position in the array.
	 * Input Params: fileName
	 *            Is passed the file location of the file to be read and
	 *            converted into a string array
	 * Output Params: Returns an ArrayList<String> where each string contains one line
	 *         of code in order.
	 * Error Conditions Tested: Input file Exits.
	 * Error Messages Generated: "File not found".
	 * Original Author: Robert Schmidt.
	 * Date of Installation: 10/4/2010.
	 * Modifications:
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
	 * 
	 * Module Name: fillInstructionsArray.
	 * Description: This function reads a specified instruction file we have
	 * formatted and it adds the data from the specified instruction
	 * file into the public static InstructionArray. This allows us
	 * to easily check to see if an instruction is in the correct
	 * format.
	 * Input Params: fileName
	 *            This is the file location of the instruction table.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/4/2010.
	 * Modifications:
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
				Instruction.instructionTypes instructionType = Instruction.instructionTypes.I;
				if (variables[5].equals("I")) 
				{
					instructionType = Instruction.instructionTypes.I;
				} 
				else if (variables[5].equals("R")) 
				{
					instructionType = Instruction.instructionTypes.R;
				} 
				else if (variables[5].equals("S")) 
				{
					instructionType = Instruction.instructionTypes.S;
				}
				else if (variables[5].equals("J"))
				{
					instructionType = Instruction.instructionTypes.J;
				}
				else if (variables[5].equals("IO"))
				{
					instructionType = Instruction.instructionTypes.IO;
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
					else if (operand.equals("SIMM")) 
					{
						operandArray.add(Instruction.operandTypes.SIGNEDIMMEDIATE);
					}
					else if (operand.equals("UIMM"))
					{
						operandArray.add(Instruction.operandTypes.UNSIGNEDIMMEDIATE);
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
					else if (operand.equals("COMPLEXADDR"))
					{
						operandArray.add(Instruction.operandTypes.COMPLEXADDRESS);
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
	 * 
	 * Module Name: fillErrorArray.
	 * Description: fillErrorArray is a function that reads a specified error file we have formatted
	 * and it adds the data from the specified instruction file into
	 * the public static ErrorArray. This allows us to easily check
	 * to see if an error is in the correct format.
	 * Input Params: fileName
	 *          Is a String that represents the name of the Error file that is to be read
	 *          into the ErrorArray.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt.
	 * Date of Installation: 10/4/2010.
	 * Modifications:
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
	 * Module Name: returnError.
	 * Description: The function returnError returns an error whose position in the ErrorArray
	 * corresponds to the number that we pass in as a parameter.
	 * Input Params: ErrorId
	 *     		       The error id number associated with the error.
	 * Output Params: returnError
	 *		       	   Returns an instance of the Error class object for the specified error.    			
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Oscar Flores.
	 * Date of Installation: 10/4/2010.
	 * Modifications: 
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
	 * Module Name: InstructionExists.
	 * Description: The function instructionExists checks the String parameter we pass in to 
	 * see if it holds an instruction and returns a boolean accordingly.
	 * Input Params: instructionString
	 *            The text instruction name that is to be looked up.
	 * Output Params: instructionExists
	 * 		   Returns a boolean value depending on the existence of the instruction.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Daniel Burnett.
	 * Date of Installation: 10/6/2010.
	 * Modifications:
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
	 * Module Name: returnInstructionViaOpcode
	 * Description: The function returnInstructionViaOpcode returns the instruction extracted 
	 * from the String parameter instructionString.
	 * Input Params: InstructionString
	 *            The text instruction name that is to be returned.
	 * Output Params: returnInstruction
	 * 			Returns an instance of the instruction class for the specified instruction. 
	 * Error Conditions Tested: String is a valid instruction.
	 * Error Messages Generated: Error 24
	 * Original Author: Rakaan Kayali.
	 * Date of Installation: 10/11/2010.
	 * Modifications:
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
	 * Module Name: isLiteral.
	 * Description: Checks to see if an operand is a valid literal. If so, it is added to the literal table.
	 * Input Params: String operandString.
	 * 					A string containing a possible Literal
	 * Output Params: isLiteral
	 * 			true if the operand is a valid literal, otherwise returns false.
	 * Error Conditions Tested: Check that literals are within the valid range.
	 * Error Messages Generated: Incorrect operands.
	 * Original Author: Daniel Burnett
	 * Date of Installation: 10/11/2010.
	 * Modifications: 
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
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/5/2010.	
	 * Modifications: None.
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
						operandArray.add(Directive.operandTypes.LABELREF);
					}
					else if (operand.equals("EXP")) 
					{
						operandArray.add(Directive.operandTypes.EXP);
					}
					else if (operand.equals("CHARSTR")) 
					{
						operandArray.add(Directive.operandTypes.CHARSTR);
					}
					else if (operand.equals("BOOL")) 
					{
						operandArray.add(Directive.operandTypes.BOOLEAN);
					}
				}
				Directive directive = new Directive(variables[0].toUpperCase(), labelType,codeLocation, operandArray);
				DirectivesArray.add(directive);
			}
		}
	}
}
