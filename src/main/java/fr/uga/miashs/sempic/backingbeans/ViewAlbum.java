/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.qualifiers.SelectedAlbum;
import fr.uga.miashs.sempic.services.FaceMessageService;
import fr.uga.miashs.sempic.services.PhotoFacade;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class ViewAlbum implements Serializable {
    
    @Inject
    @SelectedAlbum
    private Album current;
    
    @Inject
    private PhotoFacade service;
    
    @Inject 
    private PhotoController createPhoto;
    
    public List<Photo> getPhotos() {
        try {
            return service.findAll(current);
        } catch (SempicModelException ex) {
            FaceMessageService.setMessage("Cannot retrieve the photos : " + ex.getMessage());
        }
        return Collections.emptyList();
    }

    public Album getCurrent() {
        return current;
    }

    public void setCurrent(Album current) {
        this.current = current;
    }
    

    public PhotoController getCreatePhoto() {
        return createPhoto;
    }
    
}
