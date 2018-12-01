/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Invalidquantum
 */
@Entity
@Table(name = "MEMBERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Members.findAll", query = "SELECT m FROM Members m")
    , @NamedQuery(name = "Members.findByFname", query = "SELECT m FROM Members m WHERE m.fname = :fname")
    , @NamedQuery(name = "Members.findByLname", query = "SELECT m FROM Members m WHERE m.lname = :lname")
    , @NamedQuery(name = "Members.findByAddress", query = "SELECT m FROM Members m WHERE m.address = :address")
    , @NamedQuery(name = "Members.findByCity", query = "SELECT m FROM Members m WHERE m.city = :city")
    , @NamedQuery(name = "Members.findByState", query = "SELECT m FROM Members m WHERE m.state = :state")
    , @NamedQuery(name = "Members.findByZip", query = "SELECT m FROM Members m WHERE m.zip = :zip")
    , @NamedQuery(name = "Members.findByPhone", query = "SELECT m FROM Members m WHERE m.phone = :phone")
    , @NamedQuery(name = "Members.findByEmail", query = "SELECT m FROM Members m WHERE m.email = :email")
    , @NamedQuery(name = "Members.findByUserid", query = "SELECT m FROM Members m WHERE m.userid = :userid")
    , @NamedQuery(name = "Members.findByPassword", query = "SELECT m FROM Members m WHERE m.password = :password")
    , @NamedQuery(name = "Members.findByCreditcardtype", query = "SELECT m FROM Members m WHERE m.creditcardtype = :creditcardtype")
    , @NamedQuery(name = "Members.findByCreditcardnumber", query = "SELECT m FROM Members m WHERE m.creditcardnumber = :creditcardnumber")})
public class Members implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "FNAME")
    private String fname;
    @Basic(optional = false)
    @Column(name = "LNAME")
    private String lname;
    @Basic(optional = false)
    @Column(name = "ADDRESS")
    private String address;
    @Basic(optional = false)
    @Column(name = "CITY")
    private String city;
    @Basic(optional = false)
    @Column(name = "STATE")
    private String state;
    @Basic(optional = false)
    @Column(name = "ZIP")
    private int zip;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "EMAIL")
    private String email;
    @Id
    @Basic(optional = false)
    @Column(name = "USERID")
    private String userid;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "CREDITCARDTYPE")
    private String creditcardtype;
    @Column(name = "CREDITCARDNUMBER")
    private String creditcardnumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private Collection<Orders> ordersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "members")
    private Collection<Cart> cartCollection;

    public Members() {
    }

    public Members(String userid) {
        this.userid = userid;
    }
    
        public Members(String userid, String fname, String lname, String address, String city, String state, int zip) {
        this.userid = userid;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
   
    }

    public Members(String userid, String password, String fname, String lname, String address, String city, String state, int zip, String email,String creditNum, String creditType ) {
        this.userid = userid;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.password=password;
        this.email=email;
        this.creditcardnumber=creditNum;
        this.creditcardtype=creditType;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreditcardtype() {
        return creditcardtype;
    }

    public void setCreditcardtype(String creditcardtype) {
        this.creditcardtype = creditcardtype;
    }

    public String getCreditcardnumber() {
        return creditcardnumber;
    }

    public void setCreditcardnumber(String creditcardnumber) {
        this.creditcardnumber = creditcardnumber;
    }

    @XmlTransient
    public Collection<Orders> getOrdersCollection() {
        return ordersCollection;
    }

    public void setOrdersCollection(Collection<Orders> ordersCollection) {
        this.ordersCollection = ordersCollection;
    }

    @XmlTransient
    public Collection<Cart> getCartCollection() {
        return cartCollection;
    }

    public void setCartCollection(Collection<Cart> cartCollection) {
        this.cartCollection = cartCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Members)) {
            return false;
        }
        Members other = (Members) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Members[ userid=" + userid + " ]";
    }
    
}
