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
import entity.Orders;
import java.util.ArrayList;
import java.util.Collection;
import entity.Cart;
import entity.Members;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Invalidquantum
 */
public class MembersJpaController implements Serializable {

    public MembersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Members members) throws PreexistingEntityException, Exception {
        if (members.getOrdersCollection() == null) {
            members.setOrdersCollection(new ArrayList<Orders>());
        }
        if (members.getCartCollection() == null) {
            members.setCartCollection(new ArrayList<Cart>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Orders> attachedOrdersCollection = new ArrayList<Orders>();
            for (Orders ordersCollectionOrdersToAttach : members.getOrdersCollection()) {
                ordersCollectionOrdersToAttach = em.getReference(ordersCollectionOrdersToAttach.getClass(), ordersCollectionOrdersToAttach.getOno());
                attachedOrdersCollection.add(ordersCollectionOrdersToAttach);
            }
            members.setOrdersCollection(attachedOrdersCollection);
            Collection<Cart> attachedCartCollection = new ArrayList<Cart>();
            for (Cart cartCollectionCartToAttach : members.getCartCollection()) {
                cartCollectionCartToAttach = em.getReference(cartCollectionCartToAttach.getClass(), cartCollectionCartToAttach.getCartPK());
                attachedCartCollection.add(cartCollectionCartToAttach);
            }
            members.setCartCollection(attachedCartCollection);
            em.persist(members);
            for (Orders ordersCollectionOrders : members.getOrdersCollection()) {
                Members oldUseridOfOrdersCollectionOrders = ordersCollectionOrders.getUserid();
                ordersCollectionOrders.setUserid(members);
                ordersCollectionOrders = em.merge(ordersCollectionOrders);
                if (oldUseridOfOrdersCollectionOrders != null) {
                    oldUseridOfOrdersCollectionOrders.getOrdersCollection().remove(ordersCollectionOrders);
                    oldUseridOfOrdersCollectionOrders = em.merge(oldUseridOfOrdersCollectionOrders);
                }
            }
            for (Cart cartCollectionCart : members.getCartCollection()) {
                Members oldMembersOfCartCollectionCart = cartCollectionCart.getMembers();
                cartCollectionCart.setMembers(members);
                cartCollectionCart = em.merge(cartCollectionCart);
                if (oldMembersOfCartCollectionCart != null) {
                    oldMembersOfCartCollectionCart.getCartCollection().remove(cartCollectionCart);
                    oldMembersOfCartCollectionCart = em.merge(oldMembersOfCartCollectionCart);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMembers(members.getUserid()) != null) {
                throw new PreexistingEntityException("Members " + members + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Members members) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Members persistentMembers = em.find(Members.class, members.getUserid());
            Collection<Orders> ordersCollectionOld = persistentMembers.getOrdersCollection();
            Collection<Orders> ordersCollectionNew = members.getOrdersCollection();
            Collection<Cart> cartCollectionOld = persistentMembers.getCartCollection();
            Collection<Cart> cartCollectionNew = members.getCartCollection();
            List<String> illegalOrphanMessages = null;
            for (Orders ordersCollectionOldOrders : ordersCollectionOld) {
                if (!ordersCollectionNew.contains(ordersCollectionOldOrders)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orders " + ordersCollectionOldOrders + " since its userid field is not nullable.");
                }
            }
            for (Cart cartCollectionOldCart : cartCollectionOld) {
                if (!cartCollectionNew.contains(cartCollectionOldCart)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cart " + cartCollectionOldCart + " since its members field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Orders> attachedOrdersCollectionNew = new ArrayList<Orders>();
            for (Orders ordersCollectionNewOrdersToAttach : ordersCollectionNew) {
                ordersCollectionNewOrdersToAttach = em.getReference(ordersCollectionNewOrdersToAttach.getClass(), ordersCollectionNewOrdersToAttach.getOno());
                attachedOrdersCollectionNew.add(ordersCollectionNewOrdersToAttach);
            }
            ordersCollectionNew = attachedOrdersCollectionNew;
            members.setOrdersCollection(ordersCollectionNew);
            Collection<Cart> attachedCartCollectionNew = new ArrayList<Cart>();
            for (Cart cartCollectionNewCartToAttach : cartCollectionNew) {
                cartCollectionNewCartToAttach = em.getReference(cartCollectionNewCartToAttach.getClass(), cartCollectionNewCartToAttach.getCartPK());
                attachedCartCollectionNew.add(cartCollectionNewCartToAttach);
            }
            cartCollectionNew = attachedCartCollectionNew;
            members.setCartCollection(cartCollectionNew);
            members = em.merge(members);
            for (Orders ordersCollectionNewOrders : ordersCollectionNew) {
                if (!ordersCollectionOld.contains(ordersCollectionNewOrders)) {
                    Members oldUseridOfOrdersCollectionNewOrders = ordersCollectionNewOrders.getUserid();
                    ordersCollectionNewOrders.setUserid(members);
                    ordersCollectionNewOrders = em.merge(ordersCollectionNewOrders);
                    if (oldUseridOfOrdersCollectionNewOrders != null && !oldUseridOfOrdersCollectionNewOrders.equals(members)) {
                        oldUseridOfOrdersCollectionNewOrders.getOrdersCollection().remove(ordersCollectionNewOrders);
                        oldUseridOfOrdersCollectionNewOrders = em.merge(oldUseridOfOrdersCollectionNewOrders);
                    }
                }
            }
            for (Cart cartCollectionNewCart : cartCollectionNew) {
                if (!cartCollectionOld.contains(cartCollectionNewCart)) {
                    Members oldMembersOfCartCollectionNewCart = cartCollectionNewCart.getMembers();
                    cartCollectionNewCart.setMembers(members);
                    cartCollectionNewCart = em.merge(cartCollectionNewCart);
                    if (oldMembersOfCartCollectionNewCart != null && !oldMembersOfCartCollectionNewCart.equals(members)) {
                        oldMembersOfCartCollectionNewCart.getCartCollection().remove(cartCollectionNewCart);
                        oldMembersOfCartCollectionNewCart = em.merge(oldMembersOfCartCollectionNewCart);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = members.getUserid();
                if (findMembers(id) == null) {
                    throw new NonexistentEntityException("The members with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Members members;
            try {
                members = em.getReference(Members.class, id);
                members.getUserid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The members with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Orders> ordersCollectionOrphanCheck = members.getOrdersCollection();
            for (Orders ordersCollectionOrphanCheckOrders : ordersCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Members (" + members + ") cannot be destroyed since the Orders " + ordersCollectionOrphanCheckOrders + " in its ordersCollection field has a non-nullable userid field.");
            }
            Collection<Cart> cartCollectionOrphanCheck = members.getCartCollection();
            for (Cart cartCollectionOrphanCheckCart : cartCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Members (" + members + ") cannot be destroyed since the Cart " + cartCollectionOrphanCheckCart + " in its cartCollection field has a non-nullable members field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(members);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Members> findMembersEntities() {
        return findMembersEntities(true, -1, -1);
    }

    public List<Members> findMembersEntities(int maxResults, int firstResult) {
        return findMembersEntities(false, maxResults, firstResult);
    }

    private List<Members> findMembersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Members.class));
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

    public Members findMembers(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Members.class, id);
        } finally {
            em.close();
        }
    }

    public int getMembersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Members> rt = cq.from(Members.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
