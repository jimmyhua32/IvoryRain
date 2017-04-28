package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LeaderboardActivity extends AppCompatActivity {
    private User user;
    private OwnedStocks ownedStocks;
    private String username;
    private String password;
    private double totalAssets;
    private double percentChange;
    private List<Integer> allUserIDs;
    private List<String> allUsernames;
    private List<Double> allUserAssets;
    private List<String> usersRanked;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("Username");
        password = bundle.getString("Password");
        user = new User(username, password, false, getApplicationContext());
        context = getApplicationContext();

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

        //while loop filled with all user asset values
        allUserIDs = new ArrayList<>();
        File folder = new File(context.getFilesDir().getAbsolutePath());
        File[] allFiles = folder.listFiles();
        try {
            for (int i = 0; i < allFiles.length; i++) {
                if (allFiles[i].isFile()) {
                    if (!(allFiles[i].getName().startsWith("S")) &&
                            !(allFiles[i].getName().startsWith("B"))) {
                        String currentLine; //Order: id + user + password
                        BufferedReader reader = new BufferedReader(new FileReader(allFiles[i]));
                        while ((currentLine = reader.readLine()) != null) {
                            Scanner s = new Scanner(currentLine);
                            allUserIDs.add(Integer.parseInt(s.next()));
                            allUsernames.add(s.next().trim());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        allUserAssets = new ArrayList<>();
        for (int i = 0; i < allUserIDs.size(); i++) {
            OwnedStocks ownedStocksTemp = new OwnedStocks(allUserIDs.get(i), context);
            MultiStockInfo multiTemp = new MultiStockInfo
                    (ownedStocksTemp.getAssetName().toArray
                            (new String[namesTemp.size()]), getApplicationContext());
            CalcChange calcChangeTemp = new CalcChange(multiTemp, context);
            allUserAssets.add(calcChangeTemp.getTotalAssetValue());
        }

        //orders top 25 users
        usersRanked = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            double tempHighest = 0;
            int highestSpot = 0;
            for (int j = 0; j < allUserAssets.size(); j++) {
                if (allUserAssets.get(j) > tempHighest) {
                    tempHighest = allUserAssets.get(j);
                    highestSpot = j;
                }
            }
            usersRanked.add(i + ". " + allUsernames.get(highestSpot) + " " +
                    allUserAssets.get(highestSpot));
            allUsernames.remove(highestSpot);
            allUserAssets.remove(highestSpot);
        }

        //adapts arraylist into listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (LeaderboardActivity.this, android.R.layout.simple_list_item_1, usersRanked);
        list.setAdapter(adapter);

    }
}
