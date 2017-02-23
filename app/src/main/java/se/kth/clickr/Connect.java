package se.kth.clickr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import trikita.anvil.Anvil;
import trikita.anvil.DSL;

public class Connect extends AfterwareActivity{

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle(getString(R.string.connect_connecting_title));
        progressDialog.setMessage( getString(R.string.connect_connecting_message));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);


        App.getInstance().getStore().subscribe(new se.kth.clickr.Store.Subscriber<State>() {
            @Override
            public boolean shouldUpdate(State oldState, State newState) {
                return oldState.connect().state() != newState.connect().state();
            }

            @Override
            public void update(State newState) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(newState.connect().state() == State.CONNECT_STATE.CONNECTING){
                            progressDialog.show();
                        }else{
                            progressDialog.hide();
                        }
                    }
                });


            }
        });

        Anvil.mount(findViewById(R.id.content), new Anvil.Renderable() {
            @Override
            public void view() {
                State.Connect connect = App.getInstance().getStore().getState().connect();
                DSL.linearLayout(() -> {

                    DSL.orientation(LinearLayout.VERTICAL);
                    DSL.size(DSL.MATCH, DSL.MATCH);
                    DSL.padding(40);

                    DSL.textView(() -> {
                        DSL.text(R.string.enter_ip);
                    });

                    DSL.editText(() -> {
                        DSL.text(connect.ip());
                        DSL.size(DSL.MATCH, DSL.WRAP);
                        DSL.onTextChanged((s) -> {
                            App.getInstance().getStore().dispatch(new Action("CONNECT_SET_IP", s.toString()));
                        });
                    });

                    DSL.editText(() -> {
                        DSL.text(String.valueOf(connect.port()));
                        DSL.size(DSL.MATCH, DSL.WRAP);
                        DSL.rawInputType(InputType.TYPE_CLASS_NUMBER);
                        DSL.onTextChanged((s) -> {
                            App.getInstance().getStore().dispatch(new Action("CONNECT_SET_PORT", s.toString()));
                        });
                    });

                    DSL.button(() -> {
                        DSL.text(R.string.connect_action_connect);
                        DSL.size(DSL.MATCH, DSL.WRAP);
                        DSL.onClick((v) -> {
                            App.getInstance().getStore().dispatch(new Action("CONNECT_SUBMIT", null));
                        });
                    });

                });
            }
        });


    }


    @Override
    public void onEvent(Action action, State state) {

        switch (action.getType()){

            case "CONNECT_SUCCEEDED":
                startActivity(new Intent(this,Home.class));
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.hide();
    }
}
