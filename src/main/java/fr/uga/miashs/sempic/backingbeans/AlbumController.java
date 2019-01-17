/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.qualifiers.SelectedUser;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.entities.SempicUserType;
import fr.uga.miashs.sempic.services.AlbumFacade;
import fr.uga.miashs.sempic.services.PhotoFacade;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
    private AlbumFacade albumService;
    
    @Inject
    private PhotoFacade photoService;
    
    private DataModel<Album> dataModel;
    
    public AlbumController() {}
    
    @PostConstruct
    public void init() {
        current=new Album();
        current.setOwner(selectedUser);
    }
    
    /**
     * Renvoie la liste des photos de l'album courant
     * @return 
     */
    public List<Photo> getPhotos() {
        try {
            return photoService.findAll(current);
        } catch (SempicModelException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cannot retrieve the photos",ex.getMessage()));
        }
        return Collections.emptyList();
    }
    
    /**
     * Renvoie la liste des photos de l'album passé en paramètre
     * @param album
     * @return 
     */
    public List<Photo> getPhotos(Album album) {
        try {
            return photoService.findAll(album);
        } catch (SempicModelException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cannot retrieve the photos",ex.getMessage()));
        }
        return Collections.emptyList();
    }

    public Album getCurrent() {
        return current;
    }

    public void setCurrent(Album current) {
        this.current = current;
    }
    
    public String create() {
        try {
            albumService.create(current);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Album créé avec succès."));
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
            List<Photo> photos = this.getPhotos();
            Iterator li = photos.listIterator();

            while(li.hasNext()) {
                Photo photo = (Photo) li.next();
                photoService.delete(photo);
            }
      
            albumService.delete(current);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Album supprimé avec succès."));
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
        Album album = albumService.read(id);
        try {
            List<Photo> photos = getPhotos(album);
            Iterator li = photos.listIterator();

            while(li.hasNext()) {
                Photo photo = (Photo) li.next();
                photoService.deleteId(photo.getId());
            }
            
            albumService.deleteId(album.getId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Album supprimé avec succès."));
        } catch (SempicModelException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "success";
    }
    
    public DataModel<Album> getDataModel() {
        if (dataModel == null) {
            if (selectedUser.getUserType() == SempicUserType.ADMIN) {
                dataModel = new ListDataModel<>(albumService.findAll());
            } else {
                // Si l'utilisateur n'est pas ADMIN, on affiche seulement ses albums
                dataModel = new ListDataModel<>(albumService.findAlbumsOf(selectedUser));
            }
        }
        return dataModel;
    }
}
