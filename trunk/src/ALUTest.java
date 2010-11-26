
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Shaka
 *
 */
public class ALUTest {
	private String zero;
	private String neg5;
	private String neg10;
	private String neg50;
	private String pos5;
	private String pos10;
	private String pos50;
	private String zeroimm;
	private String neg5imm;
	private String neg10imm;
	private String neg50imm;
	private String pos5imm;
	private String pos10imm;
	private String pos50imm;
	private ALU ALU;
	/**
	 * Module Name:
	 * Description:
	 * Input Params:
	 * Output Params:
	 * Error Conditions Tested:
	 * Error Messages Generated:
	 * Original Author:
	 * Date of Installation:
	 * Modifications:
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUpBeforeClass(){
		// 32-bit Strings
		zero =  "00000000000000000000000000000000";
		neg5 =  "11111111111111111111111111111011";
		neg10=  "11111111111111111111111111110110";
		neg50=  "11111111111111111111111111001110";
		pos5 =  "00000000000000000000000000000101";
		pos10=  "00000000000000000000000000001010";
		pos50=  "00000000000000000000000000110010";
		zeroimm    = "0000000000000000";
		neg5imm    = "1111111111111011";
		neg10imm   = "1111111111110110";
		neg50imm   = "1111111111001110";
		pos5imm    = "0000000000000101";
		pos10imm   = "0000000000001010";
		pos50imm   = "0000000000110010";
		ALU = new ALU();
	}
	@Test
	public void TestTwosComplamentFunctions(){
		assertEquals("",ALU.GetIntegerFromTwosComplementSigned(zero),0);
		assertEquals("",ALU.GetIntegerFromTwosComplementSigned(neg5),-5);
		assertEquals("",ALU.GetIntegerFromTwosComplementSigned(neg10),-10);
		assertEquals("",ALU.GetIntegerFromTwosComplementSigned(neg50),-50);
		assertEquals("",ALU.GetIntegerFromTwosComplementSigned(pos5),5);
		assertEquals("",ALU.GetIntegerFromTwosComplementSigned(pos10),10);
		assertEquals("",ALU.GetIntegerFromTwosComplementSigned(pos50),50);
		assertEquals("",ALU.GetIntegerFromTwosComplementSigned(neg10imm),-10);
		assertEquals("",ALU.GetIntegerFromTwosComplementUnsigned(zero),0);
		assertEquals("",ALU.GetIntegerFromTwosComplementUnsigned(neg5),5);
		assertEquals("",ALU.GetIntegerFromTwosComplementUnsigned(neg10), 10);
		assertEquals("",ALU.GetIntegerFromTwosComplementUnsigned(neg50), 50);
		assertEquals("",ALU.GetIntegerFromTwosComplementUnsigned(pos5),5);
		assertEquals("",ALU.GetIntegerFromTwosComplementUnsigned(pos10), 10);
		assertEquals("",ALU.GetIntegerFromTwosComplementUnsigned(pos50), 50);
		assertEquals("",ALU.GetIntegerFromTwosComplementUnsigned(neg10imm), 10);
	}
	@Test
	public void invertBitsFunction(){
		assertEquals("",ALU.invertBits(neg10),"00000000000000000000000000001001");
	}
	@Test
	public void padZeroesTest(){
		assert(ALU.padZeros(neg10, zeroimm).equals("00000000000000000000000000000000"));
		assert(ALU.padZeros(neg10, neg5imm).equals("00000000000000000000000000000000"));
		assert(ALU.padZeros(neg10,neg10imm).equals("00000000000000000000000000000000"));
		assert(ALU.padZeros(neg10,neg50imm).equals("00000000000000000000000000000000"));
	}

}
