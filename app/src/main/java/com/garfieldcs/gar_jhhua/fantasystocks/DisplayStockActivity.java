/* Project: Ivory Rain
   1/9/2017
   This activity displays the stock information.
*/

package com.garfieldcs.gar_jhhua.fantasystocks;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayStockActivity extends AppCompatActivity {

    private Toast t;
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

            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Loading data");
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.show();

            if (stockInfo.getStatus()) {
                dialog.hide();
            }

            setContentView(R.layout.activity_display_stock);

            price = stockInfo.getPrice();
            System.out.println(price);

            TextView priceView = (TextView) findViewById(R.id.PriceValue);

            priceView.setText(price);


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

}
