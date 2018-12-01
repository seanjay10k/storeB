/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kristinabookstore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Invalidquantum
 */
public class NewClass {
    
     public static void main(String[] args) throws IOException, SQLException {
         
         Connection con= null;
         try {
             con= DriverManager.getConnection("jdbc:derby://localhost:1527/kristiDB","scott","tiger");
         } catch (SQLException ex) {
             Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         
     
         String sql = " INSERT INTO BOOKS(isbn,author,title,price,subject) VALUES(?,?,?,?,?) ";


try { 
    File file = 
      new File("BookSample.txt"); 
    Scanner sc = new Scanner(file); 
  
    while (sc.hasNextLine()) {
      //System.out.println(sc.nextLine()); 
  //} 
//} 
    
    
        //BufferedReader bReader = new BufferedReader(new FileReader("BookSample.csv"));
        String line = ""; 
        
        line=sc.nextLine();
        //while ((line = bReader.readLine()) != null) {

                String[] str = line.split("\t"); 
                
//                for(String result:str)
//                {
                    //System.out.println(result);
                    //Create preparedStatement here and set them and excute them
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1,str[0]);
                    ps.setString(2,str[1]);
                    
                    ps.setString(3,str[2]);
                    
                    double d= Double.parseDouble(str[3]);
                    ps.setDouble(4,d);
                    ps.setString(5, str[4]);
                    ps.executeUpdate();
                    ps.close();
                    //Assuming that your line from file after split will folllow that sequence
                    
                //}
            
            //if (bReader == null)
            //{
              //  bReader.close();
            //}
        }
    } catch (FileNotFoundException ex) {
        ex.printStackTrace();
    }
    
         
         
         
         
         
    }
    
    
    
    
}
