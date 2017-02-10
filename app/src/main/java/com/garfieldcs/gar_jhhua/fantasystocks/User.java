package com.garfieldcs.gar_jhhua.fantasystocks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import yahoofinance.Stock;

public class User {

    protected int id; //Eventually create a random id per user
    protected String un; //Username
    //Add email functionality later; not MVP
    //protected String email;
    private String pw; //Password
    private boolean encryptStatus;
    protected String displayName;
    private BufferedReader reader;
    private BufferedWriter writer;

    public User(boolean isNewUser) {
        File folder = new File("path"); //Add actual path later for testing
        File[] allFiles = folder.listFiles();
        if (isNewUser) {
            id = generateID();
        } else {
            try {
                for (int i = 0; i < allFiles.length; i++) {
                    if (allFiles[i].isFile()) {
                        reader = new BufferedReader(new FileReader(allFiles[i]));
                        String currentLine;
                        
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Username and display name are different; the name others see
    public void createName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName (){
        return displayName;
    }

    public String getUserName() {
        //Add some checks
        return un;
    }

    //Generates an id
    private int generateID() {

    }


/*    public boolean isUser(String username, String password) {
        if (encryptStatus) {
            decryptPW();
        }
        if (username == un && password == pw) {
            return true;
        } else if (username != un) {
            System.out.println("Wrong user"); //testing purposes
            return false;
        } else if (password != pw) {
            System.out.println("Wrong password"); //again, for testing
            return false;
        }
        System.out.println("No conditions met (Username and password incorrect");
        encryptPW();
        return false; //All conditions false
    }*/




    /* CAN IGNORE EVERYTHING BELOW FOR NOW
       CONTAINS "ENCRYPTION" WHICH REALLY DOESN'T DO ANYTHING YET
     */

    //Secures the password
    private void encryptPW() {
        String newPass = "";

        this.pw = newPass;
        encryptStatus = true;
    }

    //Decrypts the password for usage
    private void decryptPW() {
        String oldPass = "";

        this.pw = oldPass;
        encryptStatus = false;
    }
}
