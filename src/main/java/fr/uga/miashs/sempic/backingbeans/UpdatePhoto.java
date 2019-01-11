/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.Search;
import fr.uga.miashs.sempic.qualifiers.SelectedPhoto;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.entities.RdfPhoto;
import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import fr.uga.miashs.sempic.rdf.RDFStore;
import fr.uga.miashs.sempic.services.SempicRDFService;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

@Named
@ViewScoped
public class UpdatePhoto implements Serializable {
    
    private RdfPhoto rdfPhoto;
    
    private RDFStore rdfStore;
    
    @Inject
    @SelectedPhoto
    private Photo current;
    
    private final SempicRDFService rdfService;
    
    private Search search;
    
    public UpdatePhoto() {
        this.rdfService = new SempicRDFService();
        this.search = new Search();
        this.rdfStore = new RDFStore();
    }
    
    @PostConstruct
    public void init() {
        this.rdfPhoto = new RdfPhoto(current.getId());
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
    
    public RdfPhoto getRdfPhoto() {
        return rdfPhoto;
    }

    public void setRdfPhoto(RdfPhoto rdfPhoto) {
        this.rdfPhoto = rdfPhoto;
    }
    
    public Photo getCurrent() {
        return current;
    }

    public void setCurrent(Photo current) {
        this.current = current;
    }
    
    public List<Resource> getDepictions() {
        return rdfService.getDepictions();
    }
    
    public List<Resource> getCities() {
        return rdfService.getCities();
    }
    
    public List<Resource> getPersonnes() {
        return rdfService.getPersonnes();
    }
    
    public List<Resource> getPhotoDepictions() {
        return rdfService.getPhotoDepictions(current.getId());
    }
    
    public void deleteAnnotation(String pUri) {
        rdfService.deletePhotoAnnotation(current.getId(), pUri);
    }
    
    public String update() {
        try {
            setRdfInformations();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Photo modifié avec succès."));
        } catch (Exception ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "success";
    }
    
    private void setRdfInformations() throws ParseException {
        if (rdfPhoto.getTitle() != null) {
            rdfService.setTitle(current.getId(), rdfPhoto.getTitle());
        }
        if (rdfPhoto.getCity() != null) {
            rdfService.setCity(current.getId(), rdfPhoto.getCity());
        }
        System.out.println(rdfPhoto.getTakenBy());
        if (rdfPhoto.getTakenBy() != null) {
            System.out.println(rdfPhoto.getTakenBy());
            rdfService.setTakenBy(current.getId(), rdfPhoto.getTakenBy());
        }
        if (rdfPhoto.getDate() != null) {
            DateFormat formatter = new SimpleDateFormat("dd/mm/yy");
            try {
                Date date = formatter.parse(rdfPhoto.getDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                rdfService.setDate(current.getId(), cal);
            } catch(ParseException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public String addAnnotation() {
        // TODO : ajouter instance anonyme
        if (rdfPhoto.getInstance() != null) {
            rdfService.addAnnotationObject(current.getId(), SempicOnto.depicts.getURI(), rdfPhoto.getInstance());
        }
        
        return "success";
    }
}
