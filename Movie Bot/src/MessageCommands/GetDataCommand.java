package MessageCommands;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Bot.Driver;
import Interfaces.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GetDataCommand implements Command{

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

	public GetDataCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}
	
	@Override
	public void run() {
		event.getChannel().sendFile(new File(Driver.location + "MovieData.txt")).queue();
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "getdata",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return Driver.prefix.toString() + "getData";
	}

	@Override
	public String getDescription() {
		return "Sends the data for this bot";
	}

	@Override
	public void getInfo() {}

}