/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.Search;
import fr.uga.miashs.sempic.qualifiers.SelectedPhoto;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.services.PhotoFacade;
import fr.uga.miashs.sempic.services.SempicRDFService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.jena.rdf.model.Resource;

@Named
@ViewScoped
public class UpdatePhoto implements Serializable {
    
    @Inject
    @SelectedPhoto
    private Photo current;
    
    private final SempicRDFService rdfService;
    
    private Search search;

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
    
    public UpdatePhoto() {
        this.rdfService = new SempicRDFService();
        this.search = new Search();
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
    
    public List<Resource> getDepictions() {
        return rdfService.getDepictions();
    }
}
