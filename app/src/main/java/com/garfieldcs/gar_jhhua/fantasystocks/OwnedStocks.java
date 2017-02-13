package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class OwnedStocks {
    private int id; //Name of text file
    private BufferedWriter writeTo;
    private BufferedReader readFrom;
    private Context context;

    private ArrayList<String> info; //Whole string which includes name, price, quantity
    private ArrayList<String> name;
    private ArrayList<Double> price;
    private ArrayList<Integer> quantity;

    public OwnedStocks(int id, Context context) {
        this.id = id;
        this.context = context;
        name = new ArrayList<String>();
        price = new ArrayList<Double>();
        quantity = new ArrayList<Integer>();

        try {
            FileOutputStream fos = new FileOutputStream
                    (new File(context.getFilesDir(), id + ".txt"));
            writeTo = new BufferedWriter(new OutputStreamWriter(fos));
            FileReader reader = new FileReader(new File(context.getFilesDir(), id + ".txt"));
            readFrom = new BufferedReader(reader);
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
        while (infoString != null) {
            info.add(infoString);
            infoString = readFrom.readLine();
        }
        for (String i : info) {
            Scanner temp = new Scanner(i);
            name.add(temp.next());
            price.add(temp.nextDouble());
            quantity.add(temp.nextInt());
        }
        readFrom.close();
    }

    //Adds a stock and its info to a file
    public void addStock(StockInfo stock, int quantityPurchased) throws IOException {
        writeTo.write(stock.getName() + " " + stock.getPrice() + " " + quantityPurchased);
        writeTo.newLine();
        fillArrays();
    }

    //Currently sells all quantities of a purchase of stock (for now)
    public void removeStock(StockInfo stock, int quantityPurchased) throws IOException {
        String removeLine = stock.getName() + " " + stock.getPrice() + " " + quantityPurchased;
        File oldFile = new File(context.getFilesDir(), id + ".txt");
        File oldFileName = oldFile;
        File newFile = new File(context.getFilesDir(), id + "b.txt");
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
        refresh();
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
