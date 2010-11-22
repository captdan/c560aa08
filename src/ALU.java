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
	public static boolean zerodiv = false; 
	public ALU() {
		
	}
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
			overflowFromLastOperation = true;
		}
		System.out.println(aValue + " + " + bValue +" = " + cValue);
		return cValue;
		
	}
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
			overflowFromLastOperation = true;
		}
		System.out.println(aValue + " - " + bValue +" = " + cValue);
		return cValue;
	}
	public int MUL(String a, String b){
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
			overflowFromLastOperation = true;
		}
		System.out.println(aValue + " * " + bValue +" = " + cValue);
		return cValue;
		
	}
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
			overflowFromLastOperation = true;
		}
		System.out.println(aValue + " / " + bValue +" = " + cValue);
		return cValue;
		
	}
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
		overflowFromLastOperation = false;
		System.out.println(aValue + " + " + bValue +" = " + cValue);
		return cValue;
		
	}
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
		overflowFromLastOperation = false;
		System.out.println(aValue + " - " + bValue +" = " + cValue);
		return cValue;
		
	}
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
		overflowFromLastOperation = false;
		System.out.println(aValue + " * " + bValue +" = " + cValue);
		return cValue;
		
	}
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
			zerodiv = true;
		}
		
		if(cValue < 0){
			cValue = cValue*-1;
		}
		overflowFromLastOperation = false;
		System.out.println(aValue + " / " + bValue +" = " + cValue);
		return cValue;
		
	}
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
			overflowFromLastOperation = true;
		}
		System.out.println(aValue + " ^ " + bValue +" = " + c);
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
	public int ADDI(String a, String b){
		String result = extendBits(a, b);
		//System.out.println(result + " x");
		//System.out.println(a + " a");
		int r = (ADD(a,result));
		return r;
	}
	public int SUBI(String a, String b){
		String result = extendBits(a, b);
		int r = (SUB(a,result));
		return r;
	}
	public int MULI(String a, String b){
		String result = extendBits(a, b);
		int r = (MUL(a,result));
		return r;
	}
	public int DIVI(String a, String b){
		String result = extendBits(a, b);
		int r = (DIV(a,result));
		return r;
	}
	public int ADDIU(String a, String b){
		String result = extendBits(a, b);
		int r = (ADDU(a,result));
		return r;
	}
	public int SUBIU(String a, String b){
		String result = extendBits(a, b);
		int r = (SUBU(a,result));
		return r;
	}
	public int MULIU(String a, String b){
		String result = extendBits(a, b);
		int r = (MULU(a,result));
		return r;
	}
	public int DIVIU(String a, String b){
		String result = extendBits(a, b);
		int r = (DIVU(a,result));
		return r;
	}
	public String SLL(String Reg, String bits){
		
		int bits2 = Integer.parseInt(bits, 2);
		int len = Reg.length();
		
		String result = Reg.substring(bits2,len);
		
		for(int i = bits2; i>0; i--){
			result += '0';
		}
		return result;
	}
	public String SRL(String Reg, String bits){
	String result = "";
	int bits2 = Integer.parseInt(bits, 2);
	int len = Reg.length();
	for(int i = bits2; i>0; i--){
		result += '0';
	}
	result += Reg.substring(0,len-bits2);
	
	
	return result;
	}
	public String SRA(String Reg, String bits){
		int bits2 = Integer.parseInt(bits, 2);
		
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
	public static int GetIntegerFromTwosComplementSigned(String a){
		char [] bits = new char[a.length()];
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
		for(int i = 1; i < a.length(); i++){
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
	public static int GetIntegerFromTwosComplementUnsigned(String a){
		char [] bits = new char[a.length()];
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
		for(int i = 1; i < a.length(); i++){
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
		int zeroes = 32- imm.length();
		while (zeroes >0){
			result += '0';
			zeroes--;
		}
		result += imm;
		return result;
	}
	public static String extendBits(String reg, String imm){
		String result = "";
		int zeroes = reg.length() - imm.length();
		while (zeroes >0){
			if(imm.charAt(0) == '1'){
				while(zeroes >0){
					result+= '1';
					zeroes--;
				}
				result += imm;
			}
			else{
				result = padZeros(reg,imm);
				break;
			}
			zeroes--;
		}
		
		return result;
	}
	public static String hexToBin(String hex){
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
	public static String binToHex32bits(String bin){
		String a = "";
		for(int i = 0; i <=32; i++){
			a+='0';
		}
		String extended32bitbinary = extendBits(a, bin);
		//System.out.println(extended32bitbinary);
		String hex = "";
		int b = 4;
		for(int i = 0; b <= 32 ; i+=4){
			
			String singlehexinbin = extended32bitbinary.substring(i, b);
			
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
		//System.out.println(hex);
		return hex;
	}
	public static String intToBin(int in){
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
	public static String intToHex(int in){
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