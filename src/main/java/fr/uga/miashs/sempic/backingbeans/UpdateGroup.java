/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.SempicGroup;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.qualifiers.SelectedGroup;
import fr.uga.miashs.sempic.services.GroupFacade;
import fr.uga.miashs.sempic.services.SempicUserFacade;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author loicp
 */
@Named
@ViewScoped
public class UpdateGroup implements Serializable {
    
    @Inject
    @SelectedGroup
    private SempicGroup current;
    
    @Inject
    private GroupFacade service;
    
    @Inject
    private SempicUserFacade userService;

    public SempicGroup getCurrent() {
        return current;
    }

    public void setCurrent(SempicGroup current) {
        this.current = current;
    }
    
    public List<SempicUser> getUsers() {
        return userService.findAll();
    }
    
    public String update() {
        try {
            service.update(current);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Groupe modifié avec succès."));
        } catch (SempicModelException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        
        return "success";
    }
}