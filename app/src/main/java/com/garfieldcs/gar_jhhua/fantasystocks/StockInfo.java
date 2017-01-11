/* Project: Ivory Rain
   1/9/2017
   Runs and collects stock information in a separate thread.
   Returns the information collected via return methods.
*/

package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

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
    private CheckConnection c;

    //"Name" is the name of the stock
    public StockInfo(String name, Context context) {
        this.name = name;
        collectStatus = false;
        c = new CheckConnection(context); //Checks for internet connection
        new CollectDataTask().execute(name);
    }

    public String getName(){
        return name;
    }

    public String getCurrency(){
        return currency;
    }

    public String getPrice() {
        System.out.println(price);
        return price;
    }

    public String getChange() {
        return change;
    }

    //Change in percentages therefore doesn't use "symbol"
    public String getChangeP() {
        return changeP;
    }

    public String getHighY() {
        return highY;
    }

    public String getLowY() {
        return lowY;
    }

    public String getHighD() {
        return highD;
    }

    public String getLowD() {
        return lowD;
    }

    public boolean getStatus(){
        return collectStatus;
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
