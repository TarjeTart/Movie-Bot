package MessageCommands;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Bot.Driver;
import Interfaces.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MoviePingsCommand implements Command{
	
	private MessageReceivedEvent event;
	String msg;
	User user;
	Member member;
	Guild guild;
	JDA jda;
	TextChannel channel;
	private Pattern p;
	private Matcher m;

	public MoviePingsCommand(MessageReceivedEvent event) {
		this.event = event;
		msg = event.getMessage().getContentDisplay();
	}

	@Override
	public void run() {
		getInfo();
		boolean hasRole = false;
		List<Role> roles = member.getRoles();
		for(Role i : roles) {
			if(i.getId().equals(Driver.moviePingsId)) {
				hasRole = true;
			}
		}
		if(hasRole) {
			channel.sendMessage("You already have this role").queue();
		}else {
			guild.addRoleToMember(member, guild.getRoleById(Driver.moviePingsId)).queue();
			channel.sendMessage("Role has been added succesfully").queue();
		}
	}

	@Override
	public boolean check() {
		p = Pattern.compile(Driver.prefix.toString() + "moviepings",Pattern.CASE_INSENSITIVE);
		m = p.matcher(msg);
		return m.find();
	}

	@Override
	public String getName() {
		return Driver.prefix.toString() + "moviepings";
	}

	@Override
	public String getDescription() {
		return "gives you the movie pings role";
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