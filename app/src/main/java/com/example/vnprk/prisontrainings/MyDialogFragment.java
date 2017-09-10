package com.example.vnprk.prisontrainings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by VNPrk on 04.09.2017.
 */

public class MyDialogFragment extends DialogFragment {

    final String LOG_TAG = "myLogs";
    MyDialogListener mListener;

    int type = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;
        a=(Activity) context;
        try {
            // Instantiate the MyDialogListener so we can send events to the host
            mListener = (MyDialogListener) a;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(a.toString() + " must implement MyDialogListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Title!")
                .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onYesClicked(MyDialogFragment.this);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onNoClicked(MyDialogFragment.this);
                    }
                })
			   .setNeutralButton("Отмена", null)
                .setMessage("Сохранить результат");
        return adb.create();
    }

    public interface MyDialogListener {
        public void onYesClicked(DialogFragment dialog);
        public void onNoClicked(DialogFragment dialog);
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 2: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 2: onCancel");
    }
}
