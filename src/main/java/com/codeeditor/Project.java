package application;
import java.util.* ;

public class Project {
	private String name;
	private String main;
	private String file1=null,file2=null;
	private List<String>library;
	
	public Project() {
	}
	public Project(String a,String b,String c,String d,List<String> e) {
		name=a;
		main=b;
		file1=c;
		file2=d;
		library=e;
	}
	public String getName() {
		return name;
	}
	public String getFile1() {
		return file1;
	}
	public String getFile2() {
		return file2;
	}
	public String getMain() {
		return main;
	}
	public List<String> getLibrary(){
		return library;
	}
}
