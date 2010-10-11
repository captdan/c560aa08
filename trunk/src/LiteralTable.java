import java.util.ArrayList;

public class LiteralTable {
	public ArrayList<String[]> table = new ArrayList<String[]>();
	//public ArrayList<String> literals = new ArrayList<String>();
	//public ArrayList<String> addresses = new ArrayList<String>();
	
	public void AddLiteral (String literal, String addr)
	{
		String[] temp = new String[2];
		temp[0] = literal;
		temp[1] = addr;
		table.add(temp);
	}
	
	public ArrayList<String[]> GetLiteralList ()
	{
		return table;
	}
	
	
}
