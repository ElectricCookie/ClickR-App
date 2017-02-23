package se.kth.clickr.reducers;

import se.kth.clickr.Action;
import se.kth.clickr.ImmutableState;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Endpoints;

/**
 * Created by ElectricCookie on 25.08.2016.
 */
public class AccountReducer implements Store.Reducer<Action,State> {


    @Override
    public State reduce(Action action, State state) {

        Endpoints.UserProfile account = state.account();


        switch (action.getType()){

            case "ACCOUNT_REQUEST_FULFILLED":

                account =  (Endpoints.UserProfile) action.getPayload();

                break;

        }



        return ImmutableState.builder().from(state).account(account).build();
    }
}
