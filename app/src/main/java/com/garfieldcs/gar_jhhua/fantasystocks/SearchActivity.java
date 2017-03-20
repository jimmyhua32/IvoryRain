package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<String> stockSearch;
    private double investedAssets;
    private double totalAssets;
    private double bankAssets;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle bundle = getIntent().getExtras();
        investedAssets = bundle.getDouble("investedAssets");
        bankAssets = bundle.getDouble("bankAssets");
        totalAssets = bundle.getDouble("totalAssets");
        username = bundle.getString("Username");
        password = bundle.getString("Password");

        stockSearch = new ArrayList<String>();
        stockSearch.add(0, "VZ");
        stockSearch.add(0, "AMZN");
        stockSearch.add(0, "SBUX");
        stockSearch.add(0, "NKE");
        stockSearch.add(0, "YHOO");
        stockSearch.add(0, "T");
        stockSearch.add(0, "KO");
        stockSearch.add(0, "AAPL");
        stockSearch.add(0, "GOOG");

        ListView list = (ListView) findViewById(R.id.stockSearchList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (SearchActivity.this, android.R.layout.simple_list_item_1, stockSearch);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Stock Number " + position, Toast.LENGTH_LONG)
                        .show();
                goToStock(view, position);
            }
        });

    }


    public void goToStock (View view, int position) {
        ListView list = (ListView) findViewById(R.id.stockSearchList);
        String stockName = stockSearch.get(position);
        Intent intent = new Intent(this, DisplayStockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", username);
        bundle.putString("Password", password);
        bundle.putDouble("investedAssets", investedAssets);
        bundle.putDouble("bankAssets", bankAssets);
        bundle.putDouble("totalAssets", totalAssets);
        bundle.putString("name", stockName);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
