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

public class SellStockActivity extends AppCompatActivity {

    private Toast t;
    private CheckConnection c;
    private String name; //Stock name
    private StockInfo stockInfo;
    private OwnedStocks ownedStocks;
    private User user;
    private static String stockName;
    private static double stockPrice;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_stock);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("UserID");
        name = bundle.getString("name");
        user = new User(id, getApplicationContext());
        ownedStocks = new OwnedStocks(user.getID(), getApplicationContext());

        //sets up tabHost
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.summaryTab);
        spec.setIndicator("Summary");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.statsTab);
        spec.setIndicator("Stats");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.newsTab);
        spec.setIndicator("News");
        host.addTab(spec);

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

    public void sellStock (View view) {
        EditText sharesTemp = (EditText) findViewById(R.id.sharesToSell);
        int shares = Integer.parseInt(sharesTemp.getText().toString());
        Context context = getApplicationContext();
        CharSequence nullOrNegative = "Input has to be a positive integer!";
        CharSequence positiveShares = "Transaction processing...";
        CharSequence notEnoughStocks = "You do not own enough stocks!";
        CharSequence tranComplete = "Transaction complete!";
        int duration = Toast.LENGTH_SHORT;

        if (shares <= 0) {
            Toast nonToast = Toast.makeText(context, nullOrNegative, duration);
            nonToast.show();
        }
        else {
            Toast tranProcess = Toast.makeText(context, positiveShares, duration);
            tranProcess.show();
            if ((shares > ownedStocks.getShares(name))) {
                Toast noStocks = Toast.makeText(context, notEnoughStocks, duration);
                noStocks.show();
            }
            //If user has enough stocks and is selling more than 0
            else {
                try {
                    ownedStocks.removeStock(stockName, stockPrice, shares);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast complete = Toast.makeText(context, tranComplete, duration);
                complete.show();
                Intent intent = new Intent(this, ShowPortfolioActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("UserID", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    //Cancels the transaction and goes back to portfolio
    public void cancelToDisplay (View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("UserID", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Checks to see if StockInfo is done
    private class loadingData extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(SellStockActivity.this);
        boolean status;

        //Sets up progress dialog
        protected void onPreExecute() {
            status = false;

            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog = dialog.show(SellStockActivity.this,
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

            TextView sellNameView = (TextView) findViewById(R.id.sellStockName);
            TextView sellPriceView = (TextView) findViewById(R.id.sellStockPrice);
            TextView sellPCView = (TextView) findViewById(R.id.sellPCValue);
            TextView sellDHighView = (TextView) findViewById(R.id.sellDHValue);
            TextView sellDLowView = (TextView) findViewById(R.id.sellDLValue);
            TextView sellYHighView = (TextView) findViewById(R.id.sellYHValue);
            TextView sellYLowView = (TextView) findViewById(R.id.sellYLValue);


            if (c.isConnected()) {
                String fullname = name + " (" + stockInfo.getSymbol() + ")";
                sellNameView.setText(fullname);
            } else {
                sellNameView.setText(name);
            }

            SellStockActivity.stockName = stockInfo.getSymbol();
            SellStockActivity.stockPrice = stockInfo.getRawPrice();

            sellPriceView.setText(stockInfo.getPrice());
            sellPCView.setText(stockInfo.getChangeP());
            sellDHighView.setText(stockInfo.getHighD());
            sellDLowView.setText(stockInfo.getLowD());
            sellYHighView.setText(stockInfo.getHighY());
            sellYLowView.setText(stockInfo.getLowY());

            dialog.dismiss();
            super.onPostExecute(result);
        }
    }
}
