package abyss.lunarengine.spliner;

import abyss.lunarengine.gfx.Point2D;

public class SplinerData {
	private int size;
	private int ax;
	private int ay;
	private int bx;
	private int by;
	private int cx;
	private int cy;

	private boolean firstValueRead;

	private double cValueX;
	private double cValueY;
	private double cValueX12;
	private double cValueY12;
	private double cValueX23;
	private double cValueY23;
	private double deltaX12;
	private double deltaY12;
	private double deltaX23;
	private double deltaY23;
	private int currentPos;
	
	public int getAx() {
		return ax;
	}

	public void setAx(int ax) {
		this.ax = ax;
	}
	
	public int getAy() {
		return ay;
	}
	
	public void setAy(int ay) {
		this.ay = ay;
	}

	public int getBx() {
		return bx;
	}

	public void setBx(int bx) {
		this.bx = bx;
	}
	
	public int getBy() {
		return by;
	}
	
	public void setBy(int by) {
		this.by = by;
	}

	public int getCx() {
		return cx;
	}

	public void setCx(int cx) {
		this.cx = cx;
	}

	public int getCy() {
		return cy;
	}

	public void setCy(int cy) {
		this.cy = cy;
	}

	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Point2D getNextValue() {
		if(!firstValueRead) {
			firstValueRead=true;
			cValueX12=ax;
			cValueY12=ay;
			cValueX23=bx;
			cValueY23=by;
			deltaX12=(double)(bx-ax)/size;
			deltaY12=(double)(by-ay)/size;
			deltaX23=(double)(cx-bx)/size;
			deltaY23=(double)(cy-by)/size;
		}
		
		if(currentPos>=0 && currentPos<size) {
			currentPos++;
			cValueX12+=deltaX12;
			cValueY12+=deltaY12;
			cValueX23+=deltaX23;
			cValueY23+=deltaY23;
			
			double deltaX=(cValueX23-cValueX12)/size;
			double deltaY=(cValueY23-cValueY12)/size;
			cValueX=cValueX12+deltaX*currentPos;
			cValueY=cValueY12+deltaY*currentPos;

		}
		return new Point2D((int)(cValueX+0.5),(int)(cValueY+0.5));
	}

	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("SplineWave: size="+size);
		sb.append(", p1x="+ax);
		sb.append(", p1y="+ay);
		sb.append(", p2x="+bx);
		sb.append(", p2y="+by);
		sb.append(", p3x="+cx);
		sb.append(", p3y="+cy);
		return sb.toString();
	}

}
