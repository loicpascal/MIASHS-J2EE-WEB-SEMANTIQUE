/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.services;

import fr.uga.miashs.sempic.rdf.RDFStore;
import java.util.List;
import org.apache.jena.rdf.model.Resource;

public class SempicRDFService {

    private RDFStore rdfStore;

    public SempicRDFService() {
        this.rdfStore = new RDFStore();
    }
    
    public List<Resource> getPlaces() {
        List<Resource> places = rdfStore.listPopulatedPlaces();
        
        return places;
    }
    
    public List<Resource> getDepictions() {
        List<Resource> depictions = rdfStore.listDepictionClasses();
        
        return depictions;
    }
    
    public Resource createPhoto(long photoId, long albumId, long ownerId) {
        Resource photo = rdfStore.createPhoto(photoId, albumId, ownerId);
        
        return photo;
    }
}
