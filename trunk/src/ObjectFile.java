import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ObjectFile {
	
	public static void createObjectFile(Program p) {
		ArrayList<ArrayList<String>> linkingrecord = new ArrayList<ArrayList<String>>();

		// First we create Linking Record, starting with the program name
		ArrayList<String> record = makeLinkingRecord(Program.programName, true);
		linkingrecord.add(record);

		// Get list of all symbols in Symbol Table
		ArrayList<String> symbols = Program.symbolTable.getSortedListOfSymbols();

		// Iterate through all the symbols checking for ENT symbols
		for (int i = 0; i <= symbols.size(); i++) {
			ArrayList<Object> values = Program.symbolTable.getInfoFromSymbol(symbols
					.get(i));

			if (values.get(2) == SymbolTable.Uses.ENT) {
				record = makeLinkingRecord(symbols.get(i), false);
				linkingrecord.add(record);
			}
		}

		// Then we create header file
		ArrayList<String> header = new ArrayList<String>();

		header.add("H");
		header.add(Program.programName);
		header.add(Integer.toHexString(Integer.valueOf(Program.programLength)));
		header.add(String.valueOf(Program.startLocation));
		header.add(String.valueOf(Calendar.YEAR) + ":"
				+ String.valueOf(Calendar.DAY_OF_YEAR));
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date time = new Date();
		header.add(String.valueOf(dateFormat.format(time)));
		header.add(String.valueOf(Integer.toHexString(linkingrecord.size())));
		header.add("Number of Text Records");
		header.add(String.valueOf(Program.executionStart));
		header.add("SAL");
		header.add("1");
		header.add("2");
		header.add(Program.programName);

	}

	public static ArrayList<String> makeLinkingRecord(String symbol,
			boolean start) {

		ArrayList<String> linkingrecord = new ArrayList<String>();
		linkingrecord.add("L");
		linkingrecord.add(symbol);
		ArrayList<Object> values = Program.symbolTable.getInfoFromSymbol(symbol);
		String hexaddress = Integer.toHexString(Integer.valueOf((String) values
				.get(2)));
		linkingrecord.add(hexaddress);
		if (start) {
			linkingrecord.add("START");
		} else {
			linkingrecord.add("ENT");
		}
		linkingrecord.add(Program.programName);
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
