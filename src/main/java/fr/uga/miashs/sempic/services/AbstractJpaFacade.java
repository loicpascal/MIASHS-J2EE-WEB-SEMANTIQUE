package fr.uga.miashs.sempic.services;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
public abstract class AbstractJpaFacade<K, T> {

    @PersistenceContext(unitName="SempicPU"/*,type = PersistenceContextType.EXTENDED*/)
    private EntityManager em;
        
    private Class<T> entityClass;

    public AbstractJpaFacade(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    private static void analyseSQLException(Throwable t) throws SempicModelException {
        while (t != null) {
            if (t instanceof SQLIntegrityConstraintViolationException) {
                SQLIntegrityConstraintViolationException sqlEx = (SQLIntegrityConstraintViolationException) t;
                if ("23505".equals(sqlEx.getSQLState())) {
                    throw new SempicModelUniqueException("Valeur dupliquée");
                }
            }
            t = t.getCause();
        }   
    }
    @AroundInvoke
    private Object catchDatabaseException(InvocationContext ctx) throws Exception {
        try {
            return ctx.proceed();
        } catch (PersistenceException e) {
            Throwable t = e.getCause();
            analyseSQLException(t);
            throw e;
        }
    }

    public void create(T entity) throws SempicModelException {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    public T update(T entity) throws SempicModelException {
        // If the entity is detached, then reattach it. 
        if (!getEntityManager().contains(entity)) {
            entity = getEntityManager().merge(entity);
        }
        getEntityManager().flush();
        return entity;
    }

    public void delete(T entity) throws SempicModelException {
        getEntityManager().remove(entity);
        getEntityManager().flush();
    }

    public void deleteId(K id) throws SempicModelException {
        getEntityManager().remove(getEntityManager().getReference(entityClass, id));
    }

    public T read(K id) {
        return getEntityManager().find(entityClass, id);
    }

    protected CriteriaQuery findAllQuery() {
        CriteriaQuery cq = getCriteriaBuilder().createQuery();
        Root<T> r = cq.from(entityClass);
        cq.select(r);
        return cq;
    }

    public List<T> findAll() {
        return find(findAllQuery());
    }

    public List<T> find(CriteriaQuery query) {
        List<T> res = getEntityManager().createQuery(query).getResultList();
        return res;
    }

    public List<T> findRange(int[] range) {
        return findRange(range, findAllQuery());
    }

    public List<T> findRange(int[] range, CriteriaQuery query) {
        Query q = getEntityManager().createQuery(query);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return getEntityManager().getCriteriaBuilder();
    }

    public int count() {
        CriteriaQuery cq = getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
