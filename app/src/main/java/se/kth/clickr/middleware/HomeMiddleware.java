package se.kth.clickr.middleware;

import se.kth.clickr.Action;
import se.kth.clickr.State;
import se.kth.clickr.Store;

/**
 * Created by ElectricCookie on 29.08.2016.
 */
public class HomeMiddleware implements Store.Middleware<Action,State> {


    @Override
    public void dispatch(Store<Action, State> store, Action action, Store.NextDispatcher<Action> nextMiddleware) {
        
        
        switch (action.getType()){
            
        }
        
        
        
        nextMiddleware.next(action);
    }
}
