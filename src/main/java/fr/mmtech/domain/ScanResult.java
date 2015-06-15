package fr.mmtech.domain;

public class ScanResult {
    private String videoPath;
    private String subPath;

    public String getVideoPath() {
	return videoPath;
    }

    public void setVideoPath(String videoPath) {
	this.videoPath = videoPath;
    }

    public String getSubPath() {
	return subPath;
    }

    public void setSubPath(String subPath) {
	this.subPath = subPath;
    }

    public ScanResult(String videoPath, String subPath) {
	super();
	this.videoPath = videoPath;
	this.subPath = subPath;
    }

}
