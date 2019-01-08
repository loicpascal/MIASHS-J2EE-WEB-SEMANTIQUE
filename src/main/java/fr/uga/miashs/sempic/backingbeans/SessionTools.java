/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.entities.SempicGroup;
import fr.uga.miashs.sempic.qualifiers.LoggedUser;
import fr.uga.miashs.sempic.qualifiers.SelectedUser;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.entities.SempicUserType;
import fr.uga.miashs.sempic.qualifiers.SelectedAlbum;
import fr.uga.miashs.sempic.qualifiers.SelectedGroup;
import fr.uga.miashs.sempic.qualifiers.SelectedPhoto;
import fr.uga.miashs.sempic.services.AlbumFacade;
import fr.uga.miashs.sempic.services.GroupFacade;
import fr.uga.miashs.sempic.services.PhotoFacade;
import fr.uga.miashs.sempic.services.SempicUserFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@SessionScoped
public class SessionTools implements Serializable {

    @Inject
    private FacesContext facesContext;
    
    @Inject
    private SecurityContext securityContext;

    @Inject
    private SempicUserFacade userService;
    
    @Inject
    private GroupFacade groupService;
    
    @Inject
    private AlbumFacade albumService;
    
    @Inject
    private PhotoFacade photoService;

    private SempicUser connectedUser;

    /**
     * Creates a new instance of Login
     */
    public SessionTools() {
    }

    @Produces
    @LoggedUser
    @Dependent
    @Named
    public SempicUser getConnectedUser() {
        if (connectedUser == null  && securityContext.getCallerPrincipal()!=null) {
            connectedUser = userService.readByEmail(securityContext.getCallerPrincipal().getName());
        }
        return connectedUser;
    }
    
    
    /**
     * get the user that correspond to a specific userId in the request
     * parameter only admin can request another user than himself. in other
     * cases, it returns the connected user.
     *
     * @return
     */
    @Produces
    @SelectedUser
    @Dependent
    @Named
    public SempicUser getSelectedUser() throws SempicException {
        String userId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userId");
        if (userId == null || getConnectedUser().getUserType() != SempicUserType.ADMIN) {
            return getConnectedUser();
        }
        try {
            long id = Long.parseLong(userId);
            return userService.read(id);
        } catch (NumberFormatException e) {
            throw new SempicException("parameter userId is not a number: " + userId,e);
        }
    }
    
    
    @Produces
    @SelectedAlbum
    @Dependent
    @Named
    public Album getSelectedAlbum() throws SempicException {
        String albumId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("albumId");
        if (albumId != null) {
            try {
                long id = Long.parseLong(albumId);
                Album a =  albumService.read(id);
                if (getConnectedUser()==null || (getConnectedUser().getUserType() != SempicUserType.ADMIN && ! a.getOwner().equals(getConnectedUser()))){
                     throw new SempicException("not allowed to access albumId=" + albumId);
                }
                return a;
            } catch (NumberFormatException e) {
                throw new SempicException("parameter albumId is not a number: " + albumId,e);
            }
        } 
        throw new SempicException("parameter albumId not given");
    }
    
    @Produces
    @SelectedGroup
    @Dependent
    @Named
    public SempicGroup getSelectedGroup() throws SempicException {
        String groupId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("groupId");
        if (groupId != null) {
            try {
                long id = Long.parseLong(groupId);
                SempicGroup a = groupService.read(id);
                if (getConnectedUser()==null || (getConnectedUser().getUserType() != SempicUserType.ADMIN && ! a.getOwner().equals(getConnectedUser()))){
                     throw new SempicException("not allowed to access groupId = " + groupId);
                }
                return a;
            } catch (NumberFormatException e) {
                throw new SempicException("parameter groupId is not a number : " + groupId,e);
            }
        } 
        throw new SempicException("parameter groupId not given");
    }
    
    @Produces
    @SelectedPhoto
    @Dependent
    @Named
    public Photo getSelectedPhoto() throws SempicException {
        String photoId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("photoId");
        if (photoId != null) {
            try {
                long id = Long.parseLong(photoId);
                Photo a =  photoService.read(id);
                if (getConnectedUser()==null || (getConnectedUser().getUserType() != SempicUserType.ADMIN && ! a.getAlbum().getOwner().equals(getConnectedUser()))){
                     throw new SempicException("not allowed to access photoId="+photoId);
                }
                return a;
            } catch (NumberFormatException e) {
                throw new SempicException("parameter photoId is not a number: "+photoId,e);
            }
        } 
        throw new SempicException("parameter photoId not given");
    }
    
    public boolean isNotLogged() {
        return (getConnectedUser()==null);
    }
    
    public boolean isAdmin() {
        SempicUser u = getConnectedUser();
        return (u!=null && u.getUserType()==SempicUserType.ADMIN);
    }
    
     public boolean isUser() {
        SempicUser u = getConnectedUser();
        return (u!=null && u.getUserType()==SempicUserType.USER);
    }
    
    public String logout() {
        ((HttpSession)facesContext.getExternalContext().getSession(false)).invalidate();
        return "logout";
    }
}
