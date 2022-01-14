package DataSaves;
import java.util.ArrayList;

public class MovieWatcher {

	public ArrayList<String> movies;
	public final int maxMovies = 75;
	public String id;
	public String userName;
	
	public MovieWatcher(String id,String userName) {
		this.id = id;
		this.userName = userName;
		movies = new ArrayList<String>();
	}
	
	public MovieWatcher(String id, String userName, ArrayList<String> movies) {
		this.id = id;
		this.userName = userName;
		this.movies = movies;
	}
	
	public boolean addMovie(String title) {
		if(movies.size() >= maxMovies) {
			return false;
		}
		movies.add(title);
		return true;
	}
	
	public boolean removeMovie(String title) {
		boolean wasRemoved = false;
		for(int i = 0;i < movies.size();i++) {
			if(movies.get(i).toLowerCase().equals(title.toLowerCase())) {
				movies.remove(title);
				wasRemoved = true;
			}
		}
		return wasRemoved;
	}
	
}