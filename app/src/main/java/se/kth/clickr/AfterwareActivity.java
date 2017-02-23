package se.kth.clickr;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ElectricCookie on 31.08.2016.
 */
public class AfterwareActivity extends AppCompatActivity {

    private Store.Afterware<Action,State> afterware;

    AfterwareActivity(){
        afterware = new Store.Afterware<Action, State>() {
            @Override
            public void listen(Action action, State state) {
                AfterwareActivity.this.onEvent(action,state);
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    public void onEvent(Action action,State state){

    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getInstance().getStore().removeAfterware(afterware);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().getStore().registerAfterware(afterware);
    }
}
