package com.twobitdata.sdsuportal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Login extends Activity{

    private EditText
        username,
        password;

    private TextView loginFailed;

    private Button submit;

    private CheckBox rememberMe;
    Intent login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = new Intent(this, MainActivity.class);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.login);
        loginFailed = (TextView) findViewById(R.id.loginFailed);
        rememberMe = (CheckBox)findViewById(R.id.remember_me);

        isRemebered();

        rememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rememberMe.isChecked()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

                    builder.setMessage("This includes storing your password on your phone only continue if you understand the risk").setTitle("WARNING!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rememberMe.isChecked()){
                    try{
                        FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                        outputStream.write((username.getText().toString() + "," + password.getText().toString()).getBytes());
                        outputStream.close();
                    } catch (Exception e){}
                }

                if (DataManager.login(username.getText().toString(), password.getText().toString())) {
                    DataManager.retrieveData();
                    DataManager.cacheClasses();
                    startActivity(login);
                } else {
                    loginFailed.setText("Incorrect Username or Password");
                }
            }
        });
    }

    public static final String fileName = "SDSU Portal Data";

    private boolean isRemebered(){

        File file;
        FileInputStream inputStream;
        try{
            inputStream = openFileInput(fileName);
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter(",");
            username.setText(scanner.next());
            password.setText(scanner.next());

            if (DataManager.login(username.getText().toString(), password.getText().toString())) {
                DataManager.retrieveData();
                DataManager.cacheClasses();
                startActivity(login);
            } else {
                loginFailed.setText("Incorrect Username or Password");
            }
        } catch(Exception e){
            return false;
        }

        return true;
    }

}
