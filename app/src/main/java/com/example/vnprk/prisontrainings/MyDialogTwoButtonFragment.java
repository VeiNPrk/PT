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

public class MyDialogTwoButtonFragment extends DialogFragment {

    final String LOG_TAG = "myLogs";
    public static final String STR_DIALOG_TITTLE = "str_tittle";
    public static final String STR_DIALOG_MESSAGE = "str_message";
    public static final String STR_DIALOG_YESBUT = "str_yes_but";
    public static final String STR_DIALOG_NOBUT = "str_no_but";
    TwoDialogListener mListener;

    int type = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;
        a=(Activity) context;
        try {
            // Instantiate the MyDialogListener so we can send events to the host
            mListener = (TwoDialogListener) a;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(a.toString() + " must implement MyDialogListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        String tittle=args.getString(STR_DIALOG_TITTLE);
        String message=args.getString(STR_DIALOG_MESSAGE);
        String yesStrBut=args.getString(STR_DIALOG_YESBUT);
        String noStrBut=args.getString(STR_DIALOG_NOBUT);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(tittle)
                .setPositiveButton(yesStrBut, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.yesClicked(MyDialogTwoButtonFragment.this);
                    }
                })
                .setNegativeButton(noStrBut, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.noClicked(MyDialogTwoButtonFragment.this);
                    }
                })
                .setMessage(message);
        return adb.create();
    }

    public interface TwoDialogListener {
        public void yesClicked(DialogFragment dialog);
        public void noClicked(DialogFragment dialog);
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
