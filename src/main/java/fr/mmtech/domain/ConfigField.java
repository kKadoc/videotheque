package fr.mmtech.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ConfigField.
 */
@Entity
@Table(name = "T_CONFIGFIELD")
public class ConfigField implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "field_key")
    private String key;

    @Column(name = "field_content")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigField configField = (ConfigField) o;

        if ( ! Objects.equals(id, configField.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ConfigField{" +
                "id=" + id +
                ", key='" + key + "'" +
                ", content='" + content + "'" +
                '}';
    }
}
