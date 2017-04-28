package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CalcChange {

    private boolean calcStatus;
    private static double assetValue;
    private static double rawAssetChange;
    private static double initialAssetValue;
    private static double percentValueChange;
    private static double bankAssets;

    private OwnedStocks ownedStocks;
    private MultiStockInfo multi;

    //Handles all the stocks of a user
    public CalcChange(MultiStockInfo multi, OwnedStocks ownedStocks) {
        this.multi = multi;
        this.ownedStocks = ownedStocks;
        calcStatus = false;
        assetValue = 0.0;
        rawAssetChange = 0.0;
        initialAssetValue = 0.0;
        percentValueChange = 0.0;
    }

    public void execute() {
        try {
            new StockValueData().execute().get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
        calcStatus = true;
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
        return assetValue + bankAssets;
    }

    public boolean getStatus() {
        return calcStatus;
    }

    //Calculates user's asset values
    private class StockValueData extends AsyncTask<Void, Void, Void> {
        boolean status;

        @Override
        protected Void doInBackground(Void... params) {
            boolean status = false;
            while (!status) {
                status = multi.getStatus();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ArrayList<Double> price = ownedStocks.getAssetPrice();
            ArrayList<Double> allPrices = multi.getAllPrices();
            ArrayList<Integer> quantity = ownedStocks.getAssetQuantity();
            bankAssets = ownedStocks.getBankAssets();

            System.out.println("pre calc");
            for (int i = 0; i < allPrices.size(); i++) {
                double currentPrice = allPrices.get(i);
                double priceChange = price.get(i) - currentPrice;
                rawAssetChange+= priceChange * quantity.get(i);
                assetValue+= currentPrice * quantity.get(i);
                initialAssetValue+= price.get(i) * quantity.get(i);
            }
            System.out.println(rawAssetChange + " " + assetValue + " " + initialAssetValue);
            System.out.println("post calc");

            percentValueChange = initialAssetValue / assetValue * 100;
        }
    }
}
