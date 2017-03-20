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

public class OwnedStocks {
    private int id; //Name of text file
    private BufferedReader readFrom;
    private Context context;

    private Double bankAssets;
    private ArrayList<String> info; //Whole string which includes name, price, quantity
    private ArrayList<String> name;
    private ArrayList<Double> price;
    private ArrayList<Integer> quantity;
    private boolean containStock;

    public OwnedStocks(int id, Context context) {
        this.id = id;
        this.context = context;
        info = new ArrayList<String>();
        name = new ArrayList<String>();
        price = new ArrayList<Double>();
        quantity = new ArrayList<Integer>();
        containStock = false;

        try {
            readFrom = new BufferedReader(new FileReader(new File
                    (context.getFilesDir(), "S" + id + ".txt")));
            fillArrays();
        } catch (IOException e) {
            System.out.println("Something wrong went with the files");
            e.printStackTrace();
        }
    }

    //Fills the arrays with info from a file
    private void fillArrays() throws IOException {
        refresh();
        String infoString = readFrom.readLine();
        System.out.println(infoString);
        while (infoString != null) {
            info.add(infoString);
            infoString = readFrom.readLine();
            containStock = true;
        }
        for (String i : info) {
            Scanner temp = new Scanner(i);
            name.add(temp.next());
            price.add(Double.parseDouble(temp.next()));
            quantity.add(Integer.parseInt(temp.next()));
        }
    }

    public Double getBankAssets() {
        return bankAssets;
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

    public ArrayList<String> getAsset() {
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

    public Double getAssetPrice(int index) {
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

    //Adds a stock and its info to a file
    public void addStock(String symbol, String price, int quantityPurchased) throws IOException {
        PrintWriter writeTo = null;
        try {
            writeTo = new PrintWriter(new File(context.getFilesDir(), "S" + id + ".txt"));
            System.out.println(symbol + " " + price + " " + quantityPurchased);
            writeTo.println(symbol + " " + price + " " + quantityPurchased);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeTo.close();
        }
        fillArrays();
    }

    //Sells all quantities of a purchase of stock (for now)
    public void removeStock(StockInfo stock, int quantityPurchased) throws IOException {
        String removeLine = stock.getName() + " " + stock.getPrice() + " " + quantityPurchased;
        File oldFile = new File(context.getFilesDir(), "S" + id + ".txt");
        File oldFileName = oldFile;
        File newFile = new File(context.getFilesDir(), "S" + id + "b.txt");
        BufferedReader reader = new BufferedReader(new FileReader(oldFile));
        BufferedWriter writer = new BufferedWriter((new FileWriter(newFile)));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            if (!currentLine.trim().equals(removeLine)) {
                writer.write(currentLine);
                writer.flush();
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

    public int getID() {
        return id;
    }

}
