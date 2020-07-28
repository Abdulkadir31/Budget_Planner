package com.example.abdul.oep_database;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static android.os.AsyncTask.*;

public class Graph extends AppCompatActivity {
    private float[] ydata;
    private ArrayList<String> xdata;

    PieChart pieChart;
    TextView sd1,sd2;
    float[] barvalues;
    String user1;

    BarChart barChart;
    ArrayList<String> labels;
    ArrayList<BarEntry> barEntries;
    RelativeLayout rel;
    AnimationDrawable anim;

    protected void onStart() {
        sessionmaintain s2=new sessionmaintain(Graph.this);
        if(s2.getEntersession() ==""){

            Intent loginsuccess=new Intent(Graph.this,Firstpage.class);
            //loginsuccess.putExtra("Username",username);
            startActivity(loginsuccess);
        }
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        rel=findViewById(R.id.graphview);
        anim= (AnimationDrawable) rel.getBackground();
        anim.setEnterFadeDuration(4500);
        anim.setExitFadeDuration(4500);
        anim.start();


        //session data
        sessionmaintain s2=new sessionmaintain(Graph.this);
        if(s2.getEntersession() ==""){

            Intent loginsuccess=new Intent(Graph.this,Firstpage.class);
            //loginsuccess.putExtra("Username",username);
            startActivity(loginsuccess);
        }
        else{
            user1=s2.getEntersession();
            Toast.makeText(Graph.this,"Select Dates",Toast.LENGTH_LONG).show();
        }



        pieChart=(PieChart)findViewById(R.id.piechart1);

        //pieChart.setDescription();


        sd1=findViewById(R.id.sd1);
        sd2=findViewById(R.id.sd2);
        barChart=findViewById(R.id.graph);
        barChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);



        sd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int d = cal.get(Calendar.DAY_OF_MONTH);
                int m = cal.get(Calendar.MONTH);
                int y = cal.get(Calendar.YEAR);

                DatePickerDialog datedialog = new DatePickerDialog(Graph.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if(i2>9)
                            sd1.setText( i + "/" + (i1 + 1) + "/" + i2);
                        else
                            sd1.setText( i + "/" + (i1 + 1) + "/0" + i2);
                    }
                }, y, m, d);

                datedialog.show();
            }
        });
        sd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int d = cal.get(Calendar.DAY_OF_MONTH);
                int m = cal.get(Calendar.MONTH);
                int y = cal.get(Calendar.YEAR);

                DatePickerDialog datedialog = new DatePickerDialog(Graph.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                if(i2>9)
                        sd2.setText( i + "/" + (i1 + 1) + "/" + i2);
                                else
                                    sd2.setText( i + "/" + (i1 + 1) + "/0" + i2);
                    }
                }, y, m, d);

                datedialog.show();
            }
        });

    }
public void showbargraph(View v) throws ExecutionException, InterruptedException {
        if (!sd1.getText().toString().matches("Select Start Date") && !sd2.getText().toString().matches("Select End Date")){


            barChart.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
            createRandomBargraph(sd1.getText().toString(), sd2.getText().toString());
        }
        else {
            Toast.makeText(Graph.this,"Please Select Date",Toast.LENGTH_LONG).show();

        }
}

    public void showpiechart(View v) {
        if (!sd1.getText().toString().matches("Select Start Date")&& !sd2.getText().toString().matches("Select End Date")) {
            barChart.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
            pieChart.setTransparentCircleAlpha(0);
            pieChart.setRotationEnabled(true);
            pieChart.setHoleRadius(25f);
            pieChart.setCenterText("Weekley Budget");
            pieChart.setCenterTextSize(10);

            addDataset(sd1.getText().toString(),sd2.getText().toString());
        } else {
            Toast.makeText(Graph.this, "Please Select Date", Toast.LENGTH_LONG).show();
        }
    }


    private  void addDataset(String Date1,String Date2)
    {

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");

        try {
            Date date1=simpleDateFormat.parse(Date1);
            Date date2=simpleDateFormat.parse(Date2);

            Calendar mdate1= Calendar.getInstance();
            Calendar mdate2=Calendar.getInstance();
            mdate1.setTime(date1);
            mdate2.setTime(date2);
            xdata=new ArrayList<>();
            xdata=pielist(mdate1,mdate2);

            piegraph p1=new piegraph();
            p1.execute().get();

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<PieEntry> yEntry=new ArrayList<>();


        for(int i=0;i<  ydata.length;i++)
        {
            yEntry.add(new PieEntry(ydata[i], xdata.get(i)));
        }


        PieDataSet pieDataSet=new PieDataSet(yEntry,"Budget");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.animateX(2000);
        pieChart.invalidate();


    }



    public void createRandomBargraph(String Date1,String Date2) throws ExecutionException, InterruptedException {

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");

        try {
            Date date1=simpleDateFormat.parse(Date1);
            Date date2=simpleDateFormat.parse(Date2);

            Calendar mdate1= Calendar.getInstance();
            Calendar mdate2=Calendar.getInstance();
            mdate1.setTime(date1);
            mdate2.setTime(date2);
            labels=new ArrayList<>();
            labels=getList(mdate1,mdate2);
            barEntries=new ArrayList<>();


        } catch (ParseException e) {
            e.printStackTrace();
        }
        bargraph b1=new bargraph();
        b1.execute().get();


        for(int j=0;j<labels.size();j++)
        {

            barEntries.add(new BarEntry(j,barvalues[j] ));


        }

        BarDataSet barDataSet=new BarDataSet(barEntries,"Dates");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData=new BarData(barDataSet);
        barChart.setData(barData);
        // barChart.setDescription("Practice graph");
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        XAxis xAxis =barChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int)value);
            }
        });



    }

    public ArrayList<String> getList(Calendar startdate,Calendar enddate)
    {
        ArrayList<String> list=new ArrayList<>();
        while(startdate.compareTo(enddate)<=0){
            list.add(getDate(startdate));
            startdate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }


    public ArrayList<String >pielist(Calendar startdate,Calendar enddate)
    {
        ArrayList<String> list=new ArrayList<>();
        while(startdate.compareTo(enddate)<=0){
            list.add(pieDate(startdate));
            startdate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;

    }


    public String getDate(Calendar cld)
    {
        String curdate= (cld.get(Calendar.MONTH)+1)+"/"+ cld.get(Calendar.DAY_OF_MONTH);

       // curdate=cld.get(Calendar.DAY_OF_WEEK);
        return curdate;

    }

    public String pieDate(Calendar cld){
        String curdate="";

        int i1= cld.get(Calendar.DAY_OF_WEEK);
        curdate=new DateFormatSymbols().getWeekdays()[i1];

        return curdate;


    }

   public class bargraph extends AsyncTask<String, String, String> {

       @Override

       protected String doInBackground(String... strings) {

           barvalues = new float[labels.size()];

           JDBCConnection jd = new JDBCConnection();
           Connection con = jd.jconnection();


           try {

               Statement stmt = con.createStatement();

               ResultSet rs = stmt.executeQuery("Select date,expense from userrecords where date BETWEEN '"+sd1.getText().toString()+"' and '"+sd2.getText().toString()+"' and uname like '"+user1+"' order by date");
               int i = 0;
               while (rs.next()) {
                   // Log.d("graph","Inside loop");

                   barvalues[i] = rs.getInt("expense");
                   i++;
               }



           } catch (SQLException e) {
               e.printStackTrace();
           }


           return null;
       }

       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);
           Toast.makeText(Graph.this, "" + labels.size() + "", Toast.LENGTH_LONG).show();





       }

   }

   public class piegraph extends AsyncTask<String,String,String>{

       @Override
       protected String doInBackground(String... strings) {
           JDBCConnection jd = new JDBCConnection();
           Connection con = jd.jconnection();
           ydata=new float[xdata.size()];

           try {

               Statement stmt = con.createStatement();

               ResultSet rs = stmt.executeQuery("Select date,expense from userrecords where date BETWEEN '"+sd1.getText().toString()+"' and '"+sd2.getText().toString()+"' and uname like '"+user1+"' order by date");
               int i = 0;
               while (rs.next()) {
                    ydata[i]=rs.getInt("expense");

                   i++;
               }



           } catch (SQLException e) {
               e.printStackTrace();
           }

           return null;
       }

       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);
           Toast.makeText(Graph.this,""+xdata.size()+"",Toast.LENGTH_LONG).show();
       }
   }



}
