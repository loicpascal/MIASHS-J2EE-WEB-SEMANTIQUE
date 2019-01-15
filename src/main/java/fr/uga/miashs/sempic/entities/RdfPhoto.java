/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import fr.uga.miashs.sempic.rdf.RDFStore;
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

    private Resource cityResource;
    private String city;

    private Resource takenByResource;
    private String takenBy;
    
    private RDFStore rdfStore;
    
    public RdfPhoto() {}
    
    public RdfPhoto(long photoId) {
        this.rdfStore = new RDFStore();
        Resource photo = rdfStore.readPhoto(photoId);
        if (photo != null) {
            if (photo.getProperty(SempicOnto.title) != null)
                this.title = String.valueOf(photo.getProperty(SempicOnto.title).getLiteral());
            if (photo.getProperty(SempicOnto.takenIn) != null) {
                this.city = String.valueOf(photo.getProperty(SempicOnto.takenIn).getObject());
                this.cityResource = photo.getProperty(SempicOnto.takenIn).getResource();
            }
            if (photo.getProperty(SempicOnto.takenAt) != null) {
                String date = String.valueOf(photo.getProperty(SempicOnto.takenAt).getLiteral());
                String year = date.substring(0, 4);
                String month = date.substring(5, 7);
                String day = date.substring(8, 10);
                this.date = day + "/" + month + "/" + year;
            }
            if (photo.getProperty(SempicOnto.takenBy) != null) {
                this.takenBy = String.valueOf(photo.getProperty(SempicOnto.takenBy).getObject());
                this.takenByResource = photo.getProperty(SempicOnto.takenBy).getResource();
            }
        }
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

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }
    
    public Resource getCityResource() {
        return cityResource;
    }

    public void setCityResource(Resource cityResource) {
        this.cityResource = cityResource;
    }

    public Resource getTakenByResource() {
        return takenByResource;
    }

    public void setTakenByResource(Resource takenByResource) {
        this.takenByResource = takenByResource;
    }
}
