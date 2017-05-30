package draegerit.de.arduinoconsole.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.R;

public class MessageHandler {
    public static void showErrorMessage(MainActivity activity, String message) {
        if (message != null && message.trim().length() > 0) {
            new AlertDialog.Builder(activity)
                    .setTitle(activity.getResources().getString(R.string.error_title))
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
}
