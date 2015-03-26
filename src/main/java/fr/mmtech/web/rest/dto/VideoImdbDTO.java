package fr.mmtech.web.rest.dto;

public class VideoImdbDTO {

    private String imdbId;
    private String title;
    private String year;
    private String type;
    private String rate;
    private String duration;
    private String img;

    public VideoImdbDTO(String imdbId, String title, String year, String type, String rate, String duration, String img) {
	super();
	this.imdbId = imdbId;
	this.title = title;
	this.year = year;
	this.type = type;
	this.rate = rate;
	this.duration = duration;
	this.img = img;
    }

    public VideoImdbDTO() {

    }

    public String getImdbId() {
	return imdbId;
    }

    public void setImdbId(String imdbId) {
	this.imdbId = imdbId;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getYear() {
	return year;
    }

    public void setYear(String year) {
	this.year = year;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getRate() {
	return rate;
    }

    public void setRate(String rate) {
	this.rate = rate;
    }

    public String getDuration() {
	return duration;
    }

    public void setDuration(String duration) {
	this.duration = duration;
    }

    public String getImg() {
	return img;
    }

    public void setImg(String img) {
	this.img = img;
    }
}