/* Project: Ivory Rain
   1/9/2017
   This is the main menu.
 */

package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    User user;
    Toast toast;
    CheckConnection c;
    Scanner fileIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = new CheckConnection(getApplicationContext());
        //Local database for now
        try {
            fileIO = new Scanner(new File("users.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Goes to display stock screen; currently only for testing purposes
    public void goToStock(View view) {
        Intent intent = new Intent(this, DisplayStockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", "AAPL"); //For testing
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Checks for correct login and goes to the user's portfolio
    public void goToPortfolio(View view) {
        if (c.isConnected()) {
            EditText username = (EditText) findViewById(R.id.usernameEditText);
            EditText password = (EditText) findViewById(R.id.passwordEditText);
            String un = username.getText().toString();
            String pw = password.getText().toString();
            if (filterUsers(un, pw)) {
                Intent intent = new Intent(this, ShowPortfolioActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Username", un);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Incorrect login! Want to register an account?", Toast.LENGTH_SHORT);
            }
        } else {
            toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT);
        }
    }

    //Creates a new user
    public void newUser(View view) {
        //Go to registration screen
    }

    //Checks all of the users to see if this specific user exists
    private boolean filterUsers(String un, String pw) {
        while (fileIO.hasNextLine()) {
            String username = fileIO.next();
            System.out.println(username); //For testing
            String password = fileIO.next();
            System.out.println(password); //For testing
            fileIO.nextLine();
            if (un == username && pw == password) {
                return true;
            }
        }
        return false;
    }
}
