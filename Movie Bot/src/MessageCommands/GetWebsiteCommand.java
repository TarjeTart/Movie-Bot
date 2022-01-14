package MessageCommands;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
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

public class GetWebsiteCommand implements Command{

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

	public GetWebsiteCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}
	
	@Override
	public void run() {
		getInfo();
		EmbedBuilder eb = new EmbedBuilder();
		try {
			eb.setTitle("website",new URL(Driver.website).toString()).setColor(Color.cyan);
		} catch (MalformedURLException e) {
			channel.sendMessage("Error locating website").queue();
			e.printStackTrace();
		}
		channel.sendMessage(eb.build()).queue();
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "website",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "website";
	}

	@Override
	public String getDescription() {
		return "returns the website for movie watching";
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