package se.kth.clickr.middleware;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import se.kth.clickr.Action;
import se.kth.clickr.Home;
import se.kth.clickr.Login;
import se.kth.clickr.R;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Endpoints;
import se.kth.clickr.flood.ImmutableUserAuthenticateGoogleParams;
import se.kth.clickr.flood.ImmutableUserCheckUsernameParams;
import se.kth.clickr.flood.ImmutableUserRegisterGoogleParams;

/**
 * Created by ElectricCookie on 25.08.2016.
 */
public class LoginMiddleware  implements Store.Middleware<Action,State>{


    @Override
    public void dispatch(Store<Action, State> store, Action action, Store.NextDispatcher<Action> nextDispatcher) {


        switch (action.getType()){


            case "API_ERROR":
                String error = (String) action.getPayload();
                if(error.equals("notLoggedIn")){

                    Activity activity  = store.getState().currentActivity();
                    if(activity != null){
                        activity.startActivity(new Intent(activity,Login.class));
                    }
                }

                break;



            case "LOGIN_SET_USERNAME":

                String username = (String) action.getPayload();

                if(username.length() >= 2){

                    Endpoints.UserCheckUsernameParams params = ImmutableUserCheckUsernameParams.builder().username(username).build();

                    ApiMiddleware.getApi().request(Endpoints.UserCheckUsername(params, new Endpoints.UserCheckUsernameCallback() {
                        @Override
                        public void result(Endpoints.UserCheckUsernameResult result) {
                            store.dispatch(new Action("LOGIN_USERNAME_AVAILABLE",null));
                        }

                        @Override
                        public void err(String errorCode, String description) {
                            switch (errorCode){
                                case "usernameTaken":

                                    store.dispatch(new Action("LOGIN_USERNAME_ERROR", R.string.login_error_username_taken));

                                    break;
                            }
                        }
                    }));

                }


                break;

            case "LOGIN_SUBMIT":

                if(store.getState().login().username().length() < 2){

                    store.dispatch(new Action("LOGIN_USERNAME_ERROR",R.string.login_error_username_too_short));
                }else{

                    Endpoints.UserRegisterGoogleParams params = ImmutableUserRegisterGoogleParams
                            .builder()
                            .password(store.getState().login().password())
                            .username(store.getState().login().username())
                            .build();


                    ApiMiddleware.getApi().request(Endpoints.UserRegisterGoogle(params, new Endpoints.UserRegisterGoogleCallback() {
                        @Override
                        public void result(Endpoints.UserNoResult result) {
                            Context context = (Context) action.getPayload();
                            context.startActivity(new Intent(context,Home.class));
                        }

                        @Override
                        public void err(String errorCode, String description) {
                            switch (errorCode){
                                case "usernameTaken":

                                    store.dispatch(new Action("LOGIN_USERNAME_ERROR", R.string.login_error_username_taken));

                                    break;
                            }
                        }
                    }));
                }

                break;


            case "LOGIN_SET_GOOGLE_TOKEN":



                Endpoints.UserAuthenticateGoogleParams params = ImmutableUserAuthenticateGoogleParams.builder().token((String) action.getPayload()).build();

                ApiMiddleware.getApi().request(Endpoints.UserAuthenticateGoogle(params, new Endpoints.UserAuthenticateGoogleCallback() {
                    @Override
                    public void result(Endpoints.UserNoResult result) {
                        store.dispatch(new Action("LOGIN_GOOGLE_FULFILLED",null));
                    }

                    @Override
                    public void err(String errorCode, String description) {
                        store.dispatch(new Action("LOGIN_GOOGLE_REJECTED",errorCode));
                    }
                }));

                break;

        }



        nextDispatcher.next(action);
    }
}
