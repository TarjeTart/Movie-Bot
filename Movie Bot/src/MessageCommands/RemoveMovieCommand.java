package MessageCommands;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Bot.*;
import DataSaves.MovieWatcher;
import Interfaces.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RemoveMovieCommand implements Command{

	private MessageReceivedEvent event;
	String msg;
	User user;
	Member member;
	Guild guild;
	JDA jda;
	TextChannel channel;
	Pattern p;
	Matcher m;

	public RemoveMovieCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}
	
	@Override
	public void run() {
		getInfo();
		boolean isMe = member.getId().equals(Driver.myID);
		String title = msg.replace(Driver.prefix + "remove ", "");
		boolean wasRemoved = false;
		if(isMe) {
			for(MovieWatcher i : Driver.watchersMap.values()) {
				if(i.movies.remove(title)) {
					wasRemoved = true;
					break;
				}
			}
			if(wasRemoved) {
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("Movie Removed:").setColor(Color.magenta).setThumbnail(user.getAvatarUrl());
				eb.addField("", title, false);
				channel.sendMessage(eb.build()).queue();
				saveMovies();
				return;
			}else {
				channel.sendMessage(title + " was not found").queue();
				return;
			}
		}else {
			MovieWatcher watcher = Driver.watchersMap.get(user.getId());
			if(watcher.movies.size() == 0 || watcher == null) {
				channel.sendMessage("you do not have a list").queue();
				return;
			}
			if(watcher.movies.remove(title)) {
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("Movie Removed:").setColor(Color.magenta).setThumbnail(user.getAvatarUrl());
				eb.addField("", title, false);
				channel.sendMessage(eb.build()).queue();
				saveMovies();
				return;
			}else {
				channel.sendMessage(title + " was not found").queue();
				return;
			}
		}
	}

	private void saveMovies() {
		try {
			for(MovieWatcher i : Driver.watchersMap.values()) {
				Collections.sort(i.movies);
			}
			FileWriter file = new FileWriter(Driver.location + "MovieData.txt",false);
			for(MovieWatcher i : Driver.watchersMap.values()) {
				file.write(i.id + "\r\n");
				file.write(i.userName + "\r\n");
				for(String j : i.movies) {
					file.write(j + "\r\n");
				}
				file.write("-\r\n");
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean check() {
		return msg.startsWith(Driver.prefix + "remove ");
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "remove [movie title]";
	}

	@Override
	public String getDescription() {
		return "removes movie from your list";
	}

	@Override
	public void getInfo() {
		user = event.getAuthor();
		guild = event.getGuild();
		jda = event.getJDA();
		member = jda.getGuildById(guild.getId()).retrieveMember(user).complete();
		channel = event.getTextChannel();
	}

}