/* Project: Ivory Rain
   1/9/2017
   This activity displays the stock information.
*/

package com.garfieldcs.gar_jhhua.fantasystocks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class DisplayStockActivity extends AppCompatActivity {

    private Toast t;
    private CheckConnection c;
    private String name;
    private StockInfo stockInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stock);
        Context context = getApplicationContext();
        t = new Toast(context);
        c = new CheckConnection(context);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
//        name = "AAPL";
//        if (c.isConnected()) {
//            name = "AAPL"; //Temporary for testing
//
//            stockInfo = new StockInfo(name, getApplicationContext());
//
//            new loadingData().execute();
//
//        } else {
//            stockInfo = new StockInfo(null, getApplicationContext());
//
//            new loadingData().execute();
//
//        }
    }

    //Checks to see if StockInfo is done
    private class loadingData extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(DisplayStockActivity.this);
        boolean status;

        protected void onPreExecute() {
            status = false;

            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog = dialog.show(DisplayStockActivity.this,
                    "Please wait", "Retrieving data...", true);
            super.onPreExecute();
        }

        protected Void doInBackground(Void... arg0) {
            if (c.isConnected()) {
                stockInfo = new StockInfo(name, getApplicationContext());
            } else {
                stockInfo = new StockInfo(null, getApplicationContext());
            }
            while (!status) {
                status = stockInfo.getStatus();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            TextView nameView = (TextView) findViewById(R.id.StockName);
            TextView priceView = (TextView) findViewById(R.id.PriceValue);
            TextView priceChangeView = (TextView) findViewById(R.id.PCValue);
            TextView dailyHighView = (TextView) findViewById(R.id.DailyHighValue);
            TextView dailyLowView = (TextView) findViewById(R.id.DailyLowValue);
            TextView yearHighView = (TextView) findViewById(R.id.YearHighValue);
            TextView yearLowView = (TextView) findViewById(R.id.YearLowValue);


            if (c.isConnected()) {
                nameView.setText(stockInfo.getName() + " (" + stockInfo.getSymbol() + ")");
            } else {
                nameView.setText(stockInfo.getName());
            }
            priceView.setText(stockInfo.getPrice());
            priceChangeView.setText(stockInfo.getChangeP());
            dailyHighView.setText(stockInfo.getHighD());
            dailyLowView.setText(stockInfo.getLowD());
            yearHighView.setText(stockInfo.getHighY());
            yearLowView.setText(stockInfo.getLowY());

            dialog.dismiss();
            super.onPostExecute(result);
        }
    }
}
