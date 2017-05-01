package com.garfieldcs.gar_jhhua.fantasystocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class ShowOtherPortfolioActivity extends AppCompatActivity {
    private User user;
    private Integer userID;
    private String password;
    ArrayList<String> userStocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_other_portfolio);

        Bundle bundle = getIntent().getExtras();
        userID = bundle.getInt("UserID");
        User userToView = new User(userID, getApplicationContext());
        OwnedStocks ownedStocks = new OwnedStocks(userID, getApplicationContext());
        userStocks = ownedStocks.getAsset();
        

    }
}
