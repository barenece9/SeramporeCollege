package com.lnsel.seramporecollege.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lnsel.seramporecollege.R;

/**
 * Created by db on 1/24/2018.
 */

public class DialogUtil {

    public static void customDialog(Context context,String message){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dialog_title=(TextView)dialog.findViewById(R.id.dialog_title);
        dialog_title.setText("Alert !");
        TextView dialog_common_header = (TextView) dialog.findViewById(R.id.dialog_common_header);
        dialog_common_header.setText(message);

        Button dialog_confirmation_cancel = (Button) dialog.findViewById(R.id.dialog_confirmation_cancel);
        dialog_confirmation_cancel.setVisibility(View.GONE);
        dialog_confirmation_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        final Button dialog_confirmation_ok = (Button) dialog.findViewById(R.id.dialog_confirmation_ok);
        dialog_confirmation_ok.setText("Ok");
        dialog_confirmation_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //successfully submitted.............

                dialog.dismiss();

            }
        });
        dialog.show();
    }

}
