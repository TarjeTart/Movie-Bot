package MessageCommands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Bot.*;
import DataSaves.MovieWatcher;
import DataSaves.ReactedMessageSave;
import Interfaces.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GetMovieCommand implements Command{

	private MessageReceivedEvent event;
	String msg;
	User user;
	Member member;
	Guild guild;
	JDA jda;
	TextChannel channel;
	Message message;
	Pattern p;
	Matcher m;

	public GetMovieCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}
	
	@Override
	public void run() {
		getInfo();
		List<Member> mentions = message.getMentionedMembers();
		boolean allMovies = mentions.size()==0;
		User listUser = allMovies ? null : mentions.get(0).getUser();
		if(allMovies) {
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
			Message sent = channel.sendMessage(eb.build()).complete();
			Driver.messageSaves.put(sent.getId(),new ReactedMessageSave(sent,user,null,false));
			sent.addReaction(jda.getGuildById(Driver.botTestingId).getEmoteById("860230762002907136")).queue();
		}else {
			MovieWatcher watcher = Driver.watchersMap.get(listUser.getId());
			if(watcher == null) {
				channel.sendMessage("User does not have a movie list").queue();
				return;
			}
			int ran = new Random().nextInt(watcher.movies.size());
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Movie:").setColor(Color.BLACK);
			eb.addField(watcher.movies.get(ran),watcher.userName,true);
			Message sent = channel.sendMessage(eb.build()).complete();
			Driver.messageSaves.put(sent.getId(),new ReactedMessageSave(sent,user,watcher,true));
			sent.addReaction(jda.getGuildById(Driver.botTestingId).getEmoteById("860230762002907136")).queue();
		}
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "getmovie",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "getMovie {@user}";
	}

	@Override
	public String getDescription() {
		return "gets a random movie from specified users list {if no user is specified, all list are used}";
	}

	@Override
	public void getInfo() {
		user = event.getAuthor();
		guild = event.getGuild();
		jda = event.getJDA();
		member = jda.getGuildById(guild.getId()).retrieveMember(user).complete();
		channel = event.getTextChannel();
		message = event.getMessage();
	}

}