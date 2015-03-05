package com.luizgadao.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by luizcarlos on 05/03/15.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle( context.getString( R.string.error_title) )
                .setMessage( context.getString( R.string.error_message) )
                .setPositiveButton( android.R.string.ok, null );

        return builder.create();
    }
}
