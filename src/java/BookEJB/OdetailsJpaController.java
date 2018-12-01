/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookEJB;

import BookEJB.exceptions.NonexistentEntityException;
import BookEJB.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Books;
import entity.Odetails;
import entity.OdetailsPK;
import entity.Orders;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Invalidquantum
 */
public class OdetailsJpaController implements Serializable {

    public OdetailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Odetails odetails) throws PreexistingEntityException, Exception {
        if (odetails.getOdetailsPK() == null) {
            odetails.setOdetailsPK(new OdetailsPK());
        }
        odetails.getOdetailsPK().setOno(odetails.getOrders().getOno());
        odetails.getOdetailsPK().setIsbn(odetails.getBooks().getIsbn());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Books books = odetails.getBooks();
            if (books != null) {
                books = em.getReference(books.getClass(), books.getIsbn());
                odetails.setBooks(books);
            }
            Orders orders = odetails.getOrders();
            if (orders != null) {
                orders = em.getReference(orders.getClass(), orders.getOno());
                odetails.setOrders(orders);
            }
            em.persist(odetails);
            if (books != null) {
                books.getOdetailsCollection().add(odetails);
                books = em.merge(books);
            }
            if (orders != null) {
                orders.getOdetailsCollection().add(odetails);
                orders = em.merge(orders);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOdetails(odetails.getOdetailsPK()) != null) {
                throw new PreexistingEntityException("Odetails " + odetails + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Odetails odetails) throws NonexistentEntityException, Exception {
        odetails.getOdetailsPK().setOno(odetails.getOrders().getOno());
        odetails.getOdetailsPK().setIsbn(odetails.getBooks().getIsbn());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Odetails persistentOdetails = em.find(Odetails.class, odetails.getOdetailsPK());
            Books booksOld = persistentOdetails.getBooks();
            Books booksNew = odetails.getBooks();
            Orders ordersOld = persistentOdetails.getOrders();
            Orders ordersNew = odetails.getOrders();
            if (booksNew != null) {
                booksNew = em.getReference(booksNew.getClass(), booksNew.getIsbn());
                odetails.setBooks(booksNew);
            }
            if (ordersNew != null) {
                ordersNew = em.getReference(ordersNew.getClass(), ordersNew.getOno());
                odetails.setOrders(ordersNew);
            }
            odetails = em.merge(odetails);
            if (booksOld != null && !booksOld.equals(booksNew)) {
                booksOld.getOdetailsCollection().remove(odetails);
                booksOld = em.merge(booksOld);
            }
            if (booksNew != null && !booksNew.equals(booksOld)) {
                booksNew.getOdetailsCollection().add(odetails);
                booksNew = em.merge(booksNew);
            }
            if (ordersOld != null && !ordersOld.equals(ordersNew)) {
                ordersOld.getOdetailsCollection().remove(odetails);
                ordersOld = em.merge(ordersOld);
            }
            if (ordersNew != null && !ordersNew.equals(ordersOld)) {
                ordersNew.getOdetailsCollection().add(odetails);
                ordersNew = em.merge(ordersNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                OdetailsPK id = odetails.getOdetailsPK();
                if (findOdetails(id) == null) {
                    throw new NonexistentEntityException("The odetails with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(OdetailsPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Odetails odetails;
            try {
                odetails = em.getReference(Odetails.class, id);
                odetails.getOdetailsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The odetails with id " + id + " no longer exists.", enfe);
            }
            Books books = odetails.getBooks();
            if (books != null) {
                books.getOdetailsCollection().remove(odetails);
                books = em.merge(books);
            }
            Orders orders = odetails.getOrders();
            if (orders != null) {
                orders.getOdetailsCollection().remove(odetails);
                orders = em.merge(orders);
            }
            em.remove(odetails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Odetails> findOdetailsEntities() {
        return findOdetailsEntities(true, -1, -1);
    }

    public List<Odetails> findOdetailsEntities(int maxResults, int firstResult) {
        return findOdetailsEntities(false, maxResults, firstResult);
    }

    private List<Odetails> findOdetailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Odetails.class));
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

    public Odetails findOdetails(OdetailsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Odetails.class, id);
        } finally {
            em.close();
        }
    }

    public int getOdetailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Odetails> rt = cq.from(Odetails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
