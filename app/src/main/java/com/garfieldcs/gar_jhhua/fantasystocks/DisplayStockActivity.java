package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayStockActivity extends AppCompatActivity {

    private Toast t;
    private TextView priceView;
    private CheckConnection c;
    private String name;
    private Float price;
    private StockInfo stockInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        t = new Toast(context);
        c = new CheckConnection(context);
        c.isConnected();

        while (name == null) {
        }
        
        price = stockInfo.getPrice();

        priceView = (TextView) findViewById(R.id.price);
        priceView.setText("This is the price: " + price);
        setContentView(R.layout.activity_display_stock);
    }

    public void setup(String name) {
        this.name = name;
        stockInfo = new StockInfo(name, getApplicationContext());
    }

}
