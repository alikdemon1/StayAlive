package com.alisher.android.stayalive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private Button signUpBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolbar();
        initComponents();
        register();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initProgressWhileRegistering(){
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Proccessing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }

    private void register(){
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                ParseUser user = new ParseUser();
                user.setUsername(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Saving user failed.", Toast.LENGTH_SHORT).show();
                            Log.w("", "Error : " + e.getMessage() + ":::" + e.getCode());
                            if (e.getCode() == 202) {
                                Toast.makeText(RegisterActivity.this,"Username already taken. \n Please choose another username.",Toast.LENGTH_LONG).show();
                                name.setText("");
                                password.setText("");
                                email.setText("");
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "User Saved",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                    }
                });
            }
        });
    }

    private void initComponents(){
        name = (EditText) findViewById(R.id.name_register);
        email = (EditText) findViewById(R.id.email_register);
        password = (EditText) findViewById(R.id.password_register);
        signUpBtn = (Button) findViewById(R.id.register_button);
    }
}
