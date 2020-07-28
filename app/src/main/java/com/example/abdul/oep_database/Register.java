package com.example.abdul.oep_database;

import android.content.Intent;
import android.os.AsyncTask;
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

public class Register extends AppCompatActivity {

    EditText user,pass,confirmpass,email;
    Button register;
    String msg="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user=findViewById(R.id.username);
        pass=findViewById(R.id.pass);
        confirmpass=findViewById(R.id.confirmpass);
        register=findViewById(R.id.register);
        email=findViewById(R.id.email);
        }

        public void register(View v)
        {
            insertdata in=new insertdata();
            in.execute();

        }

    private class insertdata extends AsyncTask<String, String, String> {

        String username=user.getText().toString();
        String password=pass.getText().toString();
        String emailid=email.getText().toString();
        boolean alreadypresent=false;


        @Override
        protected String doInBackground(String... strings) {
            try {

                JDBCConnection jd = new JDBCConnection();
                Connection con = jd.jconnection();
                    Statement stmt = con.createStatement();
                   ResultSet rs=stmt.executeQuery("Select email from login where userid like '"+ username +"'");
                    if(rs.first()) alreadypresent=true;
                   else {

                        rs.close();
                        con.createStatement().execute("Insert Into login values('" + username + "','" + password + "','" + emailid + "')");


                        con.createStatement().execute("create table "+username+" (category VARCHAR(20), date VARCHAR(20), expense INT(10), description VARCHAR(100))");
                        msg = "successful";
                    }


            }  catch (SQLException e) {
                msg="Registration Failed";
                e.printStackTrace();
            }

            return msg;
        }
        protected void onPostExecute(String s) {

            if (msg.equalsIgnoreCase("successful")) {

                Intent loginsuccess = new Intent(Register.this, GUI.class);
                loginsuccess.putExtra("Username", username);
                startActivity(loginsuccess);
                Toast.makeText(getBaseContext(),"Successfully Registered",Toast.LENGTH_LONG).show();

            } else {
                if (alreadypresent)
                    Toast.makeText(Register.this, "User Already Present", Toast.LENGTH_LONG).show();
                else Toast.makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
