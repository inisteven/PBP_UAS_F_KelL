package com.stevenkristian.tubes.UnitTesting;

import android.content.Context;
import android.content.Intent;

import com.stevenkristian.tubes.admin.viewsMotorAdmin;
public class ActivityUtil {
    private Context context;

    public ActivityUtil(Context context){
        this.context =context;
    }

    public void startActivity(){
        context.startActivity(new Intent(context, viewsMotorAdmin.class));
    }
}
