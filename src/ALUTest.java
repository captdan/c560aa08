
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
	private String negoneHex;
	private String neg5Hex;
	private String neg10Hex;
	private String neg50Hex;
	private String pos5Hex;
	private String pos10Hex;
	private String pos50Hex;
	private String zeroHex;
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
		zeroHex = "00000000";
		negoneHex = "ffffffff";
		neg5Hex   = "fffffffB";
		neg10Hex  = "fffffff6";
		neg50Hex  = "ffffffCE";
		pos5Hex   = "00000005";
		pos10Hex  = "0000000A";
		pos50Hex  = "00000032";	
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
		assertEquals("",ALU.padZeros(neg10, zeroimm),"00000000000000000000000000000000");
		assertEquals("",ALU.padZeros(neg10, neg5imm),"00000000000000001111111111111011");
		assertEquals("",ALU.padZeros(neg10,neg10imm),"00000000000000001111111111110110");
		assertEquals("",ALU.padZeros(neg10,neg50imm),"00000000000000001111111111001110");
		assertEquals("",ALU.padZeros(neg10, pos5imm),"00000000000000000000000000000101");
		assertEquals("",ALU.padZeros(neg10,pos10imm),"00000000000000000000000000001010");
		assertEquals("",ALU.padZeros(neg10,pos50imm),"00000000000000000000000000110010");
	}
	@Test
	public void extendBitsTest(){
		assertEquals("",ALU.extendBits(neg10, zeroimm),"00000000000000000000000000000000");
		assertEquals("",ALU.extendBits(neg10, neg5imm),"11111111111111111111111111111011");
		assertEquals("",ALU.extendBits(neg10,neg10imm),"11111111111111111111111111110110");
		assertEquals("",ALU.extendBits(neg10,neg50imm),"11111111111111111111111111001110");
		assertEquals("",ALU.extendBits(neg10, pos5imm),"00000000000000000000000000000101");
		assertEquals("",ALU.extendBits(neg10,pos10imm),"00000000000000000000000000001010");
		assertEquals("",ALU.extendBits(neg10,pos50imm),"00000000000000000000000000110010");
		assertEquals("",ALU.extendBits(neg10,pos50),"00000000000000000000000000110010");
		
	}
	@Test
	public void HexToBinTest(){
		assertEquals("",ALU.hexToBin(zeroHex),  "00000000000000000000000000000000");
		assertEquals("",ALU.hexToBin(negoneHex),"11111111111111111111111111111111");
		assertEquals("",ALU.hexToBin(neg5Hex),neg5);
		assertEquals("",ALU.hexToBin(neg10Hex),neg10);
		assertEquals("",ALU.hexToBin(neg50Hex),neg50);
		assertEquals("",ALU.hexToBin(pos5Hex),pos5);
		assertEquals("",ALU.hexToBin(pos10Hex),pos10);
		assertEquals("",ALU.hexToBin(pos50Hex),pos50);
	}
	@Test
	public void BinToHex(){
		assertEquals("",ALU.binToHex32bits(zero), zeroHex);
		assertEquals("",ALU.binToHex32bits(neg5),neg5Hex.toUpperCase());
		assertEquals("",ALU.binToHex32bits(neg10),neg10Hex.toUpperCase());
		assertEquals("",ALU.binToHex32bits(neg50),neg50Hex.toUpperCase());
		assertEquals("",ALU.binToHex32bits(pos5),pos5Hex.toUpperCase());
		assertEquals("",ALU.binToHex32bits(pos10),pos10Hex.toUpperCase());
		assertEquals("",ALU.binToHex32bits(pos50),pos50Hex.toUpperCase());
	}

}
