package com.garfieldcs.gar_jhhua.fantasystocks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import yahoofinance.YahooFinance;

public class ShowPortfolioActivity extends AppCompatActivity {
    private User user;
    private OwnedStocks ownedStocks;
    private String teamName;
    private double investedAssets;
    private double totalAssets;
    private double bankAssets;
    protected ArrayList<String> Stocks = new ArrayList<String>();
    protected ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_portfolio);
        list  = (ListView) findViewById(R.id.userAssetsList);
        investedAssets = 0;
        bankAssets = 0;
        totalAssets = 0;

        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("Username");
        String password = bundle.getString("Password");
        user = new User(username, password, false, getApplicationContext());
        ownedStocks = new OwnedStocks(user.getID(), getApplicationContext());

        for (int i = 0; i < ownedStocks.getSize(); i++) {
            investedAssets += (ownedStocks.getAssetPrice(i) * ownedStocks.getAssetQuantity(i));
        }

        TextView teamName = (TextView) findViewById(R.id.userTeamName);
        TextView totalValue = (TextView) findViewById(R.id.TotalAssetValue);
        TextView bankValue = (TextView) findViewById(R.id.BankAccountValue);
        TextView investedValue = (TextView) findViewById(R.id.InvestedAssetsValue);

        String totalString = "$" + totalAssets;
        String bankString = "$" + bankAssets;
        String investedString = "$" + investedAssets;

        Stocks = ownedStocks.getAsset();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, Stocks);
        list.setAdapter(adapter);
    }

    //Loads the information on a separate thread
    private class loadingData extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(ShowPortfolioActivity.this);
        boolean status;

        //Loading circle bar... thing
        protected void onPreExecute() {
            status = false;

            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog = dialog.show(ShowPortfolioActivity.this,
                    "Please wait", "Retrieving data...", true);
            super.onPreExecute();
        }

        //Collect and analyze data
        protected Void doInBackground(Void... arg0 ) {

            return null;
        }

        //Display the information onto the screen
        protected void onPostExecute() {

        }
    }
}
