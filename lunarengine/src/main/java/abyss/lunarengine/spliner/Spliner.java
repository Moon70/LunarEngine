package abyss.lunarengine.spliner;

import abyss.lunarengine.gfx.Point2D;

public class Spliner{
	private Point2D[][] points;
	private int splineMode;
	private int size;
	private int surfIndex;
	private int surfIndexVbi;
	private int surfDeltaIndex;
	private Point2D currentPoint;
	private int waveIndex;
	private int waveIndexVbi;
	private int animFrames;
	private int moveMode=2;
	
	private Spliner(){};

	public static Spliner getInstance(int[] parameter){
		Spliner spliner=new Spliner();
		spliner.points=spliner.calc(parameter);
		return spliner;
	}

	private Point2D[][] calc(int[] parameter){
		int parameterIndex=0;
		int wavesize=0;
		splineMode=(int)parameter[parameterIndex++];
		if(splineMode>2) {
			splineMode=0;
		}
		int wavecount=(int)parameter[parameterIndex++];

		SplinerData[] splineData=new SplinerData[wavecount];

		for(int i=0;i<wavecount;i++){
			splineData[i]=new SplinerData();
			splineData[i].setSize(parameter[parameterIndex++]);
			splineData[i].setAx(parameter[parameterIndex++]);
			splineData[i].setAy(parameter[parameterIndex++]);
			splineData[i].setBx(parameter[parameterIndex++]);
			splineData[i].setBy(parameter[parameterIndex++]);
			splineData[i].setCx(parameter[parameterIndex++]);
			splineData[i].setCy(parameter[parameterIndex++]);
		}

		if(splineMode==2) {
			wavesize=0;
			for(int i=0;i<wavecount;i++){
				wavesize+=splineData[i].getSize();
			}
		}else {
			wavesize=splineData[0].getSize();
		}
		size=wavesize;
		
		if(splineMode==0 || splineMode==2) {
			this.animFrames=1;
		}else {
			this.animFrames=splineData[1].getSize();
			for(int i=2;i<wavecount;i++){
				if(this.animFrames<splineData[i].getSize()) {
					this.animFrames=splineData[i].getSize();
				}
			}
		}
		
		Point2D[][] wave=new Point2D[this.animFrames][wavesize];
		if(splineMode==0) {
			for(int i=0;i<wavesize;i++){
				wave[0][i]=splineData[0].getNextValue();
			}
		}else if(splineMode==1) {
			Point2D pointA;
			Point2D pointB;
			Point2D pointC;
			for(int w=0;w<this.animFrames;w++){
				pointA=splineData[1].getNextValue();
				pointB=splineData[2].getNextValue();
				pointC=splineData[3].getNextValue();
				
				splineData[0]=new SplinerData();
				splineData[0].setSize(wavesize);
				splineData[0].setAx(pointA.x);
				splineData[0].setAy(pointA.y);
				splineData[0].setBx(pointB.x);
				splineData[0].setBy(pointB.y);
				splineData[0].setCx(pointC.x);
				splineData[0].setCy(pointC.y);
				
				for(int i=0;i<wavesize;i++){
					wave[w][i]=splineData[0].getNextValue();
				}
			}
		}else if(splineMode==2) {
			int index=0;
			for(int i=0;i<wavecount;i++){
				for(int w=0;w<splineData[i].getSize();w++){
					wave[0][index++]=splineData[i].getNextValue();
				}
			}
		}else {
			throw new RuntimeException("Illegal spline mode");
		}
		return wave;
	}

	public Point2D move(int delta) {
		currentPoint=points[waveIndex][surfIndexVbi];
		surfIndexVbi+=delta;
		if(surfIndexVbi>=size) {
			if(moveMode==1) {
				surfIndexVbi-=size;
			}else if(moveMode==2) {
				surfIndexVbi=size-1;
			}
		}else if(surfIndexVbi<0) {
			if(moveMode==1) {
				surfIndexVbi+=size;
			}else if(moveMode==2) {
				surfIndexVbi=0;
			}
		}
		return currentPoint;
	}
	
	public void beginRender() {
		waveIndex=waveIndexVbi;
		surfIndex=surfIndexVbi;
	}
	
	public Point2D getNextValue(int delta){
		currentPoint=points[waveIndex][surfIndex];
		surfIndex+=delta;
		if(surfIndex>=size) {
			if(moveMode==1) {
				surfIndex-=size;
			}else if(moveMode==2) {
				surfIndex=size-1;
			}
		}else if(surfIndex<0) {
			if(moveMode==1) {
				surfIndex+=size;
			}else if(moveMode==2) {
				surfIndex=0;
			}
		}
		surfDeltaIndex=surfIndex;
		return currentPoint;
	}

	public Point2D getNextDeltaValue(int delta){
		surfDeltaIndex+=delta;
		if(surfDeltaIndex<0) {
			surfDeltaIndex+=size;
		}else if(surfDeltaIndex>=size) {
			surfDeltaIndex-=size;
		}
		return points[waveIndex][surfDeltaIndex];
	}
	
	public Point2D getNextWaveDeltaValue(int delta){
		return points[(waveIndex+delta)%animFrames][surfIndex];
	}

	public int getWaveLen(){
		return size;
	}

	public void setIndex(int index) {
		surfIndex=index;
	}

	public void gotoFirstValue() {
		surfIndex=0;
	}

	public void nextWave() {
		if(waveIndexVbi+1==animFrames) {
			waveIndexVbi=animFrames-1;
		}else {
			waveIndexVbi++;
		}
	}

}
