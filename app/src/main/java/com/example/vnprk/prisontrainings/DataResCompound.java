package com.example.vnprk.prisontrainings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;

/**
 * Created by VNPrk on 17.07.2017.
 */

public class DataResCompound {

    Context context;
    Drawable dataDraw = null;
    String dataStr = "";

    private static DataResCompound mInstance;

    private DataResCompound(Context _cont) {
        context=_cont;
    }

    public static void initInstance(Context _context) {
        if (mInstance == null) {
            mInstance = new DataResCompound(_context);
        }
    }

    public static DataResCompound getInstance() {
        return mInstance;
    }

    public Drawable getDrawableRes(String idRes){
        try {
            dataDraw = ResourcesCompat.getDrawable(context.getResources(),
                    context.getResources().getIdentifier(idRes, "drawable", context.getPackageName()), null);

        }
        catch (Exception ex){
            dataDraw = ResourcesCompat.getDrawable(context.getResources(),android.R.drawable.ic_dialog_alert,null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dataDraw.setTint(context.getResources().getColor(R.color.colorAccent));
            }
        }
        return dataDraw;
    }

    public String getTextRes(String idRes){
        try {
            dataStr = context.getString(context.getResources().getIdentifier(idRes,"string",context.getApplicationContext().getPackageName()));

        }
        catch (Exception ex){
            dataStr = "Ошибка";
        }
        return dataStr;
    }

}
