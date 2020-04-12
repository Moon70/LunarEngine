package abyss.lunarengine.gfx;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import abyss.lunarengine.APart;
import abyss.lunarengine.LunarEngineTools;
import abyss.lunarengine.tools.SSKPFilterInputStream;

public class BobFactory {
	public static final String PROPERTYNAME_IMAGENAME="Name";
	public static final String PROPERTYNAME_ANIMTYPE="AnimType";
	public static final String PROPERTYNAME_ANIMSPEED="AnimSpeed";

	public static Bob createBob(Class<?> clazz,String propertyfilename){
		InputStream inputStream=clazz.getResourceAsStream(propertyfilename);
		if(inputStream!=null) {
			return loadBobFromFolder(inputStream,propertyfilename);
		}
		String bobfilename=propertyfilename.substring(0, propertyfilename.lastIndexOf('/'))+".bob";
		inputStream=clazz.getResourceAsStream(bobfilename);
		if(inputStream!=null) {
			return loadBobFromBobfile(inputStream,propertyfilename);
		}
		bobfilename=propertyfilename.substring(0, propertyfilename.lastIndexOf('/'))+".zip";
		inputStream=clazz.getResourceAsStream(bobfilename);
		if(inputStream!=null) {
			return loadBobFromBobfile(inputStream,propertyfilename);
		}
		bobfilename=propertyfilename.substring(0, propertyfilename.lastIndexOf('/'))+".sskp";
		inputStream=clazz.getResourceAsStream(bobfilename);
		if(inputStream!=null) {
			return loadBobFromBobfile(new SSKPFilterInputStream(inputStream),propertyfilename);
		}
		throw new RuntimeException("Bob not found: "+bobfilename);
	}

	private static Bob loadBobFromFolder(InputStream inputStream,String propertyfilename) {
		try {
			Properties properties=new Properties();
			properties.load(inputStream);
			String name=properties.getProperty(BobFactory.PROPERTYNAME_IMAGENAME);
			String animtype=properties.getProperty(BobFactory.PROPERTYNAME_ANIMTYPE);
			String animspeed=properties.getProperty(BobFactory.PROPERTYNAME_ANIMSPEED);
			String bobFileName=propertyfilename.substring(0, propertyfilename.length()-"bob.properties".length());
			Vector<String> vecImageFilenames=getBobAnimImageFilenames(bobFileName.substring(1)+name);
			Bob bob=loadBobAnim(vecImageFilenames);
			bob.setAnimType(animtype);
			if(animspeed!=null) {
				bob.animdelay=Integer.parseInt(animspeed);
			}
			return bob;
		} catch (IOException e) {
			throw new RuntimeException("Error loading bob from folder",e);
		}
	}
	
	private static Vector<String> getBobAnimImageFilenames(String imagename){
		Vector<String> vecImageFilenames=new Vector<String>();
		int index=0;
		while(index<100){
			index++;
			String imagefilename=imagename+(index<10?"0":"")+index+".png";
			if(LunarEngineTools.isResourceAvailable(APart.jFrame,imagefilename)){
				vecImageFilenames.add(imagefilename);
			}else{
				if(index==1){
					throw new RuntimeException("Resource file >"+imagefilename+"< not found");
				}
				break;
			}
		}
		return vecImageFilenames;
	}
	
	private static Bob loadBobAnim(Vector<String> vecImageFilenames){
		Image image=LunarEngineTools.createImage(APart.jFrame,vecImageFilenames.get(0));
		int x=image.getWidth(null);
		int y=image.getHeight(null);
		Bob bob=new Bob(vecImageFilenames.size(),x,y);
		for(int i=0;i<vecImageFilenames.size();i++){
			image=LunarEngineTools.createImage(APart.jFrame,vecImageFilenames.get(i));
			BufferedImage bufferedImageBob=new BufferedImage(x,y,BufferedImage.TYPE_INT_ARGB);
			int[] bobdata=((DataBufferInt)bufferedImageBob.getRaster().getDataBuffer()).getData();
			bufferedImageBob.getGraphics().drawImage(image,0,0,APart.jFrame);
			for(int j=0;j<bob.bobdata[i].length;j++){
				bob.bobdata[i][j]=bobdata[j];
			}
		}
		return bob;
	}

	private static Bob loadBobFromBobfile(InputStream inputStream,String propertyfilename) {
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
					properties.load(new ByteArrayInputStream(ba));
				}else {
					files.put(name,ba);
				}
			}
			zip.close();
			String name=properties.getProperty(BobFactory.PROPERTYNAME_IMAGENAME).toLowerCase();
			String animtype=properties.getProperty(BobFactory.PROPERTYNAME_ANIMTYPE);
			String animspeed=properties.getProperty(BobFactory.PROPERTYNAME_ANIMSPEED);
			Image[] images=new Image[files.size()];
			MediaTracker mediaTracker = new MediaTracker(APart.jFrame);
			for(int i=0;i<files.size();i++) {
				String imagename=name+(i<9?"0"+(i+1):i+1)+".png";
				images[i]=Toolkit.getDefaultToolkit().createImage(files.get(imagename));
				mediaTracker.addImage(images[i],0);
			}
			try{
				mediaTracker.waitForAll();
			}catch (InterruptedException e){
				throw new RuntimeException("mediatracker",e);
			}
			int x=images[0].getWidth(null);
			int y=images[0].getHeight(null);
			Bob bob=new Bob(files.size(),x,y);
			for(int i=0;i<files.size();i++){
				BufferedImage bufferedImageBob=new BufferedImage(x,y,BufferedImage.TYPE_INT_ARGB);
				int[] bobdata=((DataBufferInt)bufferedImageBob.getRaster().getDataBuffer()).getData();
				bufferedImageBob.getGraphics().drawImage(images[i],0,0,APart.jFrame);
				for(int j=0;j<bob.bobdata[i].length;j++){
					bob.bobdata[i][j]=bobdata[j];
				}
			}
			bob.setAnimType(animtype);
			if(animspeed!=null) {
				bob.animdelay=Integer.parseInt(animspeed);
			}
			return bob;
		} catch (Exception e) {
			throw new RuntimeException("Error loading bob from bobfile",e);
		}
	}

}
