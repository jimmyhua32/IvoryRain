package com.garfieldcs.gar_jhhua.fantasystocks;

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
    User user;

    private String teamName;
    private double investedAssets;
    private double totalAssets;
    private double bankAssets;
    protected ArrayList<String> Stocks = new ArrayList<String>();
    protected ListView list = (ListView) findViewById(R.id.userAssetsList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_portfolio);
        investedAssets = 0;
        bankAssets = 0;
        totalAssets = bankAssets + investedAssets;



        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("Username");
        String password = bundle.getString("Password");
        User user = new User(username, password, false, getApplicationContext());
        OwnedStocks ownedStocks = new OwnedStocks(user.getID(), getApplicationContext());

        for (int i = 0; i < ownedStocks.getSize(); i++) {
            investedAssets += (ownedStocks.getAssetPrice(i) * ownedStocks.getAssetQuantity(i));
        }

        TextView teamName = (TextView) findViewById(R.id.userTeamName);
        TextView totalValue = (TextView) findViewById(R.id.TotalAssetValue);
        TextView bankValue = (TextView) findViewById(R.id.BankAccountValue);
        TextView investedValue = (TextView) findViewById(R.id.InvestedAssetsValue);

        //replace these with variables adding up assets
        teamName.setText(username);
        totalValue.setText("$10000");
        bankValue.setText("$10000");
        investedValue.setText("$0");

        Stocks = ownedStocks.getAsset();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Stocks);
        list.setAdapter(adapter);
    }

}
