package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    public static final int MAX_ID_DIGITS = 10000;

    private int id;
    private String username;
    private String password;
    private String tempUser;
    private String tempPass;
    private boolean doesExist;
    private boolean doesNameExist;
    private boolean created;
    private boolean isPassCorrect;

    private Context context;

    private ArrayList<Integer> ids;

    public User(String username, String password, boolean createUser, Context context) {
        this.context = context;
        tempUser = username;
        tempPass = password;

        File folder = new File(context.getFilesDir().getAbsolutePath());
        File[] allFiles = folder.listFiles();
        doesExist = false;
        doesNameExist = false;
        created = false;
        isPassCorrect = true;


        checkExist(allFiles);
        makeUser(createUser);
    }

    //Checks to see if the user already exists
    private void checkExist(File[] allFiles) {
        ArrayList<String> usernames = new ArrayList<String>();
        ArrayList<String> passwords = new ArrayList<String>();
        ids = new ArrayList<Integer>();
        BufferedReader reader = null;
        try {
            for (int i = 0; i < allFiles.length; i++) {
                if (allFiles[i].isFile()) {
                    if (!(allFiles[i].getName().startsWith("S")) &&
                            !(allFiles[i].getName().startsWith("B"))) {
                        String currentLine; //Order: id + user + password
                        reader = new BufferedReader(new FileReader(allFiles[i]));
                        while ((currentLine = reader.readLine()) != null) {
                            Scanner s = new Scanner(currentLine);
                            ids.add(Integer.parseInt(s.next()));
                            usernames.add(s.next().trim());
                            passwords.add(s.next().trim());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(tempUser) && passwords.get(i).equals(tempPass)) {
                username = usernames.get(i);
                password = passwords.get(i);
                id = ids.get(i);
                doesExist = true;
            } else if (usernames.get(i).equals(tempUser)) {
                doesNameExist = true;
                isPassCorrect = false;
            }
        }
    }

    //Creates the user if it is called for by MainActivity
    private void makeUser(boolean createUser) {
        if (!doesExist && createUser) {
            boolean generatedID = false;
            while (!generatedID) {
                id = (int) (Math.random() * MAX_ID_DIGITS);
                if (ids.size() == 0) {
                    generatedID = true;
                }
                for (int i : ids) {
                    if (id == i) {
                        continue;
                    }
                    generatedID = true;
                }
            }
            if (!doesNameExist) {
                this.username = tempUser;
                this.password = tempPass;
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(
                            new File(context.getFilesDir(), id + ".txt"));
                    System.out.println(id + " " + username + " " + password);
                    writer.println(id + " " + username + " " + password);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    writer.close();
                }
                doesExist = true;
                created = true;
            }
        }
    }

    //Returns to see if the user exists
    public boolean doesExist() {
        return doesExist;
    }

    //Returns to see if user was created
    public boolean userCreated() { return created; }

    //Returns to see if the password was correct
    public boolean isPassCorrect() { return isPassCorrect; }

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

}
