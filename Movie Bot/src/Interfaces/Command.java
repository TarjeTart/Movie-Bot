package Interfaces;

public interface Command {
	
	public void run();
	
	public boolean check();

	public String getName();
	
	public String getDescription();
	
	public void getInfo();
	
}