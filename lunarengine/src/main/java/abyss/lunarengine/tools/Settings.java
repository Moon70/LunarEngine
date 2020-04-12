package abyss.lunarengine.tools;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Settings {
	private Properties properties;
	private File fileProperties;
	private String nameProperties;
	
	public Settings(File properties,String nameProperties){
		this.fileProperties=properties;
		this.nameProperties=nameProperties;
		loadSettings();
	}
	
	public void saveSettings() throws IOException {
		FileWriter fileWriter=new FileWriter(fileProperties);
		try {
			properties.store(fileWriter,nameProperties);
		} finally{
			fileWriter.flush();
			fileWriter.close();
		}
	}
	
	private void loadSettings(){
		properties=new Properties();
		File file=fileProperties;
		if(!file.exists()) {
			return;
		}
		FileReader fileReader=null;
		try {
			fileReader=new FileReader(file);
			properties.load(fileReader);
		} catch (IOException e) {
			throw new RuntimeException("Error loading settings",e);
		} finally {
			if(fileReader!=null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					System.err.println("Error closing settings file");
					e.printStackTrace();
				}
			}
		}
	}
	
	public Rectangle getRectangle(String name, Rectangle defaultRectangle) {
		String s=properties.getProperty(name);
		if(s!=null) {
			String[] sa=s.split(",");
			if(sa.length==4) {
				return new Rectangle(Integer.parseInt(sa[0]),Integer.parseInt(sa[1]),Integer.parseInt(sa[2]),Integer.parseInt(sa[3]));
			}
		}
		setRectangle(name,defaultRectangle);
		return defaultRectangle;
	}
	
	public void setRectangle(String name, Rectangle rectangle) {
		properties.setProperty(name,""+rectangle.x+","+rectangle.y+","+rectangle.width+","+rectangle.height);
	}
	
	public void set(String name, String string) {
		properties.setProperty(name,string);
	}
	
	public String getString(String name) {
		return properties.getProperty(name);
	}
}
