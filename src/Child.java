/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.*;
import java.sql.Connection;

/**
 *
 * @author onatu
 */
public class Child extends User {

    public Child(String username, String password, Connection conn, JFrame MainFrame) {
        super(username, password, conn, MainFrame);
    }
}
