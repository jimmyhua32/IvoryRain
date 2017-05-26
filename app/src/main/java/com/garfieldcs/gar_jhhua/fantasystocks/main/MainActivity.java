/* Project: Ivory Rain
   1/9/2017
   This is the main menu.
 */

package com.garfieldcs.gar_jhhua.fantasystocks.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.garfieldcs.gar_jhhua.fantasystocks.widget.CheckConnection;
import com.garfieldcs.gar_jhhua.fantasystocks.R;
import com.garfieldcs.gar_jhhua.fantasystocks.info.User;

public class MainActivity extends AppCompatActivity {
    User user;
    CheckConnection c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = new CheckConnection(getApplicationContext());
    }

    //Goes to a specific screen for testing purposes
    public void bypassLogin(View view) {
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
            user = new User(un, pw, false, getApplicationContext());
            if (user.doesExist()) {
                Intent intent = new Intent(this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("UserID", user.getID());
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (!user.isPassCorrect()) {
                Toast toast = Toast.makeText(this, "Wrong password!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(this, "No user found!", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Goes to the registration screen
    public void goToNewUser(View view) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    //To be implemented when MVP is done
    public void forgotPassword() {
        Toast toast = Toast.makeText(this, "Sorry, you're out of luck for now", Toast.LENGTH_SHORT);
        toast.show();
    }
}
