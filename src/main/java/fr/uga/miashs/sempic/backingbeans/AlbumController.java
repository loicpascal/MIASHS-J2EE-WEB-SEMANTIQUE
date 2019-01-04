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
import fr.uga.miashs.sempic.entities.SempicUserType;
import fr.uga.miashs.sempic.services.AlbumFacade;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class AlbumController implements Serializable {
    
    @Inject
    @SelectedUser
    private SempicUser selectedUser;
    
    private Album current;
    
    @Inject
    private AlbumFacade service;
    
    private DataModel<Album> dataModel;
    
    public AlbumController() {
        
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
        try {
            service.create(current);
        } catch (SempicModelException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "success";
    }
    
    /**
     * Suppression de l'ablum courant
     * @return 
     */
    public String delete() {
        try {
            service.delete(current);
        } catch (SempicModelException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "success";
    }
    
    /**
     * Suppression d'un album par son identifiant
     * @param id
     * @return 
     */
    public String delete(long id) {
        try {
            service.deleteId(id);
        } catch (SempicModelException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "success";
    }
    
    public DataModel<Album> getDataModel() {
        if (dataModel == null) {
            if (selectedUser.getUserType() == SempicUserType.ADMIN) {
                dataModel = new ListDataModel<>(service.findAll());
            } else {
                // Si l'utilisateur n'est pas ADMIN, on affiche seulement ses albums
                dataModel = new ListDataModel<>(service.findAlbumsOf(selectedUser));
            }
        }
        return dataModel;
    }
}
