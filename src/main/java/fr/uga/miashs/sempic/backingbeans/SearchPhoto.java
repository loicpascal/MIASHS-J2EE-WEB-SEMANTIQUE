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
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import fr.uga.miashs.sempic.qualifiers.LoggedUser;
import fr.uga.miashs.sempic.services.AlbumFacade;
import fr.uga.miashs.sempic.services.PhotoFacade;
import fr.uga.miashs.sempic.services.SempicRDFService;
import java.io.Serializable;
import java.util.ArrayList;
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
    
    @Inject
    @LoggedUser
    private SempicUser loggedUser;
    
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
    
    public String searchAction() {
        List<Photo> _photos = new ArrayList<>();
        List<Resource> rdfPhotos = rdfService.searchPhoto(search, loggedUser.getId());
        if (!rdfPhotos.isEmpty()) {
            for(Resource res : rdfPhotos) {
                String[] sURI = res.getURI().split("/");
                String id = sURI[sURI.length - 1];
                Photo p = photoService.read(Long.valueOf(id));
                if (res.hasProperty(SempicOnto.title))
                    p.setTitle(res.getProperty(SempicOnto.title).getString());
                _photos.add(p);
            }
            setPhotos(_photos);
        } else {
            if(null != this.photos)
                this.photos.clear();
            FacesContext.getCurrentInstance().addMessage("searchInfos", new FacesMessage("Aucun résultat"));
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
        if (null != search.getObjectProperty()) {
            return rdfService.getInstancesFromObjectProperty(search.getObjectProperty());
        }
        return rdfService.getInstancesFromType(search.getType());
    }

    public List<Resource> getObjectProperties() {
        return rdfService.getObjectProperiesFromType(search.getType());
    }

    public List<Resource> getPersonnes() {
        return rdfService.getPersonnes();
    }
}
