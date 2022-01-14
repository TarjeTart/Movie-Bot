package MessageCommands;

import java.awt.Color;
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

public class GetVersionCommand implements Command{

	private MessageReceivedEvent event;
	String msg;
	User user;
	Member member;
	Guild guild;
	JDA jda;
	TextChannel channel;
	Message message;
	private Pattern p;
	private Matcher m;

	public GetVersionCommand(MessageReceivedEvent event) {
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
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Version:").setColor(Color.red);
		eb.addField("", Driver.version, false);
		channel.sendMessage(eb.build()).queue();
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "version",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "version";
	}

	@Override
	public String getDescription() {
		return "returns version of bot";
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