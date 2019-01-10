/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.services;

import fr.uga.miashs.sempic.model.rdf.SempicOnto;
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

    public List<Resource> getDepictionClasses() {
        List<Resource> depictions = rdfStore.listDepictionClasses();

        return depictions;
    }

    public List<Resource> getDepictions() {
        List<Resource> depictions = rdfStore.listInstancesByType(SempicOnto.Depiction.getURI());

        return depictions;
    }

    public List<Resource> getCities() {
        List<Resource> cities = rdfStore.listPopulatedPlaces();

        return cities;
    }

    public Resource createPhoto(long photoId, long albumId, long ownerId) {
        Resource photo = rdfStore.createPhoto(photoId, albumId, ownerId);

        return photo;
    }

    /**
     * Met à jour le titre de la photo passée en paramètre
     *
     * @param photoId
     * @param title
     * @return
     */
    public Resource setTitle(long photoId, String title) {
        Resource photo = rdfStore.readPhoto(photoId);

        // Suppression du triplet RDF représentant l'ancien titre
        rdfStore.deleteResource(photo, SempicOnto.title.getURI());

        // Ajout du triplet représentant le nouveau titre de la photo
        photo = rdfStore.createAnnotationData(photoId, SempicOnto.title.getURI(), title);

        return photo;
    }

    /**
     * Met à jour la date de la photo passée en paramètre
     *
     * @param photoId
     * @param date
     * @return
     */
    public Resource setDate(long photoId, String date) {
        Resource photo = rdfStore.readPhoto(photoId);

        // Suppression du triplet RDF représentant l'ancienne date
        rdfStore.deleteResource(photo, SempicOnto.takenAt.getURI());

        // Ajout du triplet représentant la nouvelle date de la photo
        photo = rdfStore.createAnnotationData(photoId, SempicOnto.takenAt.getURI(), date);

        return photo;
    }

    public Resource setTakenBy(long photoId, long userId) {
        Resource photo = rdfStore.readPhoto(photoId);

        // Suppression du triplet RDF représentant l'ancienne date
        rdfStore.deleteResource(photo, SempicOnto.takenBy.getURI());

        // Ajout du triplet représentant la nouvelle date de la photo
        photo = rdfStore.createAnnotationData(photoId, SempicOnto.takenBy.getURI(), String.valueOf(userId));

        return photo;
    }

    public Resource setCity(long photoId, String city) {
        Resource photo = rdfStore.readPhoto(photoId);

        // Suppression du triplet RDF représentant l'ancienne date
        rdfStore.deleteResource(photo, SempicOnto.takenIn.getURI());

        // Ajout du triplet représentant la nouvelle date de la photo
        rdfStore.createAnnotationObject(photoId, SempicOnto.takenIn.getURI(), city);

        return photo;
    }

    public Resource addAnnotationObject(long photoId, String pUri, String oUri) {
        Resource photo = rdfStore.createAnnotationObject(photoId, pUri, oUri);

        return photo;
    }

    public List<Resource> getInstancesFromType(String type) {
        List<Resource> instances = rdfStore.listInstancesByType(type);
        return instances;
    }

}
