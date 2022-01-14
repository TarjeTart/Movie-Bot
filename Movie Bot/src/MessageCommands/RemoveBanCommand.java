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

public class RemoveBanCommand implements Command{

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

	public RemoveBanCommand(MessageReceivedEvent event) {
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
		if(!Driver.bannedUsers.remove(id)) {
			channel.sendMessage("User is not banned").queue();
			return;
		}
		saveBans();
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Ban Removed:").setColor(Color.red).setThumbnail(user2.getAvatarUrl());
		eb.addField("", user2.getName(), false);
		channel.sendMessage(eb.build()).queue();
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "unban",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix + "unban [@user]";
	}

	@Override
	public String getDescription() {
		return "removes user from banned users";
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