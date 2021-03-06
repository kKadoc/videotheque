package fr.mmtech.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A File.
 */
@Entity
@Table(name = "T_FILE")
public class File implements Serializable {

    public static final String IMG_FLAG = "IMG";
    public static final String VIDEO_FLAG = "VIDEO";
    public static final String SUBS_FLAG = "SUBS";
    public static final String IGNORE_FLAG = "IGNORE";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "path", unique = true)
    private String path;

    @Column(name = "type")
    private String type;

    public File(String path, String type) {
	this.path = path;
	this.type = type;
    }

    public File() {
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || getClass() != o.getClass()) {
	    return false;
	}

	File file = (File) o;

	if (!Objects.equals(id, file.id))
	    return false;

	return true;
    }

    @Override
    public int hashCode() {
	return Objects.hashCode(id);
    }

    @Override
    public String toString() {
	return "File{" + "id=" + id + ", path='" + path + "'" + ", type='" + type + "'" + '}';
    }
}
