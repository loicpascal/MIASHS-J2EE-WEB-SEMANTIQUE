/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic;

import java.util.Date;
import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author loicp
 */
public class Search
{

    private String title;

    private Resource type;

    private Resource instance;

    private Date dateDebut;

    private Date dateFin;

    private Resource city;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Resource getType() {
        return type;
    }

    public void setType(Resource type) {
        this.type = type;
    }

    public Resource getInstance() {
        return instance;
    }

    public void setInstance(Resource instance) {
        this.instance = instance;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Resource getCity() {
        return city;
    }

    public void setCity(Resource city) {
        this.city = city;
    }

}
