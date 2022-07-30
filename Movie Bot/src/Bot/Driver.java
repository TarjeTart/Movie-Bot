package Bot;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import DataSaves.MovieWatcher;
import DataSaves.ReactedMessageSave;
import EventHandlers.MessageReactionAdd;
import EventHandlers.MessageRecieved;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Driver extends ListenerAdapter{
	
	/*
	 *ToDO:
	 *
	 */
	
	public static String token;
	public static Pattern prefix;
	public static Map<String,MovieWatcher> watchersMap = new HashMap<String, MovieWatcher>();
	public static JdaMaker jdaMaker;
	public static String location = 
			System.getProperty("user.dir").equals("D:\\Eclipse\\git\\Movie-Bot\\Movie Bot")
			? "D:\\Eclipse\\Movie Bot Data\\" : "C:\\Users\\Austin Remote\\Desktop\\Movie Bot\\Movie Bot Data\\";
	public static String moviePingsId = location.equals("C:\\Users\\Austin Remote\\Desktop\\Movie Bot\\Movie Bot Data\\") ? "796957621604974603" : "764983979560140831";//real : test
	public static String myID = "251761821885792256";
	public static String botTestingId = "512859462307151872";
	public static String movieChannel = location.equals("C:\\Users\\Austin Remote\\Desktop\\Movie Bot\\Movie Bot Data\\") ? "788932342214426635" : "862787306167861279";
	public static String version = "3.3.7";
	public static String website;
	private static int MAX_MESSAGESAVES = 1000;
	public static Map<String,ReactedMessageSave> messageSaves = new LinkedHashMap<String,ReactedMessageSave>(){
		private static final long serialVersionUID = 1L;
		@Override
		protected boolean removeEldestEntry(Map.Entry<String,ReactedMessageSave> eldest) {
			return this.size() > MAX_MESSAGESAVES;
		}
	};
	public static ArrayList<String> bannedUsers = new ArrayList<String>();

	public static void main(String[] args) {
		
		//load info from text files
		if(!getData()){
			System.out.println("Failed to load token, prefix, or website");
			return;
		}else if(!getMovies()) {
			System.out.println("Failed to load movie list");
			return;
		}else if(!getBans()) {
			System.out.println("Failed to load ban list");
			return;
		}
		
		//bot maker class
		jdaMaker = new JdaMaker(token,prefix.toString() + "help");
		//create bot
		jdaMaker.constructJda();

		System.out.println("Location var set to " + location);
		System.out.println("Running bot version: " + version);
		
	}

	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        new MessageRecieved(event);
    }//ends message received event 
	
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if(event.getUser().isBot())
			return;
		new MessageReactionAdd(event);
	}
	
	public static boolean getBans() {
		File file = new File(location + "bans.txt");
		try {
			Scanner scnr = new Scanner(file);
			while(scnr.hasNextLine()) {
				bannedUsers.add(scnr.nextLine());
			}
			scnr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//gets token and prefix
	public static boolean getData() {
		File file = new File(location + "startup.txt");
		try {
			Scanner scnr = new Scanner(file);
			prefix = Pattern.compile(scnr.nextLine(),Pattern.CASE_INSENSITIVE);
			token = scnr.nextLine();
			website = scnr.nextLine();
			scnr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static boolean getMovies() {
		File file = new File(location + "MovieData.txt");
		try {
			Scanner scnr = new Scanner(file);
			while(scnr.hasNextLine()) {
				String id = scnr.nextLine();
				String userName = scnr.nextLine();
				ArrayList<String> tmpList = new ArrayList<String>();
				String line = scnr.nextLine();
				while(!line.equals("-")) {
					tmpList.add(line);
					line = scnr.nextLine();
				}
				watchersMap.put(id, new MovieWatcher(id,userName,tmpList));
			}
			scnr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}