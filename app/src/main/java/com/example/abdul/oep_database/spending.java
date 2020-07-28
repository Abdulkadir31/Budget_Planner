package com.example.abdul.oep_database;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.design.support;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class spending extends AppCompatActivity {

        TextView stext1,stext2,stext3,date1;
        String user1;
        ListView list;
    customadaptar cs;
        ArrayList<String> cat=new ArrayList<>();
        ArrayList<String> exp=new ArrayList<>();
        ArrayList<String> desp=new ArrayList<>();
    RelativeLayout rel;
    AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);
        date1=findViewById(R.id.sdate);
        list=findViewById(R.id.list);
        rel=findViewById(R.id.spending);
        anim= (AnimationDrawable) rel.getBackground();
        anim.setEnterFadeDuration(4500);
        anim.setExitFadeDuration(4500);
        anim.start();

            cs=new customadaptar();

        sessionmaintain s2=new sessionmaintain(spending.this);
        if(s2.getEntersession() ==""){

            Intent loginsuccess=new Intent(spending.this,Firstpage.class);
            //loginsuccess.putExtra("Username",username);
            startActivity(loginsuccess);
        }
        else{
             user1=s2.getEntersession();
            Toast.makeText(spending.this,"Please Select a Date",Toast.LENGTH_LONG).show();
        }

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int d = cal.get(Calendar.DAY_OF_MONTH);
                int m = cal.get(Calendar.MONTH);
                int y = cal.get(Calendar.YEAR);

                DatePickerDialog datedialog = new DatePickerDialog(spending.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        if(i2>9)
                            date1.setText( i + "/" + (i1 + 1) + "/" + i2);
                        else
                            date1.setText( i + "/" + (i1 + 1) + "/0" + i2);
                    }
                }, y, m, d);

                datedialog.show();
            }
        });



    }
    protected void onStart() {
        sessionmaintain s2=new sessionmaintain(spending.this);
        if(s2.getEntersession() ==""){

            Intent loginsuccess=new Intent(spending.this,Firstpage.class);
            //loginsuccess.putExtra("Username",username);
            startActivity(loginsuccess);
        }
        super.onStart();
    }

    public void sbuttonclick(View v) throws ExecutionException, InterruptedException {

            desp.clear();
            cat.clear();
            exp.clear();
            cs.notifyDataSetChanged();
        Ssubmit ss=new Ssubmit();
        ss.execute().get();

        list.setAdapter(cs);

    }
    public class Ssubmit extends AsyncTask<String ,String ,String >{

        boolean data=false;
        @Override
        protected String doInBackground(String... strings) {
            JDBCConnection jd = new JDBCConnection();
            Connection con = jd.jconnection();
            try {

                    Statement stmt = con.createStatement();
                  ResultSet rs=stmt.executeQuery("select * from "+user1+" where date like '"+date1.getText().toString()+"'");

                  while(rs.next()){
                      data=true;
                    cat.add(rs.getString(1));
                    exp.add(rs.getString(3));
                    desp.add(rs.getString(4));
                  }



                } catch (SQLException e) {
                    e.printStackTrace();
                }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
    class customadaptar extends BaseAdapter{

        @Override
        public int getCount() {
            return exp.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.customlayout,null);
            stext1=convertView.findViewById(R.id.stext1);
            stext2=convertView.findViewById(R.id.stext2);
            stext3=convertView.findViewById(R.id.stext3);
            stext1.setText("Category is: "+cat.get(position));
            stext2.setText("Expense is :"+exp.get(position));
            stext3.setText("Description is :"+desp.get(position));
            return convertView;
        }
    }


}
