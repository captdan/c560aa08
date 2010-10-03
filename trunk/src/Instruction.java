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
	public ArrayList<operandTypes> operands = new ArrayList<operandTypes>();
	public instructionTypes instructionType = Instruction.instructionTypes.REGISTER;
	public static enum operandTypes 
	{
		 REGISTER, IMMEDIATE, ADDRESS, BIT, BITS;
	}
	public static enum instructionTypes 
	{
		 IMMEDIATE, SIGNED, REGISTER;
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
