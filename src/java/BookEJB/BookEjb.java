/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookEJB;

import entity.Books;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ejb.Stateless;
/**
 *
 * @author Invalidquantum
 */
@Stateless
public class BookEjb {
    
    @PersistenceContext(unitName = "KristinaBookstorePU")
    private EntityManager em;

      public void persist(Object object) {
        em.persist(object);
    }
    
      public List<Books> listBySubjects(String subj) {      
      return em.createNamedQuery("Books.findBySubject", Books.class)
              .setParameter("subject",subj)
              .getResultList();
    }
      
      
    
}
