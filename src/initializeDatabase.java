/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author onatu
 */
public class initializeDatabase {
    
     static int connectionCount=0;

    public static int getConnectionCount() {
        return connectionCount;
    }
    
    public static Connection connect(){
        Connection conn = null;
    String url = "jdbc:mysql://localhost:3306/moviecritics";//set the sql file.
    String user="root";//May change on situations.
    String password = "Onatunlu3a";//To be filled by who uses.
        
    try{
    conn=DriverManager.getConnection(url, user, password);
    connectionCount++;
    if(connectionCount==1)
    JOptionPane.showMessageDialog(null,"The connection is successful.", "Connection Info", JOptionPane.INFORMATION_MESSAGE);
    }
    catch(SQLException e){
    e.printStackTrace();
    int answ = JOptionPane.showConfirmDialog(null, "Connection has failed,try again?", "Oups!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if(answ==JOptionPane.YES_OPTION){
    connect();
    }
    else{
        JOptionPane.showMessageDialog(null,"Goodbye", "Message", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
   }
    return conn;
    }
    
}
