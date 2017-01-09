package com.garfieldcs.gar_jhhua.fantasystocks;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by gar_napodolske on 1/3/2017.
 */
public class ShowPortfolio extends Activity {

    private int playerID;
    private String teamName;
    protected ArrayList Stocks = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_portfolio);
        playerID = user.findUser();
        user.getTeamName();
    }

}

