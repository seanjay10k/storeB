/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kristinabookstore;

import entity.Books;
import entity.Cart;
import entity.Members;
import entity.Orders;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Invalidquantum
 */
public class dao {

    public static int orderno=100;
    Connection con= null;
    public dao(){
       
         try {
             con= DriverManager.getConnection("jdbc:derby://localhost:1527/kristiDB","scott","tiger");
         } catch (SQLException ex) {
             Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
         }   
         //String sql = " INSERT INTO BOOKS(isbn,author,title,price,subject) VALUES(?,?,?,?,?) ";   
        
    }
    

    
    
    
    
    public Map<Integer,String> browseBySubject(){
       Map<Integer,String> mp= new HashMap<>();
        String q1="select distinct subject from books";
        try {
            Statement stmt= con.createStatement();
            ResultSet rs= stmt.executeQuery(q1);
            int i=0;
            while(rs.next()){
                String subject= rs.getString("SUBJECT");
                mp.put(i++, subject);
            }
            rs.close();
            stmt.close();
           
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }      
         return mp;        
    }
    
    //getCart();
    
    
    
        public List<Books> booksBySubject(String subj){
        List<Books> books = new ArrayList<>();
        Books b= new Books();
        
        String q2="SELECT * FROM Books WHERE subject=\'"+subj+"\'";
        try {
            Statement stmt= con.createStatement();
            ResultSet rs= stmt.executeQuery(q2);
            int i=0;
            while(rs.next()){
                String isbn = rs.getString("ISBN");
                String author = rs.getString("AUTHOR");
                String title = rs.getString("TITLE");
                String price = rs.getString("PRICE");
                String sub = rs.getString("SUBJECT");
                System.out.println(isbn+"  "+author+"  "+title+"  "+price+"  "+sub+"  ");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }      
         return books;        
    }

        
         public List<Books> booksByAuthor(String auth){
        List<Books> books = new ArrayList<>();
        Books b= new Books();
        //like \'%"+ auth + "%\'"
        String q2="SELECT * FROM Books WHERE author like \'%"+ auth + "%\'";
        try {
            Statement stmt= con.createStatement();
            ResultSet rs= stmt.executeQuery(q2);
            int i=0;
            while(rs.next()){
                String isbn = rs.getString("ISBN");
                String author = rs.getString("AUTHOR");
                String title = rs.getString("TITLE");
                String price = rs.getString("PRICE");
                String sub = rs.getString("SUBJECT");
                System.out.println(isbn+"  "+author+"  "+title+"  "+price+"  "+sub+"  ");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }      
         return books;        
    }
        public List<Books> booksByTitle(String t){
        List<Books> books = new ArrayList<>();
        Books b= new Books();
        //like \'%"+ auth + "%\'"
        String q2="SELECT * FROM Books WHERE title like \'%"+ t + "%\'";
        try {
            Statement stmt= con.createStatement();
            ResultSet rs= stmt.executeQuery(q2);
            int i=0;
            while(rs.next()){
                String isbn = rs.getString("ISBN");
                String author = rs.getString("AUTHOR");
                String title = rs.getString("TITLE");
                String price = rs.getString("PRICE");
                String sub = rs.getString("SUBJECT");
                System.out.println(isbn+"  "+author+"  "+title+"  "+price+"  "+sub+"  ");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }      
         return books;        
    }
        
        
    public Books findByIsbn(String j) {
        String q3="SELECT * FROM Books WHERE isbn=\'"+j+"\'";
        Books bk= null;
         try {
            Statement stmt= con.createStatement();
            ResultSet rs= stmt.executeQuery(q3);
            int i=0;
            while(rs.next()){
                String isbn = rs.getString("ISBN");
                String author = rs.getString("AUTHOR");
                String title = rs.getString("TITLE");
                String price = rs.getString("PRICE");
                double dP= Double.parseDouble(price);
                String sub = rs.getString("SUBJECT");
                //System.out.println(isbn+"  "+author+"  "+title+"  "+price+"  "+sub+"  ");
            bk= new Books(isbn, author, title, dP, sub);
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }      
                
          return bk;      
                
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
      public int registerUser(Members m) {
        
        String q4="Insert into Members(FNAME,LNAME,ADDRESS,CITY,STATE,ZIP,PHONE,EMAIL,USERID,PASSWORD,CREDITCARDTYPE,CREDITCARDNUMBER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"  ;//"SELECT * FROM Books WHERE isbn=\'"+j+"\'";
        //Books bk= null;
        int success=0;
         try {
            PreparedStatement ps= con.prepareStatement(q4);
           ps.setString(1, m.getFname());
           ps.setString(2, m.getLname());
           ps.setString(3, m.getAddress());
           ps.setString(4, m.getCity());
           ps.setString(5, m.getState());
           ps.setInt(6, m.getZip());
           ps.setString(7, m.getPhone());
           ps.setString(8, m.getEmail());
           ps.setString(9, m.getUserid());
           ps.setString(10, m.getPassword());
           ps.setString(11, m.getCreditcardtype());
           ps.setString(12, m.getCreditcardnumber());
           success= ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }      
         return success;       
            
                
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }  
      
      public int getUser(String uname, String pass) {
        //String q3="SELECT * FROM Books WHERE isbn=\'"+j+"\'";
        int success=0;
        String q4="Select * from Members where userid=\'"+uname+"\' and password=\'"+pass+"\' ";//(FNAME,LNAME,ADDRESS,CITY,STATE,ZIP,PHONE,EMAIL,USERID,PASSWORD,CREDITCARDTYPE,CREDITCARDNUMBER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"  ;//"SELECT * FROM Books WHERE isbn=\'"+j+"\'";
       
        Statement stmt;
        try {
            stmt = con.createStatement();
             ResultSet rs= stmt.executeQuery(q4);
             if(rs.next()){
                 success=1;
             }
            
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }    
         return success;                   
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }  
        
      
    public int addToCart(String uid, String isbn, int qty) {
        
        String q4="Insert into Cart(userid,isbn,qty)VALUES(?,?,?)"  ;//"SELECT * FROM Books WHERE isbn=\'"+j+"\'";
        //Books bk= null;
        int success=0;
         try {
            PreparedStatement ps= con.prepareStatement(q4);
            
            ps.setString(1, uid);
           ps.setString(2, isbn);       
           ps.setInt(3, qty);
          
           success= ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }      
         return success;       
            
                
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }  
    
    
    public List<Cart> getCart(String uid) {
        
        String q4="Select * from Cart where userid=\'"+uid+"\'";//"Insert into Cart(userid,isbn,qty)VALUES(?,?,?)"  ;//"SELECT * FROM Books WHERE isbn=\'"+j+"\'";
        //Books bk= null;
        List<Cart> cartList= new ArrayList<>();
        //List<Books> bookList= new ArrayList<>();
        Statement stmt;
        try {
            stmt = con.createStatement();
             ResultSet rs= stmt.executeQuery(q4);
             while(rs.next()){
                 String userid=rs.getString("USERID");
                 String isbn=rs.getString("ISBN");
                 int qty=rs.getInt("QTY");
                 //System.out.println(userid+isbn+qty);
               Cart nC= new Cart(uid, isbn,qty);
               //nC.setQty(qty);
               cartList.add(nC);
             }
            
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }    
         return cartList;  

    } 

  

    public int deleteByIsbn(String bn, String uID) {
        int success=0;
        String q4="Delete from Cart where userid=\'"+uID+"\' and isbn=\'"+bn+"\' ";
       Statement stmt;
        try {
            stmt = con.createStatement();
            //stmt.execute(q4);
            success=stmt.executeUpdate(q4);
               
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        System.out.print(success+"  xxxxxxxxxxxxxxxxxxxxxxxxx");
        return success;
        
    }

    public int editByIsbn(String bn, String uID, int qty2) {
        
         int success=0;
         
        String q4="update Cart set qty=? where userid=? and isbn=? ";
       Statement stmt;
        try {
       PreparedStatement ps= con.prepareStatement(q4);
            ps.setInt(1, qty2);
            ps.setString(2, uID);
           ps.setString(3, bn);       
           
          
           success= ps.executeUpdate();
               
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        System.out.print(success+"  xxxxxxxxxxxxxxxxxxxxxxxxx");
        return success;

    }

   public Members getUserInfo(String uID) {
        String q4="Select * from Members where userid=\'"+uID+"\'";
        int success=0;
        Statement stmt;
        Members m=null;
        //Orders o= new Orders
        
        try {
            stmt = con.createStatement();
            ResultSet rs= stmt.executeQuery(q4);
             while(rs.next()){
                 
                 String fname=rs.getString("FNAME");//FNAME,LNAME,ADDRESS,CITY,STATE,ZIP,
                 String lname=rs.getString("LNAME");
                 String address=rs.getString("ADDRESS");
                 String city=rs.getString("CITY");
                 String state=rs.getString("STATE");
                 int zip=rs.getInt("ZIP");
                 //CAN GET MORE DATA HERE
               m= new Members(uID, fname, lname, address, city, state, zip);
             }
               
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        System.out.print(success+"  xxxxxxxxxxxxxxxxxxxxxxxxx");
        
        return m;
    }



    

    public int addToOrders(String uID, List<Cart> cartList, Members m) {
         String q4="Insert into Orders(userid,ono,received,shipped,shipAddress,shipCity,shipState,shipZip)VALUES(?,?,?,?,?,?,?,?)"  ;//"SELECT * FROM Books WHERE isbn=\'"+j+"\'";
        //Books bk= null;
        System.out.println("Invoice for Order #"+orderno);
        int success=0;
        long millis=System.currentTimeMillis();  
        java.sql.Date date=new java.sql.Date(millis);  
         try {
            PreparedStatement ps= con.prepareStatement(q4);
            
           ps.setString(1, uID);
           ps.setInt(2, orderno);       
           ps.setDate(3, date);
           ps.setDate(4, date);
           ps.setString(5, m.getAddress());
            ps.setString(6, m.getCity());
             ps.setString(7, m.getState());
              ps.setInt(8, m.getZip());
          
           success= ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }      
         return success;  
    }



    void displayOrderInvoice(String uID) {
        
         String q4="Select * from Orders where userid=\'"+uID+"\'";//"Insert into Cart(userid,isbn,qty)VALUES(?,?,?)"  ;//"SELECT * FROM Books WHERE isbn=\'"+j+"\'";

        Statement stmt;
        try {
            stmt = con.createStatement();
             ResultSet rs= stmt.executeQuery(q4);
             while(rs.next()){
                 String a=rs.getString("USERID");
                 //userid,ono,received,shipped,shipAddress,shipCity,shipState,shipZip
                 int b= rs.getInt("ono");
                 Date c=rs.getDate("received");
                 Date d=rs.getDate("shipped");
                 String e= rs.getString("shipaddress");
                 String f=rs.getString("shipcity");
                 String g= rs.getString("shipstate");
                 int h=rs.getInt("shipzip");
                 System.out.println(a+"  "+b+"  "+c.toString()+"  "+d.toString()+"  "+e+"  "+f+"  "+g+"  "+h+"  ");
             }
             
            
        } catch (SQLException ex) {
            Logger.getLogger(dao.class.getName()).log(Level.SEVERE, null, ex);
        }    

    }

    
    
}
