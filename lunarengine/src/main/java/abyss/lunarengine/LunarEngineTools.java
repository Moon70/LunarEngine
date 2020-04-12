package abyss.lunarengine;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import abyss.lunarengine.tools.SSKPFilterInputStream;

public class LunarEngineTools{
	private static long timestamp;

	public static Image createImage(Component component,File fileImage){
		Image image=Toolkit.getDefaultToolkit().getImage(fileImage.getAbsolutePath());	
		MediaTracker mediaTracker = new MediaTracker(component);
		mediaTracker.addImage(image,0);
		try{
			mediaTracker.waitForAll();
		}catch (InterruptedException e){
			throw new RuntimeException("Resource >"+fileImage+"< could not be loaded!",e);
		}
		return image;
	}

	public static Image createImage(Component component,String relativeFilePathToImage){
		Image image=null;
		try {
			URL url = getResourceUrl(component,relativeFilePathToImage);
			if(url!=null) {
				image=Toolkit.getDefaultToolkit().getImage(url);
			}else{
				relativeFilePathToImage=relativeFilePathToImage.substring(0, relativeFilePathToImage.lastIndexOf('.'))+".sskp";
				url = getResourceUrl(component,relativeFilePathToImage);
				InputStream inputStream=new SSKPFilterInputStream(url.openStream());
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				byte[] buffer=new byte[1024];
				int len=0;
				while((len=inputStream.read(buffer))>0) {
					baos.write(buffer, 0, len);
				}
				inputStream.close();
				image=Toolkit.getDefaultToolkit().createImage(baos.toByteArray());
			}
			MediaTracker mediaTracker = new MediaTracker(component);
			mediaTracker.addImage(image,0);
			mediaTracker.waitForAll();
		} catch (Exception e) {
			throw new RuntimeException("Resource >"+relativeFilePathToImage+"< could not be loaded!",e);
		}
		return image;
	}

	public static URL getResourceUrl(Object object,String relativeFilePathToResource){
		ClassLoader classLoader=object.getClass().getClassLoader();
		URL url=classLoader.getResource(relativeFilePathToResource);
		return url;
	}

	public static boolean isResourceAvailable(Object object,String relativeFilePathToImage){
		return getResourceUrl(object,relativeFilePathToImage)!=null;
	}

	public static void sleep(long milliseconds){
		try{
			Thread.sleep(milliseconds);
		}catch(InterruptedException e){}
	}

	public static void measureTimeStart(){
		timestamp=System.currentTimeMillis();
	}

	public static long measureTimeStop(){
		return System.currentTimeMillis()-timestamp;
	}

}
