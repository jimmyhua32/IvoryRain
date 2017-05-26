package com.garfieldcs.gar_jhhua.fantasystocks.widget;

import android.os.AsyncTask;

import com.garfieldcs.gar_jhhua.fantasystocks.info.MultiStockInfo;
import com.garfieldcs.gar_jhhua.fantasystocks.info.OwnedStocks;
import com.garfieldcs.gar_jhhua.fantasystocks.widget.Formatting;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CalcChange {

    private static double assetValue;
    private static double rawAssetChange;
    private static double initialAssetValue;
    private static double percentValueChange;
    private static double bankAssets;

    private boolean status;

    private Formatting f;
    private OwnedStocks ownedStocks;
    private MultiStockInfo multi;

    //Handles all the stocks of a user
    public CalcChange(MultiStockInfo multi, OwnedStocks ownedStocks) {
        this.multi = multi;
        this.ownedStocks = ownedStocks;
        f = new Formatting();
        assetValue = 0.0;
        rawAssetChange = 0.0;
        initialAssetValue = 0.0;
        percentValueChange = 0.0;
        System.out.println("calc start");
        execute();
    }

    public void execute() {
        try {
            new StockValueData().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        status = true;
    }

    //Add formatting later to accessor methods

    public double getAssetValue() {
        System.out.println(assetValue);
        return f.toDecimal(assetValue, f.TWO_DECIMAL);
    }

    public double getRawAssetChange() {
        return f.toDecimal(rawAssetChange, f.TWO_DECIMAL);
    }

    public double getInitialAssetValue() {
        return f.toDecimal(initialAssetValue, f.TWO_DECIMAL);
    }

    public double getPercentValueChange() {
        return f.toDecimal(percentValueChange, f.ONE_DECIMAL);
    }

    public double getTotalAssetValue() {
        return f.toDecimal(assetValue + bankAssets, f.TWO_DECIMAL);
    }

    public boolean getStatus() {
        return status;
    }

    //Calculates user's asset values
    private class StockValueData extends AsyncTask<Void, Void, Void> {
        ArrayList<Double> price;
        ArrayList<Double> allPrices;
        ArrayList<Integer> quantity;

        @Override
        protected Void doInBackground(Void... params) {
            /*
            Sometimes the ownedStocks methods return empty or null.
            Check if the parameters are being passed correctly into CalcChange.
             */
            price = ownedStocks.getAssetPrice();
            allPrices = multi.getAllPrices();
            quantity = ownedStocks.getAssetQuantity();
            try {
                System.out.println(price.toString());
                System.out.println(allPrices.toString());
                System.out.println(quantity.toString());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            bankAssets = ownedStocks.getBankAssets();

            System.out.println("pre calc");
            try {
                for (int i = 0; i < allPrices.size(); i++) {
                    double currentPrice = allPrices.get(i);
                    double priceChange = price.get(i) - currentPrice;
                    rawAssetChange += priceChange * quantity.get(i);
                    assetValue += currentPrice * quantity.get(i);
                    initialAssetValue += price.get(i) * quantity.get(i);

                }
                System.out.println(rawAssetChange + " " + assetValue + " " + initialAssetValue);

                percentValueChange = initialAssetValue / assetValue * 100;
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            System.out.println("post calc");
        }
    }
}
