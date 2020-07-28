package com.example.abdul.oep_database;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class saving_expense extends AppCompatActivity {

    TextView t1;
    EditText e1,e2;
    String user1;
    Spinner s1;
    RelativeLayout rel;
    AnimationDrawable anim;
    protected void onStart() {
        sessionmaintain s2=new sessionmaintain(saving_expense.this);
        if(s2.getEntersession() ==""){

            Intent loginsuccess=new Intent(saving_expense.this,Firstpage.class);
            //loginsuccess.putExtra("Username",username);
            startActivity(loginsuccess);
        }
        super.onStart();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_expense);
        t1=findViewById(R.id.date);
        e1=findViewById(R.id.amount);
        e2=findViewById(R.id.comments);
        s1=findViewById(R.id.s1);
        rel=findViewById(R.id.savingexpense);
        anim= (AnimationDrawable) rel.getBackground();
        anim.setEnterFadeDuration(4500);
        anim.setExitFadeDuration(4500);
        anim.start();

        sessionmaintain s2=new sessionmaintain(saving_expense.this);
        if(s2.getEntersession() ==""){

            Intent loginsuccess=new Intent(saving_expense.this,Firstpage.class);
            //loginsuccess.putExtra("Username",username);
            startActivity(loginsuccess);
        }
        else{
            user1=s2.getEntersession();
            Toast.makeText(saving_expense.this,"Enter Expense",Toast.LENGTH_LONG).show();
        }



        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int d = cal.get(Calendar.DAY_OF_MONTH);
                int m = cal.get(Calendar.MONTH);
                int y = cal.get(Calendar.YEAR);

                DatePickerDialog datedialog = new DatePickerDialog(saving_expense.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        if(i2>9)
                            t1.setText( i + "/" + (i1 + 1) + "/" + i2);
                        else
                            t1.setText( i + "/" + (i1 + 1) + "/0" + i2);
                    }
                }, y, m, d);

                datedialog.show();
            }
        });


    }

    public void submitclick(View v){
        expense exp=new expense();
        exp.execute();
    }

    public class expense extends AsyncTask<String,String,String>
    {
        String msg;
        int insert;
        @Override
        protected String doInBackground(String... strings) {


            JDBCConnection jd = new JDBCConnection();
            Connection con = jd.jconnection();
            if(con==null) msg="connection failed";


            else {
                try {

                    Statement stmt = con.createStatement();
                   insert= stmt.executeUpdate("Insert into "+user1+" values ('"+s1.getSelectedItem().toString()+"','"+t1.getText().toString()+"','"+e1.getText().toString()+"','"+e2.getText().toString()+"')");
                   if (insert!=0) msg="Record Entered";
                   else msg="Record not inserted";
                  ResultSet rs= con.createStatement().executeQuery("Select expense from userrecords where uname like '"+user1+"' and date like '"+t1.getText().toString()+"'");
                  if(rs.next()){
                      int expense=rs.getInt("expense")+Integer.parseInt(e1.getText().toString());
                      con.createStatement().executeUpdate("Update userrecords set expense='"+expense+"' where uname like '"+user1+"' and date like '"+t1.getText().toString()+"'");
                  }
                  else{
                      con.createStatement().executeUpdate("Insert into userrecords values ('"+user1+"','"+e1.getText().toString()+"','"+t1.getText().toString()+"')");
                  }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(saving_expense.this,msg,Toast.LENGTH_LONG).show();
        }
    }
}
