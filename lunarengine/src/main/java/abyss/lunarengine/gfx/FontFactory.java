package abyss.lunarengine.gfx;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import abyss.lunarengine.APart;
import abyss.lunarengine.LunarEngineTools;

public class FontFactory {
	public static final String PROPERTYNAME_CHARACTERS="Characters";
	public static final String PROPERTYNAME_HEIGHT="Height";
	public static final String PROPERTYNAME_FONTIMAGE="FontImage";
	public static final String PROPERTYPREFIX_DATA="Data";

	protected static void loadFont(Font font,String fontname) {
		String p=fontname+"font.properties";
		InputStream inputStream=font.getClass().getClassLoader().getResourceAsStream(p);
		if(inputStream!=null) {
			System.out.println("lff");
			loadFontFromFolder(font,inputStream,p);
			return;
		}
		
		p=fontname+".font";
		inputStream=font.getClass().getResourceAsStream(p);
		if(inputStream!=null) {
			loadFontFromFontfile(font,inputStream,fontname);
			return;
		}
		throw new RuntimeException("Font not found: "+fontname);
	}
	
	private static void loadFontFromFolder(Font font,InputStream inputStream,String propertyfilename) {
		try {
			Properties properties=new Properties();
			Reader reader=new InputStreamReader(inputStream,"UTF-8");
			properties.load(reader);
			processProperties(properties,font);
			
			String nameFontImage=properties.getProperty(PROPERTYNAME_FONTIMAGE);
			String fileFontImage=propertyfilename.substring(0, propertyfilename.length()-"font.properties".length())+nameFontImage;
			Image imageFont=LunarEngineTools.createImage(APart.jFrame,fileFontImage);
			font.fontWidth=imageFont.getWidth(null);
			BufferedImage bufferedImageFont=new BufferedImage(imageFont.getWidth(null),imageFont.getHeight(null),BufferedImage.TYPE_INT_RGB);
			bufferedImageFont.getGraphics().drawImage(imageFont,0,0,APart.jFrame);
			font.fontdata=((DataBufferInt)bufferedImageFont.getRaster().getDataBuffer()).getData();
			font.prepare();
		} catch (IOException e) {
			throw new RuntimeException("Error loading font from folder",e);
		}
	}
	
	private static void processProperties(Properties properties,Font font) {
		font.characterset=properties.getProperty(FontFactory.PROPERTYNAME_CHARACTERS);
		font.height=Integer.parseInt(properties.getProperty(FontFactory.PROPERTYNAME_HEIGHT));
		font.index=new int[font.characterset.length()][3];
		for(int i=0;i<font.characterset.length();i++) {
			String key=PROPERTYPREFIX_DATA+(i<10?"0":"")+i;
			String s=properties.getProperty(key);
			int data=Integer.parseInt(s,16);
			font.index[i][2]=data & 0x3ff;
			font.index[i][1]=(data>>10) & 0x3ff;
			font.index[i][0]=data>>20;
		}
	}
	
	private static void loadFontFromFontfile(Font font,InputStream inputStream,String propertyfilename) {
		HashMap<String,byte[]> files=new HashMap<String,byte[]>();
		try {
			Properties properties=new Properties();
			ZipInputStream zip=new ZipInputStream(inputStream);
			ZipEntry zipEntry=null;
			while((zipEntry=zip.getNextEntry())!=null){
				String name=zipEntry.getName().toLowerCase();
				byte[] ba=new byte[(int)zipEntry.getSize()];
				for(int index=0,len=ba.length;index<len;){
					index+=zip.read(ba,index,len-index);
				}
				if(name.endsWith(".properties")){
					Reader reader=new InputStreamReader(new ByteArrayInputStream(ba),"UTF-8");
					properties.load(reader);
				}else {
					files.put(name,ba);
				}
			}
			zip.close();
			
			processProperties(properties,font);
			
			Image imageFont;
			MediaTracker mediaTracker = new MediaTracker(APart.jFrame);
			String imagename="font.png";
			imageFont=Toolkit.getDefaultToolkit().createImage(files.get(imagename));
			mediaTracker.addImage(imageFont,0);
			try{
				mediaTracker.waitForAll();
			}catch (InterruptedException e){
				throw new RuntimeException("mediatracker",e);
			}
			font.fontWidth=imageFont.getWidth(null);
			BufferedImage bufferedImageFont=new BufferedImage(imageFont.getWidth(null),imageFont.getHeight(null),BufferedImage.TYPE_INT_RGB);
			bufferedImageFont.getGraphics().drawImage(imageFont,0,0,APart.jFrame);
			font.fontdata=((DataBufferInt)bufferedImageFont.getRaster().getDataBuffer()).getData();
			font.prepare();
		} catch (Exception e) {
			throw new RuntimeException("Error loading font",e);
		}
	}

}
