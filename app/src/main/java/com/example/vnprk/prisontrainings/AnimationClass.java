package com.example.vnprk.prisontrainings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by VNPrk on 06.07.2017.
 */

public class AnimationClass {
    Animation in;
    Animation out;
    TextView tv;
    View view;
    String text;
    Object value;
    Context context;
    public AnimationClass(Context _context){
        context=_context;
        in = AnimationUtils.loadAnimation(context,  R.anim.slide_in_right);
        out = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
    }
/*
    public void SetTextView(TextView _tv){
        tv=_tv;
    }
*/
    public Animation getSlideAnimation(View _view, int i, final int typeView, Object _value){
        view=_view;
        value=_value;
        if(i==0){
            in = AnimationUtils.loadAnimation(context,  R.anim.slide_in_left);
            out = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
        }
        else{
            in = AnimationUtils.loadAnimation(context,  R.anim.slide_in_right);
            out = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
        }

        out.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /*tv.setText(text);
                tv.startAnimation(in);*/
                setValueToView(view, value, typeView);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return out;
    }

    public Animation getAlphaAnimation(View _view, final int typeView, Object _value){
        view=_view;
        value=_value;

        in = AnimationUtils.loadAnimation(context,  R.anim.alpha_in);
        out = AnimationUtils.loadAnimation(context, R.anim.alpha_out);

        out.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setValueToView(view, value, typeView);
                //view.startAnimation(in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return out;
    }

    private void setValueToView(View view, Object value, int typeView){
        switch (typeView){
            case 0:
                ((TextView)view).setText((String)value);
                break;
            case 1:
                ((ImageView)view).setImageDrawable((Drawable)value);
                break;
        }
        view.startAnimation(in);
    }
}
