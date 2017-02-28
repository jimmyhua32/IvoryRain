package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class User {

    private int id;
    private String username;
    private String password;
    private boolean doesExist;
    private boolean doesNameExist;

    private ArrayList<Integer> ids;

    public User(String username, String password, boolean createUser, Context context) {
        File folder = new File(context.getFilesDir().getAbsolutePath());
        File[] allFiles = folder.listFiles();
        doesExist = false;
        doesNameExist = false;
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
        System.out.println(ids.toString());
        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(username) && passwords.get(i).equals(password)) {
                if (usernames.get(i).equals(username)) {
                    doesNameExist = true;
                }
                this.username = usernames.get(i);
                this.password = passwords.get(i);
                this.id = ids.get(i);
                System.out.println("USER EXISTS");
                doesExist = true;
            }
        }
        if (!doesExist && createUser) {
            boolean generatedID = false;
            while (!generatedID) {
                id = (int) (Math.random() * 1000);
                if (ids.size() == 0) {
                    generatedID = true;
                    break;
                }
                for (int i : ids) {
                    if (id == i) {
                        continue;
                    }
                    generatedID = true;
                }
            }
            if (!doesNameExist) {
                this.username = username;
                this.password = password;
                try {
                    //Path should be id + .txt
                    FileOutputStream fos = context.openFileOutput(id + ".txt", 0);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
                    writer.write(id + " " + username + " " + password);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("USER CREATED");
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
