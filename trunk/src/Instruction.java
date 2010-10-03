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
	public Boolean signed = false;
	public static enum operandTypes 
	{
		 REGISTER, IMMEDIATE, ADDRESS, AMOUNT, NUMBER;
	}
	public Instruction(String instructionValue, String  instructionHexValue, String instructionExtendedHexValue, ArrayList<operandTypes> operandsValue, boolean signedValue)
	{
		this.instruction = instructionValue;
		this.instructionHex = instructionHexValue;
		this.instructionExtendedHex = instructionExtendedHexValue;
		this.instructionBinary = String.valueOf(Integer.toBinaryString(Integer.parseInt(instructionHexValue)));
		this.instructionExtendedBinary = String.valueOf(Integer.toBinaryString(Integer.parseInt(instructionExtendedHexValue)));
		this.operands = operandsValue;
		this.signed = signedValue;
	}
	public Instruction()
	{
	
	}
	
}
