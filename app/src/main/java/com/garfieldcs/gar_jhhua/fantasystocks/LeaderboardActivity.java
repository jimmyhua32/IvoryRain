package com.garfieldcs.gar_jhhua.fantasystocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private User user;
    private OwnedStocks ownedStocks;
    private String username;
    private String password;
    private double totalAssets;
    private double percentChange;
    private List<String> usersRanked;
    private List<String> usersListNumbered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("Username");
        password = bundle.getString("Password");
        user = new User(username, password, false, getApplicationContext());

        //gets ownedstocks for user and gets assets and change
        ownedStocks = new OwnedStocks(user.getID(), getApplicationContext());
        ArrayList<String> namesTemp = ownedStocks.getAssetName();
        MultiStockInfo multi = new MultiStockInfo
                (namesTemp.toArray(new String[namesTemp.size()]), getApplicationContext());
        CalcChange calcChange = new CalcChange(multi, getApplicationContext());
        totalAssets = calcChange.getTotalAssetValue();
        percentChange = calcChange.getPercentValueChange();

        //displays information
        ListView list = (ListView) findViewById(R.id.leaderboardList);
        TextView userAssets = (TextView) findViewById(R.id.UserAssetValue);
        TextView userPC = (TextView) findViewById(R.id.UserPCValue);
        userAssets.setText("" + totalAssets);
        userPC.setText("" + percentChange + "%");

        //orders top 25 users
        usersRanked = new ArrayList<>();
        for (int i = 0; i <=25; i++) {

        }


        //fills arraylist of users
        usersListNumbered = new ArrayList<>();
        for (int i = 0; i <=25; i++) {
            usersListNumbered.add(i + ". ");
        }

        //adapts arraylist into listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (LeaderboardActivity.this, android.R.layout.simple_list_item_1, usersListNumbered);
        list.setAdapter(adapter);

    }
}
