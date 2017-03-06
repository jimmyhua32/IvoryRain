package com.garfieldcs.gar_jhhua.fantasystocks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class BuyStockActivity extends AppCompatActivity {

    private Toast t;
    private CheckConnection c;
    private String name;
    private StockInfo stockInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_stock);

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
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");

        if (c.isConnected()) {

            stockInfo = new StockInfo(name, getApplicationContext());

            new loadingData().execute();

        } else {
            stockInfo = new StockInfo(null, getApplicationContext());

            new loadingData().execute();

        }
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
                buyNameView.setText(stockInfo.getName() + " (" + stockInfo.getSymbol() + ")");
            } else {
                buyNameView.setText(stockInfo.getName());
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
