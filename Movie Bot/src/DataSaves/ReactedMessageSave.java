package DataSaves;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class ReactedMessageSave {

	public Message message;
	public User user;
	public MovieWatcher watcher;
	public boolean specificWatcher;
	public String title;
	public String[] titles;
	
	public ReactedMessageSave(Message message,User user,MovieWatcher watcher, boolean specificWatcher) {
		this.message = message;
		this.user = user;
		this.watcher = watcher;
		this.specificWatcher = specificWatcher;
	}
	
	public ReactedMessageSave(Message message,User user,MovieWatcher watcher, boolean specificWatcher, String title) {
		this.message = message;
		this.user = user;
		this.watcher = watcher;
		this.specificWatcher = specificWatcher;
		this.title = title;
	}
	
	public ReactedMessageSave(Message message,User user,MovieWatcher watcher, boolean specificWatcher, String[] titles) {
		this.message = message;
		this.user = user;
		this.watcher = watcher;
		this.specificWatcher = specificWatcher;
		this.titles = titles;
	}
	
}