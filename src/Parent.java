import javax.swing.*;
import java.sql.Connection;

public class Parent extends User{

    public Parent(String username, String password, Connection conn, JFrame MainFrame) {
        super(username, password, conn, MainFrame);



    }

    public void viewFamilyAnalytics(){

    }

    public void moderateContent(){

    }

    public void setRestriction(){

    }

    public void manageAccounts(){


    }

    public void editMovie(Movie movie){


    }

    public void removeMovie(Movie movie){


    }

    public void addMovie(Movie movie){


    }

    @Override
    public int getUserID() {
        return super.getUserID();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public int getUserType() {
        return super.getUserType();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public boolean isParent() {
        return super.isParent();
    }
}


