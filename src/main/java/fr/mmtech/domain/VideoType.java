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
 * A VideoType.
 */
@Entity
@Table(name = "T_VIDEOTYPE")
public class VideoType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || getClass() != o.getClass()) {
	    return false;
	}

	VideoType videoType = (VideoType) o;

	// le facteur déterminant est le nom pas l'id (surtout dans les sets qui
	// se tromperont si 2 nouveaux élements (id null) sont insérés)
	if (!Objects.equals(name, videoType.name))
	    return false;

	return true;
    }

    @Override
    public int hashCode() {
	return Objects.hashCode(id);
    }

    @Override
    public String toString() {
	return "VideoType{" + "id=" + id + ", name='" + name + "'" + '}';
    }
}
