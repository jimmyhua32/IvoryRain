package com.garfieldcs.gar_jhhua.fantasystocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.math.BigDecimal;

import yahoofinance.YahooFinance;

public class ShowPortfolioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_portfolio);

        Stock.setName("Apple");
        Stock.setSymbol("APPL");

        yahoofinance.Stock = YahooFinance.get(Stock.getSymbol);

        BigDecimal price = Stock.getName().getQuote().getPrice();

        Stock.getName().print();
    }
}
