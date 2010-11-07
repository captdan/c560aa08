import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Kerovesmo
 *
 */
public class ObjectFile {
	
	private Program p;
	ArrayList<String> headerRecord = new ArrayList<String>();
	ArrayList<ArrayList<String>> linkingRecord = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> textRecord = new ArrayList<ArrayList<String>>();
	ArrayList<String> endRecord = new ArrayList<String>();
	
	
	/**
	 * Constructor from a program object.
	 * @param program
	 */
	public ObjectFile (Program program)
	{
		p = program;
		headerRecord = this.createHeaderRecord();
		linkingRecord = this.createLinkingRecord();
		textRecord = this.createTextRecord();
		endRecord = this.createEndRecord();
	}
	
	/**
	 * 
	 * Module Name: createLinkingRecord
	 * Description: Creates a record for the linker.
	 * Input Params: N/A
	 * Output Params: ArrayList<ArrayList<String>>
	 * 					An arraylist containing the linking record.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Rakaan Kayali
	 * Date of Installation: 11/5/2010
	 * Modifications: 
	 */
	public ArrayList<ArrayList<String>> createLinkingRecord() 
	{
		
		ArrayList<ArrayList<String>> linkingrecord = new ArrayList<ArrayList<String>>();

		// First we create Linking Record, starting with the program name
		ArrayList<String> record = makeOneLinkingRecord(p.programName, true);
		linkingrecord.add(record);

		// Get list of all symbols in Symbol Table
		ArrayList<String> symbols = p.symbolTable.getSortedListOfSymbols();

		// Iterate through all the symbols checking for ENT symbols
		for (int i = 0; i < symbols.size(); i++) 
		{
			ArrayList<Object> values = p.symbolTable.getInfoFromSymbol(symbols.get(i));

			if (values.get(2) == SymbolTable.Uses.ENT) {
				record = makeOneLinkingRecord(symbols.get(i), false);
				linkingrecord.add(record);
			}
		}
		
		return linkingrecord;
	}
	
	/**
	 * 
	 * Module Name: createTextRecord
	 * Description: Creates the text record 
	 * Input Params: N/A
	 * Output Params: ArrayList<ArrayList<String>>
	 * 					The text record
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns
	 * Date of Installation: 11/5/2010
	 * Modifications: 
	 */
	public ArrayList<ArrayList<String>> createTextRecord() {
		
		ArrayList<ArrayList<String>> textRecord = new ArrayList<ArrayList<String>>();
		
		for (CodeLine codeline : p.CodeLineArray)
		{
		ArrayList<String> record = makeOneTextRecord(codeline);
		
		textRecord.add(record);
		}
		
		
		return textRecord;
	}
	
	/**
	 * 
	 * Module Name: makeOneTextRecord
	 * Description: Takes a single line and makes its text record.
	 * Input Params: codeline
	 * 					The line of code to generated a record for.
	 * Output Params: ArrayList<String>
	 * 					A list of containing the text record for the line.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Rakaan Kayali
	 * Date of Installation: 11/5/2010
	 * Modifications: 
	 */
	private ArrayList<String> makeOneTextRecord(CodeLine codeline)
	{
		ArrayList<String> textRecord = new ArrayList<String>();
		
		textRecord.add("T");
		
		textRecord.add(String.valueOf(Integer.toHexString(codeline.PC)));
		
		int debugValue = 0;
		
		String debugSign;
		
		if (codeline.directive != null && codeline.directive.directiveName.equals("DEBUG"))
		{
			debugValue = Integer.valueOf(codeline.directive.operandArray.get(0).operand);
		}
		
		if (debugValue == 0)
		{
			debugSign = "N";
		}
		else
		{
			debugSign = "Y";
		}
		
		textRecord.add(debugSign);

		if (codeline.directive != null)
		{
			textRecord.add(String.valueOf(codeline.directive.operandArray.size()));
			for (int count = 0; count < codeline.directive.operandArray.size();count++)
			{
			textRecord.add(codeline.directive.operandArray.get(count).relocationType.toString());
			if(codeline.directive.operandArray.get(count).relocationType != Operand.relocationTypes.A )
			{
				textRecord.add(codeline.directive.operandArray.get(count).operand);
			}
			
			}
		}
		
		if (codeline.instruction != null)
		{
			textRecord.add(String.valueOf(codeline.instruction.operandsArray.size()));
			for (int count = 0; count < codeline.instruction.operandsArray.size();count++)
			{
			textRecord.add(codeline.instruction.operandsArray.get(count).relocationType.toString());
			if(codeline.instruction.operandsArray.get(count).relocationType != Operand.relocationTypes.A )
			{
				if(codeline.instruction.operandsArray.get(count).operandType == Instruction.operandTypes.COMPLEXADDRESS)
				{
					textRecord.add(codeline.instruction.operandsArray.get(count).returnComplexAddressLabel());
				}
				else
				{
					textRecord.add(codeline.instruction.operandsArray.get(count).operand);
				}
				
			}
			}
		}
		
		textRecord.add(p.programName);
		
		
		return textRecord;
	}
	
	/**
	 * 
	 * Module Name: countNonAbsoluteOperands
	 * Description: If operands aren't absolute, count them.
	 * Input Params: operandsArray
	 * 					The operands to be counted.
	 * Output Params: int
	 * 					The number of operands.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns
	 * Date of Installation: 11/5/2010
	 * Modifications: 
	 */
	public int countNonAbsoluteOperands(ArrayList<Operand> operandsArray)
	{
		int count = 0;
		for(Operand operand : operandsArray)
		{
			if(operand.relocationType != Operand.relocationTypes.A)
			{
				count = count + 1;
			}
		}
		return count;
	}
	
	/**
	 * 
	 * Module Name: createHeaderRecord
	 * Description: Creates the header record.
	 * Input Params: N/A
	 * Output Params: ArrayList<String>
	 * 					The header record.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns
	 * Date of Installation: 11/5/2010
	 * Modifications: 
	 */
	public ArrayList<String> createHeaderRecord()
	{
		// Then we create header file
		ArrayList<String> header = new ArrayList<String>();

		header.add("H");
		header.add(p.programName);
		header.add(Integer.toHexString(Integer.valueOf(p.programLength)));
		header.add(String.valueOf(p.startLocation));
		header.add(String.valueOf(Calendar.YEAR) + ":"
				+ String.valueOf(Calendar.DAY_OF_YEAR));
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date time = new Date();
		header.add(String.valueOf(dateFormat.format(time)));
		header.add(String.valueOf(Integer.toHexString(linkingRecord.size())));
		header.add(String.valueOf(textRecord.size()));
		header.add(String.valueOf(p.executionStart));
		header.add("SAL");
		header.add("1");
		header.add("2");
		header.add(p.programName);

		headerRecord = header;
		return header;
	}

	/**
	 * 
	 * Module Name: createEndRecord
	 * Description: Creates the end of the record.
	 * Input Params: N/A
	 * Output Params: ArrayList<String>
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Rakaan Kayali
	 * Date of Installation: 11/5/2010
	 * Modifications: 
	 */
	public ArrayList<String> createEndRecord ()
	{
		ArrayList<String> endRecord = new ArrayList<String>();
		
		endRecord.add("E");
		endRecord.add(String.valueOf(Integer.toHexString(linkingRecord.size() + textRecord.size() + 2)));
		endRecord.add(p.programName);
		
		return endRecord;
	}
	
	/**
	 * 
	 * Module Name: makeOneLinkingRecord
	 * Description: Make a linking record for one line.
	 * Input Params: symbol
	 * 					The symbol to make a record of.
	 * 				 start
	 * 					Whether it's a start symbol or not.
	 * Output Params: ArrayList<String>
	 * 					The record.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Bobby Schmidt
	 * Date of Installation: 11/5/2010
	 * Modifications: 
	 */
	private ArrayList<String> makeOneLinkingRecord(String symbol,boolean start)
	{
		ArrayList<String> linkingrecord = new ArrayList<String>();
		linkingrecord.add("L");
		linkingrecord.add(symbol);
		ArrayList<Object> values = p.symbolTable.getInfoFromSymbol(symbol);
		String hexaddress = Integer.toHexString(Integer.valueOf((String) values.get(0)));
		linkingrecord.add(hexaddress);
		if (start) {
			linkingrecord.add("START");
		} else {
			linkingrecord.add("ENT");
		}
		linkingrecord.add(p.programName);
		return linkingrecord;
	}
	
	/**
	 * 
	 * Module Name: prettyFerret2
	 * Description: Outputs the object file.
	 * Input Params: N/A
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Daniel Burnett
	 * Date of Installation: 11/5/2010
	 * Modifications:
	 */
	public void prettyFerret2 ()
	{
		try
		{
			FileWriter fileStream = new FileWriter("ObjectFile.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileStream);
			
			bufferedWriter.write(joinStringArray(headerRecord,"|") + "\n");
			
			for(ArrayList<String> a : linkingRecord)
			{
				bufferedWriter.write(joinStringArray(a,"|") + "\n");
			}	
			
			for(ArrayList<String> a : textRecord)
			{
				bufferedWriter.write(joinStringArray(a,"|") + "\n");
			}
			
			bufferedWriter.write(joinStringArray(endRecord,"|") + "\n");
			
			bufferedWriter.close();
		}
		catch (Exception e)
		{
			System.err.println("Unable to write object file!");
		}
	}
	
	/**
	 * 
	 * Module Name: joinStringArray
	 * Description: Joins a string array into a single string.
	 * Input Params: stringArray
	 * 					The array to be joined.
	 * 				 delimiter
	 * 					The delimiter character.
	 * Output Params: String
	 * 					The joined array.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns
	 * Date of Installation: 11/5/2010
	 * Modifications: 
	 */
	private String joinStringArray(ArrayList<String> stringArray, String delimiter) 
	{
		String totalString = stringArray.get(0);
		
		for (int x = 1; x < stringArray.size(); x++) 
		{
			totalString += (delimiter + stringArray.get(x));
		}
		return totalString;
	}
}
