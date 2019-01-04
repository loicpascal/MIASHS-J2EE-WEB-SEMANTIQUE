/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.qualifiers.SelectedPhoto;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.services.PhotoFacade;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class UpdatePhoto implements Serializable {
    
    @Inject
    @SelectedPhoto
    private Photo current;
    
    public UpdatePhoto() {
        
    }
    
    public Photo getCurrent() {
        return current;
    }

    public void setCurrent(Photo current) {
        this.current = current;
    }
    
    @PostConstruct
    public void init() {
    }
}
