package abyss.lunarengine.gfx;

import abyss.lunarengine.Screen;

public class Bob{
	public static final int ANIMTYPE_NOANIM=0;
	public static final int ANIMTYPE_FORWARD=1;
	public static final int ANIMTYPE_FORWARDLOOP=2;
	public static final int ANIMTYPE_BACKWARD=3;
	public static final int ANIMTYPE_BACKWARDLOOP=4;
	public static final int ANIMTYPE_PINGPONG=5;
	private static final int ANIMTYPE_PING=6;
	private static final int ANIMTYPE_PONG=7;

	public static final int SHIFT=12;
	public static int[] screendataToWork;
	private static int clipX1=0;
	private static int clipY1=0;
	private static int clipX2=0;
	private static int clipY2=0;

	public boolean enabled=false;
	public int[][] bobdata;
	public int bobSizeX;
	public int bobSizeY;
	public int bobSizeXshifted;
	public int bobSizeYshifted;
	public int bobHalfSizeXshifted;
	public int bobHalfSizeYshifted;
	public int posXshifted;
	public int posYshifted;

	public int frame;
	public int frameAsync;
	public int animdelay=5;
	public int animPos=animdelay;
	public int animType;

	private Bob() {}
	
	@Deprecated
	public Bob(Bob bob) {
		setSize(bob.bobSizeX,bob.bobSizeY);
		this.bobdata=bob.bobdata;
		this.animType=bob.animType;
		this.animdelay=bob.animdelay;
	}
	
	public Bob(int frames,int width,int height){
		setSize(width, height);
		if(clipX1+clipY1+clipX2+clipY2==0){
			Bob.clipX1=0;
			Bob.clipY1=0;
			Bob.clipX2=Screen.screenSizeX;
			Bob.clipY2=Screen.screenSizeY;
		}
		bobdata=new int[frames][bobSizeX*bobSizeY];
	}

	public static void setClippingArea(int x1,int y1,int x2,int y2){
		Bob.clipX1=x1;
		Bob.clipY1=y1;
		Bob.clipX2=x2;
		Bob.clipY2=y2;
	}

	public void vbi(){
		if(enabled) {
			if(--animPos>0){
				return;
			}
			animPos=animdelay;
			switch(animType){
			case ANIMTYPE_NOANIM:
				break;
			case ANIMTYPE_FORWARD:
				if(++frameAsync==bobdata.length){
					frameAsync=0;
					enabled=false;
				}
				frame=frameAsync;
				break;
			case ANIMTYPE_FORWARDLOOP:
				if(++frameAsync==bobdata.length){
					frameAsync=0;
				}
				frame=frameAsync;
				break;
			case ANIMTYPE_BACKWARD:
				if(--frameAsync==-1){
					frameAsync=bobdata.length-1;
					enabled=false;
				}
				frame=frameAsync;
				break;
			case ANIMTYPE_BACKWARDLOOP:
				if(--frameAsync==-1){
					frameAsync=bobdata.length-1;
				}
				frame=frameAsync;
				break;
			case ANIMTYPE_PING:
				if(++frameAsync==bobdata.length){
					frameAsync-=2;
					animType=ANIMTYPE_PONG;
				}
				frame=frameAsync;
				break;
			case ANIMTYPE_PONG:
				if(--frameAsync==-1){
					frameAsync+=2;
					animType=ANIMTYPE_PING;
				}
				frame=frameAsync;
				break;
			}
		}
	}

	public void render(){
		render(posXshifted >> SHIFT,posYshifted >> SHIFT);
	}

	public void render(int posX,int posY){
		if(enabled){
			int pixel;
			int startX=	posX<0?					-posX:					0;
			int endX=	posX+bobSizeX>clipX2?	clipX2-posX:			bobSizeX;
			int startY=	posY<0?					-posY:					0;
			int endY=	posY+bobSizeY>clipY2?	clipY2-posY+posY:		bobSizeY+posY;
	
			int offsetBob=startY*bobSizeX;
			startY+=posY;
			int offset=startY*clipX2+posX;
			int x;
			for(int y=startY;y<endY;y++,offsetBob+=bobSizeX,offset+=clipX2){
				for(x=startX;x<endX;x++){
					pixel=bobdata[frame][offsetBob+x];
					if(pixel<0){
						screendataToWork[offset+x]=pixel;
					}
				}
			}
		}
	}

	public void setAnimType(String animType){
		if(animType.length()=="NOANIM".length() && animType.equalsIgnoreCase("NOANIM")){
			setAnimType(ANIMTYPE_NOANIM);
		}else if(animType.length()=="FORWARD".length() && animType.equalsIgnoreCase("FORWARD")){
			setAnimType(ANIMTYPE_FORWARD);
		}else if(animType.length()=="FORWARDLOOP".length() && animType.equalsIgnoreCase("FORWARDLOOP")){
			setAnimType(ANIMTYPE_FORWARDLOOP);
		}else if(animType.length()=="BACKWARD".length() && animType.equalsIgnoreCase("BACKWARD")){
			setAnimType(ANIMTYPE_BACKWARD);
		}else if(animType.length()=="BACKWARDLOOP".length() && animType.equalsIgnoreCase("BACKWARDLOOP")){
			setAnimType(ANIMTYPE_BACKWARDLOOP);
		}else if(animType.length()=="PINGPONG".length() && animType.equalsIgnoreCase("PINGPONG")){
			setAnimType(ANIMTYPE_PINGPONG);
		}else{
			throw new RuntimeException("Illegal anim type: "+animType);
		}
	}

	public void setAnimType(int animType){
		this.animType=animType;
		if(animType==ANIMTYPE_PINGPONG){
			this.animType=ANIMTYPE_PING;
		}
	}

	public void setFrame(int frame) {
		this.frame=frame;
	}
	
	public void setAnimDelay(int animDelay) {
		this.animdelay=animDelay;
	}
	
	public int getPosX(){
		return posXshifted>>SHIFT;
	}

	public int getPosY(){
		return posYshifted>>SHIFT;
	}

	private void setSize(int width,int height) {
		this.bobSizeX=width;
		this.bobSizeXshifted=width<<SHIFT;
		this.bobHalfSizeXshifted=this.bobSizeXshifted>>>1;
		this.bobSizeY=height;
		this.bobSizeYshifted=height<<SHIFT;
		this.bobHalfSizeYshifted=bobSizeYshifted>>>1;
	}
	
	public void setPos(int posX,int posY){
		this.posXshifted=posX<<SHIFT;
		this.posYshifted=posY<<SHIFT;
	}

	@Override
	public Bob clone() {
		Bob newbob=new Bob();
		newbob.setSize(this.bobSizeX, this.bobSizeY);
		newbob.bobdata=this.bobdata;
		newbob.animType=this.animType;
		newbob.animdelay=this.animdelay;
		return newbob;
	}
	
}
