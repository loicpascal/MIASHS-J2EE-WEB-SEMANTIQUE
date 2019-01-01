/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.qualifiers.SelectedUser;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.services.AlbumFacade;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class CreateAlbum implements Serializable {
    
    @Inject
    @SelectedUser
    private SempicUser selectedUser;
    
    private Album current;
    
    @Inject
    private AlbumFacade service;
    
    public CreateAlbum() {
        
    }
    
    @PostConstruct
    public void init() {
        current=new Album();
        current.setOwner(selectedUser);
    }


    public Album getCurrent() {
        return current;
    }

    public void setCurrent(Album current) {
        this.current = current;
    }
    
    public String create() {
        System.out.println(current);
        
        try {
            service.create(current);
        } catch (SempicModelException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "success";
    }
}
