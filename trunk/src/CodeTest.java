
public class CodeTest {
public static void main(String[] args){
	ALU A = new ALU();
	String a = "1001010101011101";
	int t = ALU.GetIntegerFromTwosComplementSigned(a);
	
	String b = "10101";
	int tt = ALU.GetIntegerFromTwosComplementSigned(b);
	System.out.println(t);
	System.out.println(tt);
	System.out.println(A.ADDI(a,b));

//	System.out.println(d);
	
}
}
