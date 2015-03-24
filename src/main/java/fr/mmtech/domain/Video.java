package fr.mmtech.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.mmtech.domain.util.CustomDateTimeDeserializer;
import fr.mmtech.domain.util.CustomDateTimeSerializer;

/**
 * A Video.
 */
@Entity
@Table(name = "T_VIDEO")
public class Video implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "imdb_id")
    private String imdbId;

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private Integer year;

    @Column(name = "rate", precision=10, scale=2)
    private BigDecimal rate;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "favoris")
    private Boolean favoris;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "add_date")
    private DateTime addDate;

    @ManyToMany
    @JoinTable(name = "T_VIDEO_VIDEOTYPE",
               joinColumns = @JoinColumn(name="videos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="videoTypes_id", referencedColumnName="ID"))
    private Set<VideoType> videoTypes = new HashSet<>();

    @OneToOne
    @Cascade({CascadeType.PERSIST, CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    private File videoFile;

    @OneToOne
    @Cascade({CascadeType.PERSIST, CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    private File imgFile;

    @OneToOne
    @Cascade({CascadeType.PERSIST, CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    private File subFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getFavoris() {
        return favoris;
    }

    public void setFavoris(Boolean favoris) {
        this.favoris = favoris;
    }

    public DateTime getAddDate() {
        return addDate;
    }

    public void setAddDate(DateTime addDate) {
        this.addDate = addDate;
    }

    public Set<VideoType> getVideoTypes() {
        return videoTypes;
    }

    public void setVideoTypes(Set<VideoType> videoTypes) {
        this.videoTypes = videoTypes;
    }

    public File getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(File file) {
        this.videoFile = file;
    }

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File file) {
        this.imgFile = file;
    }

    public File getSubFile() {
        return subFile;
    }

    public void setSubFile(File file) {
        this.subFile = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Video video = (Video) o;

        if ( ! Objects.equals(id, video.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", imdbId='" + imdbId + "'" +
                ", title='" + title + "'" +
                ", year='" + year + "'" +
                ", rate='" + rate + "'" +
                ", duration='" + duration + "'" +
                ", favoris='" + favoris + "'" +
                ", addDate='" + addDate + "'" +
                '}';
    }
}
