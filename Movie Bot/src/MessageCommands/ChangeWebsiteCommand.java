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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChangeWebsiteCommand implements Command{

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

	public ChangeWebsiteCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}
	
	@Override
	public void run() {
		getInfo();
		if(!user.getId().equals(Driver.myID)) {
			channel.sendMessage("You do not have access to this command").queue();
			return;
		}
		String website = msg.substring(m.group(0).length(),msg.length());
		if(website.startsWith("https://"))
			Driver.website = website;
		else
			Driver.website = "https://" + website;
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Website Set:").setColor(Color.red);
		eb.addField("", website, false);
		channel.sendMessage(eb.build()).queue();
		saveWebsite();
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "changewebsite ",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "changeWebsite [url]";
	}

	@Override
	public String getDescription() {
		return "changes the webiste for movie streaming";
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
	
	private void saveWebsite() {
		try {
			FileWriter file = new FileWriter(Driver.location + "startup.txt",false);
			file.write(Driver.prefix+ "\r\n" + Driver.token + "\r\n" + Driver.website);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}