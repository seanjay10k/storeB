/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Invalidquantum
 */
@Entity
@Table(name = "CART")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cart.findAll", query = "SELECT c FROM Cart c")
    , @NamedQuery(name = "Cart.findByUserid", query = "SELECT c FROM Cart c WHERE c.cartPK.userid = :userid")
    , @NamedQuery(name = "Cart.findByIsbn", query = "SELECT c FROM Cart c WHERE c.cartPK.isbn = :isbn")
    , @NamedQuery(name = "Cart.findByQty", query = "SELECT c FROM Cart c WHERE c.qty = :qty")})
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CartPK cartPK;
    @Basic(optional = false)
    @Column(name = "QTY")
    private int qty;
    private String isbn;
    private String userid;
    @JoinColumn(name = "ISBN", referencedColumnName = "ISBN", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Books books;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Members members;

    public Cart() {
    }

    public Cart(CartPK cartPK) {
        this.cartPK = cartPK;
    }

    public Cart(CartPK cartPK, int qty) {
        this.cartPK = cartPK;
        this.qty = qty;
    }

    public Cart(String userid, String isbn) {
        this.cartPK = new CartPK(userid, isbn);
    
    }
    public Cart(String userid, String isbn,int qty) {
        this.cartPK = new CartPK(userid, isbn);
        this.isbn=isbn;
        this.qty=qty;
        this.userid=userid;
        
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

//    public Cart(String uid, String bn, int qty) {
//        this.qty=qty;
//        this
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    public CartPK getCartPK() {
        return cartPK;
    }

    public void setCartPK(CartPK cartPK) {
        this.cartPK = cartPK;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cartPK != null ? cartPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cart)) {
            return false;
        }
        Cart other = (Cart) object;
        if ((this.cartPK == null && other.cartPK != null) || (this.cartPK != null && !this.cartPK.equals(other.cartPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Cart[ cartPK=" + cartPK + " ]";
    }
    
}
