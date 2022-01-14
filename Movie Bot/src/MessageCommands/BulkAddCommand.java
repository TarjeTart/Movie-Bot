package MessageCommands;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Bot.Driver;
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

public class BulkAddCommand implements Command{

	private MessageReceivedEvent event;
	private String msg;
	User user;
	Member member;
	Guild guild;
	JDA jda;
	TextChannel channel;
	Pattern p;
	Matcher m;

	public BulkAddCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}
	
	@Override
	public void run() {
		getInfo();
		if(user.getId().equals("371133287877312512"))
			return;
		String list = msg.replace(Driver.prefix + "bulkAdd {", "").replace("}", "");
		String[] toAdd = list.split(",");
		MovieWatcher watcher = Driver.watchersMap.get(user.getId());
		if(watcher == null) {
			watcher = new MovieWatcher(user.getId(),user.getName());
			Driver.watchersMap.put(user.getId(),watcher);
		}
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Movies Added:").setColor(Color.magenta).setThumbnail(user.getAvatarUrl());
		for(String i : toAdd) {
			eb.addField("", i, true);
		}
		Message sentMessage = channel.sendMessage(eb.build()).complete();
		sentMessage.addReaction(jda.getEmoteById("868608094480007188")).queue();//green check
		sentMessage.addReaction(jda.getEmoteById("868608251841888256")).queue();//red X
		
		Driver.messageSaves.put(sentMessage.getId(),new ReactedMessageSave(sentMessage,user,watcher,false,toAdd));
	}

	@Override
	public boolean check() {
		return msg.startsWith(Driver.prefix + "bulkAdd {");
	}

	@Override
	public String getName() {
		return Driver.prefix + "bulkAdd {movie1,movie2,etc...}";
	}

	@Override
	public String getDescription() {
		return "adds the list of movies to a users list";
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