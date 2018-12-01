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
@Table(name = "ODETAILS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Odetails.findAll", query = "SELECT o FROM Odetails o")
    , @NamedQuery(name = "Odetails.findByOno", query = "SELECT o FROM Odetails o WHERE o.odetailsPK.ono = :ono")
    , @NamedQuery(name = "Odetails.findByIsbn", query = "SELECT o FROM Odetails o WHERE o.odetailsPK.isbn = :isbn")
    , @NamedQuery(name = "Odetails.findByQty", query = "SELECT o FROM Odetails o WHERE o.qty = :qty")
    , @NamedQuery(name = "Odetails.findByPrice", query = "SELECT o FROM Odetails o WHERE o.price = :price")})
public class Odetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OdetailsPK odetailsPK;
    @Basic(optional = false)
    @Column(name = "QTY")
    private int qty;
    @Basic(optional = false)
    @Column(name = "PRICE")
    private int price;
    @JoinColumn(name = "ISBN", referencedColumnName = "ISBN", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Books books;
    @JoinColumn(name = "ONO", referencedColumnName = "ONO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Orders orders;

    public Odetails() {
    }

    public Odetails(OdetailsPK odetailsPK) {
        this.odetailsPK = odetailsPK;
    }

    public Odetails(OdetailsPK odetailsPK, int qty, int price) {
        this.odetailsPK = odetailsPK;
        this.qty = qty;
        this.price = price;
    }

    public Odetails(int ono, String isbn) {
        this.odetailsPK = new OdetailsPK(ono, isbn);
    }

    public OdetailsPK getOdetailsPK() {
        return odetailsPK;
    }

    public void setOdetailsPK(OdetailsPK odetailsPK) {
        this.odetailsPK = odetailsPK;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (odetailsPK != null ? odetailsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Odetails)) {
            return false;
        }
        Odetails other = (Odetails) object;
        if ((this.odetailsPK == null && other.odetailsPK != null) || (this.odetailsPK != null && !this.odetailsPK.equals(other.odetailsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Odetails[ odetailsPK=" + odetailsPK + " ]";
    }
    
}
