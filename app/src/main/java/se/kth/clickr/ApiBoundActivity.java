package se.kth.clickr;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import se.kth.clickr.flood.ApiStatusCallback;
import se.kth.clickr.middleware.ApiMiddleware;

/**
 * Created by ElectricCookie on 25.08.2016.
 */
public class ApiBoundActivity extends AfterwareActivity {

    private Dialog dialog;
    private ApiStatusCallback callback;




    private void prepareDialog(){

        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.connect_connecting_title)
                .setMessage(R.string.connect_re_connecting_message)
                .setPositiveButton(R.string.connect_re_connect_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiMiddleware.getApi().connect();
                    }
                })
                .setNeutralButton(R.string.connect_show_connect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ApiBoundActivity.this,Connect.class));
                    }
                }).create();

        callback = new ApiStatusCallback() {
            @Override
            public void onError(String errorCode, String description) {

            }

            @Override
            public void onReady() {
                dialog.hide();
            }

            @Override
            public void onReconnectTick(int remaining) {
                runOnUiThread(() -> {
                    dialog.show();
                    TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
                    messageView.setText(getString(R.string.connect_re_connecting_message,remaining));
                });

            }
        };

    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getInstance().getStore().dispatch(new Action("LIFECYCLE_PAUSE",null));
        ApiMiddleware.getApi().removeStatusCallback(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareDialog();
        App.getInstance().getStore().dispatch(new Action("LIFECYCLE_RESUME",this));
        ApiMiddleware.getApi().registerStatusCallback(callback);
    }
}
