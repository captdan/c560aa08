
public class Directive 
{
	public codeLocations codeLocation = codeLocations.TEXT ;
	public String directiveName = "";
	public labelTypes labelType = labelTypes.NOLABEL;
	public String syntax = "";
	
	public static enum labelTypes
	{
		NOLABEL, REQUIREDLABEL, OPTIONALLABEL
	}
	public static enum codeLocations
	{
		START,END,DATA,TEXT,INFO
	}
}
