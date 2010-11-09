import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


public class Operand
{
	public String operand;
	public Object operandType;
	
	public relocationTypes relocationType = relocationTypes.A;
	/**
	 * Enumeration of possible relocation types.
	 * @author Robert Schmidt
	 *
	 */
	public enum relocationTypes
	{
		A,R,E
	}
	
	/**
	 * Constructor to create an operand object from a value and a type.
	 * @param operandValue
	 * 			The value of the operand.
	 * @param operandTypeValue
	 * 			The relocation type of the operand.
	 */
	public Operand(String operandValue, Object operandTypeValue)
	{
		operand = operandValue;
		operandType = operandTypeValue;
	}
	
	/**
	 * Default constructor.
	 */
	public Operand() 
	{
		
	}
	
	/**
	 * 
	 * Module Name: returnValue
	 * Description: Returns the value for the operand object.
	 * Input Params: N/A
	 * Output Params: String
	 * 					The value of the operand.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/27/2010
	 * Modifications: 
	 */
	public String returnValue()
	{
		if (operandType.getClass() == Instruction.class)
		{
			Instruction.operandTypes operandTypeValue = (Instruction.operandTypes) operandType;
			switch (operandTypeValue)
			{
			case ADDRESS:
				break;
			
			default:
				break;
			}
		}
		else if(operandType.getClass() == Directive.class)
		{
			
		}
		
		//Parser.SymbTable.isInTable('')
		return "";
	}
	
	/**
	 * 
	 * Module Name: returnComplexAddressLabel
	 * Description: Returns the label part of an operand with an index register.
	 * Input Params: N/A
	 * Output Params: String
	 * 					The label to be returned.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10/27/2010
	 * Modifications: 
	 */
	public String returnComplexAddressLabel()
	{
		String label = "";
		if(this.operandType == Instruction.operandTypes.COMPLEXADDRESS)
		{
			if(this.operand.contains("("))
			{
				label = this.operand.substring(0, this.operand.indexOf('('));
			}
			else
			{
				label = this.operand;
			}
		}
		return label;
	}
	
	/**
	 * 
	 * Module Name: isValidInstructionRegister
	 * Description: This function returns a Boolean value dependent on if the Input String is a valid Register or Not.
	 * Input Params: String register Input String to be validated
	 * Output Params: Boolean This value is true or false based on if the Input String was valid or not.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Robert Schmidt
	 * Date of Installation: 10-27-2010
	 * Modifications:
	 * @param register Input String that is to be validated as a register or not.
	 * @return Boolean value that is determined by if the input String is valid or not.
	 */
	static boolean isValidInstructionRegister (String register) 
	{
		Boolean isRegister = false;
		if (register.length() == 2) 
		{
			int registerNumber = Integer.parseInt(Character.toString(register.charAt(1)));
			if (register.charAt(0) == '$' && registerNumber >= 0 && registerNumber < 8) 
			{
				isRegister = true;
			}
		}

		return isRegister;
	}
	/**
	 * 
	 * Module Name: isValidInstructionSignedImmediate
	 * Description: Function returns a Boolean value that is dependent if the input String is a valid SAL 560 SignedImmediate.
	 * Input Params: String immediate Input String that is being validated as a SignedImmediate.
	 * Output Params: Boolean value that is returned based if the input string was valid or not.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Dan
	 * Date of Installation: 10-24-2010
	 * Modifications:
	 * @param immediate Input String that is to be tested for being valid or not.
	 * @return Boolean value that is returned based if the input string was valid or not.
	 */
	static boolean isValidInstructionSignedImmediate (String immediate) 
	{
		Boolean isImmediate = false;
		int value = 99999;
		
		if(immediate.startsWith("'") && immediate.endsWith("'"))
		{
			String[] stringArray = immediate.split("'");
		
			if(stringArray[1].length() <= 2) 
			{
				if(Pattern.matches("[a-zA-Z_0-9_]*",stringArray[1]))
				{
					isImmediate = true;
				}
			}
		}
		else
		{
			if(immediate.length() > 0)
			{
				if(immediate.charAt(0) == '-')
				{
					try
					{
						value = Integer.parseInt(String.valueOf(immediate.substring(1)));
						value = (value * -1);
					}
					catch(NumberFormatException e){}
				}
				else
				{
					try
					{
						value = Integer.parseInt(String.valueOf(immediate));
					}
					catch(NumberFormatException e){}
				}
			}
			
			if(value >= -23768 && value < 23768)
			{
				isImmediate = true;
			}
		}
		
		return isImmediate;
	}
	/**
	 * 
	 * Module Name: isValidInstructionUnSignedImmediate
	 * Description: Function returns a Boolean value dependent on if the input String is a valid SAL560 UnsignedImmediate.
	 * Input Params: String immediate Input String to be validated as an UnsignedImmediate.
	 * Output Params: Boolean Value that is determined by if the String is deemed valid or not.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Dan
	 * Date of Installation: 10-27-2010
	 * Modifications: None
	 * @param immediate Input String to be tested if it is a valid UnsignedImmediate.
	 * @return Boolean value that is determined by if the String is deemed valid or not.
	 */
	static boolean isValidInstructionUnSignedImmediate (String immediate) 
	{
		Boolean isImmediate = false;
		int value = 99999;
		
		if(immediate.startsWith("'") && immediate.endsWith("'"))
		{
			String[] stringArray = immediate.split("'");
		
			if(stringArray[1].length() <= 2) 
			{
				if(Pattern.matches("[a-zA-Z_0-9_]*",stringArray[1]))
				{
					isImmediate = true;
				}
			}
		}
		else
		{
			if(immediate.length() > 1)
			{
				try
				{
					value = Integer.parseInt(String.valueOf(immediate));
				}
				catch(NumberFormatException e){}
			}
			
			if(value >= 0 && value <= 65536)
			{
				isImmediate = true;
			}
		}

		return isImmediate;
	}
	/**
	 * 
	 * Module Name: isValidInstructionSimpleAddress
	 * Description: Returns a Boolean value dependent on if the Input String is a valid SAL560 SimpleAddress.
	 * Input Params: String address Input String to be tested as a valid SAL560 SimpleAddress
	 * Output Params: Boolean Value dependent on if the Input String is a valid SAL560 SimpleAddress.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Dan
	 * Date of Installation: 10-25-2010
	 * Modifications: 11-6-2010 (Robert Schmidt) Fixed Minor Errors
	 * @param address Input string that is to be tested for being a valid SimpleAddress.
	 * @return Boolean Value dependent on if the Input String is a valid SAL560 SimpleAddress.
	 */
	static boolean isValidInstructionSimpleAddress (String address) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//has to be greater than starting LC, less than 65536
		Boolean validAddress = false;
		int Value = -1;

		if(Parser.SymbTable.isInTable(address))
		{
			ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(address);
			try
			{
				Value = Integer.parseInt(String.valueOf(values.get(0)));
			}
			catch(NumberFormatException e){}
		}
		else
		{
			try
			{
				Value = Integer.parseInt(address);
			}
			catch(NumberFormatException e){}
		}

		if(Value >= 0 && Value <= 65536)
		{
			validAddress = true;
		}
		
		return validAddress;
	}
	
	/**
	 * 
	 * Module Name: isValidInstructionComplexAddress
	 * Description: Returns a Boolean value dependent upon is the input String is a valid SAL560 Address.
	 * Input Params: String address
	 * Output Params: Boolean value dependent upon if the output value is a valid SAL560 Address.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Oscar
	 * Date of Installation: 20-25-2010
	 * Modifications: 11-6-2010 (Robert Schmidt) Fixed a few errors.
	 * @param address: Input String to be validated a SAL560 address.
	 * @return Boolean value dependent upon if the output value is a valid SAL560 Address.
	 */
	static boolean isValidInstructionComplexAddress (String address) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//has to be greater than starting LC, less than 65536
		Boolean validAddress = false;
		Boolean validLabel = false;
		Boolean validRegister = false;
		
		if(address.contains("(") && address.contains(")"))
		{
			StringTokenizer addressTokenizer = new StringTokenizer(address,"()",false);

			if(addressTokenizer.countTokens() == 2)
			{
				String label = addressTokenizer.nextToken();
				String register = addressTokenizer.nextToken();
				if(Parser.SymbTable.isInTable(label))
				{
					ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(label);
					try
					{
						Integer.parseInt(String.valueOf(values.get(0)));
						validLabel = true;
					}
					catch(NumberFormatException e){}
				}
				else
				{
					validLabel = isValidInstructionSimpleAddress(label);
				}
				
				validRegister = isValidInstructionRegister(register);
			}
			else if(addressTokenizer.countTokens() == 1)
			{
				String register = addressTokenizer.nextToken();
				validLabel = true;
				validRegister = isValidInstructionRegister(register);
			}
		}
		else
		{
			validLabel = isValidInstructionSimpleAddress(address);
			validRegister = true;
		}

		if(validLabel == true && validRegister == true)
		{
			validAddress = true;
		}
		
		return validAddress;
	}
	/**
	 * 
	 * Module Name: isValidInstructionBit
	 * Description: This function returns a Boolean value dependent upon if the input String is a valid SAL560 Bit.
	 * Input Params: String bit Input string to be validated as a SAL560 Bit.
	 * Output Params: Boolean value that is dependent upon if the Bit is valid or not.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Oscar
	 * Date of Installation: 10-25-2010
	 * Modifications: None
	 * @param bit Input String to be tested as a valid Bit or not.
	 * @return Boolean value that is dependent upon if the Bit is valid or not.
	 */
	static boolean isValidInstructionBit (String bit) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//operand should have 3 fields, each containing 1 or 0
		
		return bit.equals("1") || bit.equals("0");
	}
	/**
	 * 
	 * Module Name: isValidInstructionBits
	 * Description: This function returns a Boolean value dependent on if the input string is deemed a valid SAL560 Bits.
	 * Input Params: String bits a string of Bits
	 * Output Params: Boolean based on weather the input string is deemed valid or not.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Oscar
	 * Date of Installation: 10-25-2010
	 * Modifications: 
	 * @param bits Input string to be validated as a SAL 560 Bits or not.
	 * @return Boolean that returns if the input string is valid or not.
	 */
	static boolean isValidInstructionBits (String bits) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//# of bits to be shifted in a shift instruction (makes sense to limit this to 0-15???)
	
		//Integer.valueOf doesn't work if we have a '+' character in the number however I believe we are allowed
		//to use the '+' character for positive numbers despite that its unnecessary, therefore I check for its
		//occurrence and disregard it if its present.
		boolean answer = true;
		String absoluteValue = "";
		//check first character to see if - or digit
		if (bits.charAt(0)=='-'||Character.isDigit(bits.charAt(0)))
		{
			if (Character.isDigit(bits.charAt(0))){
				
				absoluteValue = String.valueOf(bits.charAt(0));
			}
				//check all characters that are supposed to be digits to make sure that they in fact are.
				for(int counter = 1; counter<bits.length();counter++)
				{
					if(Character.isDigit(bits.charAt(counter)))
					{
					absoluteValue = absoluteValue + bits.charAt(counter);
					}
					else
					{
						answer = false;
					}
				}
				
			int comparisonValue = Integer.valueOf(absoluteValue);
			
			if (bits.charAt(0)=='-')
			{
				comparisonValue = comparisonValue*(-1);
			}
			//check to see if our number falls within the required range of acceptable values.
			answer = (-32<=comparisonValue && comparisonValue <=31);
		}
		else
		{
			//if first character isn't +,- or digit than the directive number is invalid.
			answer = false;
		}
		
		return answer;
	}
	
	/**
	 * 
	 * Module Name: isValidInstructionNumber
	 * Description: 
	 * Input Params: 
	 * Output Params: 
	 * Error Conditions Tested: 
	 * Error Messages Generated: 
	 * Original Author: 
	 * Date of Installation: 
	 * Modifications: 
	 * @param number
	 * @return
	 */
	static boolean isValidInstructionNumber (String number) 
	{
		//This is a superfluous function since it will always return true, unless we decide to set a limit
		//for acceptable strings and/or numbers.
		
		//need to flesh this out, but each method to check operands should just return a boolean 
		//words of memory to be reserved for IO instructions...should we limit this? 
		
		return true;
	}
	
	
	
	/**
	 * 
	 * Module Name: isValidDirectiveNumber
	 * Description: Returns a Boolean based on if the String passed is deemed to be a valid SAL560 Number.
	 * Input Params: String number
	 * Output Params: Boolean value dependent upon if the input string is valid or not.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Oscar
	 * Date of Installation: 10-25-2010
	 * Modifications:
	 * @param number Input string that is being tested to see if it is a valid number.
	 * @return Boolean value that is true or false dependent if the input string is deemed valid or not.
	 */
	static boolean isValidDirectiveNumber (String number) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		// -231<=x<=230
		boolean answer = true;
		String absoluteValue = "";
		//check first character to see if +,- or digit
		if (number.charAt(0)=='+'||number.charAt(0)=='-'||Character.isDigit(number.charAt(0)))
		{
			if (Character.isDigit(number.charAt(0))){
				
				absoluteValue = String.valueOf(number.charAt(0));
			}

			
				//check all characters that are supposed to be digits to make sure that they in fact are.
				for(int counter = 1; counter<number.length();counter++)
				{
					if(Character.isDigit(number.charAt(counter)))
					{
					absoluteValue = absoluteValue + number.charAt(counter);
					}
					else
					{
						answer = false;
					}
				}
			
				
			int comparisonValue = 9999999;
			try
			{
				comparisonValue = Integer.valueOf(absoluteValue);
				if(-32768<=comparisonValue && comparisonValue <=32767)
				{
					answer = true;
				}
			}
			catch (Exception e)
			{
				
				answer = false;
			}
			
		}
		else
		{
			//if first character isn't +,- or digit than the directive number is invalid.
			answer = false;
		}
	
		return answer;
	}
	/**
	 * 
	 * Module Name: isValidDirectiveBinary
	 * Description: Determines if the string passed to the function is a valid binary number in the sal560
	 * Input Params: String binary
	 * Output Params: Boolean value dependent upon if the string passed is deemed valid or not.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Rakaan
	 * Date of Installation: 10-26-2010
	 * Modifications: Fixed Pattern Match 11-6-2010 (Robert Schmidt)
	 * @param binary This is a string value that is a potential binary number that is being validated.
	 * @return A Boolean value that returns true or false dependent upon the instruction being true or false.
	 */
	static boolean isValidDirectiveBinary (String binary) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//32 binary digits
		boolean answer = false;
		if(binary.length() <= 32)
		{
			answer = Pattern.matches("[01]*", binary);
		}
		
		return answer;
	}
	/**
	 * 
	 * Module Name: isValidDirectiveHex
	 * Description: Returns a boolean value if the String value is a valid Hex string in the sal560 assembly language.
	 * Input Params: String hex
	 * Output Params: Boolean value depending on if the hex string is considered valid or not.
	 * Error Conditions Tested:
	 * Error Messages Generated
	 * Original Author: Rakaan
	 * Date of Installation: 20-25-2010
	 * Modifications: None
	 * @param hex A string of hex to be determined valid or invalid.
	 * @return A boolean value that returns if the hex is valid or invalid.
	 */
	static boolean isValidDirectiveHex (String hex) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//max of 8 hex digits, fill in with signed value in front (either 0 or F)
		boolean result = true;
		for(int i =0; i <hex.length();i++){
			if (!(Character.isDigit(hex.charAt(i))|| hex.charAt(i)== 'A' || hex.charAt(i)== 'B'|| hex.charAt(i)== 'C' || hex.charAt(i)== 'D' || hex.charAt(i)== 'E' || hex.charAt(i)== 'F')){
				result = false;
			}
			
		}
		return (hex.length() <= 8 && result);
	}
	
	/**
	 * 
	 * Module Name: isValidDirectiveCharacterString
	 * Description: Returns a boolean value if the stringValue passed to it is a valid CharacterString in the sal560 assembler.
	 * Input Params: String stringValue
	 * Output Params: Boolean value dependent upon if the input StringValue is valid or not.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None
	 * Original Author: Rakaan
	 * Date of Installation: 20-26-2010
	 * Modifications: Fixed the Match Pattern 11-6-2010 Robert Schmidt
	 * @param string A possibly valid STR.DATA value in the SAL560.
	 * @return True iff the length of "string" is <= 16 and contains only the characters A-Z,a-z,_,0-9.
	 */
	static boolean isValidDirectiveCharacterString (String stringValue) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//length is limited to 16 characters (this was our choice) and contains only A-Z,a-z,_,0-9
		Boolean properString = false;
		//System.out.println("QWER : " + stringValue.charAt(0) + " : " + stringValue.charAt(stringValue.length()-1));
		
		if(stringValue.charAt(0) == '\'' && stringValue.charAt(stringValue.length()-1) == '\'')
		{
			String[] stringArray = Operand.splitByCharacter(stringValue, '\'');
			if(stringArray.length == 3)
			{
				String tempString = stringArray[1].replaceAll(" ", "");
				if((stringArray[1].length() <= 16) && ((Pattern.matches("[a-zA-Z_0-9_]*",tempString) || tempString.equals(""))))
				{
					properString = true;
				}
			}
		}

		return properString;
	}
	
	/**
	 * 
	 * Module Name: isValidDirectiveString
	 * Description: Determines if the String passed to it is a valid string in the sal560 assembly language.
	 * Input Params: String stringValue
	 * Output Params: Boolean dependent upon what if the String value if valid or not.S
	 * Error Conditions Tested:  None
	 * Error Messages Generated: None
	 * Original Author: Rakaan
	 * Date of Installation: 10-27-2010
	 * Modifications: Fixed the match pattern 11-6-2010 (Robert Schmidt)
	 * @param stringValue Sal560 String value for a directive.
	 * @return returns a boolean based on if the String is considered valid or not.
	 */
	static boolean isValidDirectiveString (String stringValue) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//length is limited to 16 characters (this was our choice) and contains only A-Z,a-z,_,0-9


		return (stringValue.length() <= 16) && (Pattern.matches("[a-zA-Z_0-9_]*",stringValue));
	}
	
	/**
	 * 
	 * Module Name: isValidDirectiveLabel
	 * Description: Determines if the string passed is a valid Label.
	 * Input Params: String label
	 * Output Params: Boolean dependent upon weather or not the label is valid.
	 * Error Conditions Tested: None
	 * Error Messages Generated: None 
	 * Original Author: Kermit
	 * Date of Installation: 10-27-2010
	 * Modifications: None
	 * @param label The possibly valid label for the ENT and ADR.DATAa directives.
	 * @return True if the String "label" is a previously declared label that already exists in the SymbolTable.
	 */
	static boolean isValidDirectiveLabel (String label) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//Must be an internal or external label already defined in the symbol table
		return Parser.SymbTable.isInTable(label);
	}
	
	/**
	 * 
	 * Module Name: isValidDirectiveBoolean
	 * Description: Determines if the string is a valid boolean value or not.
	 * Input Params: String bool
	 * Output Params: boolean dependent on if the input string is a valid boolean value or not.
	 * Error Conditions Tested:
	 * Error Messages Generated:  None
	 * Original Author: Kermit
	 * Date of Installation: 10-27-2010
	 * Modifications: None
	 * @param bool The possiblly valid Boolean flag for the debug function.
	 * @return True if the String "bool" is a valid Boolean flag.
	 */
	static boolean isValidDirectiveBoolean (String bool) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//Boolean debug flag, length 1, either 1 or 0
		if(bool.length() ==1)
		{
			char flag = bool.charAt(0);
			return (flag == '1' || flag == '0');
		}
		return false;
	}
	/**
	 * 
	 * Module Name: isValidDirectiveExp
	 * Description: Determines if exp string passed to it is valid or not.
	 * Input Params: String exp
	 * Output Params: Boolean depending on if the string is a valid expression or not.
	 * Error Conditions Tested: 
	 * Error Messages Generated: None
	 * Original Author: Kermit
	 * Date of Installation: 10-28-2010
	 * Modifications: Fixed the SymbolTable index off by 1 error. 11-6-2010 (Robert Schmidt)
	 * @param exp The String value of the potentially valid expression.
	 * @return True if the String is a valid SAL560 expression (max of 3 operands separated my '+' or '-')
	 */
	static boolean isValidDirectiveExp (String exp) 
	{		
		boolean result = false;
		
		String[] plusSplit = splitByCharacter(exp,'+');
		for(String plusString : plusSplit)
		{
			String[] minusSplit = splitByCharacter(plusString,'-');
			for(String minusString : minusSplit)
			{
				boolean isCorrect = false;
				if(minusString.equals("*"))
				{
					isCorrect = true;
				}
				try
				{
					int value = Integer.parseInt(minusString);
					if(value < 65536 || value > -65536)
					{
						isCorrect = true;
					}
				}
				catch(NumberFormatException e){}
				if(Parser.SymbTable.isInTable(minusString))
				{
					isCorrect = true;
				}
				if(isCorrect == false)
				{
					result = false;
				}
			}
		}
		return result;
	}
	public static String[] splitByCharacter(String tempString, char delim)
	{
		int count = 0;
		for(int x=0;x<tempString.length();x++)
		{
			if(tempString.charAt(x) == delim)
			{
				count++;
			}
		}
		String[] returnString = new String[count+1];
		int[] countLocations = new int[count];
		count = 0;
		for(int x=0;x<tempString.length();x++)
		{
			if(tempString.charAt(x) == delim)
			{
				countLocations[count] = x;
				count++;
			}
		}
		
		int startloc=0;
		
		for(int x=0;x<countLocations.length;x++)
		{
			returnString[x] = tempString.substring(startloc, countLocations[x]);
			startloc = countLocations[x]+1;
		}
		
		returnString[returnString.length-1] = tempString.substring(startloc, tempString.length());
		
		return returnString;
	}
}
