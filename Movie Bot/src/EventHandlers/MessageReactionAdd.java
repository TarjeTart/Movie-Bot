package EventHandlers;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Bot.*;
import DataSaves.MovieWatcher;
import DataSaves.ReactedMessageSave;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class MessageReactionAdd {
	
	private GuildMessageReactionAddEvent event;

	public MessageReactionAdd(GuildMessageReactionAddEvent event) {
		this.event = event;
		logReaction();
		if(!event.getChannel().getId().equals(Driver.movieChannel))
			return;
		ReactedMessageSave reactedMessageSave = Driver.messageSaves.get(event.getMessageId());
		//check if this is a reaction that is saved
		if(reactedMessageSave == null)
			return;
		//check if the reacting user is the original user
		if(!event.getUser().getId().equals(reactedMessageSave.user.getId()))
			return;
		//calls method based of emote used
		if(event.getReactionEmote().getId().equals("860230762002907136")) {
			movieRedo(reactedMessageSave);
		}else if(isListUpdate()) {
			userListUpdate(reactedMessageSave);
		}else if(event.getReactionEmote().getId().equals("868608094480007188") 
				|| event.getReactionEmote().getId().equals("868608251841888256")) {
			addMovieCheck(reactedMessageSave);
		}
		
	}
	
	private void addMovieCheck(ReactedMessageSave reactedMessageSave) {
		boolean addMovie = event.getReactionEmote().getId().equals("868608094480007188");
		if(!(reactedMessageSave.titles == null) && addMovie) {
			if((reactedMessageSave.watcher.movies.size() + reactedMessageSave.titles.length) > reactedMessageSave.watcher.maxMovies) {
				reactedMessageSave.message.getChannel().sendMessage("you can't add this many movies to your list").queue();
				return;
			}
			for(String i : reactedMessageSave.titles) {
				for(MovieWatcher j : Driver.watchersMap.values()) {
					for(String k : j.movies) {
						if(i.equals(k)) {
							reactedMessageSave.message.getChannel().sendMessage(i + " is already in a movie list").queue();
							return;
						}
					}
				}
				if(!reactedMessageSave.watcher.addMovie(i)) {
					reactedMessageSave.message.getChannel().sendMessage("trouble adding movie " + i);
					return;
				}
			}
			saveMovies();
			reactedMessageSave.message.delete().queue();
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Movies Added:").setColor(Color.magenta).setThumbnail(reactedMessageSave.user.getAvatarUrl());
			for(String i : reactedMessageSave.titles) {
				eb.addField("", i, true);
			}
			reactedMessageSave.message.getChannel().sendMessage(eb.build()).queue();
		}else {
			if(addMovie) {
				ReactedMessageSave save = reactedMessageSave;
				if(!save.watcher.addMovie(save.title)) {
					save.message.getChannel().sendMessage("You have too many movie in your list").queue();
					return;
				}
				saveMovies();
				save.message.delete().queue();
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("Movie Added:").setColor(Color.magenta).setThumbnail(save.user.getAvatarUrl());
				eb.addField("", save.title, false);
				save.message.getChannel().sendMessage(eb.build()).queue();
			}else {
				reactedMessageSave.message.getChannel().sendMessage("Movie not added").queue();
				reactedMessageSave.message.delete().queue();
			}
		}
	}
	
	private void saveMovies() {
		System.out.println(Driver.watchersMap.values().size());
		for(MovieWatcher i : Driver.watchersMap.values()) {
			Collections.sort(i.movies);
		}
		try {
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
	
	private void userListUpdate(ReactedMessageSave reactedMessageSave) {
		int start = 0;
		switch(event.getReactionEmote().getId()) {
			case "716468511806849067":
				start = 0;
				break;
			case "716467615056265236":
				start = 15;
				break;
			case "716467864584060960":
				start = 30;
				break;
			case "716467958838460437":
				start= 45;
				break;
			case "716468491359617104":
				start = 50;
				break;
			default:
				return;
		}
		if(reactedMessageSave.watcher.movies.size() < start)
			return;
		int stop = start+15;
		if(reactedMessageSave.watcher.movies.size() < stop)
			stop = reactedMessageSave.watcher.movies.size();
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(reactedMessageSave.user.getName() + "'s Movie List:").setThumbnail(reactedMessageSave.user.getAvatarUrl()).setColor(Color.BLUE);
		for(int i = start; i < stop; i++) {
			eb.addField(String.valueOf(i+1) + ".",reactedMessageSave.watcher.movies.get(i),true);
		}
		Driver.messageSaves.remove(reactedMessageSave.message.getId());
		reactedMessageSave.message.delete().queue();
		Message botMessage = event.getChannel().sendMessage(eb.build()).complete();
		Guild myGuild = event.getJDA().getGuildById(Driver.botTestingId);
		botMessage.addReaction(myGuild.getEmoteById("716468511806849067")).queue();//1
		botMessage.addReaction(myGuild.getEmoteById("716467615056265236")).queue();//2
		botMessage.addReaction(myGuild.getEmoteById("716467864584060960")).queue();//3
		botMessage.addReaction(myGuild.getEmoteById("716467958838460437")).queue();//4
		botMessage.addReaction(myGuild.getEmoteById("716468491359617104")).queue();//5
		Driver.messageSaves.put(botMessage.getId(),new ReactedMessageSave(botMessage,reactedMessageSave.user,reactedMessageSave.watcher,false));
	}
	
	private void movieRedo(ReactedMessageSave reactedMessageSave) {
		if(reactedMessageSave.specificWatcher) {
			Driver.messageSaves.remove(reactedMessageSave.message.getId());
			reactedMessageSave.message.delete().queue();
			int ran = new Random().nextInt(reactedMessageSave.watcher.movies.size());
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Movie:").setColor(Color.BLACK);
			eb.addField("",reactedMessageSave.watcher.movies.get(ran),true);
			Message sent = reactedMessageSave.message.getChannel().sendMessage(eb.build()).complete();
			Driver.messageSaves.put(sent.getId(),new ReactedMessageSave(sent,reactedMessageSave.user,reactedMessageSave.watcher,true));
			sent.addReaction(event.getJDA().getGuildById(Driver.botTestingId).getEmoteById("860230762002907136")).queue();
		}else{
			Driver.messageSaves.remove(reactedMessageSave.message.getId());
			reactedMessageSave.message.delete().queue();
			ArrayList<String> bigList = new ArrayList<String>();
			for(MovieWatcher i : Driver.watchersMap.values()) {
				for(String j : i.movies) {
					bigList.add(j);
				}
			}
			int ran = new Random().nextInt(bigList.size());
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Movie:").setColor(Color.BLACK);
			eb.addField("",bigList.get(ran),true);
			Message sent = reactedMessageSave.message.getChannel().sendMessage(eb.build()).complete();
			Driver.messageSaves.put(sent.getId(),new ReactedMessageSave(sent,reactedMessageSave.user,null,false));
			sent.addReaction(event.getJDA().getGuildById(Driver.botTestingId).getEmoteById("860230762002907136")).queue();
		}
	}
	
	private void logReaction() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		System.out.printf("[%s](%s,%s)%s: Reaction = %s\n", dtf.format(LocalDateTime.now()),
				event.getGuild().getName(),event.getChannel().getName(),
				event.getMember().getEffectiveName(),event.getReactionEmote().getName());
	}
	
	private boolean isListUpdate() {
		String id = event.getReactionEmote().getEmote().getId();
		return (id.equals("716468511806849067") || id.equals("716467615056265236") || 
				id.equals("716467864584060960") || id.equals("716467958838460437") ||
				id.equals("716468491359617104"));
	}
	
}