package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DisplayStockActivity extends AppCompatActivity {

    private Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t = new Toast(this);
        t.makeText(this, "Connection error", Toast.LENGTH_SHORT);
        if (checkConnection()) {
            t.setText("Connection success!");
            t.show();
        } else {
            t.setText("Connection error");
            t.show();
        }
        setContentView(R.layout.activity_display_stock);
    }

    private boolean checkConnection() {
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConnectivityManager cManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
