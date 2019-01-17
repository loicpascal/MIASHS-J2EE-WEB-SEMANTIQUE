/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.services;

import fr.uga.miashs.sempic.Search;
import fr.uga.miashs.sempic.entities.RdfPhoto;
import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import fr.uga.miashs.sempic.rdf.RDFStore;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.jena.rdf.model.Resource;

public class SempicRDFService {

    private final RDFStore rdfStore;

    public SempicRDFService() {
        this.rdfStore = new RDFStore();
    }

    /**
     * Renvoie la liste des lieux
     * @return 
     */
    public List<Resource> getPlaces() {
        List<Resource> places = rdfStore.listPopulatedPlaces();

        return places;
    }

    /**
     * Renvoie la liste des classes
     * @return 
     */
    public List<Resource> getDepictionClasses() {
        List<Resource> depictions = rdfStore.listDepictionClasses();

        return depictions;
    }

    /**
     * Renvoie la liste de toutes les instances
     * @return 
     */
    public List<Resource> getDepictions() {
        List<Resource> depictions = rdfStore.listInstancesByType(SempicOnto.Depiction.getURI());

        return depictions;
    }
    
    /**
     * Renvoie la liste des instances de type Person
     * @return 
     */
    public List<Resource> getPersonnes() {
        List<Resource> personnes = rdfStore.listInstancesByType(SempicOnto.Person.getURI());
        return personnes;
    }
    
    /**
     * Renvoie la liste des villes
     * @return 
     */
    public List<Resource> getCities() {
        List<Resource> cities = rdfStore.listPopulatedPlaces();

        return cities;
    }

    /**
     * Créé une photo à partir de son photoId, son albumId et son ownerId
     * @param photoId
     * @param albumId
     * @param ownerId
     * @return 
     */
    public Resource createPhoto(long photoId, long albumId, long ownerId) {
        Resource photo = rdfStore.createPhoto(photoId, albumId, ownerId);

        return photo;
    }
    
    /**
     * Met à jour les informations de la photo
     * @param rdfPhoto
     * @param photoId 
     */
    public void updateRdfInformations(RdfPhoto rdfPhoto, long photoId) {
        Resource photo = rdfStore.readPhoto(photoId);
        
        rdfStore.deleteResource(photo, SempicOnto.title.getURI());
        rdfStore.deleteResource(photo, SempicOnto.takenAt.getURI());
        rdfStore.deleteResource(photo, SempicOnto.takenBy.getURI());
        rdfStore.deleteResource(photo, SempicOnto.takenIn.getURI());
        
        if (rdfPhoto.getTitle() != null) {
            rdfStore.createAnnotationData(photoId, SempicOnto.title.getURI(), rdfPhoto.getTitle());
        }
        if (rdfPhoto.getCity() != null) {
            rdfStore.createAnnotationObject(photoId, SempicOnto.takenIn.getURI(), rdfPhoto.getCity());
        }
        if (rdfPhoto.getTakenBy() != null) {
            rdfStore.createAnnotationObject(photoId, SempicOnto.takenBy.getURI(), rdfPhoto.getTakenBy());
        }
        if (rdfPhoto.getDate() != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            try {
                Date date = formatter.parse(rdfPhoto.getDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, 1);
                rdfStore.createAnnotationDataUsingDate(photoId, SempicOnto.takenAt.getURI(), cal);
            } catch(ParseException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Ajoute un triplet RDF qui décrit la photo
     * @param photoId
     * @param pUri
     * @param o
     * @return 
     */
    public Resource addAnnotationObject(long photoId, String pUri, Resource o) {
        return rdfStore.createAnnotationObject(photoId, pUri, o);
    }

    /**
     * Ajoute un triplet RDF qui décrit la photo
     * @param photoId
     * @param pUri
     * @param oURI
     * @return 
     */
    public Resource addAnnotationObject(long photoId, String pUri, String oURI) {
        return rdfStore.createAnnotationObject(photoId, pUri, oURI);
    }
    
    /**
     * Renvoie la liste des triplets qui décrivent une photo
     * @param photoId
     * @return 
     */
    public List<Resource> getPhotoDepictions(long photoId) {
        return rdfStore.getPhotoDepictions(photoId);
    }
    
    /**
     * Supprime une annotation de la photo
     * @param photoId
     * @param pURI 
     */
    public void deletePhotoAnnotation(long photoId, String pURI) {
        Resource photo = rdfStore.readPhoto(photoId);
        
        rdfStore.deleteAnnotationByObject(photo, pURI);
    }

    /**
     * Renvoie la liste des instances qui de type l'URI passée en paramètre
     * @param type
     * @return 
     */
    public List<Resource> getInstancesFromType(String type) {
        List<Resource> instances = rdfStore.listInstancesByType(type);
        return instances;
    }

    /**
     * Renvoie la liste des instances correspondantes à la propriété passée en paramètre
     * @param objectProperty
     * @return 
     */
    public List<Resource> getInstancesFromObjectProperty(String objectProperty) {
        List<Resource> instances = rdfStore.listInstancesByObjectProperty(objectProperty);
        return instances;
    }

    /**
     * Renvoie la liste des propriétés correspondantes au type passé en paramètre
     * @param type
     * @return 
     */
    public List<Resource> getObjectProperiesFromType(String type) {
        return rdfStore.getObjectPropertyByDomain(type);
    }
    
    /**
     * Créé une instance anonyme du type passé en paramètre
     * @param type
     * @return 
     */
    public Resource createAnonInstance(String type) {
        return rdfStore.createAnonInstance(type);
    }

    /**
     * Renvoie la liste des photos correspondante à la recherche
     * @param search
     * @param id
     * @return 
     */
    public List<Resource> searchPhoto(Search search, long id) {
        return rdfStore.searchPhoto(id, search.getTitle(), search.getType(), search.getObjectProperty(), search.getInstance(),
                search.getCity(), search.getDateDebut("yyyy-MM-dd"), search.getDateFin("yyyy-MM-dd"), search.getAuthor());
    }
}
