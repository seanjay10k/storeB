/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kristinabookstore;

import BookEJB.BookEjb;
import entity.Books;
import entity.Cart;
import entity.Members;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Invalidquantum
 */
public class KristinaBookstore {

Map<Integer,String> mp= new HashMap<>();
public static String uID=null;
     
 
    public static void main(String[] args) {
     Scanner sc= new Scanner(System.in);
      dao d= new dao();
      
      //login
      System.out.println("Would you like to login? 1: Yes 0:No");
      int p1= sc.nextInt();
      if(p1==1){
       userLogin(d);
      }
   
      //registration
      System.out.println("Would you like to register a user? 1: Yes 0:No");
      int prompt= sc.nextInt();
      if(prompt==1){
     addUser(d);
      }
    
      System.out.println("Please make a choice");
       System.out.println("\n 1. Browse by Subject \n 2. Search by Author/Title/Subject \n  3. View/Edit Shopping Cart \n "
                + "4. Check Order Status \n 5. Check Out \n  6. One Click Check Out \n  7. View/Edit Personal Information \n 8. Logout");
      int choice=sc.nextInt();sc.nextLine();
      //list all subjects
       
      //ask to enter isbn and add to cart
        
      
      switch(choice){
          case 1:  listSubjects(d); isbnToCart(d); break;
          case 2:  listATS(d);isbnToCart(d);break;
          case 3:  System.out.println("Current Cart Contents:");showCart(d);editCart(d);break;
          case 5:  
                    System.out.println("Current Cart Contents:");
                    showCart(d);
                    System.out.println("Proceed to checkout(y/n)");
                    char ch= sc.nextLine().toLowerCase().charAt(0);
                    List<Object> mem=null;
                    Members m= d.getUserInfo(uID);
                    if(ch=='y'){
                        mem=enterNewAddress();
                    }
                    if(mem==null){
                     checkOutAssist(d);   
                    }
                    else{
                        List<Cart> clist= new ArrayList<>();
                        showInfoNew(mem, m);
                        clist=showCart(d);
                    //d.checkout(m);
                    //checkOut(m,d);
                    
                    d.addToOrders(uID, clist, m);
                        
                    }
                  
                    
                    break;
                    
                    
                    //d.checkout(m);
                    //checkOut(m,d);
                    //d.addToOrders(uID, clist, m);
                    
                   // break;
          case 6:  
                   checkOutAssist(d);
                    break;
          
      }
 
    }
     private static void showInfoNew(List<Object> l,Members m) {
        //List<Cart> cartList= new ArrayList<>();
         
         //showMember(m);        
         System.out.println("Shipping Address\t\t\t\t\t\tBilling Address");
         System.out.println("Name:"+m.getFname()+" "+m.getLname()+"\t\t\t\t\t"+"Name:"+l.get(0).toString()+" "+l.get(1).toString());
         System.out.println("Address:"+m.getAddress()+"\t\t\t\t\t"+"Name:"+l.get(2).toString());
         System.out.println(m.getCity()+"\t\t\t\t\t"+l.get(3).toString());
         System.out.println(m.getState()+" "+m.getZip()+"\t\t\t\t\t"+l.get(4).toString()+" "+l.get(5).toString());
         
         //cartList= showCart(d);       
         //ONO INTEGER , RECEIVED DATE, SHIPADDRESS, SHIPCITY, SHIPPED DATE, SHIPSTATE, SHIPZIP INTEGER, USERID)
         //d.addToOrders(uID,cartList,m,ono);
         
         
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private static void checkOutAssist(dao d){
                     Members m= d.getUserInfo(uID);
                    List<Cart> clist= new ArrayList<>();
                    showInfo(m,d);
                    clist=showCart(d);
                    //d.checkout(m);
                    //checkOut(m,d);
                    d.addToOrders(uID, clist, m);
    }
    
    private static List<Object> enterNewAddress() {
          Scanner sc=new Scanner(System.in);
          List<Object> m=new ArrayList<>();
               String fName,lName,add,city,state;int zipNumber;
     System.out.println("Do you want to enter new Shipping address(y/n): ");
     char ch= sc.nextLine().toLowerCase().charAt(0);
                    if(ch=='y'){
                       
     System.out.println("Enter First Name");
     fName= sc.nextLine();
     System.out.println("Enter Last Name");
     lName= sc.nextLine();
     System.out.println("Enter Address");
     add= sc.nextLine();
     System.out.println("Enter City");
     city= sc.nextLine();
     System.out.println("Enter State");
     state= sc.nextLine();
     System.out.println("Enter Zip Number");
     zipNumber= sc.nextInt();
     sc.nextLine();
     m=Arrays.asList(uID, fName, lName, add, city, state, zipNumber);
     }
                    System.out.println("Do you want to enter new Credit Card Number(y/n): ");
     char ch1= sc.nextLine().toLowerCase().charAt(0);
                    if(ch1=='y'){
     System.out.println("Enter Credit Card Type");
     String creditcardtype= sc.nextLine();
     System.out.println("Enter Credit Card Number");
     String creditcardnumber= sc.nextLine();
   }
                    
return m;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     private static void showInfo(Members m,dao d) {
        //List<Cart> cartList= new ArrayList<>();
         
         //showMember(m);        
         System.out.println("Shipping Address\t\t\t\t\t\tBilling Address");
         System.out.println("Name:"+m.getFname()+" "+m.getLname()+"\t\t\t\t\t"+"Name:"+m.getFname()+" "+m.getLname());
         System.out.println("Address:"+m.getAddress()+"\t\t\t\t\t"+"Name:"+m.getAddress());
         System.out.println(m.getCity()+"\t\t\t\t\t"+m.getCity());
         System.out.println(m.getState()+" "+m.getZip()+"\t\t\t\t\t"+m.getState()+" "+m.getZip());
         
         //cartList= showCart(d);       
         //ONO INTEGER , RECEIVED DATE, SHIPADDRESS, SHIPCITY, SHIPPED DATE, SHIPSTATE, SHIPZIP INTEGER, USERID)
         //d.addToOrders(uID,cartList,m,ono);
         
         
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
      private static void showMember(Members m) {
        
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     
     private static void checkOut(Members m, dao d) {
        
         
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public static List<Cart> showCart(dao d){
        
        List<Cart> cartList=d.getCart(uID);
        double total=0;
        for(Cart c: cartList){            
            Books b=d.findByIsbn(c.getIsbn());
            int qty=c.getQty();
            System.out.println(b.getIsbn()+"  "+b.getTitle()+"  "+b.getPrice()+"  "+qty+"  "+(b.getPrice()*qty));
            total+=(b.getPrice()*qty);
        }      
        System.out.println("Total=                                                  "+total);
     return cartList;   
    }
    
    
     private static void editCart(dao d) {
         Scanner sc= new Scanner(System.in);
         System.out.println("Enter d to delete; e to edit your cart");
         String choice=sc.nextLine();
         switch(choice.charAt(0)){
             case 'd':  System.out.println("Enter ISBN to delete");
                        String isbn=sc.nextLine();
                        int success=d.deleteByIsbn(isbn,uID);
                        if(success==1){
                        System.out.println("Deleted");
                        }
                        break;
             case 'e':  System.out.println("Enter ISBN to edit");
                        String isbn2=sc.nextLine();
                        System.out.println("Enter new qty");
                        int qty2=sc.nextInt();sc.nextLine();
                        int success2=d.editByIsbn(isbn2,uID,qty2);
             
         }
         
         
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    public static void listATS(dao d){
         System.out.println("\n 1. Author Search 2. Title Search ");//3. Go Back to Member Menu");
         Scanner sc= new Scanner(System.in);
         int choice= sc.nextInt(); sc.nextLine();
        if (choice==1){
            authorOption(d);
        }
        else if(choice==2){
            titleOption(d);
        }
        else{
            System.out.println("Sorry you didnt make right selection");
        }
    }
    public static void authorOption(dao d){
        List<Books> bls= new ArrayList<>();
        Scanner sc= new Scanner(System.in);
        System.out.println("Enter author name or part of name");
        String auth=sc.nextLine();
        bls= d.booksByAuthor(auth);       
    }
    public static void titleOption(dao d){
       List<Books> bls= new ArrayList<>();
        Scanner sc= new Scanner(System.in);
        System.out.println("Enter title name or part of name");
        String t=sc.nextLine();
        bls= d.booksByTitle(t); 
        
    }
    
    public static void addUser(dao d){
        
        Scanner sc=new Scanner(System.in);
     System.out.println("Register new user");
     
     System.out.println("Enter First Name");
     String fName= sc.nextLine();
     System.out.println("Enter Last Name");
     String lName= sc.nextLine();
     System.out.println("Enter Address");
     String add= sc.nextLine();
     System.out.println("Enter City");
     String city= sc.nextLine();
     System.out.println("Enter State");
     String state= sc.nextLine();
     System.out.println("Enter Zip Number");
     int zipNumber= sc.nextInt();
     sc.nextLine();
     System.out.println("Enter Phone");
     String phone= sc.nextLine();
     System.out.println("Enter Email");
     String email= sc.nextLine();
     System.out.println("Enter UserID");
     String userid= sc.nextLine();
     System.out.println("Enter Password");
     String password= sc.nextLine();
     System.out.println("Enter Credit Card Type");
     String creditcardtype= sc.nextLine();
     System.out.println("Enter Creedit Card Number");
     String creditcardnumber= sc.nextLine();
     
     Members member= new Members(userid, password, fName, lName, add, city, state, zipNumber, email, city, creditcardtype);
     int success=d.registerUser(member);
     if(success==1){
         System.out.println("Successfully added");
     }
     else{
          System.out.println("Register not successfull");
    }

    
}
    public static void userLogin(dao d){
    Scanner sc= new Scanner(System.in);
    System.out.println("Member Login:");
     System.out.println("Enter UserID");
     String uid= sc.nextLine();
     System.out.println("Enter Password");
     String pword= sc.nextLine();
     int foundUser= d.getUser(uid, pword);
     if(foundUser==1){
         uID=uid;
     }
     System.out.println(uID);
    
    
    }
    
    public static void listSubjects(dao d){
        Scanner sc= new Scanner(System.in);
        Map<Integer,String> mp= new HashMap<>();
    
     mp=d.browseBySubject();
      for(int i=0;i<mp.size();i++){         
      System.out.println(i+"...."+mp.get(i));         
      }
        System.out.println("Choose a subject");
      int i=sc.nextInt();
      String sub= mp.get(i);
           System.out.println(sub);
    
      List<Books> blst= d.booksBySubject(sub);
      for(Books bl: blst){
          System.out.println(bl.getIsbn()+"  "+bl.getAuthor()+"  "+ bl.getTitle()+"  "+bl.getSubject());   
      }
}
    public static void isbnToCart(dao d){
        Scanner sc= new Scanner(System.in);
    System.out.println("type a isbn to add to chart");
      String j=sc.nextLine();//sc.nextLine();
      System.out.println("Enter quantity");
      int k=sc.nextInt();sc.nextLine();
      Books bk= d.findByIsbn(j);
      System.out.println(bk.getIsbn()+"  "+bk.getAuthor()+"  "+bk.getTitle()+"  "+bk.getPrice()+"  "+bk.getSubject()+"  ");
      Cart nC= new Cart(uID, bk.getIsbn());
      int done=d.addToCart(uID, bk.getIsbn(),k);
      if(done==1){
          System.out.println("added to cart");
      }
      
    }
}


