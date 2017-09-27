package no.ntnu.lorgen.chatmate.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author klorg
 */
@Data
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Message implements Serializable {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "userid")
    String user;
    String text;

    @XmlElement(nillable=true)
    @Version
    Timestamp version;

    @XmlTransient
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    Chat chat;

    public Message(String user, String text) {
        this.user = user;
        this.text = text;
    }
}
