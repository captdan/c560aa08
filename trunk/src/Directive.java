import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Kermit Stearns.
 * This class is an object that allows us to store a Directive and determine certain
 * attributes of that Directive such as codeLocation, directive name, label type, operand types as well as separating the operands for directives with operands. 
 */
public class Directive 
{	
	/**
	 * Used to keep track of the relative location of each directive within the program,
	 * including .start,.end,.data,.text,.info.
	 */
	public codeLocations codeLocation = codeLocations.TEXT ;
	/**
	 * Stores the name of the current directive.
	 */
	public String directiveName = "";
	/**
	 * Stores the label Type of the current directive.
	 */
	public labelTypes labelType = labelTypes.NOLABEL;
	/**
	 * Stores the Operands of the current directive if it has any.
	 */
	public ArrayList<operandTypes> operands = new ArrayList<operandTypes>();
	/**
	 * Stores every operand in the directive individually.
	 */
	public ArrayList<Operand> operandArray = new ArrayList<Operand>();
	
	/**
	 * 
	 * @author Kermit Stearns.
	 * keeps track of the current label types.
	 */
	public static enum labelTypes
	{
		NOLABEL, REQUIREDLABEL, OPTIONALLABEL
	}
	/**
	 * 
	 * @author Kermit Stearns.
	 * keeps track of the current relative locations.
	 */
	public static enum codeLocations
	{
		START, END, DATA, TEXT, INFO
	}
	/**
	 * 
	 * @author Kermit Stearns.
	 * keeps track of the current operand types.
	 */
	public static enum operandTypes
	{
		NUMBER, BINARY, HEX, STRING, LABEL, LABELREF, BOOLEAN, EXP,CHARSTR,IMMEDIATE
	}
	/**
	 * Constructor for an operand type object.
	 */
	public Directive(String directiveNameValue,labelTypes labelTypeValue, codeLocations codeLocationValue, ArrayList<operandTypes> operandsValue)
	{
		this.directiveName = directiveNameValue;
		this.labelType = labelTypeValue;
		this.codeLocation = codeLocationValue;
		this.operands = operandsValue;
	}
	/**
	 * constructor for an empty Directive object.
	 */
	public Directive()
	{
		
	}
	/**
	 * Module Name: returnPrintString.
	 * Description:	Creates a String with all the information linked to the current directive being parsed,
	 * such as Directive name, label, code location, and operands.
	 * Input Params: None.
	 * Output Params: None.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores
	 * Date of Installation: 10/10/2010.
	 * Modifications: None.
	 * @return returnString holds the String to be printed.
	 */
	public String returnPrintString()
	{
		String returnString = "";
		returnString += "Directive Name: " + this.directiveName + "\n";
		returnString += "Directive Label: " + this.labelType.toString() + "\n";
		returnString += "Directive Code Location: " + this.codeLocation + "\n";
		returnString += "Operands: \n";
		for(Operand operand : this.operandArray)
		{
			returnString += "\t" + operand.operand + "\n";
		}
		
		returnString += "Operand Types: \n";
		for(operandTypes operand : this.operands)
		{
			returnString += "\t" + operand.toString() + "\n";
		}
		
		return returnString;
	}
	/**
	 * 
	 * Module Name: returnBinaryCodeLine
	 * Description: This function returns the Binary Code for the whole CodeLine
	 * Input Params: None
	 * Output Params: String formatted in Binary that is the encoding of the CodeLine in Binary
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010
	 * Modifications:
	 * @return String formatted in Binary that is the encoding of the CodeLine in Binary
	 */
	public String returnBinaryCodeLine(CodeLine cl)
	{
		String binaryCodeLine = "";
		if(this.operandArray.size() > 0)
		{
			binaryCodeLine += padZeros(toBinary(this.operandArray.get(0),cl).get(0).operand,32);
		}
		else
		{
			binaryCodeLine += padZeros("",32);
		}
		return binaryCodeLine;
	}
	/**
	 * Module Name: returnHexCodeLine
	 * Description: This function returns the Hex Code for the whole CodeLine
	 * Input Params: None
	 * Output Params: String formatted in Hex that is the encoding of the CodeLine in Hex
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010
	 * Modifications:
	 * @return String formatted in Hex that is the encoding of the CodeLine in Hex
	 */
	public String returnHexCodeLine(CodeLine cl)
	{
		String binaryString = returnBinaryCodeLine(cl);
		String hexString = "";
		for(int x = 0;x<(binaryString.length() / 4);x++)
		{
			//System.out.println(binaryString.substring((4*x), (4*x)+4));
			hexString += Integer.toHexString(Integer.valueOf(binaryString.substring((4*x), (4*x)+4), 2));
		}
		return hexString;

	}
	/**
	 * Module Name: padZeros
	 * Description: This function adds a specified number of zeros to the input String.
	 * Input Params: String stringValue,int StringLength These are the specified max number of Zeros and the Input String to be padded.
	 * Output Params: A string value with the padded Zeros
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010
	 * Modifications:
	 * @param stringValue Input String Value to be padded by Zeros
	 * @param StringLength Max number of zeros to pad.
	 * @return String with padded Zeros on it.
	 */
	public static String padZeros(String stringValue,int StringLength)
	{
		String newString = "";
		for(int x=0;x<(StringLength-stringValue.length());x++)
		{
			newString += "0";
		}
		newString += stringValue;
		return newString;
	}
	/**
	 * 
	 * Module Name:toBinary
	 * Description:converts codeline into binary equivalent
	 * Input Params:inputOperand holds the operand for the codeline to be converted.
	 * Input Params: cl holds the codeline to be converted to binary.		
	 * Error Conditions Tested:
	 * Error Messages Generated
	 * Original Author: Robert Schmidt
	 * Date of Installation:11/09/2010
	 * Modifications:none.
	 * @param inputOperand holds the operand for the codeline to be converted.
	 * @param cl holds the codeline to be converted to binary.
	 * @return binaryOperands
	 * 				holds the codeline translated into binary.
	 */
	public ArrayList<Operand> toBinary(Operand inputOperand, CodeLine cl)
	{
		ArrayList<Operand> binaryOperands = new ArrayList<Operand>();
		
		int value = 0;
		
		 if(inputOperand.operandType == Directive.operandTypes.BINARY)
			{
				binaryOperands.add(new Operand(inputOperand.operand,Directive.operandTypes.BINARY));
			}
			else if(inputOperand.operandType == Directive.operandTypes.BOOLEAN)
			{
				binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(Integer.valueOf(inputOperand.operand))),Directive.operandTypes.BOOLEAN));
			}
			else if(inputOperand.operandType == Directive.operandTypes.CHARSTR || inputOperand.operandType == Directive.operandTypes.STRING)
			{
				String tempString = inputOperand.operand.subSequence(1, inputOperand.operand.length()-1).toString();

				//System.out.println("OK : " + inputOperand.operand);

				String tempString2 = ""; 

				for(int x=0;x<tempString.length();x++)
				{
					int asciiCode = (int)tempString.charAt(x);
					tempString2 = tempString2 + String.valueOf(Integer.toBinaryString(asciiCode));
				}
				//Fill end of string with blank spaces 
				for(int x=0; x<(4-(tempString.length() % 4));x++)
				{
					if((4-(tempString.length() % 4) != 4))
					{
					tempString2 = tempString2 + "00100000"; 
					}
				}
				if(tempString2.equals(""))
				{
					tempString2 = "00100000001000000010000000100000";
				}
				binaryOperands.add(new Operand(tempString2,Directive.operandTypes.CHARSTR));
			}
			else if(inputOperand.operandType == Directive.operandTypes.EXP)
			{

				int stringLength = 0;
				StringTokenizer expressionTokenizer = new StringTokenizer(inputOperand.operand,"+-",false);
				
					
					while(expressionTokenizer.hasMoreTokens())
					{
						String operand = expressionTokenizer.nextToken();
						stringLength = stringLength + operand.length();
						if((stringLength - 1 - operand.length()) >= 0)
						{
							String operator = String.valueOf(inputOperand.operand.charAt((stringLength - 1 - operand.length())));
							if(operator.equals("-"))
							{
								try
								{
									value = value - Integer.parseInt(operand);
								}
								catch(NumberFormatException e){}
								if(Parser.SymbTable.isInTable(operand))
								{
									ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(operand);
									try
									{
										value = value - Integer.parseInt(String.valueOf(values.get(0)));
									}
									catch(Exception e){}
								}
								if(operand.equals("*"))
								{
									value = value - cl.PC;
								}
							}
							else
							{
								try
								{
									value = value + Integer.parseInt(operand);
								}
								catch(NumberFormatException e){}
								if(Parser.SymbTable.isInTable(operand))
								{
									ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(operand);
									try
									{
										value = value + Integer.parseInt(String.valueOf(values.get(0)));
									}
									catch(Exception e){}
								}
								if(operand.equals("*"))
								{
									value = value + cl.PC;
								}
							}
						}
						else
						{
							try
							{
								value = value + Integer.parseInt(operand);
							}
							catch(NumberFormatException e){}
							if(Parser.SymbTable.isInTable(operand))
							{
								ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(operand);
								try
								{
									value = value + Integer.parseInt(String.valueOf(values.get(0)));
								}
								catch(Exception e){}
							}
							if(operand.equals("*"))
							{
								value = value + cl.PC;
							}
						}
						binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(value)),Directive.operandTypes.EXP));
				}
			}
			else if(inputOperand.operandType == Directive.operandTypes.HEX)
			{
				binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(Integer.parseInt(inputOperand.operand, 16))),Directive.operandTypes.HEX));
			}
			else if(inputOperand.operandType == Directive.operandTypes.LABEL || inputOperand.operandType == Directive.operandTypes.LABELREF)
			{
				if(Parser.SymbTable.isInTable(inputOperand.operand))
				{
					ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(inputOperand.operand);
					if (values.get(2) == SymbolTable.Uses.EXTERNAL)
					{
						value = 0;
					}
					else
					{
						try
						{
							value = value + Integer.parseInt(String.valueOf(values.get(0)));
						}
						catch(Exception e){}
					}
				}
				else if(inputOperand.operand.equals("*"))
				{
					value = cl.PC;
				}
				binaryOperands.add(new Operand(Integer.toBinaryString(value),Directive.operandTypes.LABEL));
			}
			else if(inputOperand.operandType == Directive.operandTypes.NUMBER)
			{
				binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(Integer.parseInt(inputOperand.operand))),Directive.operandTypes.NUMBER));
			}
			else if(inputOperand.operandType == Directive.operandTypes.IMMEDIATE)
			{
				Operand tempOperand = null;
				if(inputOperand.operand.startsWith("'"))
				{
					String tempString = inputOperand.operand.subSequence(1, inputOperand.operand.length()-1).toString();
					String tempString2 = ""; 
					for(int x=0;x<tempString.length();x++)
					{
						int asciiCode = (int)tempString.charAt(x);
						tempString2 = tempString2 + padZeros(String.valueOf(Integer.toBinaryString(asciiCode)),8);
					}
					tempOperand = new Operand(tempString2,Directive.operandTypes.IMMEDIATE);
				}
				else
				{
					tempOperand = new Operand(String.valueOf(Integer.toBinaryString(Integer.valueOf(inputOperand.operand))),Directive.operandTypes.IMMEDIATE);
				}
				binaryOperands.add(tempOperand);
			}


		 return binaryOperands;
	}
}
