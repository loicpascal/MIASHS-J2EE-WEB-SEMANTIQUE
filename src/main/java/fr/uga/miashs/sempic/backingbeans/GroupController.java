/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.services.GroupFacade;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.qualifiers.SelectedUser;
import fr.uga.miashs.sempic.services.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicGroup;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.entities.SempicUserType;
import fr.uga.miashs.sempic.services.FaceMessageService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class GroupController implements Serializable {
    
    @Inject
    @SelectedUser
    private SempicUser selectedUser;
    
    private SempicGroup current;
    
    @Inject
    private GroupFacade service;
    
    @Inject
    private SempicUserFacade userService;
    
    @Inject
    private GroupFacade groupService;
    
    private DataModel<SempicGroup> dataModel;
    
    public GroupController() {}
    
    @PostConstruct
    public void init() {
        current=new SempicGroup();
        current.setOwner(selectedUser);
    }

    public List<SempicUser> getUsers() {
        return userService.findAll();
    }

    public SempicGroup getCurrent() {
        return current;
    }

    public void setCurrent(SempicGroup current) {
        this.current = current;
    }
    
    public String create() {
        try {
            service.create(current);
            FaceMessageService.setMessage("Groupe créé avec succès");
        } catch (SempicModelException ex) {
            FaceMessageService.setMessage(ex.getMessage());
            return "failure";
        }
        
        return "success";
    }
    
    public DataModel<SempicGroup> getDataModel() {
        if (dataModel == null) {
            if (selectedUser.getUserType() == SempicUserType.ADMIN) {
                dataModel = new ListDataModel<>(groupService.findAll());
            } else {
                dataModel = new ListDataModel<>();
            }
        }
        return dataModel;
    }
}
