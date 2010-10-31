import java.util.ArrayList;
import java.util.StringTokenizer;


public class Operand
{
	public String operand;
	
	public Operand(String operandValue)
	{
		operand = operandValue;
	}
	public Operand() 
	{

	}
	
	/**Instruction Operand Checking:
	 * 	REGISTER, IMMEDIATE, ADDRESS, BIT, BITS, NUMBER, IO
	 * 
	 * @param label
	 * @return
	 */
	static boolean isValidInstructionRegister (String register) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//Valid register 0-7
		return false;
	}
	static boolean isValidInstructionImmediate (String immediate) 
	{
		String unsigned = "";
		boolean isImmediate = false;
		char startsign = immediate.charAt(0);
		if(startsign != '+' || startsign != '-' || Character.isDigit(startsign)){
			// if it contains something else, it must be an error
			Parser.currentErrorArray.add(Parser.returnError(21));
			return false;
		}
		else{
			if(startsign == '+' || startsign == '-'){
				for(int i =1; i <immediate.length();i++){
					unsigned = unsigned + immediate.charAt(i);
				}
			}
			else{
				unsigned = new String (immediate);
			}
			// check that the unsigned immediate has only digits
			
			for(int i =0; i < unsigned.length() ; i++){
				if(!Character.isDigit(unsigned.charAt(i))){
					// if the immediate contains anything other that digits, throw an error
					Parser.currentErrorArray.add(Parser.returnError(21));
					return false;
				}
			}
			// at this point we know the immediate is a valid integer, so we check the bounds
			int valueOfUnsigned = Integer.parseInt(unsigned);
			if(startsign == '-'){
				isImmediate =  (valueOfUnsigned <= 231);
				if(isImmediate == false){
					Parser.currentErrorArray.add(Parser.returnError(5));
				}
			}
			else{
				isImmediate = (valueOfUnsigned<= 230);
				if(isImmediate == false){
					Parser.currentErrorArray.add(Parser.returnError(5));
				}
			}
			
			
		}
		
		//need to flesh this out, but each method to check operands should just return a boolean 
		//can have a single '+' or '-' preceeding it, must fit signed value in 16 bits (-231<=x<=230)
		
		
		return isImmediate;
	}
	static boolean isValidInstructionAddress (String address) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//has to be greater than starting LC, less than 65536
		int addr = Integer.parseInt(address);
		return ((addr > Parser.startingLocation) && (addr < 65536));
	}
	static boolean isValidInstructionBit (String bit) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//operand should have 3 fields, each containing 1 or 0
		boolean result = true;
		StringTokenizer st = new StringTokenizer(bit,", 	",false);
		//it has to have 3 operands
		if(st.countTokens() == 3){
			String currentOperand;
			// check each operand contains only 0s and 1s
			while(st.hasMoreTokens()){
				currentOperand  = st.nextToken();
				for(int i = 0; i < currentOperand.length();i++){
					if(currentOperand.charAt(i) != '0' || currentOperand.charAt(i) != '1'){
						result = false;
						Parser.currentErrorArray.add(Parser.returnError(22));
						break;
					}
				}
			}
		}
		else{
			Parser.currentErrorArray.add(Parser.returnError(0));
		}
		
		return result;
	}
	static boolean isValidInstructionBits (String bits) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//# of bits to be shifted in a shift instruction (makes sense to limit this to 0-15???)
	
		//Integer.valueOf doesn't work if we have a '+' character in the number however I believe we are allowed
		//to use the '+' character for positive numbers despite that its unnecessary, therefore I check for its
		//occurrence and disregard it if its present.
		boolean answer = true;
		String absoluteValue = "";
		//check first character to see if +,- or digit
		if (bits.charAt(0)=='+'||bits.charAt(0)=='-'||Character.isDigit(bits.charAt(0)))
		{
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
	static boolean isValidInstructionNumber (String number) 
	{
		//This is a superfluous function since it will always return true, unless we decide to set a limit
		//for acceptable strings and/or numbers.
		
		//need to flesh this out, but each method to check operands should just return a boolean 
		//words of memory to be reserved for IO instructions...should we limit this? 
		
		return true;
	}
	
	
	/**
	 * Directive Operand checking:
	 * 	NUMBER, BINARY, HEX, STRING, LABEL, LABELREF, BOOLEAN, EXP
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
				
			int comparisonValue = Integer.valueOf(absoluteValue);
			
			if (number.charAt(0)=='-')
			{
				comparisonValue = comparisonValue*(-1);
			}
			//check to see if our number falls within the required range of acceptable values.
			answer = (-231<=comparisonValue && comparisonValue <=230);
		}
		else
		{
			//if first character isn't +,- or digit than the directive number is invalid.
			answer = false;
		}
		
		return answer;
	}
	static boolean isValidDirectiveBinary (String binary) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//32 binary digits
		boolean answer = true;
		
		for(int i=0; i<binary.length();i++){
				if(Character.getNumericValue(binary.charAt(i))!=0||Character.getNumericValue(binary.charAt(i))!=1)
						{
							answer = false;
						}
			}
		
		return (binary.length()<= 32 && answer);
	}
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
	 * @param string A possibly valid STR.DATA value in the SAL560.
	 * @return True iff the length of "string" is <= 16 and contains only the characters A-Z,a-z,_,0-9.
	 */
	static boolean isValidDirectiveString (String string) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//length is limited to 16 characters (this was our choice) and contains only A-Z,a-z,_,0-9
		return ((string.length() <= 16) && (string.matches("\\w")));
	}
	
	/**
	 * @param label The possibly valid label for the ENT and ADR.DATAa directives.
	 * @return True iff the String "label" is a previously declared label that already exists in the SymbolTable.
	 */
	static boolean isValidDirectiveLabel (String label) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//Must be an internal or external label already defined in the symbol table
		return Parser.SymbTable.isInTable(label);
	}
	
	/**
	 * 
	 * @param bool The possiblly valid Boolean flag for the debug function.
	 * @return True iff the String "bool" is a valid Boolean flag.
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
	 * @param exp The String value of the potentially valid expression.
	 * @return True iff the String is a valid SAL560 expression (max of 3 operands separated my '+' or '-')
	 */
	static boolean isValidDirectiveExp (String exp) 
	{

		//valid expression(include handling * notation)
		
		//boolean value to return
		
		boolean result = false;
		
		//Tokenizing machine ensures only valid operators are '+' and '-' so a valid expression will have a max of 3 tokens in the machine
		
		StringTokenizer expressionTokenizer = new StringTokenizer(exp,"+-",false);
		
		//Should only have 3 operands
		
		if(expressionTokenizer.countTokens() <= 3)
		{
			while(expressionTokenizer.hasMoreTokens())
			{
				String operand = expressionTokenizer.nextToken();
				
				//first check to see if it's a valide integer value
				try
				{
					Integer.parseInt(operand);
					result = true;
				}
				catch(NumberFormatException e)
				{
					result = false;
				}
				
				//if it's not an integer, it could still be a label that is a valid integer, so check SymbTable
				if(result == false)
				{
					if(Parser.SymbTable.isInTable(operand))
					{
						ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(operand);
						try
						{
							//Checks the "sub" value of the label to see if the label is a substitute for a valid integer
							Integer.parseInt(String.valueOf(values.get(1)));
							result = true;
						}
						catch(NumberFormatException e)
						{
							result = false;
						}
					
					}
				}
			
				//failing that, it could be a "*" so we check for that
				if(result == false)
				{
					result = ((operand.length() == 1) && (operand.charAt(0) == '*'));
				}
			}
		}
		return result;
	}
}
