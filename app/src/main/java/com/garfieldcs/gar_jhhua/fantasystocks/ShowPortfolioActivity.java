package com.garfieldcs.gar_jhhua.fantasystocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import yahoofinance.YahooFinance;

public class ShowPortfolioActivity extends AppCompatActivity {
    User user;

    private String teamName;
    protected ArrayList<String> Stocks = new ArrayList<String>();
    protected ListView list = (ListView) findViewById(R.id.userAssetsList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_portfolio);

        HashMap<String, Integer> nameQuantity = new HashMap<>();

        nameQuantity.put("Apple", 3);
        nameQuantity.put("Google", 4);
        nameQuantity.put("Costco", 2);
        nameQuantity.put("Yahoo", 1);
        nameQuantity.put("Starbucks", 5);
        nameQuantity.put("Microsoft", 6);
        nameQuantity.put("Amazon", 6);

        ArrayList<HashMap<String, Integer>> dataList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("Username");

        TextView teamName = (TextView) findViewById(R.id.userTeamName);
        TextView totalValue = (TextView) findViewById(R.id.TotalAssetValue);
        TextView bankValue = (TextView) findViewById(R.id.BankAccountValue);
        TextView investedValue = (TextView) findViewById(R.id.InvestedAssetsValue);

        //replace these with variables adding up assets
        teamName.setText(username);
        totalValue.setText("$10000");
        bankValue.setText("$10000");
        investedValue.setText("$0");

        SimpleAdapter adapter = new SimpleAdapter(this, dataList, R.layout.list_portfolio_stocks,
                new String[] {"First Line", "Second Line"},
                new int[] {R.id.list_main_text, R.id.list_quantity});

        Iterator it = nameQuantity.entrySet().iterator();

        while (it.hasNext()) {
            HashMap<String, Integer> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultsMap.put("First Line", pair.getValue().hashCode());
            resultsMap.put("Second Line", pair.getValue().hashCode());
            dataList.add(resultsMap);
        }
        list.setAdapter(adapter);
    }

}
