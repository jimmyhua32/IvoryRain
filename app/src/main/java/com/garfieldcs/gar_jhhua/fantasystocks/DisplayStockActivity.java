package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class DisplayStockActivity extends AppCompatActivity {

    private Toast t;
    private TextView priceView;
    private CheckConnection c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        t = new Toast(context);
        c = new CheckConnection(context);
        if (c.isConnected()) {
            t.makeText(context, "Connection success", Toast.LENGTH_SHORT).show();
        } else {
            t.makeText(context, "Connection error", Toast.LENGTH_SHORT).show();
        }


        priceView = (TextView) findViewById(R.id.price);
        new CollectDataTask().execute("INTC");
        setContentView(R.layout.activity_display_stock);
    }


        //Eventually add an array of stuff
        protected String doInBackground(String... param) {
            BigDecimal price = new BigDecimal(-1);
            try {
                if (c.isConnected()) {
                    stock = YahooFinance.get(param[0]);
                    price = stock.getQuote().getPrice();
                    return "$" + price;
                } else {
                    return "Connection error";
                }
            } catch (IOException e) {
                return "Stock error";
            }
        }

        protected void onPostExecute(String result) {
            priceView.setText("$" + price);
        }

    }
}
