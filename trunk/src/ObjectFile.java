import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

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
		linkingRecord = this.createLinkingRecord();
		textRecord = this.createTextRecord();
		headerRecord = this.createHeaderRecord();
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
		if((codeline.directive != null || codeline.instruction != null) && codeline.errors.size() == 0)
		{
		
		textRecord.add("T");
		
		textRecord.add(String.valueOf(Integer.toHexString(codeline.PC)));
		
		String debugSign = "";
		
		if (p.debugMode == false)
		{
			debugSign = "N";
		}
		else
		{
			debugSign = "Y";
		}
		textRecord.add(debugSign);
		
		//Still need data_word checking for directives
		if (codeline.directive != null)
		{
			textRecord.add(codeline.directive.returnHexCodeLine(codeline));
			
		}
		
		//Still need data_word checking for instructions
		if (codeline.instruction != null)
		{
			textRecord.add(codeline.instruction.returnHexCodeLine());
			
		}
		
		if (codeline.directive != null)
		{
			//adds number of adjustments
			textRecord.add(String.valueOf(codeline.directive.operandArray.size()));

			if((codeline.directive.operands.size() > 0) && (codeline.directive.operands.get(0).equals(Directive.operandTypes.EXP)))
			{
				StringTokenizer expressionTokenizer = new StringTokenizer(codeline.directive.operandArray.get(0).operand,"+-",true);
				
				boolean result = false;
				boolean negative = false;
				
				
				//first check to see if it's a valid integer value
				String operand = expressionTokenizer.nextToken();
				if(operand == "-")	
				{
					negative = true;
					
				}
				try
				{
					Integer.parseInt(operand);
					textRecord.add("A");
					result = true;
				}
				catch(NumberFormatException e)
				{
					//do nothing
				}
				
				if((result == false) && (operand.equals("*")))
				{
					textRecord.add("A");
					result = true;
				}
				
				//If it's not an integer or a "*" then it must be a label
				if(result == false)
				{
						ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(operand);
						if(values.get(2).equals(SymbolTable.Uses.EXTERNAL))
						{
							textRecord.add("E");
							if(negative)
							{
								textRecord.add("-");
							}
							else
							{
								textRecord.add("+");
							}
							
							textRecord.add(operand);
						}
						else
						{
							textRecord.add("R");
							if(negative)
							{
								textRecord.add("-");
							}
							else
							{
								textRecord.add("+");
							}
							
							textRecord.add(String.valueOf(p.startLocation));
						}	
				}
				
				//Done with 1st operand, now check if there's more to the expression. If so then we have at least 2 more tokens (+/- and operand)
				while(expressionTokenizer.hasMoreTokens())
				{
					String symb = expressionTokenizer.nextToken();
					String op = expressionTokenizer.nextToken();
					
					result = false;
					
					//first check to see if it's a valid integer value
					try
					{
						Integer.parseInt(op);
						textRecord.add("A");
						textRecord.add(symb);
						result = true;
					}
					catch(NumberFormatException e)
					{
						//do nothing
					}
					
					if((result == false) && (op.equals("*")))
					{
						textRecord.add("A");
						textRecord.add(symb);
						result = true;
					}
					
					//If it's not an integer or a "*" then it must be a label
					if(result == false)
					{
							ArrayList<Object> values = Parser.SymbTable.getInfoFromSymbol(op);
							if(values.get(2).equals(SymbolTable.Uses.EXTERNAL))
							{
								textRecord.add("E");
								textRecord.add(symb);
								textRecord.add(op);
							}
							else
							{
								textRecord.add("R");
								textRecord.add(symb);
								textRecord.add(String.valueOf(p.startLocation));
							}	
					}
				}
					
								
			}
			
			else
			{
				for (int count = 0; count < codeline.directive.operandArray.size();count++)
				{
					textRecord.add(codeline.directive.operandArray.get(count).relocationType.toString());
					if(codeline.directive.operandArray.get(count).relocationType == Operand.relocationTypes.E )
					{
						textRecord.add(codeline.directive.operandArray.get(count).operand);
					}
					else if(codeline.directive.operandArray.get(count).relocationType == Operand.relocationTypes.R )
					{
						//textRecord.add(codeline.directive.operandArray.get(count).operand);
						textRecord.add("+");
						textRecord.add(String.valueOf(p.startLocation));
					}
				}
			}
			
		}
		
		if (codeline.instruction != null)
		{
			textRecord.add(String.valueOf(codeline.instruction.operandsArray.size()));
			for (int count = 0; count < codeline.instruction.operandsArray.size();count++)
			{
				//TODO Fix complex address relocation type output
			
			if(codeline.instruction.operandsArray.get(count).operandType != Instruction.operandTypes.COMPLEXADDRESS)
			{
				textRecord.add(codeline.instruction.operandsArray.get(count).relocationType.toString());
				if(codeline.instruction.operandsArray.get(count).relocationType != Operand.relocationTypes.A )
				{
					if(codeline.instruction.operandsArray.get(count).relocationType == Operand.relocationTypes.E )
					{
						textRecord.add(codeline.instruction.operandsArray.get(count).operand);
					}
					else if(codeline.instruction.operandsArray.get(count).relocationType == Operand.relocationTypes.R )
					{
						//textRecord.add(codeline.instruction.operandsArray.get(count).operand);
						textRecord.add("+");
						textRecord.add(String.valueOf(p.startLocation));
					}
				}
			}
			else
			{
				
				textRecord.add(codeline.instruction.operandsArray.get(count).returnComplexAddressLabel());
			}
			

			}
		}
		
		textRecord.add(p.programName);
		
		//textRecord.add(codeline.originalLineOfCode);
		
	
		}
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
		Calendar now = Calendar.getInstance();
		
		header.add("H");
		header.add(p.programName);
		header.add(Integer.toHexString(Integer.valueOf(p.programLength)));
		header.add(String.valueOf(p.startLocation));
		header.add(String.valueOf(now.get(Calendar.YEAR)) + ":"
				+ String.valueOf(now.get(Calendar.DAY_OF_YEAR)));
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
		try
		{
			ArrayList<Object> values = p.symbolTable.getInfoFromSymbol(symbol);
			String hexaddress = Integer.toHexString(Integer.valueOf((String) values.get(0)));
			linkingrecord.add(hexaddress);
			if (start) {
				linkingrecord.add("START");
			} else {
				linkingrecord.add("ENT");
			}
			linkingrecord.add(p.programName);
		}
		catch(Exception e){}
		

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
		FileWriter fileStream;
		try 
		{
			fileStream = new FileWriter("ObjectFile.txt");

		BufferedWriter bufferedWriter = new BufferedWriter(fileStream);
		
		bufferedWriter.write(joinStringArray(headerRecord,"|") + "\n");
		
		for(ArrayList<String> a : linkingRecord)
		{
			bufferedWriter.write(joinStringArray(a,"|") + "\n");
		}	
		
		for(ArrayList<String> a : textRecord)
		{
			if(a.size() > 0)
			{
				bufferedWriter.write(joinStringArray(a,"|") + "\n");
			}
		}
		
		bufferedWriter.write(joinStringArray(endRecord,"|") + "\n");
		
		bufferedWriter.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String totalString = "";
		if(stringArray.size() > 0)
		{
			totalString = stringArray.get(0);
		
			for (int x = 1; x < stringArray.size(); x++) 
			{
				totalString += (delimiter + stringArray.get(x));
			}
		}
		return totalString;
	}
}
