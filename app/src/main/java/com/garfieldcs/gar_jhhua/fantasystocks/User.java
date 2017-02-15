package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class User {

    protected int id;
    protected String username;
    private String password;
    private boolean doesExist;

    private ArrayList<Integer> ids;

    public User(String username, String password, boolean createUser, Context context) {
        File folder = new File(context.getFilesDir().getAbsolutePath());
        File[] allFiles = folder.listFiles();
        doesExist = false;
        ArrayList<String> usernames = new ArrayList<String>();
        ArrayList<String> passwords = new ArrayList<String>();
        ids = new ArrayList<Integer>();

        try {
            for (int i = 0; i < allFiles.length; i++) {
                if (allFiles[i].isFile()) {
                    BufferedReader reader = new BufferedReader(new FileReader(allFiles[i]));
                    String currentLine;
                    while((currentLine = reader.readLine()) != null) {
                        Scanner s = new Scanner(currentLine);
                        usernames.add(s.next());
                        passwords.add(s.next());
                        ids.add(s.nextInt());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(username) && passwords.get(i).equals(password)) {
                this.username = usernames.get(i);
                this.password = passwords.get(i);
                this.id = ids.get(i);
                doesExist = true;
            }
        }
        if (!doesExist && createUser) {
            boolean generatedID = false;
            while (!generatedID) {
                id = generateID();
                for (int i : ids) {
                    if (id == i) {
                        break;
                    }
                }
                generatedID = true;
            }
            if (!this.username.equals(username)) {
                this.username = username;
                this.password = password;
                try {
                    //Path should be id + .txt
                    BufferedWriter writer = new BufferedWriter(new FileWriter
                            (new File(context.getFilesDir(), id + ".txt")));
                    writer.write(id + " " + username + " " + password);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                doesExist = true;
            } else {
                //If username already exists
                throw new IllegalArgumentException("User already exists");
            }
        } else if (doesExist && createUser) {
            throw new IllegalArgumentException("User already exists");
        }
    }

    //Returns and sees if the user exists
    public boolean doesExist() {
        return doesExist;
    }

    public String getUserName() {
        //Add some checks
        return username;
    }

    //For testing only, will delete later
    protected String getPassword() {
        return password;
    }

    public int getID() {
        return id;
    }

    //Generates an id
    private int generateID() {
        boolean validID = false;
        int tempID = -1;
        while (!validID) { //Work on this
            tempID = (int) (Math.random() * 1000);
            for (int i : ids) {
                if (tempID == i) {
                    break;
                }
            }
        }
        return tempID;
    }


/*    public boolean isUser(String username, String password) {
        if (encryptStatus) {
            decryptPW();
        }
        if (username == this.username && password == this.password) {
            return true;
        } else if (username != this.username) {
            System.out.println("Wrong user"); //testing purposes
            return false;
        } else if (password != this.password) {
            System.out.println("Wrong password"); //again, for testing
            return false;
        }
        System.out.println("No conditions met (Username and password incorrect");
        encryptPW();
        return false; //All conditions false
    }*/




    /* CAN IGNORE EVERYTHING BELOW FOR NOW
       CONTAINS "ENCRYPTION" WHICH REALLY DOESN'T DO ANYTHING YET


    //Secures the password
    private void encryptPW() {
        String newPass = "";

        this.password = newPass;
        encryptStatus = true;
    }

    //Decrypts the password for usage
    private void decryptPW() {
        String oldPass = "";

        this.password = oldPass;
        encryptStatus = false;
    }
    */
}
