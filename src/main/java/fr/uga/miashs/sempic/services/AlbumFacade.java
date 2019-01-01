/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.services;

import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.util.List;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Stateful
public class AlbumFacade extends AbstractJpaFacade<Long,Album> {


    public AlbumFacade() {
        super(Album.class);
    }
    
    public List<Album> findAlbumsOf(SempicUser owner) {
        Query q = getEntityManager().createNamedQuery("findUserAlbums");
        q.setParameter("owner", owner);
        return q.getResultList();
    }    
}
