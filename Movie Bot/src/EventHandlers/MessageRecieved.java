package EventHandlers;
import MessageCommands.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;

import Bot.*;
import Interfaces.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageRecieved {

	private ArrayList<Command> commands = new ArrayList<Command>();
	private ArrayList<Command> adminCommands = new ArrayList<Command>();
	MessageReceivedEvent event;
	
	public MessageRecieved(MessageReceivedEvent event) {
		this.event=event;
		logMessage();
		
		if(event.getAuthor().isBot())
			return;
		
		Matcher prefixMatch = Driver.prefix.matcher(event.getMessage().getContentDisplay());
		
		if(!prefixMatch.find() || prefixMatch.start() != 0)
			return;
		if(!event.getChannel().getId().equals(Driver.movieChannel))
			return;
		for(String i : Driver.bannedUsers) {
				if(event.getAuthor().getId().equals(i)) {
					event.getChannel().sendMessage("You are banned from using this bot").queue();
					return;
				}
		}
		
		//bot commands
		MoviePingsCommand moviePingsCommand = new MoviePingsCommand(event);
		commands.add(moviePingsCommand);
		AddMovieCommand addMovieCommand = new AddMovieCommand(event);
		commands.add(addMovieCommand);
		RemoveMovieCommand removeMovieCommand = new RemoveMovieCommand(event);
		commands.add(removeMovieCommand);
		ListMoviesCommand listMoviesCommand = new ListMoviesCommand(event);
		commands.add(listMoviesCommand);
		GetMovieCommand getMovieCommand = new GetMovieCommand(event);
		commands.add(getMovieCommand);
		ChooseFromCommand chooseFromCommand = new ChooseFromCommand(event);
		commands.add(chooseFromCommand);
		GetDataCommand getDataCommand = new GetDataCommand(event);
		commands.add(getDataCommand);
		ChangePrefixCommand changePrefixCommand = new ChangePrefixCommand(event);
		commands.add(changePrefixCommand);
		GetVersionCommand getVersionCommand = new GetVersionCommand(event);
		adminCommands.add(getVersionCommand);
		AdminHelpCommand adminHelpCommand = new AdminHelpCommand(event, adminCommands);
		commands.add(adminHelpCommand);
		BulkAddCommand bulkAddCommand = new BulkAddCommand(event);
		commands.add(bulkAddCommand);
		AddBanCommand addBanCommand = new AddBanCommand(event);
		adminCommands.add(addBanCommand);
		RemoveBanCommand removeBanCommand = new RemoveBanCommand(event);
		adminCommands.add(removeBanCommand);
		GetWebsiteCommand getWebsiteCommand = new GetWebsiteCommand(event);
		commands.add(getWebsiteCommand);
		ChangeWebsiteCommand changeWebsiteCommand = new ChangeWebsiteCommand(event);
		adminCommands.add(changeWebsiteCommand);
		
		//check commands
		if(hasMoviePingRole()) {
			if(addMovieCommand.check())
				addMovieCommand.run();
			else if(removeMovieCommand.check())
				removeMovieCommand.run();
			else if(listMoviesCommand.check())
				listMoviesCommand.run();
			else if(getMovieCommand.check())
				getMovieCommand.run();
			else if(chooseFromCommand.check())
				chooseFromCommand.run();
			else if(changePrefixCommand.check())
				changePrefixCommand.run();
			else if(getDataCommand.check())
				getDataCommand.run();
			else if(getVersionCommand.check())
				getVersionCommand.run();
			else if(adminHelpCommand.check())
				adminHelpCommand.run();
			else if(addBanCommand.check())
				addBanCommand.run();
			else if(removeBanCommand.check())
				removeBanCommand.run();
			else if(getWebsiteCommand.check())
				getWebsiteCommand.run();
			else if(changeWebsiteCommand.check())
				changeWebsiteCommand.run();
			else if(bulkAddCommand.check())
				bulkAddCommand.run();
			else if(moviePingsCommand.check())
				moviePingsCommand.run();
		}else {
			if(moviePingsCommand.check()) {
				moviePingsCommand.run();
				return;
			}
			event.getChannel().sendMessage("You need the movie pings role to use this command. Do \"" 
					+ Driver.prefix.toString() + "moviepings\" to get the role").queue();
		}
		
		//help command
		if(event.getMessage().getContentDisplay().equalsIgnoreCase(Driver.prefix + "help"))
			new HelpCommand(commands,event);
		
	}
	
	private boolean hasMoviePingRole() {
		Member member = event.getGuild().retrieveMember(event.getAuthor()).complete();
		for(Role i : member.getRoles()) {
			if(i.getId().equals(Driver.moviePingsId))
				return true;
		}
		return false;
	}
	
	private void logMessage() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		System.out.printf("[%s](%s,%s)%s: %s\n", dtf.format(LocalDateTime.now()),
				event.getGuild().getName(),event.getChannel().getName(),
				event.getMember().getEffectiveName(),event.getMessage().getContentDisplay());
	}
	
}