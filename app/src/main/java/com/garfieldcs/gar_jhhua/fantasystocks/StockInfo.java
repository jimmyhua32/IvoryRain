/* Project: Ivory Rain
   1/9/2017
   Runs and collects stock information in a separate thread.
   Returns the information collected via return methods.
*/

package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.reflect.Array;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockInfo {
    private String name;
    private String symbol;
    private CheckConnection c;
    private Stock stock;

    //"Name" is the name of the stock
    public StockInfo(String name, Context context) {
        this.name = name;
        c = new CheckConnection(context); //Checks for internet connection
        new CollectDataTask().execute(name);
        symbol = new CollectDataTask().getSymbol();
    }

    public String getName(){
        return name;
    }

    public String getSymbol(){
        new CollectDataTask().getSymbol();
        return symbol;
    }

    //Array contains {price, change, changeP, highY, lowY, highD, lowD}
    public String getPrice() {
        return symbol + Array.getFloat(new CollectDataTask().getInfo(), 0);
    }

    public String getChange() {
        return symbol + Array.getFloat(new CollectDataTask().getInfo(), 1);
    }

    //Change in percentages therefore doesn't use "symbol"
    public String getChangeP() {
        return Array.getFloat(new CollectDataTask().getInfo(), 2) + "%";
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

    //Collects data in a separate thread; called each time you need info?
    private class CollectDataTask extends AsyncTask<String, Void, String> {
        Stock stock;
        Float price; //Current (as of the app being updated) price
        Float change; //Change in price
        Float changeP; //Change in price using percentages
        Float highY; //Yearly high
        Float lowY; //Yearly low
        Float highD; //Daily high
        Float lowD; //Daily low
        String symbol; //Unit of currency

        //Eventually add an array of stuff
        protected String doInBackground(String... param) {
            try {
                if (c.isConnected()) {
                    stock = YahooFinance.get(param[0]);
                    symbol = stock.getSymbol() + " ";
                    //Numbers are originally in BigDecimal
                    price = stock.getQuote().getPrice().floatValue();
                    change = stock.getQuote().getChange().floatValue();
                    changeP = stock.getQuote().getChangeInPercent().floatValue();
                    highY = stock.getQuote().getYearHigh().floatValue();
                    lowY = stock.getQuote().getYearLow().floatValue();
                    highD = stock.getQuote().getDayHigh().floatValue();
                    lowD = stock.getQuote().getDayLow().floatValue();
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
            //Test and see what symbol looks like
            System.out.println("****** " + symbol + "******");
        }

        //Returns the information gathered
        protected String getSymbol() {
            return symbol;
        }

        protected Float[] getInfo() {
            return new Float[] {price, change, changeP, highY, lowY, highD, lowD};
        }
    }
}
