package com.garfieldcs.gar_jhhua.fantasystocks.info;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.garfieldcs.gar_jhhua.fantasystocks.widget.Formatting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class OwnedStocks {
    protected static final double INITIAL_BANK_VALUE = 20000;

    private int id;
    private BufferedReader bankReadFrom;
    private Context context;

    private double bankAssets;
    private ArrayList<String> info;
    private ArrayList<String> name;
    private ArrayList<Double> price;
    private ArrayList<Integer> quantity;
    private boolean containStock;

    public OwnedStocks(int id, Context context) {
        this.id = id;
        this.context = context;
        info = new ArrayList<>();
        name = new ArrayList<>();
        price = new ArrayList<>();
        quantity = new ArrayList<>();
        containStock = false;

        try {
            fillArrays();
        } catch (IOException e) {
            System.out.println("Something wrong went with the files");
            e.printStackTrace();
        }
    }

    //Adds a stock by replicating the owned stocks file and adding a new line w/ the info
    public void addStock(String symbol, double price, int quantityPurchased) throws IOException {
        File oldFile = new File(context.getFilesDir(), "S" + id + ".txt");
        File newFile = new File(context.getFilesDir(), "S" + id + "b.txt");
        newFile.createNewFile();
        BufferedReader reader = new BufferedReader(new FileReader(oldFile));
        PrintWriter writeTo = new PrintWriter(newFile);
        String str;
        while ((str = reader.readLine()) != null) {
            writeTo.println(str);
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
        newFile.renameTo(oldFile);

        fillArrays();
    }

    /*
    Removes a stock by reading each line and writing it to a new file except for the line
    to be removed. That line would only be rewritten if the quantity after it is sold is
    greater than 0.
    */
    public void removeStock(String name, double price, int quantitySold) throws IOException {
        File oldFile = new File(context.getFilesDir(), "S" + id + ".txt");
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
        if (removeLine.isEmpty()) {
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
        newFile.renameTo(oldFile);

        fillArrays();
    }


    //Fills the arrays with info from a file
    private void fillArrays() throws IOException {
        refresh();
        File read = new File(context.getFilesDir(), "S" + id + ".txt");
        read.createNewFile();
        File bankRead = new File(context.getFilesDir(), "B" + id + ".txt");
        bankRead.createNewFile();
        BufferedReader readFrom = new BufferedReader(new FileReader(read));
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
        readFrom.close();
        bankReadFrom.close();
    }

    //Clears all the ArrayLists so they can be refilled
    private void refresh() {
        info.clear();
        name.clear();
        price.clear();
        quantity.clear();
    }

    //Updates the amount of money the user has left to spend
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
        }
    }

    public int getShares (String symbol) {
        System.out.println(name.indexOf(symbol));
        return quantity.get(name.indexOf(symbol));
    }

    public double getBankAssets() {
        return Formatting.toDecimal(bankAssets, Formatting.TWO_DECIMAL);
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
}
