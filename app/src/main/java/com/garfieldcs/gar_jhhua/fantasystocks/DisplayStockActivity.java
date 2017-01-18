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
    private String price;
    private StockInfo stockInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stock);
        Context context = getApplicationContext();
        t = new Toast(context);
        c = new CheckConnection(context);
        if (c.isConnected()) {
            //t.makeText(context, "Connection Success!", Toast.LENGTH_SHORT);
            name = "AAPL"; //Temporary for testing
            stockInfo = new StockInfo(name, getApplicationContext());


            new loadingData().execute();

        } else {
            //Eventually make all the TextView fields display something like "Null" or "N/A"
            //t.makeText(context, "Connection Failed!", Toast.LENGTH_SHORT);

        }
    }

    //Gets the stock needed to be displayed
    public void setup(String name) {
        this.name = name;
        stockInfo = new StockInfo(name, getApplicationContext());
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
            while (!status) {
                status = stockInfo.getStatus();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            TextView nameView = (TextView) findViewById(R.id.StockName);
            TextView priceView = (TextView) findViewById(R.id.PriceValue);

            nameView.setText(stockInfo.getName());
            priceView.setText(stockInfo.getPrice());

            dialog.dismiss();
            super.onPostExecute(result);
        }
    }
}
