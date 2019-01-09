/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import fr.uga.miashs.sempic.rdf.RDFStore;
import java.util.Date;
import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author loicp
 */
public class RdfPhoto {
    private String title;

    private String type;

    private String instance;

    private String date;

    private String city;
    
    private RDFStore rdfStore;
    
    public RdfPhoto() {
        
    }
    
    public RdfPhoto(long photoId) {
        this.rdfStore = new RDFStore();
        Resource photo = rdfStore.readPhoto(photoId);
        this.title = String.valueOf(photo.getProperty(SempicOnto.title).getLiteral());
        this.city = String.valueOf(photo.getProperty(SempicOnto.takenIn));
        this.date = String.valueOf(photo.getProperty(SempicOnto.takenAt).getLiteral());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
