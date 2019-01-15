/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author loicp
 */

@SessionScoped
public class Search implements Serializable {

    private String title;

    private String type;

    private String objectProperty;

    private String instance;

    private Date dateDebut;

    private Date dateFin;

    private String city;

    private String author;

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

    public String getObjectProperty() {
        return objectProperty;
    }

    public void setObjectProperty(String objectProperty) {
        this.objectProperty = objectProperty;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getDateDebut() {
        return getDateDebut("dd/MM/yyyy");
    }

    public String getDateDebut(String format) {
        if (null != dateDebut) {
            SimpleDateFormat simpleFormat = new SimpleDateFormat(format);
            return simpleFormat.format(dateDebut);
        }
        return null;
    }

    public void setDateDebut(String dateDebut) throws ParseException {
        this.dateDebut = null;
        if (null != dateDebut) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.dateDebut = formatter.parse(dateDebut);
            
        }
    }

    public String getDateFin() {
        return getDateFin("dd/MM/yyyy");
    }

    public String getDateFin(String format) {
        if (null != dateFin) {
            SimpleDateFormat simpleFormat = new SimpleDateFormat(format);
            return simpleFormat.format(dateFin);
        }
        return null;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = null;
        if (null != dateFin) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                this.dateFin = formatter.parse(dateFin);
            } catch (ParseException ex) {
                Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
