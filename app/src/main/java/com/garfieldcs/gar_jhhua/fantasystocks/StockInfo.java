package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.reflect.Array;

import yahoofinance.YahooFinance;

public class StockInfo {
    private String name;
    private String symbol;
    private CheckConnection c;
    private yahoofinance.Stock stock;

    //"Name" is the name of the stock
    public StockInfo(String name, Context context) {
        this.name = name;
        c = new CheckConnection(context); //Checks for internet connection
        new CollectDataTask().execute(name);
        new CollectDataTask().getSymbol();
    }

    public String getName(){
        return name;
    }

    public String getSymbol(){
        new CollectDataTask().getSymbol();
        return symbol;
    }

    public String getPrice() {
        return symbol + Array.getFloat(new CollectDataTask().getInfo(), 0);
    }

    public String getChange() {
        return symbol + Array.getFloat(new CollectDataTask().getInfo(), 1);
    }

    public String getChangeP() {
        return symbol + Array.getFloat(new CollectDataTask().getInfo(), 2);
    }

    public String getHighY() {
        return symbol + Array.getFloat(new CollectDataTask().getInfo(), 3);
    }

    public String getLowY() {
        return symbol + Array.getFloat(new CollectDataTask().getInfo(), 4);
    }

    public String getHighD() {
        return symbol + Array.getFloat(new CollectDataTask().getInfo(), 5);
    }

    public String getLowD() {
        return symbol + Array.getFloat(new CollectDataTask().getInfo(), 6);
    }

    //Collects data in a separate thread
    private class CollectDataTask extends AsyncTask<String, Void, String> {
        yahoofinance.Stock stock;
        String price;
        String change; //Change in price
        String changeP; //Change in price using percentages
        String highY; //Yearly high
        String lowY; //Yearly low
        String highD; //Daily high
        String lowD; //Daily low
        String symbol;

        //Eventually add an array of stuff
        protected String doInBackground(String... param) {
            try {
                if (c.isConnected()) {
                    stock = YahooFinance.get(param[0]);
                    symbol = stock.getSymbol() + " ";
                    price = symbol + stock.getQuote().getPrice();
                    change = symbol + stock.getQuote().getChange();
                    changeP = "" + stock.getQuote().getChangeInPercent();
                    highY = symbol + stock.getQuote().getYearHigh();
                    lowY = symbol + stock.getQuote().getYearLow();
                    highD = symbol + stock.getQuote().getDayHigh();
                    lowD = symbol + stock.getQuote().getDayLow();
                    return "Connection success";
                } else {
                    return "Connection error";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Stock error";
            }
        }

        protected void onPostExecute(String result) {
            //Test and see what symbol looks like
            System.out.println("****** " + symbol + "*");
        }

        protected String getSymbol() {
            return symbol;
        }

        protected String[] getInfo() {
            return new String[] {price, change, changeP, highY, lowY, highD, lowD};
        }
    }
}
