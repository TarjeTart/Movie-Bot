package MessageCommands;
import java.awt.Color;
import java.util.ArrayList;

import Interfaces.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand {

	public HelpCommand(ArrayList<Command> commands, MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Commands").setColor(Color.yellow);
		for(Command i : commands) {
			eb.addField(i.getName(),i.getDescription(),false);
		}
		event.getChannel().sendMessage(eb.build()).queue();
	}
	
}