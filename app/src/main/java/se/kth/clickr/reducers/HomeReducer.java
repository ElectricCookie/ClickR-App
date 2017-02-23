package se.kth.clickr.reducers;

import se.kth.clickr.Action;
import se.kth.clickr.ImmutableHome;
import se.kth.clickr.ImmutableState;
import se.kth.clickr.State;
import se.kth.clickr.Store;

/**
 * Created by ElectricCookie on 29.08.2016.
 */
public class HomeReducer implements Store.Reducer<Action,State> {


    @Override
    public State reduce(Action action, State state) {

        State.Home home = state.home();

        switch (action.getType()){


            case "HOME_SELECT_FRAGMENT":

                State.HOME_FRAGMENTS fragment = (State.HOME_FRAGMENTS) action.getPayload();

                home = ImmutableHome.builder().from(home).currentFragment(fragment).build();

                break;
            default:
                return state;
        }

        return ImmutableState.builder().from(state).home(home).build();

    }
}
