/**
 * 
 */

/**
 * Author: Oscar Flores.
 * Description: this Simulator takes two 32-bit binary Strings and returns an Integer string as the result of each
 * arthimatic operation. 
 *
 */
public class ArithmeticSimulator {
	
	public ArithmeticSimulator() {
		
	}
	public int ADD(String a, String b){
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		int cValue = aValue + bValue;
		return cValue;
		
	}
	public int SUB(String a, String b){
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		int cValue = aValue - bValue;
		return cValue;
		
	}
	public int MUL(String a, String b){
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		int cValue = aValue * bValue;
		return cValue;
		
	}
	public int DIV(String a, String b){
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		int cValue = aValue / bValue;
		return cValue;
		
	}
	public int ADDU(String a, String b){
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		int cValue = aValue + bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		return cValue;
		
	}
	public int SUBU(String a, String b){
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		int cValue = aValue - bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		return cValue;
		
	}
	public int MULU(String a, String b){
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		int cValue = aValue * bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		return cValue;
		
	}
	public int DIVU(String a, String b){
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		int cValue = aValue / bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		return cValue;
		
	}
	
	/**
	 * 
	 * Module Name: GetIntegerFromTwosComplementSigned.
	 * Description: Converts String into Integer Value (Signed) given the String of bits is in two's Complement
	 * Input Params: String a: A String of 32 bits, in two's complemet
	 * Output Params: Decimal representation of String a.
	 * Error Conditions Tested: NONE.
	 * Error Messages Generated: NONE.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/20/2010.
	 * Modifications: NONE.
	 */
	private int GetIntegerFromTwosComplementSigned(String a){
		char [] bits = new char[a.length()];
		int firstZero = 0;
		boolean negative = false;
		if(a.charAt(0) == '1'){
			negative = true;
			
		}
		for(int i = a.length()-1; i >0; i--)
		{
			if(a.charAt(i) == '1'){
				// invert the rest of the bits
				bits[i] = a.charAt(i);
				i--;
				while(i >=0){
					if(a.charAt(i) == '0'){
						bits[i] = '1';
					}
					else{
						bits[i] = '0';
					}
					i--;
				}
				break;
			}
			else{
				// copy bits
				bits[i] = a.charAt(i);
			}
		}
		
		 //  at this point this is a two's complement String
		
		
		String bin = "";
		for(int i = 0; i < a.length(); i++){
			bin += bits[i];
		}
		System.out.println(bin);
		int value = Integer.parseInt(bin,2);
		if(negative){
			value = value*-1;
		}
		
		
		return value;
		
		
	}
	/**
	 * 
	 * Module Name: GetIntegerFromTwosComplementSigned.
	 * Description: Converts String into Integer Value (Unsigned) given the String of bits is in two's Complement
	 * Input Params: String a: A String of 32 bits, in two's complemet
	 * Output Params: Decimal representation of String a.
	 * Error Conditions Tested: NONE.
	 * Error Messages Generated: NONE.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/20/2010.
	 * Modifications: NONE.
	 */
	private int GetIntegerFromTwosComplementUnsigned(String a){
		char [] bits = new char[a.length()];
		int firstZero = 0;
		for(int i = a.length()-1; i >0; i--)
		{
			if(a.charAt(i) == '1'){
				// invert the rest of the bits
				bits[i] = a.charAt(i);
				i--;
				while(i >=0){
					if(a.charAt(i) == '0'){
						bits[i] = '1';
					}
					else{
						bits[i] = '0';
					}
					i--;
				}
				break;
			}
			else{
				// copy bits
				bits[i] = a.charAt(i);
			}
		}
		
		 //  at this point this is a two's complement String
		
		
		String bin = "";
		for(int i = 0; i < a.length(); i++){
			bin += bits[i];
		}
		System.out.println(bin);
		int value = Integer.parseInt(bin,2);
		return value;
	}
}
