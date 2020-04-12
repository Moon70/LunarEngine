package abyss.lunarengine.sinatra;

public class SinusWave {
	private double radius;
	private double angleStep;
	private double angleStart;
	private double angleEnd;

	private double currentAngle;
	private double currentValue;
	private boolean firstValueRead;

	private static final double DEG2RAD=Math.PI/180.0;
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getAngleStep() {
		return angleStep;
	}

	public void setAngleStep(double angleStep) {
		this.angleStep = angleStep;
	}

	public double getAngleStart() {
		return angleStart;
	}

	public void setAngleStart(double angleStart) {
		this.angleStart = angleStart;
	}

	public double getAngleEnd() {
		return angleEnd;
	}

	public void setAngleEnd(double angleEnd) {
		this.angleEnd = angleEnd;
	}

	public int getWaveLength() {
		return angleStep==0?1:(int)((angleEnd-angleStart)/angleStep);
	}

	public double getNextValue() {
		if(!firstValueRead) {
			firstValueRead=true;
			currentAngle=angleStart;
		}
		currentValue=radius*Math.sin(currentAngle*DEG2RAD);

		currentAngle+=angleStep;
		if(currentAngle>=angleEnd) {
			currentAngle-=(angleEnd-angleStart);
		}
		return currentValue;
	}

	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("r="+radius);
		sb.append(", angleStep="+angleStep);
		sb.append(", angleStart="+angleStart);
		sb.append(", angleEnd="+angleEnd);
		return sb.toString();
	}

}
