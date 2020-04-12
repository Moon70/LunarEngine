package abyss.lunarengine.screenoptionsselector;

public class DataService {
	private static DataService instance;
	private Model model;

	private DataService(){
		model=new Model();
	}

	public static DataService getInstance(){
		if(instance==null) {
			instance=new DataService();
		}
		return instance;
	}
	
	public Model getModel() {
		return model;
	}
	
}
