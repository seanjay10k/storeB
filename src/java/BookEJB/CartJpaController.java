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
import entity.Cart;
import entity.CartPK;
import entity.Members;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Invalidquantum
 */
public class CartJpaController implements Serializable {

    public CartJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cart cart) throws PreexistingEntityException, Exception {
        if (cart.getCartPK() == null) {
            cart.setCartPK(new CartPK());
        }
        cart.getCartPK().setIsbn(cart.getBooks().getIsbn());
        cart.getCartPK().setUserid(cart.getMembers().getUserid());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Books books = cart.getBooks();
            if (books != null) {
                books = em.getReference(books.getClass(), books.getIsbn());
                cart.setBooks(books);
            }
            Members members = cart.getMembers();
            if (members != null) {
                members = em.getReference(members.getClass(), members.getUserid());
                cart.setMembers(members);
            }
            em.persist(cart);
            if (books != null) {
                books.getCartCollection().add(cart);
                books = em.merge(books);
            }
            if (members != null) {
                members.getCartCollection().add(cart);
                members = em.merge(members);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCart(cart.getCartPK()) != null) {
                throw new PreexistingEntityException("Cart " + cart + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cart cart) throws NonexistentEntityException, Exception {
        cart.getCartPK().setIsbn(cart.getBooks().getIsbn());
        cart.getCartPK().setUserid(cart.getMembers().getUserid());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cart persistentCart = em.find(Cart.class, cart.getCartPK());
            Books booksOld = persistentCart.getBooks();
            Books booksNew = cart.getBooks();
            Members membersOld = persistentCart.getMembers();
            Members membersNew = cart.getMembers();
            if (booksNew != null) {
                booksNew = em.getReference(booksNew.getClass(), booksNew.getIsbn());
                cart.setBooks(booksNew);
            }
            if (membersNew != null) {
                membersNew = em.getReference(membersNew.getClass(), membersNew.getUserid());
                cart.setMembers(membersNew);
            }
            cart = em.merge(cart);
            if (booksOld != null && !booksOld.equals(booksNew)) {
                booksOld.getCartCollection().remove(cart);
                booksOld = em.merge(booksOld);
            }
            if (booksNew != null && !booksNew.equals(booksOld)) {
                booksNew.getCartCollection().add(cart);
                booksNew = em.merge(booksNew);
            }
            if (membersOld != null && !membersOld.equals(membersNew)) {
                membersOld.getCartCollection().remove(cart);
                membersOld = em.merge(membersOld);
            }
            if (membersNew != null && !membersNew.equals(membersOld)) {
                membersNew.getCartCollection().add(cart);
                membersNew = em.merge(membersNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CartPK id = cart.getCartPK();
                if (findCart(id) == null) {
                    throw new NonexistentEntityException("The cart with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CartPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cart cart;
            try {
                cart = em.getReference(Cart.class, id);
                cart.getCartPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cart with id " + id + " no longer exists.", enfe);
            }
            Books books = cart.getBooks();
            if (books != null) {
                books.getCartCollection().remove(cart);
                books = em.merge(books);
            }
            Members members = cart.getMembers();
            if (members != null) {
                members.getCartCollection().remove(cart);
                members = em.merge(members);
            }
            em.remove(cart);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cart> findCartEntities() {
        return findCartEntities(true, -1, -1);
    }

    public List<Cart> findCartEntities(int maxResults, int firstResult) {
        return findCartEntities(false, maxResults, firstResult);
    }

    private List<Cart> findCartEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cart.class));
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

    public Cart findCart(CartPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cart.class, id);
        } finally {
            em.close();
        }
    }

    public int getCartCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cart> rt = cq.from(Cart.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
