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

    public CalcChange(ArrayList<String> name, Context context) {
        this.name = name;
        this.context = context;
        assetValue = 0.0;
        rawAssetChange = 0.0;
        initialAssetValue = 0.0;
        percentValueChange = 0.0;
    }

    public void calcChange(int id, Context context) {
        ownedStocks = new OwnedStocks(id, context);

        try {
            new StockPriceData().execute().get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class StockPriceData extends AsyncTask<String[], Void, Double[]> {
        MultiStockInfo multi;

        @Override
        protected void onPreExecute() {
            multi = new MultiStockInfo(name.toArray(new String[name.size()]), context);
        }


        @Override
        protected Double[] doInBackground(String[]... params) {
            ArrayList<Double> price = ownedStocks.getAssetPrice();
            ArrayList<Double> allPrices = multi.getAllPrices();
            ArrayList<Integer> quantity = ownedStocks.getAssetQuantity();

            for (int i = 0; i < name.size(); i++) {
                Double currentPrice = allPrices.get(i);
                double priceChange = price.get(i) - currentPrice;
                rawAssetChange+= priceChange * quantity.get(i);
                assetValue+= currentPrice * quantity.get(i);
                initialAssetValue+= price.get(i) * quantity.get(i);
            }

            percentValueChange = initialAssetValue / assetValue * 100;
            System.out.println("fin");
            return null;
        }

        @Override
        protected void onPostExecute(Double[] result) {

        }

    }
}
