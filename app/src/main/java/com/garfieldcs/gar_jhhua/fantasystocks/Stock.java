package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import java.io.IOException;
import java.math.BigDecimal;

import yahoofinance.YahooFinance;

/**
 * Created by gar_napodolske on 12/6/2016.
 */
public class Stock {
    private String name;
    private String symbol;
    private CheckConnection c;

    public Stock(String name, Context context) {
        this.name = name;
        c = new CheckConnection(context);
    }


    public String getName(){
        return name;
    }
    public String getSymbol(){
        return symbol;
    }

    private class CollectDataTask extends AsyncTask<String, Void, String> {
        yahoofinance.Stock stock;
        BigDecimal price;

        //Eventually add an array of stuff
        protected String doInBackground(String... param) {
            price = new BigDecimal(-1);
            try {
                if (c.isConnected()) {
                    stock = YahooFinance.get(param[0]);
                    price = stock.getQuote().getPrice();
                    return "$" + price;
                } else {
                    return "Connection error";
                }
            } catch (IOException e) {
                return "Stock error";
            }
        }

        protected void onPostExecute(String result) {
            priceView.setText("$" + price);
        }

    }
}
