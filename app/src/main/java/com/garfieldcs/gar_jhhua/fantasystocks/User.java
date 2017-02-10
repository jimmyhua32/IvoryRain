package com.garfieldcs.gar_jhhua.fantasystocks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import yahoofinance.Stock;

public class User {

    protected int id;
    protected String username;
    private String password;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean doesExist;

    private ArrayList<String> usernames;
    private ArrayList<String> passwords;
    private ArrayList<Integer> ids;

    public User(String username, String password) {
        File folder = new File("path"); //Add actual path later for testing
        File[] allFiles = folder.listFiles();
        doesExist = false;
        try {
            for (int i = 0; i < allFiles.length; i++) {
                if (allFiles[i].isFile()) {
                    reader = new BufferedReader(new FileReader(allFiles[i]));
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
        if (!doesExist) {
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
                    writer = new BufferedWriter(new FileWriter(new File("path")));
                    writer.write(id + " " + username + " " + password);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                doesExist = true;
            } else {
                //If username already exists
            }
        }
    }

    public boolean doesExist() {

        return false;
    }

    public String getUserName() {
        //Add some checks
        return username;
    }

    //Generates an id
    private int generateID() {
        return 0;
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
