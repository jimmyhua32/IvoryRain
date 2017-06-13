package com.garfieldcs.gar_jhhua.fantasystocks.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.garfieldcs.gar_jhhua.fantasystocks.widget.CheckConnection;
import com.garfieldcs.gar_jhhua.fantasystocks.R;
import com.garfieldcs.gar_jhhua.fantasystocks.info.User;
import com.garfieldcs.gar_jhhua.fantasystocks.info.OwnedStocks;
import com.garfieldcs.gar_jhhua.fantasystocks.info.StockInfo;

import java.io.IOException;

public class BuyStockActivity extends AppCompatActivity {

    private Toast t;
    private CheckConnection c;
    private String name;
    private StockInfo stockInfo;
    private OwnedStocks ownedStocks;
    private static String stockName;
    private static double stockPrice;
    private int userID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_stock);

        userID = getIntent().getIntExtra("UserID", -1);
        name = getIntent().getStringExtra("name");
        ownedStocks = new OwnedStocks(userID, getApplicationContext());


        Context context = getApplicationContext();
        t = new Toast(context);
        c = new CheckConnection(context);

        if (c.isConnected()) {

            stockInfo = new StockInfo(name, getApplicationContext());

            new loadingData().execute();

        } else {
            stockInfo = new StockInfo(null, getApplicationContext());

            new loadingData().execute();

        }
    }

    //Adds a stock using OwnedStocks
    public void purchaseStock (View view) {
        EditText sharesTemp = (EditText) findViewById(R.id.sharesWanted);
        int shares = Integer.parseInt(sharesTemp.getText().toString());
        Context context = getApplicationContext();
        CharSequence nullOrNegative = "Input has to be a positive integer!";
        CharSequence positiveShares = "Transaction processing...";
        CharSequence notEnoughMoney = "Not enough money in bank!";
        CharSequence tranComplete = "Transaction complete!";
        int duration = Toast.LENGTH_SHORT;

        if (shares <= 0) {
            Toast nonToast = Toast.makeText(context, nullOrNegative, duration);
            nonToast.show();
        }
        else {
            Toast tranProcess = Toast.makeText(context, positiveShares, duration);
            tranProcess.show();
            if ((shares * stockInfo.getRawPrice()) > ownedStocks.getBankAssets()) {
                Toast noMoney = Toast.makeText(context, notEnoughMoney, duration);
                noMoney.show();
            }
            else {
                try {
                    ownedStocks.addStock(stockName, stockPrice, shares);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast complete = Toast.makeText(context, tranComplete, duration);
                complete.show();
                Intent intent = new Intent(this, ShowPortfolioActivity.class);
                intent.putExtra("UserID", userID);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        }

    }

    public void cancelToDisplay (View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("UserID", userID);
        startActivity(intent);
    }

    //Checks to see if StockInfo is done
    private class loadingData extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(BuyStockActivity.this);
        boolean status;

        //Sets up progress dialog
        protected void onPreExecute() {
            status = false;

            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog = dialog.show(BuyStockActivity.this,
                    "Please wait", "Retrieving data...", true);
            super.onPreExecute();
        }

        //Checks to see if data finished collecting
        protected Void doInBackground(Void... arg0) {
            while (!status) {
                status = stockInfo.getStatus();
            }
            return null;
        }

        //Changes the views and dismisses the progress dialog
        protected void onPostExecute(Void result) {

            TextView buyNameView = (TextView) findViewById(R.id.buyStockName);
            TextView buyPriceView = (TextView) findViewById(R.id.buyStockPrice);
            TextView buyPCView = (TextView) findViewById(R.id.buyPCValue);
            TextView buyDHighView = (TextView) findViewById(R.id.buyDHValue);
            TextView buyDLowView = (TextView) findViewById(R.id.buyDLValue);
            TextView buyYHighView = (TextView) findViewById(R.id.buyYHValue);
            TextView buyYLowView = (TextView) findViewById(R.id.buyYLValue);


            if (c.isConnected()) {
                String fullname = stockInfo.getName() + " (" + stockInfo.getSymbol() + ")";
                buyNameView.setText(fullname);
            } else {
                buyNameView.setText(name);
            }

            BuyStockActivity.stockName = stockInfo.getSymbol();
            BuyStockActivity.stockPrice = stockInfo.getRawPrice();

            buyPriceView.setText(stockInfo.getPrice());
            buyPCView.setText(stockInfo.getChangeP());
            buyDHighView.setText(stockInfo.getHighD());
            buyDLowView.setText(stockInfo.getLowD());
            buyYHighView.setText(stockInfo.getHighY());
            buyYLowView.setText(stockInfo.getLowY());

            dialog.dismiss();
            super.onPostExecute(result);
        }
    }
}