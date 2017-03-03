package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class User {

    private int id;
    private String username;
    private String password;
    private String tempUser;
    private String tempPass;
    private String[] assetNames;

    private boolean doesExist;
    private boolean doesNameExist;

    private Context context;
    private Toast t;

    private ArrayList<Integer> ids;

    public User(String username, String password, boolean createUser, Context context) {
        AssetManager am = context.getAssets();
        this.context = context;
        tempUser = username;
        tempPass = password;

        try {
            assetNames = am.list("users");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //File folder = new File(context.getFilesDir().getAbsolutePath());
        //File[] allFiles = folder.listFiles();
        File[] allFiles = new File[assetNames.length];
        for (int i = 0; i < assetNames.length; i++) {
            System.out.println(assetNames[i]);
            allFiles[i] = new File(assetNames[i]);
        }
        doesExist = false;
        doesNameExist = false;
        t = new Toast(context);

        checkExist(allFiles);
        makeUser(createUser);
        am.close();
    }

    //Checks to see if the user already exists
    private void checkExist(File[] allFiles) {
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
    }

    //Creates the user if it is called for by MainActivity
    private void makeUser(boolean createUser) {
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
                this.username = tempUser;
                this.password = tempPass;
                try {
                    FileOutputStream fos = new FileOutputStream(new File("users/" + id + ".txt"));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
                    writer.write(id + " " + username + " " + password);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                t = t.makeText(context, "User created. Welcome!", Toast.LENGTH_SHORT);
                doesExist = true;
            } else {
                t = t.makeText(context, "Username already exists", Toast.LENGTH_SHORT);
                throw new IllegalArgumentException("User already exists");
            }
        } else if (doesExist && createUser) {
            t = t.makeText(context, "User already exists", Toast.LENGTH_SHORT);
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

}
