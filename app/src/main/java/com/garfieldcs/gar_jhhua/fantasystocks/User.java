package com.garfieldcs.gar_jhhua.fantasystocks;

import java.util.ArrayList;

import yahoofinance.Stock;

/**
 * Created by gar_napodolske on 1/4/2017.
 */
public class User {

    protected int id;
    protected String un;
    protected String email;
    private String pw;
    protected int lastID = 000001;
    protected String teamName;
    protected int leagueID;
    protected StockInfo ownedStocks;

    private void createUser (String username, String contact, String password) {
        un = username;
        email = contact;
        pw = password;
        id = createNewIdentification(lastID);
    }

    private int createNewIdentification (int lastID){
        int newID = lastID + 1;
        this.lastID = newID;
        return newID;
    }

    protected int findUser (){
        return id;
    }

    protected String getTeamName (){
        return teamName;
    }

//    protected StockInfo[] getStocks (){
//        return StockInfo ownedStocks;
//    }
}
