/* Project: Ivory Rain
   1/9/2017
   Runs and collects stock information in a separate thread.
   Returns the information collected via return methods.
*/

package com.garfieldcs.gar_jhhua.fantasystocks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockInfo {
    private boolean collectStatus;
    private String name;
    private static String currency;
    private static String price;
    private static String change;
    private static String changeP;
    private static String highY;
    private static String lowY;
    private static String highD;
    private static String lowD;

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
        return currency;
    }

    public String getPrice() {
        return price;
    }

    public String getChange() {
        return change;
    }

    //Change in percentage
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
    private class CollectDataTask extends AsyncTask<String, Integer, String[]> {
        private Stock stock;
        private String currency;
        private String price;
        private String change;
        private String changeP;
        private String highY;
        private String lowY;
        private String highD;
        private String lowD;
        private String symbol;

        protected String[] doInBackground(String... param) {
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
                    symbol = stock.getQuote().getSymbol();
                    return new String[]
                            {currency, price, change, changeP, highY, lowY, highD, lowD, symbol};
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPreExecute() {
            collectStatus = false;
        }


        protected void onPostExecute(String[] result) {
            StockInfo.currency = result[0];
            StockInfo.price = result[1];
            StockInfo.change = result[2];
            StockInfo.changeP = result[3];
            StockInfo.highY = result[4];
            StockInfo.lowY = result[5];
            StockInfo.highD = result[6];
            StockInfo.lowD = result[7];
            System.out.println(result[8]); //Add symbol as a field later if it works
            collectStatus = true;
        }
    }
}
