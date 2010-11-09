import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
	/**
	 * 
	 * @author Team c560aa08 collaboration.
	 * this class creates an Instruction object that allow us to easily iterate through an instruction line of code, and parse its Hex and Binary values.
	 * 
	 */
public class Instruction 
{	
	/**
	 * Stores the Number of registers in an instruction.
	 */
	public int numberOfRegisters = 0;
	/**
	 * Stores the instruction name.
	 */
	public String instruction = "";
	/**
	 * Stores the binary representation of the instruction.
	 */
	public String instructionBinary = "";
	/**
	 * Stores the hexadecimal representation of the instruction.
	 */
	
	public String instructionHex = "";
	/**
	 * Stores the Extended representation of the instruction.
	 */
	public String instructionExtended = "";
	/**
	 * Stores the extended hexadecimal representation of the instruction.
	 */
	public String instructionExtendedHex = "";
	/**
	 * Stores the extended binary representation of the instruction.
	 */
	public String instructionExtendedBinary = "";
	/**
	 * Address code for instructions
	 */
	public String addressCode = "00";
	/**
	 * Stores individual operands in the directive.
	 */
	public ArrayList<Operand> operandsArray = new ArrayList<Operand>();
	/**
	 * Stores the Operands of the current directive if it has any.
	 */
	public ArrayList<operandTypes> operands = new ArrayList<operandTypes>();
	/**
	 * keeps track of the current instruction types.
	 */
	public instructionTypes instructionType = Instruction.instructionTypes.R;
	/**
	 * 
	 * @author Kermit Stearns.
	 * Enumeration for operand types.
	 */
	public static enum operandTypes 
	{
		 REGISTER, SIGNEDIMMEDIATE, UNSIGNEDIMMEDIATE, ADDRESS, BIT, BITS, NUMBER, COMPLEXADDRESS;
	}
	/**
	 * 
	 * @author Kermit Stearns.
	 * Enumeration for instruction types.
	 */
	public static enum instructionTypes 
	{
		 I, S, R, J,IO;
	}
	/**
	 * 
	 * Module Name: returnPrintString.
	 * Description: Creates a String with all the information linked to the current Instruction being parsed,
	 * such as Directive name, type, binary and hex (including extended) representation, and operands.
	 * Input Params: None.
	 * Output Params: 
	 * Error Conditions Tested:
	 * Error Messages Generated:
	 * Original Author: Oscar Flores
	 * Date of Installation: 09/03/2010
	 * Modifications: added returnBinaryCodeLine, toBinary, returnHexCodeLine,padZeros,returnRegisterBinary and returnOperand
	 * @return
	 */
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
	/**
	 * Constructor for Instruction object.
	 * @param instructionValue
	 * 				Original line of code without comments containing an instruction.
	 * @param instructionHexValue
	 * 				Stores the hex representation of the instruction.
	 * @param instructionExtendedHexValue
	 * 				Stores the extended hex representation of the instruction.
	 * @param operandsValue
	 * 				Contains operands in the instruction.
	 * @param instructionTypeValue
	 * 				Contains the type of instruction.
	 */
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
			binaryCodeLine += padZeros(this.instructionBinary,6);												//Opcode 	6 bits
			binaryCodeLine += padZeros("",2); 																	//Unused 	2 bits
			binaryCodeLine += padZeros(toBinary(returnOperand(0,operandTypes.REGISTER)).get(0).operand,3); 		//Reg 1 	3 bits
			binaryCodeLine += padZeros(toBinary(returnOperand(1,operandTypes.REGISTER)).get(0).operand,3); 		//Reg 2 	3 bits
			binaryCodeLine += padZeros(toBinary(returnOperand(2,operandTypes.REGISTER)).get(0).operand,3); 		//Reg 3 	3 bits
			binaryCodeLine += padZeros(toBinary(returnOperand(2,operandTypes.BITS)).get(0).operand,6);			//Shift 	6 bits
			binaryCodeLine += padZeros("",3); 																	//Unused 	2 bits
			binaryCodeLine += padZeros(this.instructionExtendedBinary,6);										//Function 	2 bits
		}
		if(this.instructionType == Instruction.instructionTypes.J)
		{
			binaryCodeLine += padZeros(this.instructionBinary,6);												//Opcode	6 bits
			binaryCodeLine += padZeros("",2);																	//AddrCode	2 bits
			binaryCodeLine += padZeros("",8);																	//Unused	8 bits
			binaryCodeLine += padZeros(toBinary(returnOperand(0,operandTypes.NUMBER)).get(0).operand,16);		//Address	16 Bits
		}
		if(this.instructionType == Instruction.instructionTypes.I)
		{
			binaryCodeLine += padZeros(this.instructionBinary,6);													//Opcode	6 bits
			binaryCodeLine += padZeros("",2);																		//Unused	2 bits	
			binaryCodeLine += padZeros(toBinary(returnOperand(0,operandTypes.REGISTER)).get(0).operand,3);			//Reg 1 	3 bits
			binaryCodeLine += padZeros(toBinary(returnOperand(1,operandTypes.REGISTER)).get(0).operand,3);			//Reg 2 	3 bits
			binaryCodeLine += padZeros("",2);																		//Unused 	2 bits
			binaryCodeLine += padZeros(toBinary(returnOperand(-1,operandTypes.SIGNEDIMMEDIATE)).get(0).operand,16);	//IMM		16 Bits
		}
		if(this.instructionType == Instruction.instructionTypes.IO)
		{
			binaryCodeLine += padZeros(this.instructionBinary,6);													//Opcode	6 bits
			binaryCodeLine += padZeros("",2);																		//AdrrCode	2 bits	
			binaryCodeLine += padZeros(toBinary(returnOperand(1,operandTypes.COMPLEXADDRESS)).get(1).operand,3);	//Reg 1 	3 bits
			binaryCodeLine += padZeros(toBinary(returnOperand(0,operandTypes.NUMBER)).get(0).operand,5);			//NumWords 	5 bits
			binaryCodeLine += padZeros(toBinary(returnOperand(1,operandTypes.COMPLEXADDRESS)).get(0).operand,16);	//IMM		16 Bits
		}
		if(this.instructionType == Instruction.instructionTypes.S)
		{
			String reg1 = "";
			String reg2 = "";
			String addr = "";
			if(returnOperand(0,operandTypes.REGISTER).operandType == Instruction.operandTypes.COMPLEXADDRESS)
			{
				reg1 = padZeros(toBinary(returnOperand(0,operandTypes.COMPLEXADDRESS)).get(1).operand,3);
				addr = padZeros(toBinary(returnOperand(0,operandTypes.COMPLEXADDRESS)).get(0).operand,16);
			}
			else if(returnOperand(1,operandTypes.REGISTER).operandType == Instruction.operandTypes.COMPLEXADDRESS)
			{
				reg1 = padZeros(toBinary(returnOperand(0,operandTypes.REGISTER)).get(0).operand,3);
				reg2 = padZeros(toBinary(returnOperand(1,operandTypes.COMPLEXADDRESS)).get(1).operand,3);
				addr = padZeros(toBinary(returnOperand(1,operandTypes.COMPLEXADDRESS)).get(0).operand,16);
			}
			else
			{
				reg1 = padZeros(toBinary(returnOperand(0,operandTypes.REGISTER)).get(0).operand,3);
				reg2 = padZeros(toBinary(returnOperand(1,operandTypes.REGISTER)).get(0).operand,3);
				addr = padZeros(toBinary(returnOperand(2,operandTypes.ADDRESS)).get(0).operand,16);
			}
			binaryCodeLine += padZeros(this.instructionBinary,6);	//Opcode	6 bits
			binaryCodeLine += padZeros(this.addressCode,2);			//Address	2 bits
			binaryCodeLine += padZeros(reg1,3);						//Reg 1 	3 bits
			binaryCodeLine += padZeros(reg2,3);						//Reg 2 	3 bits
			binaryCodeLine += padZeros("",2);						//Unused	2 Bits
			binaryCodeLine += padZeros(addr,16);					//ADDR		16 Bits
		}
		
		
		return binaryCodeLine;
	}
	
	/**
	 * 
	 * Module Name: toBinary
	 * Description: This function takes an input String and tries to convert it to a binary output.
	 * Input Params: Operand inputOperand The operand string that is to be converted to Binary.
	 * Output Params: String Formatted in binary that is the conversion from the input string.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11-6-2010
	 * Modifications: 
	 * @param inputOperand The string that is to be converted to Binary.
	 * @return String Formatted in binary that is the conversion from the input operand.
	 */
	public ArrayList<Operand> toBinary(Operand inputOperand)
	{
		ArrayList<Operand> binaryOperands = new ArrayList<Operand>();
		
		int value = 0;
		int value2 = 0;
		
		if(inputOperand.operandType == Instruction.operandTypes.REGISTER)
		{
			if (inputOperand.operand.length() == 2)
			{
				value = Integer.parseInt(Character.toString(inputOperand.operand.charAt(1)));
			}
			binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(value)),Instruction.operandTypes.REGISTER));
		}
		else if(inputOperand.operandType == Instruction.operandTypes.ADDRESS)
		{
			if(Parser.SymbTable.isInTable(inputOperand.operand))
			{
				ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(inputOperand.operand);
				try
				{
					value = Integer.parseInt(String.valueOf(values.get(0)));
				}
				catch(NumberFormatException e){}
			}
			else
			{
				try
				{
					value = Integer.parseInt(inputOperand.operand);
				}
				catch(NumberFormatException e){}
			}
			binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(value)),Instruction.operandTypes.ADDRESS));
		}
		else if(inputOperand.operandType == Instruction.operandTypes.BIT)
		{
			binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(Integer.valueOf(inputOperand.operand))),Instruction.operandTypes.BIT));
		}
		else if(inputOperand.operandType == Instruction.operandTypes.BITS)
		{
			binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(Integer.valueOf(inputOperand.operand))),Instruction.operandTypes.BITS));
		}
		else if(inputOperand.operandType == Instruction.operandTypes.COMPLEXADDRESS)
		{
			StringTokenizer addressTokenizer = new StringTokenizer(inputOperand.operand,"()",false);

			if(addressTokenizer.countTokens() == 2)
			{
				String label = addressTokenizer.nextToken();
				String register = addressTokenizer.nextToken();
				if(Parser.SymbTable.isInTable(label))
				{
					ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(label);
					try
					{
						
						value = Integer.parseInt(String.valueOf(values.get(0)));
					}
					catch(NumberFormatException e){}
				}
				else
				{
					value = Integer.parseInt(toBinary(new Operand(label,Instruction.operandTypes.ADDRESS)).get(0).operand);
				}
				value2 = Integer.parseInt(toBinary(new Operand(register,Instruction.operandTypes.REGISTER)).get(0).operand);
			}
			else if(addressTokenizer.countTokens() == 1)
			{
				String register = addressTokenizer.nextToken();
				if(register.charAt(0) != '$')
				{
					value = Integer.parseInt(toBinary(new Operand(register,Instruction.operandTypes.ADDRESS)).get(0).operand);
					value2 = 0;
				}
				else
				{
					value = 0;
					value2 = Integer.parseInt(toBinary(new Operand(register,Instruction.operandTypes.REGISTER)).get(0).operand);
				}
			}
			binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(value)),Instruction.operandTypes.ADDRESS));
			binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(value2)),Instruction.operandTypes.REGISTER));
		}
		else if(inputOperand.operandType == Instruction.operandTypes.NUMBER)
		{
			binaryOperands.add(new Operand(String.valueOf(Integer.toBinaryString(Integer.valueOf(inputOperand.operand))),Instruction.operandTypes.NUMBER));
		}
		else if(inputOperand.operandType == Instruction.operandTypes.SIGNEDIMMEDIATE)
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
				tempOperand = new Operand(tempString2,Instruction.operandTypes.SIGNEDIMMEDIATE);
			}
			else
			{

				String binaryString = String.valueOf(Integer.toBinaryString(Integer.valueOf(inputOperand.operand)));
				
				if(binaryString.length() > 16)
				{
					binaryString = binaryString.substring(binaryString.length()-16, binaryString.length());
				}
				
				tempOperand = new Operand(binaryString,Instruction.operandTypes.SIGNEDIMMEDIATE);
				
			}
			binaryOperands.add(tempOperand);
		}
		else if(inputOperand.operandType == Instruction.operandTypes.UNSIGNEDIMMEDIATE)
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
				tempOperand = new Operand(tempString2,Instruction.operandTypes.SIGNEDIMMEDIATE);
			}
			else
			{
				tempOperand = new Operand(String.valueOf(Integer.toBinaryString(Integer.valueOf(inputOperand.operand))),Instruction.operandTypes.SIGNEDIMMEDIATE);
			}
			binaryOperands.add(tempOperand);
		}
			
		return binaryOperands;
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
	 * Module Name: returnOperandString
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
	public String returnOperandString(int operandIndex)
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
	/**
	 * 
	 * Module Name: returnOperand
	 * Description: returns the operand.
	 * Input Params:operandIndex holds the index of the operand that needs to be returned
	 * Input Params: operandType holds to type of the operand
	 * Output Params: operandValue holds the value of the operand 
	 * Error Conditions Tested: none.
	 * Error Messages Generated: none.
	 * Original Author: Robert Schmidt
	 * Date of Installation: 11/9/2010
	 * Modifications:none.
	 * @param operandIndex holds the index of the operand that needs to be returned
	 * @param operandType holds to type of the operand
	 * @return operandValue holds the value of the operand   
	 */
	public Operand returnOperand(int operandIndex, Instruction.operandTypes operandType)
	{
		Operand operandValue = null;
		if(operandIndex == -1)
		{
			for(Operand tempOperand : operandsArray)
			{
				if (operandType == tempOperand.operandType)
				{
					operandValue = tempOperand;
				}
			}
		}
		else if(operandIndex < operandsArray.size() && operandsArray.get(operandIndex).operandType == operandType)
		{
			operandValue = operandsArray.get(operandIndex);
		}
		else
		{
			operandValue = new Operand("0",Instruction.operandTypes.NUMBER);
		}
		
		return operandValue;
	}
	
}
