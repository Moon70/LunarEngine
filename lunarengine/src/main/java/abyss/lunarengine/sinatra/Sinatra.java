package abyss.lunarengine.sinatra;

public class Sinatra{
	private int wave[];
	private int waveLen;
	private int surfIndex;
	private int surfDeltaIndex;
	private int newIndex;
	
	public final static int PLAYMODE_ONCE=1;
	public final static int PLAYMODE_REPEAT=2;
	public final static int PLAYMODE_PINGPONG=3;
	public int playmode=PLAYMODE_REPEAT;
	private boolean ping=true;
	
	private Sinatra(){};
	
	public static Sinatra getInstance(double[] parameter){
		Sinatra result=new Sinatra();
		result.waveLen=(int)parameter[0];
		result.wave=result.calc(parameter);
		return result;
	}

	private int[] calc(double[] parameter){
		int parameterIndex=0;
		int wavesize=(int)parameter[parameterIndex++];
		int wavecount=(int)parameter[parameterIndex++];

		SinusWave[] sinusWaves=new SinusWave[wavecount];

		for(int i=0;i<wavecount;i++){
			sinusWaves[i]=new SinusWave();
			sinusWaves[i].setRadius(parameter[parameterIndex++]);
			sinusWaves[i].setAngleStep(parameter[parameterIndex++]);
			sinusWaves[i].setAngleStart(parameter[parameterIndex++]);
			sinusWaves[i].setAngleEnd(parameter[parameterIndex++]);
		}

		int wave[]=new int[wavesize];
		for(int i=0;i<wavesize;i++){
			double wert=0;
			for(int w=0;w<wavecount;w++){
				wert+=sinusWaves[w].getNextValue();
			}
			wave[i]=(int)(wert+0.5);
		}
		return wave;
	}

	public void move(int delta){
		newIndex+=delta;
		if(newIndex>=waveLen) {
			newIndex-=waveLen;
		}
		if(newIndex<0) {
			newIndex+=waveLen;
		}
	}
	
	public void beginRender() {
		surfIndex=newIndex;
	}

	public int getCurrentValue(int index) {
		return wave[index];
	}
	
	public int getNextValue(int delta){
		switch(playmode) {
		case PLAYMODE_ONCE:
			surfIndex+=delta;
			if(surfIndex>=waveLen) {
				surfIndex=waveLen-1;
			}
			if(surfIndex<0) {
				surfIndex=0;
			}
			break;
		case PLAYMODE_REPEAT:
			surfIndex+=delta;
			if(surfIndex>=waveLen) {
				surfIndex-=waveLen;
			}
			if(surfIndex<0) {
				surfIndex+=waveLen;
			}
			break;
		case PLAYMODE_PINGPONG:
			if(ping) {
				surfIndex+=delta;
			}else {
				surfIndex-=delta;
			}
			if(surfIndex>=waveLen) {
				surfIndex=waveLen-1;
				ping=!ping;
			}
			if(surfIndex<0) {
				surfIndex=0;
				ping=!ping;
			}
			break;
		}
		surfDeltaIndex=surfIndex;
		return wave[surfIndex];
	}

	public int getNextDeltaValue(int delta){
		surfDeltaIndex+=delta;
		if(surfDeltaIndex<0) {
			surfDeltaIndex+=waveLen;
		}else if(surfDeltaIndex>=waveLen) {
			surfDeltaIndex-=waveLen;
		}
		return wave[surfDeltaIndex];
	}

	public int getWaveLen(){
		return waveLen;
	}

	public void setIndex(int index) {
		surfIndex=index;
	}

	public int getIndex() {
		return surfIndex;
	}

	public void setDeltaIndex(int deltaIndex) {
		surfDeltaIndex=deltaIndex;
	}
	
	public void resetDeltaIndex() {
		surfDeltaIndex=surfIndex;
	}
	
	public int getDeltaIndex() {
		return surfDeltaIndex;
	}

	public Sinatra clone() {
		Sinatra sinatra=new Sinatra();
		sinatra.wave=wave.clone();
		sinatra.waveLen=waveLen;
		sinatra.playmode=playmode;
		return sinatra;
	}
}
