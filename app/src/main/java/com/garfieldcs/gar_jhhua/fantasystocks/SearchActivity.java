package com.garfieldcs.gar_jhhua.fantasystocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ArrayList<String> stockSearch = new ArrayList<String>();
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

    }

       /* lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.list_content);
                String text = textView.getText().toString();
                System.out.println("Choosen Country = : " + text);
    }

    public void goToStock (View view) {
        ListView list = (ListView) findViewById(R.id.stockSearchList);
        list.setOnItemClickListener();
        String selectedStock =
    } */
}
