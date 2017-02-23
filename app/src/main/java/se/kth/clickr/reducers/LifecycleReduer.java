package se.kth.clickr.reducers;

import android.app.Activity;

import se.kth.clickr.Action;
import se.kth.clickr.ImmutableState;
import se.kth.clickr.State;
import se.kth.clickr.Store;

/**
 * Created by ElectricCookie on 25.08.2016.
 */
public class LifecycleReduer implements Store.Reducer<Action,State> {


    @Override
    public State reduce(Action action, State state) {


        switch (action.getType()){

            case "LIFECYCLE_RESUME":


                return ImmutableState.builder().from(state).currentActivity((Activity) action.getPayload()).build();



            default:

                return state;
        }

    }
}
