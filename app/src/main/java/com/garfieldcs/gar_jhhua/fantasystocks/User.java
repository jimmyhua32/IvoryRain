package com.garfieldcs.gar_jhhua.fantasystocks;

import java.util.ArrayList;

import yahoofinance.Stock;

public class User {

    //Be sure to edit variable names
    protected int id;
    protected String un;
    protected String email;
    private String pw;
    protected int lastID = 000000;
    protected String teamName;
    protected int leagueID;
    protected ArrayList<String> ownedStocks;
    protected ArrayList<Integer> orderValues;

    public void createUser (String username, String contact, String password) {
        un = username;
        email = contact;
        pw = password;
        id = createNewIdentification(lastID);
        ownedStocks = new ArrayList<String>();
        orderValues = new ArrayList<Integer>();
    }

    private int createNewIdentification (int lastID){
        int newID = lastID + 1;
        this.lastID = newID;
        return newID;
    }

    //Adds a stock; value is the order in which the user buys the stock
    public void addStocks (String stockName, int value){
        ownedStocks.add(stockName);
        orderValues.add(value);
    }

    public void removeStocks (String stockName){
        ownedStocks.remove(stockName);
    }

    public int getUserID (){
        return id;
    }

    public String getTeamName (){
        return teamName;
    }

    public String toString() {
        return "User = " + un;
    }
}
