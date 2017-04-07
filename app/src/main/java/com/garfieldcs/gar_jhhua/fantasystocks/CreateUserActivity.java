package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUserActivity extends AppCompatActivity {

    CheckConnection c;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        c = new CheckConnection(getApplicationContext());
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Creates a new user
    public void newUser(View view) {
        if (c.isConnected()) {
            //Temporary; will be a new screen later
            EditText username = (EditText) findViewById(R.id.usernameEditText);
            EditText password = (EditText) findViewById(R.id.passwordEditText);
            String un = username.getText().toString();
            String pw = password.getText().toString();
            user = new User(un, pw, true, getApplicationContext());
            if (user.userCreated()) {
                Toast toast = Toast.makeText(
                        this, "User created! Please login.", Toast.LENGTH_SHORT);
                toast.show();
                goToMain(view);
            } else {
                Toast toast = Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
