package MessageCommands;

import java.awt.Color;
import java.util.ArrayList;
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

public class AdminHelpCommand implements Command{

	private MessageReceivedEvent event;
	String msg;
	User user;
	Member member;
	Guild guild;
	JDA jda;
	TextChannel channel;
	Message message;
	ArrayList<Command> list = new ArrayList<Command>();
	Pattern p;
	Matcher m;

	public AdminHelpCommand(MessageReceivedEvent event, ArrayList<Command> list) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
		this.list = list;
	}
	
	@Override
	public void run() {
		getInfo();
		if(!user.getId().equals(Driver.myID)) {
			channel.sendMessage("You do not have access to this command").queue();
			return;
		}
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Admin Commands:").setColor(Color.GREEN);
		for(Command i : list) {
			eb.addField(i.getName(),i.getDescription(),false);
		}
		channel.sendMessage(eb.build()).queue();
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "adminhelp",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "adminHelp";
	}

	@Override
	public String getDescription() {
		return "gives a list of admin commands (only available to admins)";
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