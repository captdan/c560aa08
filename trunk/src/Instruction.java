import java.util.ArrayList;
import java.util.regex.Pattern;

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
	public instructionTypes instructionType = Instruction.instructionTypes.R;
	public static enum operandTypes 
	{
		 REGISTER, SIGNEDIMMEDIATE, UNSIGNEDIMMEDIATE, ADDRESS, BIT, BITS, NUMBER, COMPLEXADDRESS;
	}
	public static enum instructionTypes 
	{
		 I, S, R, J,IO;
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
	public String returnBinaryCodeLine()
	{
		String binaryCodeLine = "";
		//R type instructions
		if(this.instructionType == Instruction.instructionTypes.R)
		{
			String shiftString = "";
			if(this.instruction.equals("SRL") || this.instruction.equals("SLL") || this.instruction.equals("SRA"))
			{
				shiftString = String.valueOf(Integer.toBinaryString(Integer.parseInt(operandsArray.get(2).operand)));
				operandsArray.get(2).operand = "";
			}
			
			binaryCodeLine += padZeros(this.instructionBinary,6);			//Opcode 	6 bits
			binaryCodeLine += padZeros("",2); 								//Unused 	2 bits
			binaryCodeLine += returnRegisterBinary(returnOperand(0)); 		//Reg 1 	3 bits
			binaryCodeLine += returnRegisterBinary(returnOperand(1)); 		//Reg 2 	3 bits
			binaryCodeLine += returnRegisterBinary(returnOperand(2)); 		//Reg 3 	3 bits
			binaryCodeLine += padZeros(shiftString,6); 						//Shift 	6 bits
			binaryCodeLine += padZeros("",3); 								//Unused 	2 bits
			binaryCodeLine += padZeros(this.instructionExtendedBinary,6);	//Function 	2 bits
		}
		if(this.instructionType == Instruction.instructionTypes.J)
		{
			binaryCodeLine += padZeros(this.instructionBinary,6);			//Opcode	6 bits
			binaryCodeLine += padZeros("",2);								//AddrCode	2 bits
			binaryCodeLine += padZeros("",8);								//Unused	8 bits
			binaryCodeLine += padZeros(toBinary(returnOperand(0)),16);		//Address	16 Bits
		}
		
		
		return binaryCodeLine;
	}
	
	/**
	 * 
	 * Module Name: toBinary
	 * Description: This function takes an input String and tries to convert it to a binary output.
	 * Input Params: String inputString The string that is to be converted to Binary.
	 * Output Params: String Formatted in binary that is the conversion from the input string.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010
	 * Modifications: 
	 * @param inputString The string that is to be converted to Binary.
	 * @return String Formatted in binary that is the conversion from the input string.
	 */
	public String toBinary(String inputString)
	{
		String binaryString = "";
		if(Pattern.matches(inputString, "[0_9*]"))
		{
			binaryString = String.valueOf(Integer.toBinaryString(Integer.valueOf(inputString)));
		}
		else if(Pattern.matches(inputString, "[0_9-*]"))
		{
			if(inputString.startsWith("-"))
			{
				binaryString = String.valueOf(Integer.toBinaryString((-1*Integer.valueOf(inputString.substring(1)))));
			}
		}
		else if(Pattern.matches(inputString, "['][a_zA_Z0_9_*][']"))
		{
			for(int x=0;x<inputString.subSequence(1, inputString.length()-1).length(); x++)
			{
				binaryString += Integer.toString((int)inputString.subSequence(1, inputString.length()-1).charAt(x));
			}
		}
		else if(Pattern.matches(inputString, "[a_zA_Z0_9_*]"))
		{
			
		}
			
		return binaryString;
	}

	/**
	 * 
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
	/**
	 * 
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
	 * Module Name: returnRegisterBinary
	 * Description: This function returns a String that is formatted in Binary of what the input register was.
	 * Input Params: String registerString a SAL560 Register Name.
	 * Output Params: String a String formatted in Binary that represents the register name.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010
	 * Modifications:
	 * @param registerString Input String register to be converted to Binary.
	 * @return Output String formatted in binary representing the input register.
	 */
	public static String returnRegisterBinary(String registerString)
	{
		int registerNumber = 0;
		if (registerString.length() == 2)
		{
			registerNumber = Integer.parseInt(Character.toString(registerString.charAt(1)));
		}
		return padZeros(String.valueOf(Integer.toBinaryString(registerNumber)),3);
		
	}
	
	/**
	 * 
	 * Module Name: returnOperand
	 * Description: This function takes an input OperandIndex and outputs the operand String.
	 * Input Params: int operandIndex This is the index in the operandsArray.
	 * Output Params: String, This string is the output of the contents in the register.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010
	 * Modifications: 
	 * @param operandIndex This is the input OperandIndex.
	 * @return This is the return String with the contents of the operand in it.
	 */
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
