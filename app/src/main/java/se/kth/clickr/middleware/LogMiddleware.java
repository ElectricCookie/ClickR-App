package se.kth.clickr.middleware;

import android.util.Log;

import se.kth.clickr.Action;
import se.kth.clickr.State;
import se.kth.clickr.Store;

/**
 * Created by ElectricCookie on 24.08.2016.
 */
public class LogMiddleware implements Store.Middleware<Action,State> {

    private final static String TAG = "REDUX-LOG";

    @Override
    public void dispatch(Store<Action, State> store, Action action, Store.NextDispatcher<Action> nextDispatcher) {

        Log.d(TAG, "[DISPATCHED] --> " + action.getType());

        nextDispatcher.next(action);

    }
}
