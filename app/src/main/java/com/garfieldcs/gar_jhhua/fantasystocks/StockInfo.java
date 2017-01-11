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
    private String symbol;
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

    public String getSymbol(){
        return symbol;
    }

    //Array contains {price, change, changeP, highY, lowY, highD, lowD}
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

    private void setPrice(String price) {
        this.price = price;
    }

    private void setChange(String change) {
        this.change = change;
    }

    private void setChangeP(String changeP) {
        this.changeP = changeP;
    }

    private void setHighY(String highY) {
        this.highY = highY;
    }

    private void setLowY(String lowY) {
        this.lowY = lowY;
    }

    private void setHighD(String highD) {
        this.highD = highD;
    }

    private void setLowD(String lowD) {
        this.lowD = lowD;
    }

    private void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    //Collects data in a separate thread
    private class CollectDataTask extends AsyncTask<String, Void, String> {
        private Stock stock;

        //Eventually add an array of stuff
        protected String doInBackground(String... param) {
            try {
                if (c.isConnected()) {
                    stock = YahooFinance.get(param[0]);
                    setSymbol(stock.getSymbol() + " ");
                    //Numbers are originally in BigDecimal
                    setPrice(symbol + stock.getQuote().getPrice().floatValue());
                    setChange(symbol + stock.getQuote().getChange().floatValue());
                    setChangeP(stock.getQuote().getChangeInPercent().floatValue() +"%");
                    setHighY(symbol + stock.getQuote().getYearHigh().floatValue());
                    setLowY(symbol + stock.getQuote().getYearLow().floatValue());
                    setHighD(symbol + stock.getQuote().getDayHigh().floatValue());
                    setLowD(symbol + stock.getQuote().getDayLow().floatValue());

                    //For debugging, DELETE LATER
                    System.out.println("****** CONNECTED ******");
                    return "Connection success";
                } else {
                    //For debugging, DELETE LATER
                    System.out.println("****** CONNECTION LOST ******");
                    return "Connection error";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Stock error";
            }
        }

        protected void onPostExecute(String result) {
            collectStatus = true;
        }
    }
}
