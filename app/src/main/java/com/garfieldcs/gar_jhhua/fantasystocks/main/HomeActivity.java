package com.garfieldcs.gar_jhhua.fantasystocks.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.garfieldcs.gar_jhhua.fantasystocks.R;
import com.garfieldcs.gar_jhhua.fantasystocks.info.MultiStockInfo;
import com.garfieldcs.gar_jhhua.fantasystocks.info.OwnedStocks;
import com.garfieldcs.gar_jhhua.fantasystocks.info.User;
import com.garfieldcs.gar_jhhua.fantasystocks.widget.CalcChange;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements Runnable {

    private CalcChange calcChange;
    private int userID;
    private OwnedStocks ownedStocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bundle = getIntent().getExtras();
        userID = bundle.getInt("UserID");
        ownedStocks = new OwnedStocks(userID, getApplicationContext());

        run();
        new LoadingData().execute();
    }

    @Override
    public void run() {
        ArrayList<String> namesTemp = ownedStocks.getAssetName();
        MultiStockInfo multiStockInfo = new MultiStockInfo(
                namesTemp.toArray(new String[namesTemp.size()]), getApplicationContext());
        calcChange = new CalcChange(multiStockInfo, ownedStocks);
    }

    private class LoadingData extends AsyncTask<Void, Void, Double> {

        @Override
        protected Double doInBackground(Void... params) {
            return calcChange.getTotalAssetValue();
        }

        @Override
        protected void onPostExecute(Double result) {
            setContentView(R.layout.activity_home);
            TextView nameAssets = (TextView) findViewById(R.id.TeamNandA);
            String name = new User(userID, getApplicationContext()).getUserName() + " $" + result;
            nameAssets.setText(name);
        }
    }

    public void goToSearch (View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("UserID", userID);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToLeader (View view) {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("UserID", userID);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToPortfolio (View view) {
        Intent intent = new Intent(this, ShowPortfolioActivity.class);
        Bundle bundle = new Bundle();
        System.out.println(userID + " userID");
        bundle.putInt("UserID", userID);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}