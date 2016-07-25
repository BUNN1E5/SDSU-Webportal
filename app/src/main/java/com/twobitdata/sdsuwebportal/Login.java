package com.twobitdata.sdsuwebportal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity{

    private EditText
        username,
        password;

    private TextView loginFailed;

    private Button submit;
    Intent login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = new Intent(this, MainActivity.class);

        username = (EditText)findViewById(R.id.username);
        password  = (EditText)findViewById(R.id.password);
        submit = (Button)findViewById(R.id.login);
        loginFailed = (TextView)findViewById(R.id.loginFailed);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DataManager.login(username.getText().toString(), password.getText().toString())){
                    DataManager.retrieveData();
                    startActivity(login);
                } else{
                    loginFailed.setText("Incorrect Username or Password");
                }
            }
        });
    }

}
