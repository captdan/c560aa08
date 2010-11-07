import java.util.ArrayList;

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
		NUMBER, BINARY, HEX, STRING, LABEL, LABELREF, BOOLEAN, EXP,CHARSTR
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
	 * 
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
	 * @return
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
	
	/*
	 if(inputOperand.operandType == Directive.operandTypes.BINARY)
		{
			binaryOperands.add(new Operand(inputOperand.operand,Directive.operandTypes.BINARY));
		}
		else if(inputOperand.operandType == Directive.operandTypes.BOOLEAN)
		{
			binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(Integer.valueOf(inputOperand.operand))),Directive.operandTypes.BOOLEAN));
		}
		else if(inputOperand.operandType == Directive.operandTypes.CHARSTR)
		{
			String tempString = inputOperand.operand.subSequence(1, inputOperand.operand.length()-1).toString();
			String tempString2 = ""; 
			for(int x=0;x<tempString.length();x++)
			{
				int asciiCode = (int)tempString.charAt(x);
				tempString2 = tempString2 + String.valueOf(Integer.toBinaryString(asciiCode));
			}
			//Fill end of string with blank spaces 
			for(int x=0; x<(tempString2.length() - (4 * Math.floor(tempString2.length() / 4)));x++)
			{
				tempString2 = tempString2 + "00100000"; 
			}
			binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(Integer.valueOf(inputOperand.operand))),Directive.operandTypes.CHARSTR));
		}
		else if(inputOperand.operandType == Directive.operandTypes.EXP)
		{
			//TODO this needs to be done, not sure exactly how
		}
		else if(inputOperand.operandType == Directive.operandTypes.HEX)
		{
			binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(Integer.parseInt(inputOperand.operand, 16))),Directive.operandTypes.HEX));
		}
		else if(inputOperand.operandType == Directive.operandTypes.LABEL)
		{
			
		}
	 */
}
