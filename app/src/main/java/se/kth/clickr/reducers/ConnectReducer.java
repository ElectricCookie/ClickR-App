package se.kth.clickr.reducers;

import se.kth.clickr.Action;
import se.kth.clickr.ImmutableConnect;
import se.kth.clickr.ImmutableState;
import se.kth.clickr.State;
import se.kth.clickr.Store;

/**
 * Created by ElectricCookie on 24.08.2016.
 */
public class ConnectReducer implements Store.Reducer<Action, State> {


    @Override
    public State reduce(Action action, State state) {

        State.Connect connect = state.connect();

        switch (action.getType()) {

            case "CONNECT_SET_PORT":

                connect = ImmutableConnect.builder().from(connect).port(Integer.valueOf((String) action.payload)).build();

                break;

            case "CONNECT_SET_IP":

                connect = ImmutableConnect.builder().from(connect).ip((String) action.payload).build();

                break;

            case "CONNECT_SUBMIT":

                connect = ImmutableConnect.builder().from(connect).state(State.CONNECT_STATE.CONNECTING).build();

                break;

            case "CONNECT_SUCCEEDED":

                connect = ImmutableConnect.builder().from(connect).state(State.CONNECT_STATE.SUCCEEDED).build();

                break;

            case "CONNECT_ERROR":
                connect = ImmutableConnect
                        .builder()
                        .from(connect)
                        .state(State.CONNECT_STATE.CONNECTION_FAILED)
                        .error((int) action.getPayload())
                        .build();

                break;

            default:

                return state;

        }


        return ImmutableState.builder().from(state).connect(connect).build();
    }
}
