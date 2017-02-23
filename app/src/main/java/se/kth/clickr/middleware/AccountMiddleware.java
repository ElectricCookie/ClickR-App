package se.kth.clickr.middleware;

import android.util.Log;

import se.kth.clickr.Action;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Endpoints;
import se.kth.clickr.flood.ImmutableUserGetAccountParams;

/**
 * Created by ElectricCookie on 25.08.2016.
 */
public class AccountMiddleware  implements Store.Middleware<Action,State>{


    @Override
    public void dispatch(Store<Action,State> store, Action action, Store.NextDispatcher<Action> nextMiddlware) {



        switch (action.getType()){

            case "ACCOUNT_REQUEST_LOAD":


                Endpoints.UserGetAccountParams params = ImmutableUserGetAccountParams.builder().build();

                ApiMiddleware.getApi().request(Endpoints.UserGetAccount(params, new Endpoints.UserGetAccountCallback() {
                    @Override
                    public void result(Endpoints.UserProfile result) {
                        store.dispatch(new Action("ACCOUNT_REQUEST_FULFILLED",result));
                    }

                    @Override
                    public void err(String errorCode, String description) {
                        // Handled by other handlers.
                    }
                }));

                break;

        }


        nextMiddlware.next(action);

    }

}
