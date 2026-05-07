/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.ArrayList;

/**
 *
 * @author onatu
 */
public abstract class User {

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getUserType() {
        return userType;
    }

    public String getEmail() {
        return email;
    }

    public boolean isParent() {
        return isParent;
    }

    private int userID;
    private String username;
    private String password;
    private int userType;//1 for father,2 for mother,3 for son,4 for daughter
    private String email;
    private boolean isParent;

    public ArrayList<Movie> movies;

    public User(int userID, String username, String password, int userType, String email) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.email = email;
        this.isParent = (userType == 1 || userType == 2);
        movies=new ArrayList<>();
    }
    
}
