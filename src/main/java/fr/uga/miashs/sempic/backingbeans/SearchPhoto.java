/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.Search;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.services.AlbumFacade;
import fr.uga.miashs.sempic.services.PhotoFacade;
import fr.uga.miashs.sempic.services.SempicRDFService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.jena.rdf.model.Resource;

@Named
@SessionScoped
public class SearchPhoto implements Serializable {

    private List<Photo> photos;
    
    @Inject
    private AlbumFacade albumService;

    @Inject
    private PhotoFacade photoService;
    
    private final SempicRDFService rdfService;
    
    private Search search;

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
    
    public SearchPhoto() {
        this.rdfService = new SempicRDFService();
        this.search = new Search();
    }
    
    @PostConstruct
    public void init() {
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    
    public List<Resource> getCities() {
        return rdfService.getCities();
    }
    
    public String search() {
        try {
            Album album = albumService.read(Long.valueOf(1));
            setPhotos(photoService.findAll(album));
        } catch (SempicModelException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "search";
        }
        
        return "search";
    }
    
    /*public List<Resource> getCities() {
        return rdfService.getPlaces();
    }*/
    
    public List<Resource> getDepictionClasses() {
        return rdfService.getDepictionClasses();
    }

    public List<Resource> getInstances() {
        return rdfService.getInstancesFromType(search.getType());
    }
}
