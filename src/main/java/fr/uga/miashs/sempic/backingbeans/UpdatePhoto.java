/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.qualifiers.SelectedPhoto;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.entities.RdfPhoto;
import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import fr.uga.miashs.sempic.services.FaceMessageService;
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
    
    private RdfPhoto rdfPhoto;
    
    @Inject
    @SelectedPhoto
    private Photo current;
    
    private final SempicRDFService rdfService;
    
    private String annotationType;
    
    public UpdatePhoto() {
        this.rdfService = new SempicRDFService();
    }
    
    @PostConstruct
    public void init() {
        this.rdfPhoto = new RdfPhoto(current.getId());
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
    
    public String getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
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
    
    public List<Resource> getDepictionClasses() {
        return rdfService.getDepictionClasses();
    }
    
    public List<Resource> getObjectProperiesFromType(String type) {
        return rdfService.getObjectProperiesFromType(type);
    }
    
    public List<Resource> getInstances() {
        return rdfService.getInstancesFromType(annotationType);
    }
    
    public String update() {
        try {
            rdfService.updateRdfInformations(rdfPhoto, current.getId());
            FaceMessageService.setMessage("Photo modifié avec succès.");
        } catch (Exception ex) {
            FaceMessageService.setMessage(ex.getMessage());
            return "failure";
        }
        
        return "success";
    }
    
    public String addAnnotation() {
        if (rdfPhoto.getInstance() != null) {
            rdfService.addAnnotationObject(current.getId(), SempicOnto.depicts.getURI(), rdfPhoto.getInstance());
            FaceMessageService.setMessage("Annotation ajoutée avec succès.");
        } else if (annotationType != null) {
            rdfService.createAnonInstance(current.getId(), annotationType);
            FaceMessageService.setMessage("Annotation ajoutée avec succès.");
        } else {
            return "failure";
        }
        
        return "success";
    }
    
    public String deleteAnnotation(String pUri) {
        rdfService.deletePhotoAnnotation(current.getId(), pUri);
        return "success";
    }
}
