/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic;

import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author loicp
 */
public class Search {
    
    private Resource city;
    
    private Resource type;

    public Search() {
        
    }
    
    public Resource getType() {
        return type;
    }

    public void setType(Resource type) {
        this.type = type;
    }
    
    public Resource getCity() {
        return city;
    }

    public void setCity(Resource city) {
        this.city = city;
    }
}
