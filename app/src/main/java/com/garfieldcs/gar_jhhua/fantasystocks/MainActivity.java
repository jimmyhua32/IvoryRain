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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    User user;
    Toast toast;
    CheckConnection c;
    //Add user database (even if very rough/small)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = new CheckConnection(getApplicationContext());
        //Later add a way to collect a database of users
    }

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
            if (user.isUser(un, pw)) {
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
        if (c.isConnected()) {
            EditText username = (EditText) findViewById(R.id.usernameEditText);
            EditText password = (EditText) findViewById(R.id.passwordEditText);
            String un = username.getText().toString();
            String pw = password.getText().toString();
            //Add checks to see if user exists and add them to database
            user = new User(un, pw);
        } else {
            toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT);
        }
    }
}
