package se.kth.clickr.middleware;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import se.kth.clickr.Action;
import se.kth.clickr.App;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Api;
import se.kth.clickr.flood.ApiStatusCallback;
import se.kth.clickr.flood.BaseUrlProvider;

/**
 * Created by ElectricCookie on 24.08.2016.
 */
public class ApiMiddleware implements Store.Middleware<Action,State> {

    private Context mContext;
    private static Api api;
    private final String TAG = this.getClass().getName();
    private static BaseUrlProvider provider;


    public ApiMiddleware(Context context){
        mContext = context;
        api = Api.getInstance(context);

        provider = new BaseUrlProvider(context);


        api.registerStatusCallback(new ApiStatusCallback() {
            @Override
            public void onError(String errorCode, String description) {
                App.getInstance().getStore().dispatch(new Action("API_ERROR",errorCode));
            }

            @Override
            public void onReady() {
                App.getInstance().getStore().dispatch(new Action("API_READY",null));
            }

            @Override
            public void onReconnectTick(int remaining) {
                App.getInstance().getStore().dispatch(new Action("API_RECONNECTING",remaining));
            }
        });

    }

    public static BaseUrlProvider getBaseUrlProvider(){
        return provider;
    }


    public static Api getApi() {
        return api;
    }

    @Override
    public void dispatch(Store<Action, State> store, Action action, Store.NextDispatcher<Action> nextDispatcher) {

        switch (action.getType()){


            case "CONNECT_SUCCEEDED":
                String baseUrl = store.getState().connect().ip()+":"+store.getState().connect().port();
                new BaseUrlProvider(mContext).setBaseUrl(baseUrl);
                api.connect();

                break;

            case "LIFECYCLE_PAUSE":
                    api.pauseConnection();
                break;
            case "LIFECYCLE_RESUME":
                    mContext = (Context) action.getPayload();
                    Log.d(TAG,"Resuming connection");
                    api.resumeConnection((Activity) action.getPayload());
                break;


        }

        nextDispatcher.next(action);

    }
}
