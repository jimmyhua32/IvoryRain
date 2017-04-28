package com.garfieldcs.gar_jhhua.fantasystocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShowOtherPortfolioActivity extends AppCompatActivity {
    private User user;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_other_portfolio);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("Username");
    }
}
