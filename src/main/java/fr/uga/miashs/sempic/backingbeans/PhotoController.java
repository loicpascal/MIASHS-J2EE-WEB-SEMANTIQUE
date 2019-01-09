/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.qualifiers.SelectedAlbum;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.entities.RdfPhoto;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.qualifiers.LoggedUser;
import fr.uga.miashs.sempic.services.PhotoFacade;
import fr.uga.miashs.sempic.services.SempicRDFService;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class PhotoController implements Serializable {
    
    @Inject
    @SelectedAlbum
    private Album selectedAlbum;
    
    private List<Part> files;
    
    private Photo current;

    @Inject
    private PhotoFacade service;
    
    private final SempicRDFService rdfService;
    
    @Inject
    @LoggedUser
    private SempicUser loggedUser;
    
    public PhotoController() {
        this.rdfService = new SempicRDFService();
    }
    
    @PostConstruct
    public void init() {
    }
    
    public Photo getCurrent() {
        return current;
    }

    public void setCurrent(Photo current) {
        this.current = current;
    }

    public List<Part> getFiles() {
        return files;
    }

    public void setFiles(List<Part> files) {
        this.files = files;
    }
    
    public String create() {
        if (selectedAlbum==null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("parameter albumId must be given"));
            return "failure";
        }
        boolean partiallyFailed=false;
        for (Part p : files) {
            Photo current=new Photo();
            current.setAlbum(selectedAlbum);
            try {
                service.create(current,p.getInputStream());
                rdfService.createPhoto(current.getId(), selectedAlbum.getId(), loggedUser.getId());
                rdfService.setTakenBy(current.getId(), loggedUser.getId());
            } catch (SempicModelException ex) {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
               partiallyFailed=true;
               
            } catch (IOException ex) {
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
                 partiallyFailed=true;
                
            }
        }
        if (partiallyFailed) {
             return "failure";
        }
        else {
            init();
            return "success";
        }
    }
    
    /**
     * Suppression de la photo courante
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
     * Suppression d'une photo par son identifiant
     * @param id
     * @return 
     */
    public String delete(long id) {
        try {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Photo supprimée avec succès"));
            service.deleteId(id);
        } catch (SempicModelException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "success";
    }
}
