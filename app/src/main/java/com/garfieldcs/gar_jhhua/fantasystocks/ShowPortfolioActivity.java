package com.garfieldcs.gar_jhhua.fantasystocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ShowPortfolioActivity extends AppCompatActivity {
    private User user;
    private OwnedStocks ownedStocks;
    private String username;
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
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Based on position in the List
    public void goToStock (View view, int position) {
        String stockName = ownedStocks.getAssetName(position);
        System.out.println(stockName + 2);
        Intent intent = new Intent(this, DisplayStockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", username);
        bundle.putString("Password", password);
        bundle.putString("name", stockName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToLeader (View view) {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Username", username);
        bundle.putString("Password", password);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Loads the information on a separate thread
    private class LoadingData extends AsyncTask<Void, Void, double[]> {
        ProgressDialog dialog = new ProgressDialog(ShowPortfolioActivity.this);
        boolean status;
        double investedAssets;
        double bankAssets;
        double totalAssets;
        double percentChange;
        List<String> stocks;

        //Loading circle bar... thing
        @Override
        protected void onPreExecute() {
            status = false;
            stocks = new ArrayList<>();
            stocks = ownedStocks.getAssetRaw();
            System.out.println(stocks.toString());

            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog = dialog.show(ShowPortfolioActivity.this,
                    "Please wait", "Retrieving data...", true);

            super.onPreExecute();
        }

        //Collect data from OwnedStocks
        @Override
        protected double[] doInBackground(Void... params) {
            bankAssets = ownedStocks.getBankAssets();
            investedAssets = ownedStocks.getInitialAssetValue();
            totalAssets = bankAssets + investedAssets;//ownedStocks.getTotalAssets();
            percentChange = 0.0;//ownedStocks.getPercentValueChange();
            System.out.println
                    (bankAssets + " " + investedAssets + " " + totalAssets + " " + percentChange);
            return new double[] {bankAssets, investedAssets, totalAssets, percentChange};
        }


        //Display the information onto the screen
        @Override
        protected void onPostExecute(double[] result) {
            //{bankAssets, investedAssets, totalAssets}
            setContentView(R.layout.activity_show_portfolio);
            ListView list = (ListView) findViewById(R.id.userAssetsList);
            TextView teamName = (TextView) findViewById(R.id.userTeamName);
            TextView totalValue = (TextView) findViewById(R.id.TotalAssetValue);
            TextView bankValue = (TextView) findViewById(R.id.BankAccountValue);
            TextView investedValue = (TextView) findViewById(R.id.InvestedAssetsValue);
            TextView percentValue = (TextView) findViewById(R.id.PercentChangeValue);

            teamName.setText(user.getUserName().toUpperCase());
            bankValue.setText("$" + result[0]);
            investedValue.setText("$" + result[1]);
            totalValue.setText("$" + result[2]);
            percentValue.setText(result[3] + "%");

            //Organizes stocks into a clickable list
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (ShowPortfolioActivity.this, android.R.layout.simple_list_item_1, stocks);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    goToStock(view, position);
                }
            });

            dialog.dismiss();

            super.onPostExecute(result);
        }
    }
}
