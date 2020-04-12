package abyss.lunarengine.screenoptionsselector;

import java.util.Vector;

public class Model{
	private String windowtitle;
	private Vector<Resolution> resolutions=new Vector<Resolution>();
	private int selectedResolutionIndex;
	private boolean fullscreenExclusiveMode;
	private boolean zoom;
	private boolean launch;
	
	private int selectedPanel;
	
	public void setWindowTitle(String windowtitle) {
		this.windowtitle=windowtitle;
	}
	
	public String getWindowTitle(){
		return windowtitle;
	}
	
	public void setFullscreenExclusiveMode(boolean fullscreenExclusiveMode) {
		this.fullscreenExclusiveMode=fullscreenExclusiveMode;
	}

	public boolean isFullscreenExclusiveModeEnabled() {
		return fullscreenExclusiveMode;
	}
	
	public void setZoom(boolean zoom) {
		this.zoom=zoom;
	}

	public boolean isZoomEnabled() {
		return zoom;
	}
	
	public void setLaunch(boolean launch) {
		this.launch=launch;
	}

	public boolean isLaunchEnabled() {
		return launch;
	}
	
	public void setSelectedResolutionIndex(int selectedResolutionIndex) {
		this.selectedResolutionIndex=selectedResolutionIndex;
	}

	public int getSelectedResolutionIndex() {
		return selectedResolutionIndex;
	}
	
	public void addResolution(Resolution resolution) {
		if(resolution.isDefault()) {
			selectedResolutionIndex=resolutions.size();
		}
		resolutions.add(resolution);
	}
	
	public String[] getResolutionLables() {
		String[] sa=new String[resolutions.size()];
		Resolution resolution;
		for(int i=0;i<resolutions.size();i++) {
			resolution=resolutions.get(i);
			sa[i]=resolution+(resolution.isDefault()?" (recommended)":"");
		}
		return sa;
	}
	
	public int getSelectedPanel() {
		return selectedPanel;
	}

	public Resolution getSelectedResolution() {
		return resolutions.get(selectedResolutionIndex);
	}
	
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append(resolutions.get(selectedResolutionIndex));
		sb.append(", fem=");
		sb.append(fullscreenExclusiveMode);
		sb.append(", zoom=");
		sb.append(zoom);
		sb.append(", launch=");
		sb.append(launch);
		return sb.toString();
	}
	
}
