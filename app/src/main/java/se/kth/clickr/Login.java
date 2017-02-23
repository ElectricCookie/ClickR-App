package se.kth.clickr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import trikita.anvil.Anvil;

public class Login extends ApiBoundActivity implements GoogleApiClient.OnConnectionFailedListener{


    GoogleApiClient mGoogleApiClient;
    private final String TAG = "LoginActivity.java";
    private final String ID_TOKEN_PROVIDER = "223063453029-k890cg72jbvrohvkdq0feofpo05jnvkq.apps.googleusercontent.com";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Anvil.mount(findViewById(R.id.content),new LoginView(this));


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(ID_TOKEN_PROVIDER)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);




    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        App.getInstance().getStore().dispatch(new Action("LOGIN_GOOGLE_AUTH_FAILED",null));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            App.getInstance().getStore().dispatch(new Action("LOGIN_SET_GOOGLE_TOKEN",acct.getIdToken()));
        } else {
            App.getInstance().getStore().dispatch(new Action("LOGIN_GOOGLE_AUTH_FAILED",null));

        }
    }

    @Override
    public void onEvent(Action action, State state) {

        switch (action.getType()){
            case "LOGIN_GOOGLE_FULFILLED":

                startActivity(new Intent(this,Home.class));

                break;
        }
    }
}
