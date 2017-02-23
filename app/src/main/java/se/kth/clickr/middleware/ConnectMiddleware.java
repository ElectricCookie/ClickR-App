package se.kth.clickr.middleware;

import android.content.Context;
import android.content.Intent;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;

import java.util.regex.Pattern;

import se.kth.clickr.Action;
import se.kth.clickr.Home;
import se.kth.clickr.R;
import se.kth.clickr.State;
import se.kth.clickr.Store;

/**
 * Created by ElectricCookie on 24.08.2016.
 */
public class ConnectMiddleware  implements Store.Middleware<Action,State>{


    public ConnectMiddleware(){}

    @Override
    public void dispatch(Store<Action, State> store, Action action, Store.NextDispatcher<Action> nextDispatcher) {


        switch (action.getType()){

            case "CONNECT_SUBMIT":
                // Check if IP is valid
                if(!isIp(store.getState().connect().ip())){

                    store.dispatch(new Action("CONNECT_ERROR", R.string.connect_invalid_ip));

                    return;
                }else{

                    String ip = store.getState().connect().ip();
                    int port = store.getState().connect().port();

                    String url = "http://"+ip+":"+port+"/main/about";

                    AsyncHttpGet req = new AsyncHttpGet(url);

                    req.setTimeout(2000);

                    AsyncHttpClient.getDefaultInstance().executeString(req,new AsyncHttpClient.StringCallback(){
                        @Override
                        public void onCompleted(final Exception e, AsyncHttpResponse response, String result) {

                            if (e != null) {
                                store.dispatch(new Action("CONNECT_ERROR",R.string.connect_connection_failed));
                            }else {
                                store.dispatch(new Action("CONNECT_SUCCEEDED",null));
                            }

                        }
                    });




                    break;

                }





        }

        nextDispatcher.next(action);

    }

    public boolean isIp(String ip){
        Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        return pattern.matcher(ip).matches();
    }
}
