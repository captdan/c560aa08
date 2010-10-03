import java.util.ArrayList;

public class Instructions 
{
	public int numberOfRegisters = 0;
	public String instruction = "";
	public String instructionBinary = "";
	public String instructionHex = "";
	public ArrayList<operandTypes> operands = new ArrayList<operandTypes>();
	public Boolean signed = false;
	public enum operandTypes 
	{
		 REGISTER, IMMEDIATE, ADDRESS, AMOUNT, NUMBER;
	}
	public Instructions(String instructionValue, String  instructionHexValue, ArrayList<operandTypes> operandsValue, boolean signedValue)
	{
		
		this.instruction = instructionValue;
		this.instructionHex = instructionHexValue;
		this.instructionBinary = String.valueOf(Integer.toBinaryString(Integer.parseInt(instructionHexValue)));
		this.operands = operandsValue;
		this.signed = signedValue;
	}
	
}
