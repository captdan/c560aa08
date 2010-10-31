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
	public String returnBinaryCodeLine()
	{
		String hexCodeLine = "";
		//R type instructions
		if(this.instructionType == Instruction.instructionTypes.REGISTER)
		{
			String shiftString = "";
			if(this.instruction.equals("SRL") || this.instruction.equals("SLL") || this.instruction.equals("SRA"))
			{
				shiftString = String.valueOf(Integer.toBinaryString(Integer.parseInt(operandsArray.get(2).operand)));
				operandsArray.get(2).operand = "";
			}
			
			hexCodeLine += padZeros(this.instructionBinary,6);			//Opcode 	6 bits
			hexCodeLine += padZeros("",2); 								//Unused 	2 bits
			hexCodeLine += returnRegisterBinary(returnOperand(0)); 		//Reg 1 	3 bits
			hexCodeLine += returnRegisterBinary(returnOperand(1)); 		//Reg 2 	3 bits
			hexCodeLine += returnRegisterBinary(returnOperand(2)); 		//Reg 3 	3 bits
			hexCodeLine += padZeros(shiftString,6); 					//Shift 	6 bits
			hexCodeLine += padZeros("",3); 								//Unused 	2 bits
			hexCodeLine += padZeros(this.instructionExtendedBinary,6);	//Function 	2 bits
		}
		
		
		return hexCodeLine;
	}
	public String returnHexCodeLine()
	{
		String binaryString = returnBinaryCodeLine();
		String hexString = "";
		for(int x = 0;x<(binaryString.length() / 4);x++)
		{
			//System.out.println(binaryString.substring((4*x), (4*x)+4));
			hexString += Integer.toHexString(Integer.valueOf(binaryString.substring((4*x), (4*x)+4), 2));
		}
		return hexString;

	}
	public Instruction()
	{
	
	}
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
	public static String returnRegisterBinary(String registerString)
	{
		int registerNumber = 0;
		if (registerString.length() == 2)
		{
			registerNumber = Integer.parseInt(Character.toString(registerString.charAt(1)));
		}
		return padZeros(String.valueOf(Integer.toBinaryString(registerNumber)),3);
		
	}
	public String returnOperand(int operandIndex)
	{
		String operandString = "";
		if(operandIndex < operandsArray.size())
		{
			operandString = operandsArray.get(operandIndex).operand;
		}
		else
		{
			operandString = "";
		}
		
		return operandString;
	}
	
}
