/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "UniqueGroupName", columnNames = {"name"})
})
public class SempicGroup implements Serializable {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotBlank(message="Il faut un nom de groupe")
    private String name;
    
    @NotNull
    @ManyToOne
    private SempicUser owner;
    
    @ManyToMany
    private Set<SempicUser> members;

    public SempicGroup() {
        
    }
    
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SempicUser getOwner() {
        return owner;
    }

    public void setOwner(SempicUser owner) {
        this.owner = owner;
    }

    public Set<SempicUser> getMembers() {
        return members;
    }  

    public void setMembers(Set<SempicUser> members) {
        this.members = members;
        members.add(owner);
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SempicGroup other = (SempicGroup) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Group{" + "id=" + id + ", name=" + name + ", owner=" + owner + '}';
    }
    
    
    
    
    
    
}
