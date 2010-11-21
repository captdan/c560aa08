/**
 * 
 */

/**
 * Author: Oscar Flores.
 * Description: this Simulator takes two binary Strings and returns an Integer string as the result of each
 * arthimatic operation. 
 *
 */
public class ALU {
	public static boolean overflowFromLastOperation = false;
	public ALU() {
		
	}
	public int ADD(String a, String b){
		int cValue = 0;
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		try{
		cValue = aValue + bValue;
		}
		catch(Exception overflow){
			overflowFromLastOperation = true;
		}
		return cValue;
		
	}
	public int SUB(String a, String b){
		int cValue = 0;
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		try{
		cValue = aValue - bValue;
		}
		catch(Exception overflow){
			overflowFromLastOperation = true;
		}
		return cValue;
	}
	public int MUL(String a, String b){
		int cValue = 0;
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		try{
		cValue = aValue * bValue;
		}
		catch(Exception overflow){
			overflowFromLastOperation = true;
		}
		return cValue;
		
	}
	public int DIV(String a, String b){
		int cValue = 0;
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		try{
		cValue = aValue/bValue;
		}
		catch(Exception overflow){
			overflowFromLastOperation = true;
		}
		return cValue;
		
	}
	public int ADDU(String a, String b){
		int aValue = GetIntegerFromTwosComplementUnsigned(a);
		int bValue = GetIntegerFromTwosComplementUnsigned(b);
		int cValue = aValue + bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		overflowFromLastOperation = false;
		return cValue;
		
	}
	public int SUBU(String a, String b){
		int aValue = GetIntegerFromTwosComplementUnsigned(a);
		int bValue = GetIntegerFromTwosComplementUnsigned(b);
		int cValue = aValue - bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		overflowFromLastOperation = false;
		return cValue;
		
	}
	public int MULU(String a, String b){
		int aValue = GetIntegerFromTwosComplementUnsigned(a);
		int bValue = GetIntegerFromTwosComplementUnsigned(b);
		int cValue = aValue * bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		overflowFromLastOperation = false;
		return cValue;
		
	}
	public int DIVU(String a, String b){
		int aValue = GetIntegerFromTwosComplementUnsigned(a);
		int bValue = GetIntegerFromTwosComplementUnsigned(b);
		int cValue = aValue / bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		overflowFromLastOperation = false;
		return cValue;
		
	}
	public int PWR(String a, String b){
		int c = 0;
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		try{
		c = (int) Math.pow(aValue, bValue);
		}
		catch(Exception overflow){
			overflowFromLastOperation = true;
		}
		return c;
	}
	public String AND(String a, String b){
		String result = "";
		if(a.length() == b.length()){
			for(int i = 0; i <a.length(); i++){
				if((a.charAt(i)== b.charAt(i)) && (a.charAt(i) == '1')){
					result = result + '1';
				}
				else
				{
					result = result + '0';
				}
			}
			
		}
		else{
			// TODO generate error
		}
		return result;
	}
	public String OR(String a, String b){
		String result = "";
		if(a.length() == b.length()){
			for(int i = 0; i <a.length(); i++){
				if((a.charAt(i)!= b.charAt(i) || a.charAt(i)== b.charAt(i)) && (a.charAt(i) == '1' || b.charAt(i) == '1')){
					result = result + '1';
				}
				else
				{
					result = result + '0';
				}
			}
			
		}
		else{
			// TODO generate error
		}
		return result;
	}
	public String ORI(String reg, String imm){
		String result = padZeros(reg, imm);
		return OR(reg,result);
	}
	public String XOR(String a, String b){
		String result = "";
		if(a.length() == b.length()){
			for(int i = 0; i <a.length(); i++){
				if((a.charAt(i)!= b.charAt(i))){
					result = result + '1';
				}
				else
				{
					result = result + '0';
				}
			}
			
		}
		else{
			// TODO generate error
		}
		return result;
	}	
	public String NOR(String a, String b){
		String result = OR(a,b);
		return invertBits(result);
	}
	public String XORI(String reg, String imm){
		String result = padZeros(reg, imm);
		return XOR(reg,result);
	}
	public String NORI(String reg, String imm){
		String result = padZeros(reg, imm);
		return NOR(reg,result);
	}
	public String ANDI(String reg, String imm){
		String result = padZeros(reg, imm);
		return AND(reg,result);
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
		//System.out.println(bin);
		int value = Integer.parseInt(bin,2);
		
		if(negative){
			value = value*-1;
		}
		//System.out.println(value);
		
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
		//System.out.println(bin);
		int value = Integer.parseInt(bin,2);
		return value;
	}
	public static String invertBits(String a){
		String result = "";
		for(int i = 0; i < a.length();i++){
			if(a.charAt(i)=='1'){
				result += '0';
			}
			else{
				result +='1';
			}
		}
		return result;
	}
	public static String padZeros(String reg,String imm){
		String result = "";
		int zeroes = reg.length() - imm.length();
		while (zeroes >0){
			result += '0';
			zeroes--;
		}
		result += imm;
		return result;
	}
}
