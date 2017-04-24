package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class MultiStockInfo {
    private CheckConnection c;
    private String[] names;

    private static boolean collectStatus;
    private static ArrayList<Double> allPrices;

    public MultiStockInfo(String[] names, Context context) {
        if (names != null) {
            this.names = names;
            c = new CheckConnection(context); //Checks for internet connection
            collectStatus = false;
            try {
                new CollectDataTask().execute(names).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            noConnection();
        }
    }

    private void noConnection() {
        allPrices = null;
        collectStatus = true;
    }

    private ArrayList<Double> getAllPrices() {
        return allPrices;
    }

    private ArrayList<String> getAllNames() {
        return new ArrayList<>(Arrays.asList(names));
    }

    private boolean getStatus() {
        return collectStatus;
    }

    //Collects data in a separate thread
    private class CollectDataTask extends AsyncTask<String[], Void, ArrayList<Double>> {

        protected ArrayList<Double> doInBackground(String[]... param) {
            ArrayList<Double> prices = new ArrayList<>();
            try {
                if (c.isConnected()) {
                    String[] nameArray = param[0];
                    for (String n : nameArray) {
                        Stock stock = YahooFinance.get(n);
                        prices.add(Formatting.toDecimal(stock.getQuote().getPrice()));
                    }
                } else {
                    noConnection();
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prices;
        }

        protected void onPostExecute(ArrayList<Double> result) {
            if (result != null) {
                allPrices = result;
            }
            collectStatus = true;
            super.onPostExecute(result);
        }
    }
}
