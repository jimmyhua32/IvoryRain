package com.garfieldcs.gar_jhhua.fantasystocks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private CalcChange calcChange;
    private String username;
    private String password;
    private List<Integer> allUserIDs;
    private List<Integer> userIDsRanked;
    private List<String> allUsernames;
    private List<Double> allUserAssets;
    private List<String> usersRanked;
    private Context context;
    private ArrayList<String> namesTemp;

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
        namesTemp = ownedStocks.getAssetName();
        MultiStockInfo multi = new MultiStockInfo
                (namesTemp.toArray(new String[namesTemp.size()]), getApplicationContext());
        calcChange = new CalcChange(multi, ownedStocks);

        fillArrays();
        sortUsers();
        new LoadingData().execute();
    }

    private void fillArrays() {
        //loop to fill all user IDs and names
        allUserIDs = new ArrayList<>();
        allUsernames = new ArrayList<>();
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
        System.out.println(allUserIDs.toString());

        /*
        Perhaps somewhere in here, ownedStocks is being passed into calcChange incorrectly
         */

        //ERROR RIGHT HERE
        ArrayList<OwnedStocks> allUserOS = new ArrayList<>();
        for (int i = 0; i < allUserIDs.size(); i++) {
            allUserOS.add(new OwnedStocks(allUserIDs.get(i), context));
            ArrayList<String> specificNamesTemp = allUserOS.get(i).getAssetName();
            MultiStockInfo multiTemp = new MultiStockInfo
                    (specificNamesTemp.toArray
                            (new String[specificNamesTemp.size()]), getApplicationContext());
            CalcChange calcChangeTemp = new CalcChange(multiTemp, allUserOS.get(i));
            allUserAssets.add(calcChangeTemp.getTotalAssetValue());
        }
        //END
    }

    //sends user to view of another user's portfolio
    public void goToUser (View view, int position) {
        Integer userViewID = userIDsRanked.get(position);
        Intent intent = new Intent(this, ShowOtherPortfolioActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("UserToViewID", userViewID);
        bundle.putString("Username", username);
        bundle.putString("Password", password);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //ranks all users from 1-10 based on highest asset values
    private void sortUsers() {
        usersRanked = new ArrayList<>();
        userIDsRanked = new ArrayList<>();
        int maxIndex = 10;
        if (allUserAssets.size() < 10) {
            maxIndex = allUserAssets.size();
        }
        for (int i = 0; i < maxIndex; i++) {
            double tempHighest = 0;
            int highestSpot = 0;
            for (int j = 0; j < allUserAssets.size(); j++) {
                if (allUserAssets.get(j) > tempHighest) {
                    tempHighest = allUserAssets.get(j);
                    highestSpot = j;
                }
            }
            //adds highest user to next rank on ranked list and removes that user from lists
            //that the next highest is chosen from
            usersRanked.add((i + 1) + ". " + allUsernames.get(highestSpot) + " $" +
                    allUserAssets.get(highestSpot));
            userIDsRanked.add(allUserIDs.get(highestSpot));
            allUserIDs.remove(highestSpot);
            allUsernames.remove(highestSpot);
            allUserAssets.remove(highestSpot);
        }
    }

    private class LoadingData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //displays information
            ListView list = (ListView) findViewById(R.id.leaderboardList);
            TextView userAssets = (TextView) findViewById(R.id.UserAssetValue);
            TextView userPC = (TextView) findViewById(R.id.UserPCValue);
            userAssets.setText("" + calcChange.getTotalAssetValue());
            userPC.setText("" + calcChange.getPercentValueChange() + "%");

            //adapts arraylist into listview
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (LeaderboardActivity.this, R.layout.custom_layout, usersRanked);
            list.setAdapter(adapter);

            //clickable list to redirect to user's portfolio for inspection
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    goToUser(view, position);
                }
            });
        }

    }
}
