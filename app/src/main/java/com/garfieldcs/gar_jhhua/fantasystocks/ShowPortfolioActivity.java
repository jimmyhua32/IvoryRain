package com.garfieldcs.gar_jhhua.fantasystocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.math.BigDecimal;
import java.util.ArrayList;

import yahoofinance.YahooFinance;

public class ShowPortfolioActivity extends AppCompatActivity {
    User user;

    private String teamName;
    protected ArrayList<String> Stocks = new ArrayList<String>();
    protected ListView list = (ListView) findViewById(R.id.userAssetsList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_portfolio);

        Stocks.add("Apple");
        Stocks.add("Google");
        Stocks.add("Costco");
        Stocks.add("Yahoo");
        Stocks.add("Starbucks");
        Stocks.add("Microsoft");
        Stocks.add("Amazon");

        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("Username");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Stocks);

        list.setAdapter(adapter);
    }

}
