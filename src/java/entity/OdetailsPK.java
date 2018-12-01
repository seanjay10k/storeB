/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Invalidquantum
 */
@Embeddable
public class OdetailsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ONO")
    private int ono;
    @Basic(optional = false)
    @Column(name = "ISBN")
    private String isbn;

    public OdetailsPK() {
    }

    public OdetailsPK(int ono, String isbn) {
        this.ono = ono;
        this.isbn = isbn;
    }

    public int getOno() {
        return ono;
    }

    public void setOno(int ono) {
        this.ono = ono;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ono;
        hash += (isbn != null ? isbn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OdetailsPK)) {
            return false;
        }
        OdetailsPK other = (OdetailsPK) object;
        if (this.ono != other.ono) {
            return false;
        }
        if ((this.isbn == null && other.isbn != null) || (this.isbn != null && !this.isbn.equals(other.isbn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OdetailsPK[ ono=" + ono + ", isbn=" + isbn + " ]";
    }
    
}
