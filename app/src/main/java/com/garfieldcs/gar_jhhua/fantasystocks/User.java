package com.garfieldcs.gar_jhhua.fantasystocks;

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

    private void createUser (String username, String contact, String password) {
        un = username;
        email = contact;
        pw = password;
        id = createNewIdentification(lastID);
    }

    private int createNewIdentification (int lastID){
        int newID = lastID + 1;
        lastID = newID;
        return newID;
    }

    protected int findUser (){
        return id;
    }

    protected String getTeamName (){
        return teamName;
    }
}
