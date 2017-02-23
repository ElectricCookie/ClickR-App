package se.kth.clickr;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.kth.clickr.flood.Endpoints;

@Value.Immutable
@Gson.TypeAdapters
public abstract class State {

    public enum CONNECT_STATE{
        IDLE,
        SUCCEEDED,
        CONNECTING,
        CONNECTION_FAILED,

    }

    @Value.Immutable
    @Gson.TypeAdapters
    public abstract static class Connect{
        public abstract CONNECT_STATE state();
        public abstract String ip();
        public abstract int port();
        public abstract int error();
    }



    public enum LOGIN_STATE{
        AUTHENTICATING,
        AUTHENTICATION_FAILED,
        NON_EXISTENT_USER,
        REGISTERING,
        SUCCEEDED
    }

    public enum HOME_FRAGMENTS{
        OPEN_SCENARIOS,
        SCENARIO_SESSIONS,
        COUPLED_SCENARIOS
    }

    public enum GENERIC_STATE{
        READY,
        LOADING,
        FULFILLED,
        IDLE,
        REJECTED
    }


    @Value.Immutable
    @Gson.TypeAdapters
    public abstract static class Login{
        public abstract LOGIN_STATE state();
        public abstract String username();
        public abstract String password();
        public abstract boolean usernameValid();
        public abstract int usernameError();
        public abstract String googleToken();

    }


    @Value.Immutable
    @Gson.TypeAdapters
    public abstract static class Home{

        public abstract HOME_FRAGMENTS currentFragment();
        public abstract boolean showJoin();

    }

    @Value.Immutable
    @Gson.TypeAdapters
    public abstract static class TestScenarios{

        public abstract Map<String,Endpoints.TestScenariosTestScenario> items();
        public abstract GENERIC_STATE state();
        public abstract boolean showJoinDialog();

        @Nullable
        public abstract String joinId();

    }


    @Value.Immutable
    @Gson.TypeAdapters
    public abstract static class ScenarioSessions{

        public abstract Map<String,Endpoints.ScenarioSessionsScenarioSession> items();
        public abstract GENERIC_STATE state();

    }


    @Value.Immutable
    @Gson.TypeAdapters
    public abstract static class Tracks{

        public abstract Map<String,Endpoints.TracksTrack> items();
        public abstract GENERIC_STATE state();

    }


    public enum PLAYBACK_STATE{
        WAITING_TRACKS,
        FINISHED,
        STOPPPED,
        PLAYING,
    }


    @Value.Immutable
    @Gson.TypeAdapters
    public static abstract class Playback{

        public abstract PLAYBACK_STATE state();
        public abstract int offset();
        @Nullable
        public abstract String currentTrackId();

        public abstract Map<String,File> filesReady();
        public abstract Map<String,Integer> downloadProgress();

        public abstract boolean allowControls();

        @Nullable
        public abstract String scenarioSessionId();




    }


    public abstract Connect connect();
    public abstract Login login();
    public abstract Tracks tracks();
    public abstract Home home();
    public abstract Playback playback();
    public abstract TestScenarios testScenarios();
    public abstract ScenarioSessions scenarioSessions();

    @Nullable public abstract Endpoints.UserProfile account();



    @Nullable public abstract Activity currentActivity();

    static State getDefault(Context context){
        return ImmutableState.
                builder()
                .connect(
                        ImmutableConnect
                            .builder()
                            .ip("")
                            .port(0)
                            .state(CONNECT_STATE.IDLE)
                            .error(0)
                        .build()
                )
                .currentActivity(null)
                .account(null)
                .playback(
                        ImmutablePlayback
                            .builder()
                            .state(PLAYBACK_STATE.WAITING_TRACKS)
                            .currentTrackId(null)
                            .downloadProgress(new HashMap<String,Integer>())
                            .allowControls(true)
                            .filesReady(new HashMap<String,File>())
                            .offset(0)
                            .build()
                )
                .tracks(
                        ImmutableTracks
                                .builder()
                                .items(new HashMap<String,Endpoints.TracksTrack>())
                                .state(GENERIC_STATE.IDLE)
                                .build()
                )

                .home(
                        ImmutableHome
                            .builder()
                            .currentFragment(HOME_FRAGMENTS.OPEN_SCENARIOS)
                            .showJoin(false)
                        .build()
                )

                .testScenarios(
                        ImmutableTestScenarios
                            .builder()
                            .items(new HashMap<String,Endpoints.TestScenariosTestScenario>())
                            .showJoinDialog(false)
                            .joinId(null)
                            .state(GENERIC_STATE.IDLE)
                            .build()
                )

                .scenarioSessions(
                        ImmutableScenarioSessions
                            .builder()
                            .items(new HashMap<String,Endpoints.ScenarioSessionsScenarioSession>())
                            .state(GENERIC_STATE.IDLE)
                            .build()
                )

                .login(
                        ImmutableLogin
                            .builder()
                            .state(LOGIN_STATE.AUTHENTICATING)
                            .googleToken("")
                            .password("")
                            .username("")
                            .usernameValid(true)
                            .usernameError(R.string.login_error_username_too_short)
                        .build()
                )
                .build();
    }



}
