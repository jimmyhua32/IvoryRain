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

public class MainActivity extends AppCompatActivity {
    User user;
    Toast toast;
    CheckConnection c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = new CheckConnection(getApplicationContext());
    }

    //Goes to a specific screen for testing purposes
    public void bypassLogin(View view) {
        //Intent intent = new Intent(this, DisplayStockActivity.class);
        Intent intent = new Intent(this, ShowPortfolioActivity.class);
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
            System.out.println(un);
            String pw = password.getText().toString();
            System.out.println(pw);
            user = new User(un, pw, false, getApplicationContext());
            if (user.doesExist()) {
                Intent intent = new Intent(this, ShowPortfolioActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Username", user.getUserName());
                bundle.putString("Password", user.getPassword());
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                System.out.println("No user found");
                toast.makeText(getApplicationContext(),
                        "Incorrect login! Want to register an account?", Toast.LENGTH_SHORT);
            }
        } else {
            toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT);
        }
    }

    //Creates a new user
    public void newUser(View view) {
        if (c.isConnected()) {
            //Temporary; will be a new screen later
            EditText username = (EditText) findViewById(R.id.usernameEditText);
            EditText password = (EditText) findViewById(R.id.passwordEditText);
            String un = username.getText().toString();
            System.out.println(un);
            String pw = password.getText().toString();
            System.out.println(pw);
            user = new User(un, pw, true, getApplicationContext());
        }
    }
}
