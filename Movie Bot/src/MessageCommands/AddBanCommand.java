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

public class AddBanCommand implements Command{

	private MessageReceivedEvent event;
	private String msg;
	User user;
	Member member;
	Guild guild;
	JDA jda;
	TextChannel channel;
	Message message;
	Pattern p;
	Matcher m;

	public AddBanCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}
	
	@Override
	public void run() {
		getInfo();
		boolean isNotValid = message.getMentionedUsers().size() == 0;
		boolean isMe = member.getId().equals(Driver.myID);
		if(!isMe) {
			channel.sendMessage("You do not have access to this command").queue();
			return;
		}
		if(isNotValid) {
			channel.sendMessage("No mentioned user").queue();
			return;
		}
		User user2 = message.getMentionedUsers().get(0);
		String id = message.getMentionedUsers().get(0).getId();
		for(String i : Driver.bannedUsers) {
			if(i.equals(id)) {
				channel.sendMessage("This users is already banned").queue();
				return;
			}
		}
		Driver.bannedUsers.add(id);
		saveBans();
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Ban Added:").setColor(Color.red).setThumbnail(user2.getAvatarUrl());
		eb.addField("", user2.getName(), false);
		channel.sendMessage(eb.build()).queue();
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "ban ",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "ban [@user]";
	}

	@Override
	public String getDescription() {
		return "adds user to banned users";
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
	
	private void saveBans() {
		try {
			if(Driver.bannedUsers.size() == 0) {
				FileWriter file = new FileWriter(Driver.location + "bans.txt",false);
				file.write("");
				file.close();
				return;
			}
			FileWriter file = new FileWriter(Driver.location + "bans.txt",false);
			for(int i = 0; i < Driver.bannedUsers.size()-1;i++) {
				file.write(i + "\r\n");
			}
			file.write(Driver.bannedUsers.get(Driver.bannedUsers.size()-1));
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}