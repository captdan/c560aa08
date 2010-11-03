import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ObjectFile {
	
	private Program p;
	ArrayList<String> headerRecord = new ArrayList<String>();
	ArrayList<ArrayList<String>> linkingRecord = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> textRecord = new ArrayList<ArrayList<String>>();
	ArrayList<String> endRecord = new ArrayList<String>();
	
	
	
	public ObjectFile (Program program)
	{
		p = program;
	}
	
	public ArrayList<ArrayList<String>> createLinkingRecord() {
		
		ArrayList<ArrayList<String>> linkingrecord = new ArrayList<ArrayList<String>>();

		// First we create Linking Record, starting with the program name
		ArrayList<String> record = makeOneLinkingRecord(p.programName, true);
		linkingrecord.add(record);

		// Get list of all symbols in Symbol Table
		ArrayList<String> symbols = p.symbolTable.getSortedListOfSymbols();

		// Iterate through all the symbols checking for ENT symbols
		for (int i = 0; i <= symbols.size(); i++) {
			ArrayList<Object> values = p.symbolTable.getInfoFromSymbol(symbols
					.get(i));

			if (values.get(2) == SymbolTable.Uses.ENT) {
				record = makeOneLinkingRecord(symbols.get(i), false);
				linkingrecord.add(record);
			}
		}
		
		return linkingrecord;
	}
	
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

	public ArrayList<String> createEndRecord ()
	{
		ArrayList<String> endRecord = new ArrayList<String>();
		
		endRecord.add("E");
		endRecord.add(String.valueOf(Integer.toHexString(linkingRecord.size() + textRecord.size() + 2)));
		endRecord.add(p.programName);
		
		return endRecord;
	}
	
	private ArrayList<String> makeOneLinkingRecord(String symbol,
			boolean start) {

		ArrayList<String> linkingrecord = new ArrayList<String>();
		linkingrecord.add("L");
		linkingrecord.add(symbol);
		ArrayList<Object> values = p.symbolTable.getInfoFromSymbol(symbol);
		String hexaddress = Integer.toHexString(Integer.valueOf((String) values
				.get(2)));
		linkingrecord.add(hexaddress);
		if (start) {
			linkingrecord.add("START");
		} else {
			linkingrecord.add("ENT");
		}
		linkingrecord.add(p.programName);
		return linkingrecord;
	}

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
