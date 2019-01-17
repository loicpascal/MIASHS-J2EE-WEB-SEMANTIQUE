/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import fr.uga.miashs.sempic.services.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.services.FaceMessageService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class UserController implements Serializable {
    
    private SempicUser current;
    
    @Inject
    private SempicUserFacade userService;
    
    @Inject
    private transient Pbkdf2PasswordHash hashAlgo;
    
    private DataModel<SempicUser> dataModel;

    
    public UserController() {
    }
    
    @PostConstruct
    public void init() {
        current=new SempicUser();
    }


    public SempicUser getCurrent() {
        return current;
    }

    public void setCurrent(SempicUser current) {
        this.current = current;
    }
    
    public String getPassword() {
        return null;
    }
    
    
    public void setPassword(@NotBlank(message="Password is required") String password) {
        getCurrent().setPasswordHash(hashAlgo.generate(password.toCharArray()));
    }
    
    /*public String generateHash(String s) {
        return hashAlgo.generate(s.toCharArray());
    }*/
    
    public String create() {
        try {
            userService.create(current);
        } 
        catch (SempicModelUniqueException ex) {
            FaceMessageService.setMessage("Un utilisateur avec cette adresse mail existe déjà");
            return "failure";
        }
        catch (SempicModelException ex) {
            FaceMessageService.setMessage(ex.getMessage());
            return "failure";
        }
        
        return "success";
    }
    
    /**
     * Suppression d'un utilisateur par son identifiant
     * @param id
     * @return 
     */
    public String delete(long id) {
        SempicUser user = userService.read(id);
        try {
            userService.deleteId(user.getId());
            FaceMessageService.setMessage("Utilisateur supprimé avec succès.");
        } catch (SempicModelException ex) {
            FaceMessageService.setMessage(ex.getMessage());
            return "failure";
        }
        
        return "success";
    }
    
    public DataModel<SempicUser> getDataModel() {
        if (dataModel == null) {
            dataModel = new ListDataModel<>(userService.findAll());
        }
        return dataModel;
    }
}
