package abyss.lunarengine.tools;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Simple Script Kiddy Protection Filter Input Stream.
 * <br>Sometimes, files like readable game-leveldata (property files) should be made unreadable, to prevent manipulation by 'script kiddies' to avoid cheating.
 * <br>Its nothing but a simple EOR-protection.
 */
public class SSKPFilterInputStream extends FilterInputStream {
	private final byte[] ba="noitcetorPyddiKtpircSelpmiS".getBytes(Charset.forName("UTF-8"));
	private int index=-1;
	private InputStream inputStream;
	private int i;

	public SSKPFilterInputStream(InputStream in) {
		super(in);
		this.inputStream=in;
	}

	@Override
	public int read() throws IOException{
		if((i=inputStream.read())==-1){
			return -1;
		}
		return (sscp((byte)i)&255);
	}

	@Override
	public int read(byte[] b) throws IOException{
		int count=inputStream.read(b);
		for(i=0;i<count;i++){
			b[i]=sscp(b[i]);
		}
		return count;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException{
		int count=inputStream.read(b,off,len);
		for(i=off;i<off+count;i++){
			b[i]=sscp(b[i]);
		}
		return count;
	}

	private byte sscp(byte b){
		if(++index==ba.length){
			index=0;
		}
		return (byte)(b^ba[index]);
	}

}
