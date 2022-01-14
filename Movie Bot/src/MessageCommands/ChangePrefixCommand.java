package MessageCommands;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Bot.*;
import Interfaces.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChangePrefixCommand implements Command{

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

	public ChangePrefixCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}
	
	@Override
	public void run() {
		getInfo();
		String newPrefix = msg.substring(m.group(0).length(),msg.length());
		if(newPrefix.length() <= 0) {
			channel.sendMessage("Invalid prefix").queue();
			return;
		}
		Driver.prefix = Pattern.compile(newPrefix);
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("New Prefix:").setColor(Color.gray);
		eb.addField("", newPrefix, false);
		channel.sendMessage(eb.build()).queue();
		savePrefix();
		Driver.jdaMaker.getJda().getPresence().setActivity(Activity.playing(Driver.prefix.toString() + "help"));
	}

	private void savePrefix() {
		try {
			FileWriter file = new FileWriter(Driver.location + "startup.txt",false);
			file.write(Driver.prefix.toString() + "\r\n" + Driver.token + "\r\n" + Driver.website);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "changeprefix ",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "changePrefix [new prefix]";
	}

	@Override
	public String getDescription() {
		return "changes the prefix for bot commands";
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