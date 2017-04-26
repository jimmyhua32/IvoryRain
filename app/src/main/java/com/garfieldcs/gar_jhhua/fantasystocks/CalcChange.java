package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CalcChange {

    private static double assetValue;
    private static double rawAssetChange;
    private static double initialAssetValue;
    private static double percentValueChange;

    private ArrayList<String> name;
    private Context context;
    private OwnedStocks ownedStocks;

    //Handles all the stocks of a user
    public CalcChange(ArrayList<String> name, Context context) {
        this.name = name;
        this.context = context;
        assetValue = 0.0;
        rawAssetChange = 0.0;
        initialAssetValue = 0.0;
        percentValueChange = 0.0;
    }

    public void execute(int id) {
        System.out.println("pre stock");
        ownedStocks = new OwnedStocks(id, context);
        System.out.println("post stock");

        try {
            new StockPriceData().execute().get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
    }

    //Add formatting later to accessor methods

    public double getAssetValue() {
        return assetValue;
    }

    public double getRawAssetChange() {
        return rawAssetChange;
    }

    public double getInitialAssetValue() {
        return initialAssetValue;
    }

    public double getPercentValueChange() {
        return percentValueChange;
    }

    public double getTotalAssetValue() {
        return assetValue + ownedStocks.getBankAssets();
    }

    private class StockPriceData extends AsyncTask<String[], Void, Double[]> {
        MultiStockInfo multi;

        @Override
        protected void onPreExecute() {
            System.out.println("pre multi");
            multi = new MultiStockInfo(name.toArray(new String[name.size()]), context);
            System.out.println("post multi");
        }

        @Override
        protected Double[] doInBackground(String[]... params) {
            ArrayList<Double> price = ownedStocks.getAssetPrice();
            ArrayList<Double> allPrices = multi.getAllPrices();
            ArrayList<Integer> quantity = ownedStocks.getAssetQuantity();
            double tRawAssetChange = 0.0;
            double tAssetValue = 0.0;
            double tInitialAssetValue = 0.0;

            System.out.println("pre calc");
            for (int i = 0; i < allPrices.size(); i++) {
                double currentPrice = allPrices.get(i);
                double priceChange = price.get(i) - currentPrice;
                tRawAssetChange+= priceChange * quantity.get(i);
                tAssetValue+= currentPrice * quantity.get(i);
                tInitialAssetValue+= price.get(i) * quantity.get(i);
            }
            System.out.println("post calc");
            return new Double[] {tRawAssetChange, tAssetValue, tInitialAssetValue};
        }

        @Override
        protected void onPostExecute(Double[] result) {
            rawAssetChange = result[0];
            assetValue = result[1];
            initialAssetValue = result[2];

            percentValueChange = initialAssetValue / assetValue * 100;
            System.out.println("fin");
        }
    }
}
