/* Project: Ivory Rain
   1/9/2017
   Runs and collects stock information in a separate thread.
   Returns the information collected via return methods.
*/

package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockInfo {
    private boolean collectStatus;
    private String name;
    private String currency;
    private String price;
    private String change;
    private String changeP;
    private String highY;
    private String lowY;
    private String highD;
    private String lowD;
    private String test;
    private CheckConnection c;

    //"Name" is the name of the stock
    public StockInfo(String name, Context context) {
        this.name = name;
        collectStatus = false;
        c = new CheckConnection(context); //Checks for internet connection
        try {
            new CollectDataTask().execute(name).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public String getName(){
        return name;
    }

    public String getCurrency(){
        while (getStatus())
            return currency;
        return "Not found";
    }

    public String getPrice() {
        System.out.println(price);
        while (getStatus())
            return price;
        return "Not found";
    }

    public String getChange() {
        while (getStatus())
            return change;
        return "Not found";
    }

    //Change in percentages therefore doesn't use "symbol"
    public String getChangeP() {
        while (getStatus())
            return changeP;
        return "Not found";
    }

    public String getHighY() {
        while (getStatus())
            return highY;
        return "Not found";
    }

    public String getLowY() {
        while (getStatus())
            return lowY;
        return "Not found";
    }

    public String getHighD() {
        while (getStatus())
            return highD;
        return "Not found";
    }

    public String getLowD() {
        while (getStatus())
            return lowD;
        return "Not found";
    }

    private boolean getStatus(){
        return collectStatus;
    }

    //Test value; Delete later
    public String getTest() {
        while (getStatus())
            return test;
        return "Not found";
    }

    //Collects data in a separate thread
    private class CollectDataTask extends AsyncTask<String, Void, String> {
        private Stock stock;

        //Eventually add an array of stuff
        protected String doInBackground(String... param) {
            try {
                if (c.isConnected()) {
                    stock = YahooFinance.get(param[0]);
                    currency = stock.getCurrency() + " ";
                    price = currency + stock.getQuote().getPrice().floatValue();
                    change = currency + stock.getQuote().getChange().floatValue();
                    changeP = stock.getQuote().getChangeInPercent().floatValue() + "%";
                    highY = currency + stock.getQuote().getYearHigh().floatValue();
                    lowY = currency + stock.getQuote().getYearLow().floatValue();
                    highD = currency + stock.getQuote().getDayHigh().floatValue();
                    lowD = currency + stock.getQuote().getDayLow().floatValue();
                    test = "" + stock.getQuote().getPrice(); //Test value
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
            System.out.println(price); //Testing
            collectStatus = true;
        }
    }
}
