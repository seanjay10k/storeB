/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookEJB;

import BookEJB.exceptions.IllegalOrphanException;
import BookEJB.exceptions.NonexistentEntityException;
import BookEJB.exceptions.PreexistingEntityException;
import entity.Books;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Odetails;
import java.util.ArrayList;
import java.util.Collection;
import entity.Cart;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Invalidquantum
 */
public class BooksJpaController implements Serializable {

    public BooksJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Books books) throws PreexistingEntityException, Exception {
        if (books.getOdetailsCollection() == null) {
            books.setOdetailsCollection(new ArrayList<Odetails>());
        }
        if (books.getCartCollection() == null) {
            books.setCartCollection(new ArrayList<Cart>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Odetails> attachedOdetailsCollection = new ArrayList<Odetails>();
            for (Odetails odetailsCollectionOdetailsToAttach : books.getOdetailsCollection()) {
                odetailsCollectionOdetailsToAttach = em.getReference(odetailsCollectionOdetailsToAttach.getClass(), odetailsCollectionOdetailsToAttach.getOdetailsPK());
                attachedOdetailsCollection.add(odetailsCollectionOdetailsToAttach);
            }
            books.setOdetailsCollection(attachedOdetailsCollection);
            Collection<Cart> attachedCartCollection = new ArrayList<Cart>();
            for (Cart cartCollectionCartToAttach : books.getCartCollection()) {
                cartCollectionCartToAttach = em.getReference(cartCollectionCartToAttach.getClass(), cartCollectionCartToAttach.getCartPK());
                attachedCartCollection.add(cartCollectionCartToAttach);
            }
            books.setCartCollection(attachedCartCollection);
            em.persist(books);
            for (Odetails odetailsCollectionOdetails : books.getOdetailsCollection()) {
                Books oldBooksOfOdetailsCollectionOdetails = odetailsCollectionOdetails.getBooks();
                odetailsCollectionOdetails.setBooks(books);
                odetailsCollectionOdetails = em.merge(odetailsCollectionOdetails);
                if (oldBooksOfOdetailsCollectionOdetails != null) {
                    oldBooksOfOdetailsCollectionOdetails.getOdetailsCollection().remove(odetailsCollectionOdetails);
                    oldBooksOfOdetailsCollectionOdetails = em.merge(oldBooksOfOdetailsCollectionOdetails);
                }
            }
            for (Cart cartCollectionCart : books.getCartCollection()) {
                Books oldBooksOfCartCollectionCart = cartCollectionCart.getBooks();
                cartCollectionCart.setBooks(books);
                cartCollectionCart = em.merge(cartCollectionCart);
                if (oldBooksOfCartCollectionCart != null) {
                    oldBooksOfCartCollectionCart.getCartCollection().remove(cartCollectionCart);
                    oldBooksOfCartCollectionCart = em.merge(oldBooksOfCartCollectionCart);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBooks(books.getIsbn()) != null) {
                throw new PreexistingEntityException("Books " + books + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Books books) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Books persistentBooks = em.find(Books.class, books.getIsbn());
            Collection<Odetails> odetailsCollectionOld = persistentBooks.getOdetailsCollection();
            Collection<Odetails> odetailsCollectionNew = books.getOdetailsCollection();
            Collection<Cart> cartCollectionOld = persistentBooks.getCartCollection();
            Collection<Cart> cartCollectionNew = books.getCartCollection();
            List<String> illegalOrphanMessages = null;
            for (Odetails odetailsCollectionOldOdetails : odetailsCollectionOld) {
                if (!odetailsCollectionNew.contains(odetailsCollectionOldOdetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Odetails " + odetailsCollectionOldOdetails + " since its books field is not nullable.");
                }
            }
            for (Cart cartCollectionOldCart : cartCollectionOld) {
                if (!cartCollectionNew.contains(cartCollectionOldCart)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cart " + cartCollectionOldCart + " since its books field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Odetails> attachedOdetailsCollectionNew = new ArrayList<Odetails>();
            for (Odetails odetailsCollectionNewOdetailsToAttach : odetailsCollectionNew) {
                odetailsCollectionNewOdetailsToAttach = em.getReference(odetailsCollectionNewOdetailsToAttach.getClass(), odetailsCollectionNewOdetailsToAttach.getOdetailsPK());
                attachedOdetailsCollectionNew.add(odetailsCollectionNewOdetailsToAttach);
            }
            odetailsCollectionNew = attachedOdetailsCollectionNew;
            books.setOdetailsCollection(odetailsCollectionNew);
            Collection<Cart> attachedCartCollectionNew = new ArrayList<Cart>();
            for (Cart cartCollectionNewCartToAttach : cartCollectionNew) {
                cartCollectionNewCartToAttach = em.getReference(cartCollectionNewCartToAttach.getClass(), cartCollectionNewCartToAttach.getCartPK());
                attachedCartCollectionNew.add(cartCollectionNewCartToAttach);
            }
            cartCollectionNew = attachedCartCollectionNew;
            books.setCartCollection(cartCollectionNew);
            books = em.merge(books);
            for (Odetails odetailsCollectionNewOdetails : odetailsCollectionNew) {
                if (!odetailsCollectionOld.contains(odetailsCollectionNewOdetails)) {
                    Books oldBooksOfOdetailsCollectionNewOdetails = odetailsCollectionNewOdetails.getBooks();
                    odetailsCollectionNewOdetails.setBooks(books);
                    odetailsCollectionNewOdetails = em.merge(odetailsCollectionNewOdetails);
                    if (oldBooksOfOdetailsCollectionNewOdetails != null && !oldBooksOfOdetailsCollectionNewOdetails.equals(books)) {
                        oldBooksOfOdetailsCollectionNewOdetails.getOdetailsCollection().remove(odetailsCollectionNewOdetails);
                        oldBooksOfOdetailsCollectionNewOdetails = em.merge(oldBooksOfOdetailsCollectionNewOdetails);
                    }
                }
            }
            for (Cart cartCollectionNewCart : cartCollectionNew) {
                if (!cartCollectionOld.contains(cartCollectionNewCart)) {
                    Books oldBooksOfCartCollectionNewCart = cartCollectionNewCart.getBooks();
                    cartCollectionNewCart.setBooks(books);
                    cartCollectionNewCart = em.merge(cartCollectionNewCart);
                    if (oldBooksOfCartCollectionNewCart != null && !oldBooksOfCartCollectionNewCart.equals(books)) {
                        oldBooksOfCartCollectionNewCart.getCartCollection().remove(cartCollectionNewCart);
                        oldBooksOfCartCollectionNewCart = em.merge(oldBooksOfCartCollectionNewCart);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = books.getIsbn();
                if (findBooks(id) == null) {
                    throw new NonexistentEntityException("The books with id " + id + " no longer exists.");
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
            Books books;
            try {
                books = em.getReference(Books.class, id);
                books.getIsbn();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The books with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Odetails> odetailsCollectionOrphanCheck = books.getOdetailsCollection();
            for (Odetails odetailsCollectionOrphanCheckOdetails : odetailsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the Odetails " + odetailsCollectionOrphanCheckOdetails + " in its odetailsCollection field has a non-nullable books field.");
            }
            Collection<Cart> cartCollectionOrphanCheck = books.getCartCollection();
            for (Cart cartCollectionOrphanCheckCart : cartCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the Cart " + cartCollectionOrphanCheckCart + " in its cartCollection field has a non-nullable books field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(books);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Books> findBooksEntities() {
        return findBooksEntities(true, -1, -1);
    }

    public List<Books> findBooksEntities(int maxResults, int firstResult) {
        return findBooksEntities(false, maxResults, firstResult);
    }

    private List<Books> findBooksEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Books.class));
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

    public Books findBooks(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Books.class, id);
        } finally {
            em.close();
        }
    }

    public int getBooksCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Books> rt = cq.from(Books.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
