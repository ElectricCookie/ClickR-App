package se.kth.clickr.reducers;

import android.content.Context;
import android.util.Pair;

import se.kth.clickr.Action;
import se.kth.clickr.ImmutableLogin;
import se.kth.clickr.ImmutableState;
import se.kth.clickr.State;
import se.kth.clickr.Store;

/**
 * Created by ElectricCookie on 25.08.2016.
 */
public class LoginReducer implements Store.Reducer<Action,State> {


    @Override
    public State reduce(Action action, State state) {


        State.Login login = state.login();

        switch (action.getType()){

            case "LOGIN_AUTHENTICATING":
                login = ImmutableLogin.builder().from(login).state(State.LOGIN_STATE.AUTHENTICATING).build();
                break;

            case "LOGIN_SET_GOOGLE_TOKEN":
                login = ImmutableLogin.builder().from(login)
                        .state(State.LOGIN_STATE.AUTHENTICATING)
                        .googleToken((String) action.getPayload())
                        .build();
                break;

            case "LOGIN_GOOGLE_FULFILLED":
                login = ImmutableLogin.builder().from(login).state(State.LOGIN_STATE.SUCCEEDED).build();
                break;

            case "USER_AUTHENTICATE_GOOGLE_REJECTED":
                switch ((String) action.getPayload()){
                    case "invalidToken":
                        login = ImmutableLogin.builder().from(login).state(State.LOGIN_STATE.AUTHENTICATION_FAILED).build();
                        break;
                    case "notExistent":
                        login = ImmutableLogin.builder().from(login).state(State.LOGIN_STATE.NON_EXISTENT_USER).build();
                        break;
                }
                break;

            case "LOGIN_SET_USERNAME":
                login = ImmutableLogin.builder().from(login).username((String) action.getPayload()).build();
                break;

            case "LOGIN_SET_PASSWORD":
                login = ImmutableLogin.builder().from(login).password((String) action.getPayload()).build();
                break;

            case "LOGIN_USERNAME_AVAILABLE":

                login = ImmutableLogin.builder().from(login).usernameValid(true).build();


                break;
            case "LOGIN_USERNAME_ERROR":

                login = ImmutableLogin.builder().from(login).usernameValid(false).usernameError((int) action.getPayload()).build();

                break;

            default:

                return state;

        }


        return ImmutableState.builder().from(state).login(login).build();
    }
}
