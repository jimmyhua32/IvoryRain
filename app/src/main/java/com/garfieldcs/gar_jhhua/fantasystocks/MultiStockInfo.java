package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class MultiStockInfo {
    private CheckConnection c;
    private String[] names;

    private static boolean collectStatus;
    private static ArrayList<Double> allPrices;
    private static ArrayList<String> allNames;

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

    public ArrayList<Double> getAllPrices() {
        return allPrices;
    }

    public ArrayList<String> getAllNames() {
        return allNames;
    }

    public boolean getStatus() {
        return collectStatus;
    }

    //Collects data in a separate thread
    private class CollectDataTask extends AsyncTask<String[], Void, ArrayList<Double>> {
        String[] nameArray;

        @Override
        protected ArrayList<Double> doInBackground(String[]... param) {
            ArrayList<Double> prices = new ArrayList<>();
            try {
                if (c.isConnected()) {
                    nameArray = param[0];
                    for (String n : nameArray) {
                        Stock stock = YahooFinance.get(n);
                        prices.add(Formatting.toDecimal
                                (stock.getQuote().getPrice(), Formatting.TWO_DECIMAL));
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

        @Override
        protected void onPostExecute(ArrayList<Double> result) {
            if (result != null) {
                allPrices = result;
            }
            new CollectDataTask2().execute(nameArray);
        }
    }

    private class CollectDataTask2 extends AsyncTask<String[], Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String[]... param) {
            ArrayList<String> names = new ArrayList<>();
            try {
                if (c.isConnected()) {
                    String[] nameArray = param[0];
                    for (String n : nameArray) {
                        Stock stock = YahooFinance.get(n);
                        names.add(stock.getName());
                    }
                } else {
                    noConnection();
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return names;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null) {
                allNames = result;
            }
            collectStatus = true;
            System.out.println("multi done");
            super.onPostExecute(result);
        }

    }
}
