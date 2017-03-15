package com.garfieldcs.gar_jhhua.fantasystocks;

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

import java.io.IOException;

public class BuyStockActivity extends AppCompatActivity {

    private Toast t;
    private CheckConnection c;
    private String name;
    private StockInfo stockInfo;
    private OwnedStocks ownedStocks;
    private User user;
    private double investedAssets;
    private double totalAssets;
    private double bankAssets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_stock);

        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("Username");
        String password = bundle.getString("Password");
        investedAssets = bundle.getDouble("investedAssets");
        totalAssets = bundle.getDouble("totalAssets");
        bankAssets = bundle.getDouble("bankAssets");
        name = bundle.getString("name");

        user = new User(username, password, false, getApplicationContext());
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
            if ((shares * stockInfo.getRawPrice()) > bankAssets) {
                Toast noMoney = Toast.makeText(context, notEnoughMoney, duration);
                noMoney.show();
            }
            else {
                investedAssets += (shares * stockInfo.getRawPrice());
                bankAssets -= (shares * stockInfo.getRawPrice());
                try {
                    ownedStocks.addStock("GOOG", shares);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast complete = Toast.makeText(context, tranComplete, duration);
                complete.show();
            }
        }

    }

    public void cancelToDisplay (View view) {
        Intent intent = new Intent(this, DisplayStockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("investedAssets", investedAssets);
        bundle.putDouble("bankAssets", bankAssets);
        bundle.putDouble("totalAssets", totalAssets);
        bundle.putString("name", name);
        intent.putExtras(bundle);
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
