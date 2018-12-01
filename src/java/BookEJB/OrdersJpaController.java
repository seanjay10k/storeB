/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookEJB;

import BookEJB.exceptions.IllegalOrphanException;
import BookEJB.exceptions.NonexistentEntityException;
import BookEJB.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Members;
import entity.Odetails;
import entity.Orders;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Invalidquantum
 */
public class OrdersJpaController implements Serializable {

    public OrdersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orders orders) throws PreexistingEntityException, Exception {
        if (orders.getOdetailsCollection() == null) {
            orders.setOdetailsCollection(new ArrayList<Odetails>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Members userid = orders.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getUserid());
                orders.setUserid(userid);
            }
            Collection<Odetails> attachedOdetailsCollection = new ArrayList<Odetails>();
            for (Odetails odetailsCollectionOdetailsToAttach : orders.getOdetailsCollection()) {
                odetailsCollectionOdetailsToAttach = em.getReference(odetailsCollectionOdetailsToAttach.getClass(), odetailsCollectionOdetailsToAttach.getOdetailsPK());
                attachedOdetailsCollection.add(odetailsCollectionOdetailsToAttach);
            }
            orders.setOdetailsCollection(attachedOdetailsCollection);
            em.persist(orders);
            if (userid != null) {
                userid.getOrdersCollection().add(orders);
                userid = em.merge(userid);
            }
            for (Odetails odetailsCollectionOdetails : orders.getOdetailsCollection()) {
                Orders oldOrdersOfOdetailsCollectionOdetails = odetailsCollectionOdetails.getOrders();
                odetailsCollectionOdetails.setOrders(orders);
                odetailsCollectionOdetails = em.merge(odetailsCollectionOdetails);
                if (oldOrdersOfOdetailsCollectionOdetails != null) {
                    oldOrdersOfOdetailsCollectionOdetails.getOdetailsCollection().remove(odetailsCollectionOdetails);
                    oldOrdersOfOdetailsCollectionOdetails = em.merge(oldOrdersOfOdetailsCollectionOdetails);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrders(orders.getOno()) != null) {
                throw new PreexistingEntityException("Orders " + orders + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orders orders) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orders persistentOrders = em.find(Orders.class, orders.getOno());
            Members useridOld = persistentOrders.getUserid();
            Members useridNew = orders.getUserid();
            Collection<Odetails> odetailsCollectionOld = persistentOrders.getOdetailsCollection();
            Collection<Odetails> odetailsCollectionNew = orders.getOdetailsCollection();
            List<String> illegalOrphanMessages = null;
            for (Odetails odetailsCollectionOldOdetails : odetailsCollectionOld) {
                if (!odetailsCollectionNew.contains(odetailsCollectionOldOdetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Odetails " + odetailsCollectionOldOdetails + " since its orders field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getUserid());
                orders.setUserid(useridNew);
            }
            Collection<Odetails> attachedOdetailsCollectionNew = new ArrayList<Odetails>();
            for (Odetails odetailsCollectionNewOdetailsToAttach : odetailsCollectionNew) {
                odetailsCollectionNewOdetailsToAttach = em.getReference(odetailsCollectionNewOdetailsToAttach.getClass(), odetailsCollectionNewOdetailsToAttach.getOdetailsPK());
                attachedOdetailsCollectionNew.add(odetailsCollectionNewOdetailsToAttach);
            }
            odetailsCollectionNew = attachedOdetailsCollectionNew;
            orders.setOdetailsCollection(odetailsCollectionNew);
            orders = em.merge(orders);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getOrdersCollection().remove(orders);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getOrdersCollection().add(orders);
                useridNew = em.merge(useridNew);
            }
            for (Odetails odetailsCollectionNewOdetails : odetailsCollectionNew) {
                if (!odetailsCollectionOld.contains(odetailsCollectionNewOdetails)) {
                    Orders oldOrdersOfOdetailsCollectionNewOdetails = odetailsCollectionNewOdetails.getOrders();
                    odetailsCollectionNewOdetails.setOrders(orders);
                    odetailsCollectionNewOdetails = em.merge(odetailsCollectionNewOdetails);
                    if (oldOrdersOfOdetailsCollectionNewOdetails != null && !oldOrdersOfOdetailsCollectionNewOdetails.equals(orders)) {
                        oldOrdersOfOdetailsCollectionNewOdetails.getOdetailsCollection().remove(odetailsCollectionNewOdetails);
                        oldOrdersOfOdetailsCollectionNewOdetails = em.merge(oldOrdersOfOdetailsCollectionNewOdetails);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orders.getOno();
                if (findOrders(id) == null) {
                    throw new NonexistentEntityException("The orders with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orders orders;
            try {
                orders = em.getReference(Orders.class, id);
                orders.getOno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orders with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Odetails> odetailsCollectionOrphanCheck = orders.getOdetailsCollection();
            for (Odetails odetailsCollectionOrphanCheckOdetails : odetailsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Orders (" + orders + ") cannot be destroyed since the Odetails " + odetailsCollectionOrphanCheckOdetails + " in its odetailsCollection field has a non-nullable orders field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Members userid = orders.getUserid();
            if (userid != null) {
                userid.getOrdersCollection().remove(orders);
                userid = em.merge(userid);
            }
            em.remove(orders);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orders> findOrdersEntities() {
        return findOrdersEntities(true, -1, -1);
    }

    public List<Orders> findOrdersEntities(int maxResults, int firstResult) {
        return findOrdersEntities(false, maxResults, firstResult);
    }

    private List<Orders> findOrdersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orders.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Orders findOrders(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orders.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orders> rt = cq.from(Orders.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
