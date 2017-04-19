package com.garfieldcs.gar_jhhua.fantasystocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private User user;
    private OwnedStocks ownedStocks;
    private String username;
    private String password;
    private double totalAssets;
    private double percentChange;
    private List<String> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("Username");
        password = bundle.getString("Password");
        user = new User(username, password, false, getApplicationContext());
        ownedStocks = new OwnedStocks(user.getID(), getApplicationContext());
        totalAssets = ownedStocks.getTotalAssets();
        percentChange = ownedStocks.getPercentValueChange();
        TextView userAssets = (TextView) findViewById(R.id.UserAssetValue);
        TextView userPC = (TextView) findViewById(R.id.UserPCValue);
        userAssets.setText("" + totalAssets);
        userPC.setText("" + percentChange + "%");

    }
}
