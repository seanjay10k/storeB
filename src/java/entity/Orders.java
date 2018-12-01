/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Invalidquantum
 */
@Entity
@Table(name = "ORDERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o")
    , @NamedQuery(name = "Orders.findByOno", query = "SELECT o FROM Orders o WHERE o.ono = :ono")
    , @NamedQuery(name = "Orders.findByReceived", query = "SELECT o FROM Orders o WHERE o.received = :received")
    , @NamedQuery(name = "Orders.findByShipped", query = "SELECT o FROM Orders o WHERE o.shipped = :shipped")
    , @NamedQuery(name = "Orders.findByShipaddress", query = "SELECT o FROM Orders o WHERE o.shipaddress = :shipaddress")
    , @NamedQuery(name = "Orders.findByShipcity", query = "SELECT o FROM Orders o WHERE o.shipcity = :shipcity")
    , @NamedQuery(name = "Orders.findByShipstate", query = "SELECT o FROM Orders o WHERE o.shipstate = :shipstate")
    , @NamedQuery(name = "Orders.findByShipzip", query = "SELECT o FROM Orders o WHERE o.shipzip = :shipzip")})
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ONO")
    private Integer ono;
    @Basic(optional = false)
    @Column(name = "RECEIVED")
    @Temporal(TemporalType.DATE)
    private Date received;
    @Column(name = "SHIPPED")
    @Temporal(TemporalType.DATE)
    private Date shipped;
    @Column(name = "SHIPADDRESS")
    private String shipaddress;
    @Column(name = "SHIPCITY")
    private String shipcity;
    @Column(name = "SHIPSTATE")
    private String shipstate;
    @Column(name = "SHIPZIP")
    private Integer shipzip;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    private Collection<Odetails> odetailsCollection;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne(optional = false)
    private Members userid;

    public Orders() {
    }

    public Orders(Integer ono) {
        this.ono = ono;
    }

    public Orders(Integer ono, Date received) {
        this.ono = ono;
        this.received = received;
    }

    public Integer getOno() {
        return ono;
    }

    public void setOno(Integer ono) {
        this.ono = ono;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    public Date getShipped() {
        return shipped;
    }

    public void setShipped(Date shipped) {
        this.shipped = shipped;
    }

    public String getShipaddress() {
        return shipaddress;
    }

    public void setShipaddress(String shipaddress) {
        this.shipaddress = shipaddress;
    }

    public String getShipcity() {
        return shipcity;
    }

    public void setShipcity(String shipcity) {
        this.shipcity = shipcity;
    }

    public String getShipstate() {
        return shipstate;
    }

    public void setShipstate(String shipstate) {
        this.shipstate = shipstate;
    }

    public Integer getShipzip() {
        return shipzip;
    }

    public void setShipzip(Integer shipzip) {
        this.shipzip = shipzip;
    }

    @XmlTransient
    public Collection<Odetails> getOdetailsCollection() {
        return odetailsCollection;
    }

    public void setOdetailsCollection(Collection<Odetails> odetailsCollection) {
        this.odetailsCollection = odetailsCollection;
    }

    public Members getUserid() {
        return userid;
    }

    public void setUserid(Members userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ono != null ? ono.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orders)) {
            return false;
        }
        Orders other = (Orders) object;
        if ((this.ono == null && other.ono != null) || (this.ono != null && !this.ono.equals(other.ono))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Orders[ ono=" + ono + " ]";
    }
    
}
