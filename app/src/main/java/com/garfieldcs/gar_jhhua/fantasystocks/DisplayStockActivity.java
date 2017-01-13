/* Project: Ivory Rain
   1/9/2017
   This activity displays the stock information.
*/

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
    private String price;
    private StockInfo stockInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        t = new Toast(context);
        c = new CheckConnection(context);
        if (c.isConnected()) {
            //t.makeText(context, "Connection Success!", Toast.LENGTH_SHORT);
            name = "AAPL"; //Temporary for testing
            stockInfo = new StockInfo(name, getApplicationContext());

            price = stockInfo.getTest();
            System.out.println(price);

            priceView = (TextView) findViewById(R.id.PriceValue);
            if (priceView != null) {
                priceView.setText(price);
            }
            setContentView(R.layout.activity_display_stock);
        } else {
            //t.makeText(context, "Connection Failed!", Toast.LENGTH_SHORT);
            setContentView(R.layout.activity_display_stock);
        }
    }

    //Gets the stock needed to be displayed
    public void setup(String name) {
        this.name = name;
        stockInfo = new StockInfo(name, getApplicationContext());
    }

}
