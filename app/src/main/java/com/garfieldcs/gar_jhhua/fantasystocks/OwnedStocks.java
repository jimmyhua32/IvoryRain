package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class OwnedStocks {
    public static final double INITIAL_BANK_VALUE = 20000;

    private int id;
    private BufferedReader bankReadFrom;
    private BufferedReader readFrom;
    private Context context;

    private double assetValue;
    private double bankAssets;
    private double initialAssetValue;
    private double percentValueChange;
    private double rawAssetChange;
    private ArrayList<String> info;
    private ArrayList<String> name;
    private ArrayList<Double> price;
    private ArrayList<Integer> quantity;
    private boolean containStock;

    private StockInfo stock;
    private Double currentPrice;
    private String fullName;

    public OwnedStocks(int id, Context context) {
        this.id = id;
        this.context = context;
        info = new ArrayList<String>();
        name = new ArrayList<String>();
        price = new ArrayList<Double>();
        quantity = new ArrayList<Integer>();
        containStock = false;

        try {
            fillArrays();
        } catch (IOException e) {
            System.out.println("Something wrong went with the files");
            e.printStackTrace();
        }
    }

    //Fills the arrays with info from a file
    private void fillArrays() throws IOException {
        refresh();
        File read = new File(context.getFilesDir(), "S" + id + ".txt");
        read.createNewFile();
        File bankRead = new File(context.getFilesDir(), "B" + id + ".txt");
        bankRead.createNewFile();
        readFrom = new BufferedReader(new FileReader(read));
        bankReadFrom = new BufferedReader(new FileReader(bankRead));

        String infoString;
        while ((infoString = readFrom.readLine()) != null) {
            containStock = true; //So calling for something in an array doesn't return null
            info.add(infoString);
        }
        for (String s : info) {
            Scanner temp = new Scanner(s);
            name.add(temp.next());
            price.add(Double.parseDouble(temp.next()));
            quantity.add(Integer.parseInt(temp.next()));
        }
        calcBankAssets();
        calcChange();
        readFrom.close();
        bankReadFrom.close();
    }

    private void calcBankAssets() {
        PrintWriter writeTo;
        try {
            BufferedReader tempRead = new BufferedReader(new FileReader
                    (new File(context.getFilesDir(), "B" + id + ".txt")));
            String tempLine = tempRead.readLine();
            if (tempLine == null) {
                writeTo = new PrintWriter(new File(context.getFilesDir(), "B" + id + ".txt"));
                writeTo.println(INITIAL_BANK_VALUE);
                bankAssets = INITIAL_BANK_VALUE;
                writeTo.flush();
                writeTo.close();
            } else {
                if (!(tempLine.equals(""))) {
                    bankAssets = Math.round(Double.parseDouble
                            (bankReadFrom.readLine())* 100) / 100.0;
                } else {
                    writeTo = new PrintWriter(new File(context.getFilesDir(), "B" + id + ".txt"));
                    writeTo.println(INITIAL_BANK_VALUE);
                    bankAssets = INITIAL_BANK_VALUE;
                    writeTo.flush();
                    writeTo.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Calculates various changes in asset value
    public void calcChange() {
        assetValue = 0.0;
        rawAssetChange = 0.0;
        initialAssetValue = 0.0;
        System.out.println("Calculating value changes");

        for (int i = 0; i < info.size(); i++) {
            System.out.println("Pre-stock");
            stock = new StockInfo(name.get(i), context);
            System.out.println("Post-stock");
            try {
                new StockPriceData().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e ) {
                e.printStackTrace();
            }
            System.out.println("Post-stock 2");
            double priceChange = price.get(i) - currentPrice;
            rawAssetChange+= priceChange * quantity.get(i);
            assetValue+= currentPrice * quantity.get(i);
            initialAssetValue+= price.get(i) * quantity.get(i);
        }

        //For testing, remove when above works
        /*
        int count = 0;
        for (double i : price) {
            initialAssetValue+= i * quantity.get(count);
            count++;
        }
        */
        percentValueChange = initialAssetValue / assetValue * 100;
    }

    private void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    private void setFullName(String fullName) {
        this.fullName = fullName;
    }

    //Adds a stock by replicating the owned stocks file and adding a new line w/ the info
    public void addStock(String symbol, double price, int quantityPurchased) throws IOException {
        File oldFile = new File(context.getFilesDir(), "S" + id + ".txt");
        File oldFileName = oldFile;
        File newFile = new File(context.getFilesDir(), "S" + id + ".txt");
        newFile.createNewFile();
        BufferedReader reader = new BufferedReader(new FileReader(oldFile));
        PrintWriter writeTo = new PrintWriter(newFile);
        String str;
        while ((str = reader.readLine()) != null) {
            writeTo.println(str);
            writeTo.flush();
        }
        writeTo.println(symbol + " " + price + " " + quantityPurchased);
        writeTo.flush();

        //Updates the value of bank assets
        writeTo = new PrintWriter(new File(context.getFilesDir(), "B" + id + ".txt"));

        writeTo.println(bankAssets - (price * quantityPurchased));
        writeTo.flush();

        reader.close();
        writeTo.close();
        oldFile.delete();
        newFile.renameTo(oldFileName);

        fillArrays();
    }

    /*
    Removes a stock by reading each line and writing it to a new file except for the line
    to be removed. That line would only be rewritten if the quantity after it is sold is
    greater than 0.
    */
    public void removeStock(String name, double price, int quantitySold) throws IOException {
        File oldFile = new File(context.getFilesDir(), "S" + id + ".txt");
        File oldFileName = oldFile;
        File newFile = new File(context.getFilesDir(), "S" + id + "b.txt");
        newFile.createNewFile();
        BufferedReader tempRead = new BufferedReader(new FileReader(oldFile));

        String currentLine;
        String removeLine = "";
        //Finds the line to be removed within the original file since we only have the symbol
        while (((currentLine = tempRead.readLine()) != null)) {
            Scanner s = new Scanner(currentLine);
            String removeName = s.next();
            if (removeName.equals(name)) {
                removeLine = currentLine;
            }
        }
        tempRead.close();
        if (removeLine.isEmpty() || removeLine == null) {
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(oldFile));
        PrintWriter writer = new PrintWriter(newFile);
        while ((currentLine = reader.readLine()) != null) {
            if (!currentLine.equals(removeLine)) {
                writer.println(currentLine);
                writer.flush();
            } else {
                Scanner s = new Scanner(currentLine);
                String tempName = s.next();
                String pricePurchased = s.next();
                int quantity = s.nextInt() - quantitySold;
                if (quantity > 0) {
                    writer.println(tempName + " " + pricePurchased + " " + quantity);
                    writer.flush();
                }
                PrintWriter writeTo = new PrintWriter(new File
                        (context.getFilesDir(), "B" + id + ".txt"));
                writeTo.println(bankAssets + (quantitySold * price));
                writeTo.flush();
                writeTo.close();
                break;
            }
        }
        reader.close();
        writer.close();
        oldFile.delete();
        newFile.renameTo(oldFileName);

        fillArrays();
    }

    //Clears all the ArrayLists so they can be refilled
    private void refresh() {
        info.clear();
        name.clear();
        price.clear();
        quantity.clear();
    }

    public int getShares (String symbol) {
        return quantity.get(name.indexOf(symbol));
    }

    public double getBankAssets() {
        return bankAssets;
    }

    public double getAssetValue() {
        //calcChange();
        assetValue = 0;
        return assetValue;
    }

    public double getInitialAssetValue() {
        return initialAssetValue;
    }

    public double getRawAssetChange() {
        calcChange();
        return rawAssetChange;
    }

    public double getPercentValueChange() {
        calcChange();
        return percentValueChange;
    }

    public double getTotalAssets() {
        //calcChange();
        return bankAssets + assetValue;
    }

    public int getSize() {
        return info.size();
    }

    public String getAsset(int index) {
        if (containStock) {
            return info.get(index);
        }
        return null;
    }

    //Makes strings that are easier to read than those in info
    public ArrayList<String> getAsset() {
        ArrayList<String> readableInfo = new ArrayList<String>();
        for (int i = 0; i < info.size(); i++) {
            stock = new StockInfo(name.get(i), context);
            new StockNameData().execute();
            String tempName = fullName;
            String priceUSD = "$" + price.get(i);
            String singleQuantity = "Quantity: " + quantity.get(i);
            readableInfo.add(tempName + priceUSD + singleQuantity);
        }
        return readableInfo;
    }

    public ArrayList<String> getAssetRaw() {
        return info;
    }

    public String getAssetName(int index) {
        if (containStock) {
            return name.get(index);
        }
        return null;
    }

    public ArrayList<String> getAssetName() {
        return name;
    }

    public double getAssetPrice(int index) {
        if (containStock) {
            return price.get(index);
        }
        return 0.0;
    }

    public ArrayList<Double> getAssetPrice() {
        return price;
    }

    public Integer getAssetQuantity(int index) {
        if (containStock) {
            return quantity.get(index);
        }
        return 0;
    }

    public ArrayList<Integer> getAssetQuantity() {
        return quantity;
    }

    public int getID() {
        return id;
    }

    //Just retrieves the stock data from StockInfo for calcChange and getAsset
    private class StockPriceData extends AsyncTask<Void, Void, Double> {

        @Override
        protected Double doInBackground(Void... params) {
            boolean status = false;
            while (!status) {
                status = stock.getStatus();
            }
            System.out.println(stock.getRawPrice());
            return stock.getRawPrice();
        }

        @Override
        protected void onPostExecute(Double result) {
            setCurrentPrice(result);
            super.onPostExecute(result);
        }
    }

    private class StockNameData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            boolean status = false;
            while (!status) {
                status = stock.getStatus();
            }
            System.out.println(stock.getRawPrice());
            return stock.getName();
        }

        @Override
        protected void onPostExecute(String result) {
            setFullName(result);
            super.onPostExecute(result);
        }
    }
}
