package com.example.abdul.oep_database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class sessionmaintain {
    SharedPreferences share;
    Context c;
    private String entersession;

    public void removesession(){
        share.edit().clear().commit();
    }



    public String getEntersession() {
        entersession=share.getString("userid","");
        return entersession;

    }

    public void setEntersession(String entersession) {
        this.entersession = entersession;
        share.edit().putString("userid",entersession).commit();
    }



    public  sessionmaintain(Context context){
        this.c=context;
        share= context.getSharedPreferences("userinfo",context.MODE_PRIVATE);


    }
}
