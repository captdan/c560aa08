import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ObjectFile {
	
	private Program p;
	int linkingrecordsize;
	int textrecordsize;
	
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
		linkingrecordsize = linkingrecord.size();
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
		header.add(String.valueOf(Integer.toHexString(linkingrecordsize)));
		header.add(String.valueOf(textrecordsize));
		header.add(String.valueOf(p.executionStart));
		header.add("SAL");
		header.add("1");
		header.add("2");
		header.add(p.programName);

		return header;
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

	public static String joinStringArray(ArrayList<String> stringArray,
			String delimiter) {
		String totalString = stringArray.get(0);
		for (int x = 1; x < stringArray.size(); x++) {
			totalString += (delimiter + stringArray.get(x));
		}
		return totalString;
	}
}
