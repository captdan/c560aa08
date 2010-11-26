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

	public ALU() {
		
	}
	/**
	 * 
	 * Module Name: ADD.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the addition of both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 32 bit string in two's complement.
	 * Output Params: an integer containing the addition of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int ADD(String a, String b){
		int cValue = 0;
		
		int aValue = GetIntegerFromTwosComplementSigned(a);
		
		int bValue = GetIntegerFromTwosComplementSigned(b);
		
		if(a.charAt(0) == '0'){
			aValue = Integer.parseInt(a, 2);
		}
		if(b.charAt(0)== '0'){
			bValue = Integer.parseInt(b, 2);
		}
		try{
		cValue = aValue + bValue;
		}
		catch(Exception overflow){
			System.err.println("Over Flow");
			System.exit(1);
		}
		System.out.println(aValue + " + " + bValue +" = " + cValue);
		return cValue;
		
	}
	/**
	 * 
	 * Module Name: SUB.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the difference of both strings (a-b).
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 32 bit string in two's complement.
	 * Output Params: an integer containing the difference of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int SUB(String a, String b){
		int cValue = 0;
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		if(a.charAt(0) == '0'){
			aValue = Integer.parseInt(a, 2);
		}
		if(b.charAt(0)== '0'){
			bValue = Integer.parseInt(b, 2);
		}
		try{
		cValue = aValue - bValue;
		
		}
		catch(Exception overflow){
			System.err.println("Over Flow");
			System.exit(1);
		}
		System.out.println(aValue + " - " + bValue +" = " + cValue);
		return cValue;
	}
	/**
	 * 
	 * Module Name: MUL.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the product of both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 32 bit string in two's complement.
	 * Output Params: an integer containing the product of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int MUL(String a, String b){
		
		//System.out.println("immetiate: " + b);
		int cValue = 0;
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		if(a.charAt(0) == '0'){
			aValue = Integer.parseInt(a, 2);
		}
		if(b.charAt(0)== '0'){
			bValue = Integer.parseInt(b, 2);
		}
		try{
		cValue = aValue * bValue;
		}
		catch(Exception overflow){
			System.err.println("Over Flow");
			System.exit(1);
		}
		System.out.println(aValue + " * " + bValue +" = " + cValue);
		return cValue;
		
	}
	/**
	 * 
	 * Module Name: DIV.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the division of both strings (a/b).
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 32 bit string in two's complement.
	 * Output Params: an integer containing the division of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int DIV(String a, String b){
		int cValue = 0;
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		if(a.charAt(0) == '0'){
			aValue = Integer.parseInt(a, 2);
		}
		if(b.charAt(0)== '0'){
			bValue = Integer.parseInt(b, 2);
		}
		try{
		cValue = aValue/bValue;
		}
		catch(Exception overflow){
			System.out.println("Zero Division");
			//System.exit(1);
		}
		System.out.println(aValue + " / " + bValue +" = " + cValue);
		return cValue;
		
	}
	/**
	 * 
	 * Module Name: ADDU.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the addition of the absolute value both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 32 bit string in two's complement.
	 * Output Params: an integer containing the addition of the absolute value of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int ADDU(String a, String b){
		int aValue = GetIntegerFromTwosComplementUnsigned(a);
		int bValue = GetIntegerFromTwosComplementUnsigned(b);
		if(a.charAt(0) == '0'){
			aValue = Integer.parseInt(a, 2);
		}
		if(b.charAt(0)== '0'){
			bValue = Integer.parseInt(b, 2);
		}
		int cValue = aValue + bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		
		System.out.println(aValue + " + " + bValue +" = " + cValue);
		return cValue;
		
	}
	/**
	 * 
	 * Module Name: SUBU.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the difference of the absolute value both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 32 bit string in two's complement.
	 * Output Params: an integer containing the difference of the absolute value of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int SUBU(String a, String b){
		int aValue = GetIntegerFromTwosComplementUnsigned(a);
		int bValue = GetIntegerFromTwosComplementUnsigned(b);
		if(a.charAt(0) == '0'){
			aValue = Integer.parseInt(a, 2);
		}
		if(b.charAt(0)== '0'){
			bValue = Integer.parseInt(b, 2);
		}
		int cValue = aValue - bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		
		System.out.println(aValue + " - " + bValue +" = " + cValue);
		return cValue;
		
	}
	/**
	 * 
	 * Module Name: MULU.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the product of the absolute value both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 32 bit string in two's complement.
	 * Output Params: an integer containing the product of the absolute value of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int MULU(String a, String b){
		int aValue = GetIntegerFromTwosComplementUnsigned(a);
		int bValue = GetIntegerFromTwosComplementUnsigned(b);
		if(a.charAt(0) == '0'){
			aValue = Integer.parseInt(a, 2);
		}
		if(b.charAt(0)== '0'){
			bValue = Integer.parseInt(b, 2);
		}
		int cValue = aValue * bValue;
		if(cValue < 0){
			cValue = cValue*-1;
		}
		
		System.out.println(aValue + " * " + bValue +" = " + cValue);
		return cValue;
		
	}
	/**
	 * 
	 * Module Name: DIVU.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the division of the absolute value both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 32 bit string in two's complement.
	 * Output Params: an integer containing the division of the absolute value of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int DIVU(String a, String b){
		int cValue = 0;
		int aValue = GetIntegerFromTwosComplementUnsigned(a);
		int bValue = GetIntegerFromTwosComplementUnsigned(b);
		if(a.charAt(0) == '0'){
			aValue = Integer.parseInt(a, 2);
		}
		if(b.charAt(0)== '0'){
			bValue = Integer.parseInt(b, 2);
		}
		try{
		cValue = aValue / bValue;
		}
		catch(Exception zerodivision){
			System.err.println("Division by Zero occurred");
			System.exit(1);
		}
		
		if(cValue < 0){
			cValue = cValue*-1;
		}
		
		System.out.println(aValue + " / " + bValue +" = " + cValue);
		return cValue;
		
	}
	/**
	 * 
	 * Module Name: PWR.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the value of a raised to the power b.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 32 bit string in two's complement.
	 * Output Params: returns an integer with the value of a raised to the power b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int PWR(String a, String b){
		int c = 0;
		int aValue = GetIntegerFromTwosComplementSigned(a);
		int bValue = GetIntegerFromTwosComplementSigned(b);
		if(a.charAt(0) == '0'){
			aValue = Integer.parseInt(a, 2);
		}
		if(b.charAt(0)== '0'){
			bValue = Integer.parseInt(b, 2);
		}
		try{
		c = (int) Math.pow(aValue, bValue);
		}
		catch(Exception overflow){
			
		}
		System.out.println(aValue + " ^ " + bValue +" = " + c);
		return c;
	}
	/**
	 * 
	 * Module Name: AND.
	 * Description: takes two binary strings of the same length and performs a logic AND on their bits.
	 * Input Params: two binary Strings.
	 * Output Params: resulting string from performing logic AND on strings a and b.
	 * Error Conditions Tested: Strings have the same length.
	 * Error Messages Generated: "unable to AND strings with different lengths";
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
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
	/**
	 * 
	 * Module Name: OR.
	 * Description: takes two binary strings of the same length and performs a logic OR on their bits.
	 * Input Params: two binary Strings.
	 * Output Params: resulting string from performing logic OR on strings a and b.
	 * Error Conditions Tested: Strings have the same length.
	 * Error Messages Generated: "unable to OR strings with different lengths";
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
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
	/**
	 * 
	 * Module Name: ORI.
	 * Description: takes two binary strings of the same length and performs a logic ORI on their bits.
	 * Input Params: two binary Strings.
	 * Output Params: resulting string from performing logic ORI on strings a and b.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String ORI(String reg, String imm){
		
		String temp = reg.substring(16);
		String result = OR(reg, temp);
		result += reg.substring(0, 16);
		
		return result;
	}
	/**
	 * 
	 * Module Name: XOR.
	 * Description: takes two binary strings of the same length and performs a logic XOR on their bits.
	 * Input Params: two binary Strings.
	 * Output Params: resulting string from performing logic XOR on strings a and b.
	 * Error Conditions Tested: Strings have the same length.
	 * Error Messages Generated: "unable to XOR strings with different lengths";
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
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
	/**
	 * 
	 * Module Name: NOR.
	 * Description: takes two binary strings of the same length and performs a logic NOR on their bits.
	 * Input Params: two binary Strings.
	 * Output Params: resulting string from performing logic NOR on strings a and b.
	 * Error Conditions Tested: Strings have the same length.
	 * Error Messages Generated: "unable to NOR strings with different lengths";
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String NOR(String a, String b){
		String result = OR(a,b);
		return invertBits(result);
	}
	/**
	 * 
	 * Module Name: XORI.
	 * Description: takes two binary strings of the same length and performs a logic XORI on their bits.
	 * Input Params: two binary Strings.
	 * Output Params: resulting string from performing logic XORI on strings a and b.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String XORI(String reg, String imm){
		String temp = reg.substring(16);
		String result = XOR(reg, temp);
		result += reg.substring(0, 16);
		return result;
	}
	/**
	 * 
	 * Module Name: NORI.
	 * Description: takes two binary strings of the same length and performs a logic NORI on their bits.
	 * Input Params: two binary Strings.
	 * Output Params: resulting string from performing logic NORI on strings a and b.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String NORI(String reg, String imm){
		String temp = reg.substring(16);
		String result = NOR(reg, temp);
		result += reg.substring(0, 16);
		return result;
	}
	/**
	 * 
	 * Module Name: ANDI.
	 * Description: takes two binary strings of the same length and performs a logic ANDI on their bits.
	 * Input Params: two binary Strings.
	 * Output Params: resulting string from performing logic ANDI on strings a and b.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String ANDI(String reg, String imm){
		String temp = reg.substring(16);
		String result = AND(reg, temp);
		result += reg.substring(0, 16);
		return result;
	}
	/**
	 * 
	 * Module Name: ADDI.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the addition of both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 16 bit string in two's complement.
	 * Output Params: an integer containing the addition of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int ADDI(String a, String b){
		String result = extendBits(a, b);
		//System.out.println(result + " x");
		//System.out.println(a + " a");
		int r = (ADD(a,result));
		return r;
	}
	/**
	 * 
	 * Module Name: SUBI.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the difference of both strings (a-b).
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 16-bit string in two's complement.
	 * Output Params: an integer containing the difference of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int SUBI(String a, String b){
		String result = extendBits(a, b);
		int r = (SUB(a,result));
		return r;
	}
	/**
	 * 
	 * Module Name: MULI.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the product of both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 16-bit string in two's complement.
	 * Output Params: an integer containing the product of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int MULI(String a, String b){
	//	System.out.println("-----------------------------------------------------------");
		//System.out.println(a);
		//System.out.println(b);
	//	System.out.println("-----------------------------------------------------------");
		String result = extendBits(a, b);
		//System.out.print("MULI ");
		int r = (MUL(a,result));
		return r;
	}
	/**
	 * 
	 * Module Name: DIVI.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the division of both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 16-bit string in two's complement.
	 * Output Params: an integer containing the division of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int DIVI(String a, String b){
		String result = extendBits(a, b);
		int r = (DIV(a,result));
		return r;
	}
	/**
	 * 
	 * Module Name: ADDIU.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the addition of the absolute value both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 16-bit string in two's complement.
	 * Output Params: an integer containing the addition of the absolute value of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int ADDIU(String a, String b){
		String result = extendBits(a, b);
		int r = (ADDU(a,result));
		return r;
	}
	/**
	 * 
	 * Module Name: SUBIU.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the difference of the absolute value both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 16-bit string in two's complement.
	 * Output Params: an integer containing the difference of the absolute value of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int SUBIU(String a, String b){
		String result = extendBits(a, b);
		int r = (SUBU(a,result));
		return r;
	}
	/**
	 * 
	 * Module Name: MULIU.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the product of the absolute value both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 16-bit string in two's complement.
	 * Output Params: an integer containing the product of the absolute value of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int MULIU(String a, String b){
		String result = extendBits(a, b);
		int r = (MULU(a,result));
		return r;
	}
	/**
	 * 
	 * Module Name: DIVIU.
	 * Description:	Takes in two 32 bits binary strings in two's complement and returns an integer with the division of the absolute value both strings.
	 * Input Params: String a: a 32-bit string in two's complement.
	 * 				 String b: a 16-bit string in two's complement.
	 * Output Params: an integer containing the division of the absolute value of strings a and b.
	 * Error Conditions Tested:  None.	
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public int DIVIU(String a, String b){
		String result = extendBits(a, b);
		int r = (DIVU(a,result));
		return r;
	}
	/**
	 * 
	 * Module Name: SLL.
	 * Description: performs a Shift logical left bit n bits.
	 * Input Params: Reg: binary string to be shifted.
	 * 				 bits: int containing the number of bits to be shifted.
	 * Output Params: a string of bits that has been shifted to the left by n bits.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String SLL(String Reg, int bits){
		
		int bits2 = bits;
		int len = Reg.length();
		
		String result = Reg.substring(bits2,len);
		
		for(int i = bits2; i>0; i--){
			result += '0';
		}
		return result;
	}
	/**
	 * 
	 * Module Name: SRL.
	 * Description: performs a Shift logical right bit n bits.
	 * Input Params: Reg: binary string to be shifted.
	 * 				 bits: int containing the number of bits to be shifted.
	 * Output Params: a string of bits that has been shifted to the right by n bits.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String SRL(String Reg, int bits){
	String result = "";
	int bits2 = bits;
	int len = Reg.length();
	for(int i = bits2; i>0; i--){
		result += '0';
	}
	result += Reg.substring(0,len-bits2);
	return result;
	}
	/**
	 * 
	 * Module Name: SRA.
	 * Description: performs a Shift right arithmetic bit n bits.
	 * Input Params: Reg: binary string to be shifted.
	 * 				 bits: int containing the number of bits to be shifted.
	 * Output Params: a string of bits that has been shifted to the right by n bits.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String SRA(String Reg, int bits){
		int bits2 = bits;
		
		int len = Reg.length();

		String result = Reg.substring(0,len-bits2);
		result = extendBits(Reg, result);
		
		return result;
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
	public int GetIntegerFromTwosComplementSigned(String a){
		if(a.equals("10000000000000000000000000000000")){
			return Integer.MAX_VALUE;
		}
		//System.out.println("A: " + a);
	
		int value = 0;
		if(a.charAt(0) == '1'){
			
			value = GetIntegerFromTwosComplementUnsigned(a);
			value= value*-1;
		}

		else{
		value = Integer.parseInt(a,2);
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
	public int GetIntegerFromTwosComplementUnsigned(String a){
		if(a.equals("10000000000000000000000000000000")){
			return Integer.MAX_VALUE;
		}
		//System.out.println("a:  " + a);
		String bin = "";
		char [] bits = new char[a.length()];
		if(a.charAt(0)== '1'){
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
		
		
		
		for(int i = 0; i < a.length(); i++){
			//System.out.print(bits[i]);
			bin += bits[i];
		}
		}
		else{
			bin = a;
		}
		//System.out.println("bin:"+bin);
		int value = Integer.parseInt(bin,2);
		return value;
	}
	/**
	 * 
	 * Module Name: invertBits.
	 * Description: Inverts the bits of a given binary string.
	 * Input Params: a binatry string.
	 * Output Params: a string with inverted bits.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None
	 * Original Author: Oscar Flores
	 * Date of Installation: 11/21/2010
	 * Modifications: None.
	 */
	public  String invertBits(String a){
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
	/**
	 * 
	 * Module Name: padZeros.
	 * Description: pads with zeroes a binary string smaller than 32-bits.
	 * Input Params: a binary string to be padded.
	 * Output Params: s 32-bit binary string padded with leading zeroes.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public  String padZeros(String reg,String imm){
		String result = "";
		int zeroes = 32- imm.length();
		while (zeroes >0){
			result += '0';
			zeroes--;
		}
		result += imm;
		return result;
	}
	/**
	 * 
	 * Module Name: extendBits.
	 * Description: extend the bits of s binary string of less than 32-bits to a string of 32-bits.
	 * Input Params: binary string to be extended.
	 * Output Params: an 32-bit extended binary string.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public  String extendBits(String reg, String imm){
		String result = "";
		
		int zeroes = 32 - imm.length();
		if(zeroes >0){
		//System.out.println(zeroes);
		while (zeroes > 0){
			if(imm.charAt(0) == '1'){
				while(zeroes >0){
					result+= '1';
					zeroes--;
				}
				result += imm;
				break;
			}
			else{
				result = padZeros(reg,imm);
				break;
			}
		}
		}
		else{
			result = imm;
		}
		return result;
	}
	/**
	 * 
	 * Module Name: hexToBin.
	 * Description: converts a hex number to a binary string.
	 * Input Params: hex: a hex String.
	 * Output Params: a binary string.
	 * Error Conditions Tested: None.
	 * Error Messages Generated:  None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public  String hexToBin(String hex){
		String result = "";
		for(int i = 0; i <hex.length(); i++){
			if(hex.charAt(i) == '0'){
				result += "0000";
			}
			else if (hex.charAt(i) == '1'){
				result+="0001";
			}
			else if (hex.charAt(i) == '2'){
				result+="0010";
			}
			else if (hex.charAt(i) == '3'){
				result+="0011";
			}
			else if (hex.charAt(i) == '4'){
				result+="0100";
			}
			else if (hex.charAt(i) == '5'){
				result+="0101";
			}
			else if (hex.charAt(i) == '6'){
				result+="0110";
			}
			else if (hex.charAt(i) == '7'){
				result+="0111"; 
			}
			else if (hex.charAt(i) == '8'){
				result+="1000";
			}
			else if (hex.charAt(i) == '9'){
				result+="1001";
			}
			else if (hex.charAt(i) == 'A' || hex.charAt(i) == 'a'){
				result+="1010";
			}
			else if (hex.charAt(i) == 'B' || hex.charAt(i) == 'b'){
				result+="1011";
			}
			else if (hex.charAt(i) == 'C' || hex.charAt(i) == 'c'){
				result+="1100";
			}
			else if (hex.charAt(i) == 'D' || hex.charAt(i) == 'd'){
				result+="1101";
			}
			else if (hex.charAt(i) == 'E' || hex.charAt(i) == 'e'){
				result+="1110";
			}
			else if (hex.charAt(i) == 'F' || hex.charAt(i) == 'f'){
				result+="1111";
			}
		}
		return result;
	}
	/**
	 * 
	 * Module Name: binToHex32bits.
	 * Description: converts a 32-bits binary string to a hex string.
	 * Input Params: bin: a binary String.
	 * Output Params: a hex string.
	 * Error Conditions Tested: None.
	 * Error Messages Generated:  None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String binToHex32bits(String bin){
		String a = "";
		for(int i = 0; i <=32; i++){
			a+='0';
		}
		String extended32bitbinary = extendBits(a, bin);
		//System.out.println(extended32bitbinary);
		String hex = "";
		int b = 4;
		for(int i = 0; i+4 <= 32 ; i+=4){
			
			String singlehexinbin = extended32bitbinary.substring(i, i+4);
			//System.out.println();
			//System.out.println(i);
			//System.out.println(b);
			//System.out.println(singlehexinbin);
			if(singlehexinbin.equals("0000")){
				hex+='0';
			}
			else if(singlehexinbin.equals("0001")){
				hex+='1';
			}
			else if(singlehexinbin.equals("0010")){
				hex+='2';
			}
			else if(singlehexinbin.equals("0011")){
				hex+='3';
			}
			else if(singlehexinbin.equals("0100")){
				hex+='4';
			}
			else if(singlehexinbin.equals("0101")){
				hex+='5';
			}
			else if(singlehexinbin.equals("0110")){
				hex+='6';
			}
			else if(singlehexinbin.equals("0111")){
				hex+='7';
			}
			else if(singlehexinbin.equals("1000")){
				hex+='8';
			}
			else if(singlehexinbin.equals("1001")){
				hex+='9';
			}
			else if(singlehexinbin.equals("1010")){
				hex+='A';
			}
			else if(singlehexinbin.equals("1011")){
				hex+='B';
			}
			else if(singlehexinbin.equals("1100")){
				hex+='C';
			}
			else if(singlehexinbin.equals("1101")){
				hex+='D';
			}
			else if(singlehexinbin.equals("1110")){
				hex+='E';
			}
			else if(singlehexinbin.equals("1111")){
				hex+='F';
			}
			b+=4;
		}
		//System.out.println("^^^^");
		//System.out.println(hex);
		return hex;
	}
	/**
	 * Module Name: intToBin.
	 * Description: converts an integer to a 32-bit two's complement binary string.
	 * Input Params: in: an arbitraty integer.
	 * Output Params: a 32-bit binary string.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String intToBin(int in){
		String result = "";
		if(in < 0){
			result = Integer.toBinaryString(in);
			
		}
		else{
			result = "0";
			String reg = "";
			for(int i = 0; i <32;i++){
				reg += '0';
			}
			result += Integer.toBinaryString(in);
			result = extendBits(reg,result);
			
		}
		return result;
	}
	/**
	 * 
	 * Module Name: intToHex.
	 * Description: converts an integer to a 8 digit hex string.
	 * Input Params: in: an arbitraty integer.
	 * Output Params: a 8-digit hex string.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores.
	 * Date of Installation: 11/21/2010.
	 * Modifications: None.
	 */
	public String intToHex(int in){
		String result = "";
		if(in <0){
			result = Integer.toHexString(in);
		}
		else{
			String bin = intToBin(in);
			//System.out.println(bin);
			result = binToHex32bits(bin);
		}
		return result;
	}
} 