package com.garfieldcs.gar_jhhua.fantasystocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ShowPortfolioActivity extends AppCompatActivity {
    private User user;
    private OwnedStocks ownedStocks;
    private String username;
    private double investedAssets;
    private double totalAssets;
    private double bankAssets;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_portfolio);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("Username");
        password = bundle.getString("Password");
        user = new User(username, password, false, getApplicationContext());
        ownedStocks = new OwnedStocks(user.getID(), getApplicationContext());
        new LoadingData().execute();

    }

    public void goToSearch (View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", username);
        bundle.putString("Password", password);
        bundle.putDouble("investedAssets", investedAssets);
        bundle.putDouble("bankAssets", bankAssets);
        bundle.putDouble("totalAssets", totalAssets);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Loads the information on a separate thread
    private class LoadingData extends AsyncTask<Void, Void, Double[]> {
        ProgressDialog dialog = new ProgressDialog(ShowPortfolioActivity.this);
        boolean status;
        Double investedAssets;
        Double bankAssets;
        Double totalAssets;
        List<String> stocks;

        //Loading circle bar... thing
        protected void onPreExecute() {
            status = false;
            stocks = new ArrayList<String>();
            stocks = ownedStocks.getAsset();

            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog = dialog.show(ShowPortfolioActivity.this,
                    "Please wait", "Retrieving data...", true);

            investedAssets = 0.0;
            super.onPreExecute();
        }

        //Collect and analyze data
        protected Double[] doInBackground(Void... arg0 ) {
            bankAssets = 20000.0; //temporary for testing
            stocks.add("Apple 13");
            if (ownedStocks.getSize() > 0) {
                for (int i = 0; i < ownedStocks.getSize(); i++) {
                    investedAssets = investedAssets +
                            (ownedStocks.getAssetPrice(i) * ownedStocks.getAssetQuantity(i));
                    System.out.println(investedAssets);
                }
            }
            totalAssets = investedAssets + bankAssets;
            return new Double[] {bankAssets, investedAssets, totalAssets}; //result
        }


        //Display the information onto the screen
        protected void onPostExecute(Double[] result) {
            //{bankAssets, investedAssets, totalAssets}

            setContentView(R.layout.activity_show_portfolio);
            ListView list = (ListView) findViewById(R.id.userAssetsList);
            TextView teamName = (TextView) findViewById(R.id.userTeamName);
            TextView totalValue = (TextView) findViewById(R.id.TotalAssetValue);
            TextView bankValue = (TextView) findViewById(R.id.BankAccountValue);
            TextView investedValue = (TextView) findViewById(R.id.InvestedAssetsValue);

            teamName.setText(user.getUserName().toUpperCase());
            bankValue.setText("$" + result[0]);
            investedValue.setText("$" + result[1]);
            totalValue.setText("$" + result[2]);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (ShowPortfolioActivity.this, android.R.layout.simple_list_item_1, stocks);
            list.setAdapter(adapter);

            dialog.dismiss();
        }


    }
}
