/* Project: Ivory Rain
   1/9/2017
   This is the main menu.
 */

package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

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
            System.out.println(un);
            String pw = password.getText().toString();
            user = new User(un, pw, false, getApplicationContext());
            if (user.doesExist()) {
                Intent intent = new Intent(this, ShowPortfolioActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Username", user.getUserName());
                bundle.putString("Password", user.getPassword());
                intent.putExtras(bundle);
                //For testing
                //new LoadTestData().execute();
                startActivity(intent);
            } else if (!user.isPassCorrect()) {
                Toast toast = Toast.makeText(this, "Wrong password!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(this, "No user found!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    //Creates a new user
    public void newUser(View view) {
        if (c.isConnected()) {
            //Temporary; will be a new screen later
            EditText username = (EditText) findViewById(R.id.usernameEditText);
            EditText password = (EditText) findViewById(R.id.passwordEditText);
            String un = username.getText().toString();
            String pw = password.getText().toString();
            user = new User(un, pw, true, getApplicationContext());
            if (user.userCreated()) {
                Toast toast = Toast.makeText(this, "User created!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    //For testing only
    private class LoadTestData extends AsyncTask<Void, Void, StockInfo[]> {
        StockInfo[] stocks;

        protected void onPreExecute() {
            stocks = new StockInfo[3];
        }

        protected StockInfo[] doInBackground(Void... result) {
            StockInfo intel = new StockInfo("INTC", getApplicationContext());
            StockInfo apple = new StockInfo("AAPL", getApplicationContext());
            StockInfo google = new StockInfo("GOOG", getApplicationContext());
            stocks[0] = intel;
            stocks[1] = apple;
            stocks[2] = google;
            return stocks;
        }

        protected void onPostExecute(StockInfo[] result) {
            OwnedStocks ownedStocks = new OwnedStocks(user.getID(), getApplicationContext());
            try {
                ownedStocks.addStock(result[0], 100);
                ownedStocks.addStock(result[1], 55);
                ownedStocks.addStock(result[2], 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
