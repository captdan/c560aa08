import java.util.ArrayList;

public class Instruction 
{
	public int numberOfRegisters = 0;
	public String instruction = "";
	public String instructionBinary = "";
	public String instructionHex = "";
	public String instructionExtended = "";
	public String instructionExtendedHex = "";
	public String instructionExtendedBinary = "";
	public ArrayList<Operand> operandsArray = new ArrayList<Operand>();
	public ArrayList<operandTypes> operands = new ArrayList<operandTypes>();
	public instructionTypes instructionType = Instruction.instructionTypes.REGISTER;
	public static enum operandTypes 
	{
		 REGISTER, IMMEDIATE, ADDRESS, BIT, BITS, NUMBER, IO;
	}
	public static enum instructionTypes 
	{
		 IMMEDIATE, SIGNED, REGISTER, J;
	}
	public String returnPrintString()
	{
		String returnString = "";
		returnString += "Instruction: " + this.instruction + "\n";
		returnString += "Instruction Type: " + this.instructionType.toString() + "\n";
		returnString += "Instruction Binary: " + this.instructionBinary + "\n";
		returnString += "Instruction Hex: " + this.instructionHex + "\n";
		if(this.instructionExtended.equals("") != true)
		{
			returnString += "Instruction Extended: " + this.instructionExtended + "\n";
			returnString += "Instruction Extended Hex: " + this.instructionExtendedHex + "\n";
			returnString += "Instruction Extended Binary: " + this.instructionExtendedBinary + "\n";
		}
		returnString += "Operands: " + "\n";
		for(Operand operand : this.operandsArray)
		{
			returnString += "\t" + operand.operand + "\n";
		}
		returnString += "Operand Types: " + "\n";
		for(Instruction.operandTypes operands : this.operands)
		{
			returnString += "\t" + operands.toString() + "\n";
		}
		
		return returnString;
	}
	public Instruction(String instructionValue, String  instructionHexValue, String instructionExtendedHexValue, ArrayList<operandTypes> operandsValue, instructionTypes instructionTypeValue)
	{
		this.instruction = instructionValue;
		this.instructionHex = instructionHexValue;
		this.instructionExtendedHex = instructionExtendedHexValue;
		this.numberOfRegisters = operandsValue.size();
		this.operands = operandsValue;
		this.instructionType = instructionTypeValue;
		
		if(instructionHexValue.equals("") == false)
		{
			this.instructionBinary = String.valueOf(Integer.toBinaryString(Integer.parseInt(instructionHexValue.trim(), 16)));
		}
		
		if(instructionExtendedHexValue.equals("") == false)
		{
			this.instructionExtendedBinary = String.valueOf(Integer.toBinaryString(Integer.parseInt(instructionExtendedHexValue.trim(), 16)));	
		}


	}
	public Instruction()
	{
	
	}
	
}
