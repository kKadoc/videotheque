package fr.mmtech.web.rest.dto;

public class GuessDTO {

	private String imdbId;
	private String title;
	private String year;
	private String type;
	
	public GuessDTO(String imdbId, String title, String year, String type) {
		super();
		this.imdbId = imdbId;
		this.title = title;
		this.year = year;
		this.type = type;
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

	
	@Override
	public String toString() {
		return title + " ["+imdbId+"] "+year+" - "+type;
	}
}