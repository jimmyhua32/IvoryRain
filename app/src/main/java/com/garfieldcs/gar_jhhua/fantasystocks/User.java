package com.garfieldcs.gar_jhhua.fantasystocks;

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
    protected int leagueID;

    public User(String username, String password) {
        un = username;
        pw = password;
        encryptPW();

    }

    //Adds a stock; value is the order in which the user buys the stock
    public void addStocks (String stockName, int value){

    }


    //Returns the stock at an index
    public String getStock(int index) {
        return null;
    }

    //Only one league for now
    public void addToLeague(int leagueID) {
        this.leagueID = leagueID;
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

    //Used during login
    public boolean isUser(String username, String password) {
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
    }

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
