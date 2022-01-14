package MessageCommands;

import java.awt.Color;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Bot.Driver;
import Interfaces.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChooseFromCommand implements Command{

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

	public ChooseFromCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}
	
	@Override
	public void run() {
		getInfo();
		String list = msg.substring(m.group(0).length(),msg.length()).replace("}","");
		String[] movies = list.split(",");
		int ran = new Random().nextInt(movies.length);
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Result:").setColor(Color.ORANGE).setThumbnail(user.getAvatarUrl());
		eb.addField("",movies[ran], false);
		channel.sendMessage(eb.build()).queue();
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "choosefrom \\{",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "chooseFrom {movie1,movie2,etc.}";
	}

	@Override
	public String getDescription() {
		return "chooses between listed movies";
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