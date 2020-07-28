package com.example.abdul.oep_database;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GUI extends AppCompatActivity {
        RelativeLayout rel;
        AnimationDrawable anim;
    @Override
    protected void onStart() {
        sessionmaintain s2=new sessionmaintain(GUI.this);
        if(s2.getEntersession() ==""){

            Intent loginsuccess=new Intent(GUI.this,Firstpage.class);
            //loginsuccess.putExtra("Username",username);
            startActivity(loginsuccess);
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);
        rel=findViewById(R.id.gui);
        anim= (AnimationDrawable) rel.getBackground();
        anim.setEnterFadeDuration(4500);
        anim.setExitFadeDuration(4500);
        anim.start();


        sessionmaintain s2=new sessionmaintain(GUI.this);
        if(s2.getEntersession() ==""){

            Intent loginsuccess=new Intent(GUI.this,Firstpage.class);
            //loginsuccess.putExtra("Username",username);
            startActivity(loginsuccess);
        }
        else{
            String username=s2.getEntersession();
            Toast.makeText(GUI.this,"Welcome "+username,Toast.LENGTH_LONG).show();
        }

    }
   public void logoutofapp(View v){
       sessionmaintain s2=new sessionmaintain(GUI.this);
        s2.removesession();
        Intent loginsuccess=new Intent(GUI.this,Firstpage.class);

        startActivity(loginsuccess);
    }
    public void spendclick(View v)
    {
        Intent loginsuccess=new Intent(GUI.this,saving_expense.class);
        //loginsuccess.putExtra("Username",username);
        startActivity(loginsuccess);

    }

    public void expenseclick(View v)
    {
        Intent loginsuccess=new Intent(GUI.this,spending.class);
//        loginsuccess.putExtra("Username",username);
        startActivity(loginsuccess);

    }

    public void graphClick(View v)
    {
        Intent loginsuccess=new Intent(GUI.this,Graph.class);
        startActivity(loginsuccess);
    }


}
