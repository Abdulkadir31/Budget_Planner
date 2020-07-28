package com.example.abdul.oep_database;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends AppCompatActivity {
 private EditText user,pass1;
 public Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user=findViewById(R.id.user);
        pass1=findViewById(R.id.pass1);
        login=findViewById(R.id.login1);


    }

    public void login(View v)
    {
        logindb lb=new logindb();
        lb.execute();

    }

    private class logindb extends AsyncTask<String, String, String> {

        String user1=user.getText().toString();
        String pass=pass1.getText().toString();
         private boolean next=false;
         String msg="";
        @Override
        protected String doInBackground(String... strings) {



                JDBCConnection jd = new JDBCConnection();
                Connection con = jd.jconnection();
                if(con==null) msg="connection failed";


                else {
                    try {

                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery("Select email from login where userid like'" + user1 + "'and password like'" + pass + "'");

                        if (rs.first()) next = true;
                        else msg="Invalid Credentials";

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            return null;

        }
            @Override
        protected void onPostExecute(String s) {
            if (!next) {
                Toast.makeText(Login.this, msg, Toast.LENGTH_LONG).show();
            }
            else
            {
                sessionmaintain s1=new sessionmaintain(Login.this);
                s1.setEntersession(user1);

                Intent in=new Intent(Login.this,GUI.class);
                //in.putExtra("sessionid",user1);
                startActivity(in);


            }
        }
    }




}
