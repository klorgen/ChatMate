/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.lorgen.chatmate.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author klorg
 */
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@XmlRootElement
public class UserGroup implements Serializable {
    public static final String ADMIN = "admin";
    public static final String USER = "user";
    
    @Id String name;
    @Id String userid;
    
}
