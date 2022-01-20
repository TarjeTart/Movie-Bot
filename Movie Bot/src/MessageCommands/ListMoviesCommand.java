package MessageCommands;

import java.awt.Color;
import java.util.List;
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

public class ListMoviesCommand implements Command{

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

	public ListMoviesCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}
	
	@Override
	public void run() {
		getInfo();
		List<Member> mentions = message.getMentionedMembers();
		User listUser = mentions.size()==0 ? user : mentions.get(0).getUser();
		MovieWatcher watcher = Driver.watchersMap.get(listUser.getId());
		if(watcher == null) {
			channel.sendMessage("This user does not have a movie list").queue();
			return;
		}
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(listUser.getName() + "'s Movie List:").setThumbnail(listUser.getAvatarUrl()).setColor(Color.BLUE);
		for(int i = 0; i < (watcher.movies.size()<15 ? watcher.movies.size() : 15); i++) {
			eb.addField(String.valueOf(i+1) + ".",watcher.movies.get(i),true);
		}
		Message botMessage = channel.sendMessage(eb.build()).complete();
		Guild myGuild = jda.getGuildById(Driver.botTestingId);
		botMessage.addReaction(myGuild.getEmoteById("716468511806849067")).queue();//1
		botMessage.addReaction(myGuild.getEmoteById("716467615056265236")).queue();//2
		botMessage.addReaction(myGuild.getEmoteById("716467864584060960")).queue();//3
		botMessage.addReaction(myGuild.getEmoteById("716467958838460437")).queue();//4
		botMessage.addReaction(myGuild.getEmoteById("716468491359617104")).queue();//5
		Driver.messageSaves.put(botMessage.getId(),new ReactedMessageSave(botMessage,listUser,watcher,false));
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "list",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "list {@user}";
	}

	@Override
	public String getDescription() {
		return "returns users list {returns your list if no user specified}";
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