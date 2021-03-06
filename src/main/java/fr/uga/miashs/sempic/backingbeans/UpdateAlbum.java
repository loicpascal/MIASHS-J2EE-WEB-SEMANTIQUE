/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.qualifiers.SelectedAlbum;
import fr.uga.miashs.sempic.services.AlbumFacade;
import fr.uga.miashs.sempic.services.FaceMessageService;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author loicp
 */
@Named
@ViewScoped
public class UpdateAlbum implements Serializable {
    
    @Inject
    @SelectedAlbum
    private Album current;
    
    @Inject
    private AlbumFacade service;

    public Album getCurrent() {
        return current;
    }

    public void setCurrent(Album current) {
        this.current = current;
    }
    
    public String update() {
        try {
            service.update(current);
            FaceMessageService.setMessage("Album modifié avec succès.");
        } catch (SempicModelException ex) {
            FaceMessageService.setMessage(ex.getMessage());
            return "failure";
        }
        return "success";
    }
}
