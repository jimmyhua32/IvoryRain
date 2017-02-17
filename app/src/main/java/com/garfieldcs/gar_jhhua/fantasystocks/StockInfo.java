/* Project: Ivory Rain
   1/9/2017
   Runs and collects stock information in a separate thread.
   Returns the information collected via return methods.
*/

package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.math.BigDecimal;
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

    private static Double rPrice;
    private static Double rChange;
    private static Double rChangeP;
    private static Double rHighY;
    private static Double rLowY;
    private static Double rHighD;
    private static Double rLowD;

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

    public Double getRawPrice() {
        return rPrice;
    }

    public Double getRawChange() {
        return rChange;
    }

    public Double getRawChangeP() {
        return rChangeP;
    }

    public Double getRawHighY() {
        return rHighY;
    }

    public Double getRawLowY() {
        return rLowY;
    }

    public Double getRawHighD() {
        return rHighD;
    }

    public Double getRawLowD() {
        return rLowD;
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
                    String price = toDecimal(stock.getQuote().getPrice()) + "";
                    String change = toDecimal(stock.getQuote().getChange()) + "";
                    String changeP = toDecimal(stock.getQuote().getChangeInPercent()) + "";
                    String highY = toDecimal(stock.getQuote().getYearHigh()) + "";
                    String lowY = toDecimal(stock.getQuote().getYearLow()) + "";
                    String highD = toDecimal(stock.getQuote().getDayHigh()) + "";
                    String lowD = toDecimal(stock.getQuote().getDayLow()) + "";
                    String symbol = stock.getQuote().getSymbol() + "";
                    String name = stock.getName();

                    return new String[]
                            {currency, currency + price, currency + change, changeP + "%",
                                    currency + highY, currency + lowY, currency + highD,
                                    currency + lowD, symbol, name, price, change, changeP, highY,
                                    lowY, highD, lowD};
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
            if (result!=null) {
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
                StockInfo.rPrice = Double.parseDouble(result[10]);
                StockInfo.rChange = Double.parseDouble(result[11]);
                StockInfo.rChangeP = Double.parseDouble(result[12]);
                StockInfo.rHighY = Double.parseDouble(result[13]);
                StockInfo.rLowY = Double.parseDouble(result[14]);
                StockInfo.rHighD = Double.parseDouble(result[15]);
                StockInfo.rLowD = Double.parseDouble(result[16]);
            }
            StockInfo.collectStatus = true;
            super.onPostExecute(result);
        }

        //Rounds the number to 2 decimal places
        private Double toDecimal(BigDecimal value) {
            return Math.round((value.floatValue() * 100)) / 100.0;
        }
    }
}
