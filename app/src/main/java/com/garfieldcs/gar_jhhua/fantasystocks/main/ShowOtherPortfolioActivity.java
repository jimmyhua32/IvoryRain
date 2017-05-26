package com.garfieldcs.gar_jhhua.fantasystocks.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.garfieldcs.gar_jhhua.fantasystocks.R;
import com.garfieldcs.gar_jhhua.fantasystocks.info.User;
import com.garfieldcs.gar_jhhua.fantasystocks.widget.CalcChange;
import com.garfieldcs.gar_jhhua.fantasystocks.info.MultiStockInfo;
import com.garfieldcs.gar_jhhua.fantasystocks.info.OwnedStocks;

import java.util.ArrayList;
import java.util.List;

public class ShowOtherPortfolioActivity extends AppCompatActivity {
    private User userToView;
    private int userToViewID;
    private String usernameToView;
    private OwnedStocks ownedStocks;
    private CalcChange calcChange;
    ArrayList<String> userStocks;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_other_portfolio);

        Bundle bundle = getIntent().getExtras();
        userToViewID = bundle.getInt("UserID");
        id = bundle.getInt("UserID");
        userToView = new User(userToViewID, getApplicationContext());
        ownedStocks = new OwnedStocks(userToViewID, getApplicationContext());
        userStocks = ownedStocks.getAsset();
        usernameToView = userToView.getUserName();

        ArrayList<String> namesTemp = ownedStocks.getAssetName();
        MultiStockInfo multi = new MultiStockInfo
                (namesTemp.toArray(new String[namesTemp.size()]), getApplicationContext());
        calcChange = new CalcChange(multi, ownedStocks);

        new LoadingData().execute();
    }

    public void goToHome (View view) {
        Intent intent = new Intent(this, ShowPortfolioActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("UserID", id);
        bundle.putInt("UserToViewID", userToViewID);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class LoadingData extends AsyncTask<Void, Void, double[]> {
        ProgressDialog dialog = new ProgressDialog(ShowOtherPortfolioActivity.this);
        double investedAssets;
        double bankAssets;
        double totalAssets;
        double percentChange;
        List<String> stocks;

        //Loading circle bar... thing
        @Override
        protected void onPreExecute() {
            stocks = new ArrayList<>();
            stocks = ownedStocks.getAsset();

            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog = dialog.show(ShowOtherPortfolioActivity.this,
                    "Please wait", "Retrieving data...", true);
            super.onPreExecute();
        }

        //Collect data from OwnedStocks and CalcChange
        @Override
        protected double[] doInBackground(Void... params) {
            bankAssets = ownedStocks.getBankAssets();
            investedAssets = calcChange.getAssetValue();
            totalAssets = calcChange.getTotalAssetValue();
            percentChange = calcChange.getPercentValueChange();
            System.out.println
                    (bankAssets + " " + investedAssets + " " + totalAssets + " " + percentChange);
            return new double[] {bankAssets, investedAssets, totalAssets, percentChange};
        }


        //Display the information onto the screen
        @Override
        protected void onPostExecute(double[] result) {
            //{bankAssets, investedAssets, totalAssets}
            setContentView(R.layout.activity_show_other_portfolio);
            ListView list = (ListView) findViewById(R.id.userAssetsList);
            TextView teamName = (TextView) findViewById(R.id.userTeamName);
            TextView totalValue = (TextView) findViewById(R.id.TotalAssetValue);
            TextView bankValue = (TextView) findViewById(R.id.BankAccountValue);
            TextView investedValue = (TextView) findViewById(R.id.InvestedAssetsValue);
            TextView percentValue = (TextView) findViewById(R.id.PercentChangeValue);

            teamName.setText(usernameToView.toUpperCase());
            bankValue.setText("$" + result[0]);
            investedValue.setText("$" + result[1]);
            totalValue.setText("$" + result[2]);
            percentValue.setText(result[3] + "%");

            //Organizes stocks into a clickable list
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (ShowOtherPortfolioActivity.this, android.R.layout.simple_list_item_1, stocks);
            list.setAdapter(adapter);

            dialog.dismiss();

            super.onPostExecute(result);
        }
    }
}
