package com.garfieldcs.gar_jhhua.fantasystocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.math.BigDecimal;
import java.util.ArrayList;

import yahoofinance.YahooFinance;

public class ShowPortfolioActivity extends AppCompatActivity {
    User user;

    private String teamName;
    protected ArrayList Stocks = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_portfolio);
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("Username");
        //Add some way to get current user data

    }

}
