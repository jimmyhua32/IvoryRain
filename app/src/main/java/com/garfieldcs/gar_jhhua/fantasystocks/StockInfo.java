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
    private String name;
    private static String nameR;
    private static boolean collectStatus;
    private static String currency;
    private static String price;
    private static String change;
    private static String changeP;
    private static String highY;
    private static String lowY;
    private static String highD;
    private static String lowD;
    private static String symbol;

    private CheckConnection c;

    //"Name" is the name of the stock
    public StockInfo(String name, Context context) {
        if (name != null) {
            this.name = name;
            c = new CheckConnection(context); //Checks for internet connection
            try {
                new CollectDataTask().execute(name).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            noConnection();
        }
    }

    public String getName(){
        return nameR;
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

    public String getSymbol() {
        return symbol;
    }

    public boolean getStatus(){
        return collectStatus;
    }

    private void noConnection() {
        name = "Not found";
        nameR = "Not found";
        currency = "Not found";
        price = "Not found";
        change = "Not found";
        changeP = "Not found";
        highY = "Not found";
        lowY = "Not found";
        highD = "Not found";
        lowD = "Not found";
        symbol = "Not found";
        collectStatus = true;
    }

    //Collects data in a separate thread
    private class CollectDataTask extends AsyncTask<String, Integer, String[]> {

        protected String[] doInBackground(String... param) {
            try {
                if (c.isConnected()) {
                    Stock stock = YahooFinance.get(param[0]);
                    String currency = stock.getCurrency() + " ";
                    String price = currency +
                            Math.round((stock.getQuote().getPrice().floatValue() * 100) / 100);
                    String change = currency +
                            Math.round((stock.getQuote().getChange().floatValue() * 100) / 100);
                    String changeP = Math.round((stock.getQuote().getChangeInPercent().floatValue()
                            * 100) / 100) + "%";
                    String highY = currency +
                            Math.round((stock.getQuote().getYearHigh().floatValue() * 100) / 100);
                    String lowY = currency +
                            Math.round((stock.getQuote().getYearLow().floatValue() * 100) / 100);
                    String highD = currency +
                            Math.round((stock.getQuote().getDayHigh().floatValue() * 100) / 100);
                    String lowD = currency +
                            Math.round((stock.getQuote().getDayLow().floatValue() * 100) / 100);
                    String symbol = stock.getQuote().getSymbol();
                    String name = stock.getName();
                    return new String[]
                            {currency, price, change, changeP, highY,
                                    lowY, highD, lowD, symbol, name};
                } else {
                    noConnection();
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String[] result) {
            if (c.isConnected()) {
                StockInfo.currency = result[0];
                StockInfo.price = result[1];
                StockInfo.change = result[2];
                StockInfo.changeP = result[3];
                StockInfo.highY = result[4];
                StockInfo.lowY = result[5];
                StockInfo.highD = result[6];
                StockInfo.lowD = result[7];
                StockInfo.symbol = result[8];
                StockInfo.nameR = result[9];
                StockInfo.collectStatus = true;
            }
            super.onPostExecute(result);
        }
    }
}
