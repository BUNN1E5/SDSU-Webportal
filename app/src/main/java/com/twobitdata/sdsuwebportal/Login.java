package com.twobitdata.sdsuwebportal;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity{

    private EditText
        username = (EditText)findViewById(R.id.username),
        password = (EditText)findViewById(R.id.password);

    private Button submit = (Button)findViewById(R.id.login);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.login(username.getText().toString(), password.getText().toString());
            }
        });

    }


}
