import java.util.ArrayList;


public class Directive 
{
	public codeLocations codeLocation = codeLocations.TEXT ;
	public String directiveName = "";
	public labelTypes labelType = labelTypes.NOLABEL;
	public ArrayList<operandTypes> operands = new ArrayList<operandTypes>();
	public ArrayList<Operand> operandArray = new ArrayList<Operand>();
	
	public static enum labelTypes
	{
		NOLABEL, REQUIREDLABEL, OPTIONALLABEL
	}
	public static enum codeLocations
	{
		START, END, DATA, TEXT, INFO
	}
	public static enum operandTypes
	{
		NUMBER, BINARY, HEX, STRING, LABEL, LABELREF, BOOLEAN
	}
	public Directive(String directiveNameValue,labelTypes labelTypeValue, codeLocations codeLocationValue, ArrayList<operandTypes> operandsValue)
	{
		this.directiveName = directiveNameValue;
		this.labelType = labelTypeValue;
		this.codeLocation = codeLocationValue;
		this.operands = operandsValue;
	}
	public Directive()
	{
		
	}
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
}
