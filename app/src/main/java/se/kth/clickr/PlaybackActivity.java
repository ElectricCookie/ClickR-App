package se.kth.clickr;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.kth.clickr.flood.BaseUrlProvider;
import se.kth.clickr.flood.Endpoints;
import se.kth.clickr.flood.ImmutableScenarioSessionsButtonPress;
import se.kth.clickr.flood.ImmutableScenarioSessionsSaveOffsetParams;
import se.kth.clickr.middleware.ApiMiddleware;
import trikita.anvil.Anvil;
import trikita.anvil.DSL;
import trikita.anvil.cardview.v7.CardViewv7DSL;
import trikita.anvil.design.DesignDSL;
import trikita.anvil.recyclerview.Recycler;

public class PlaybackActivity extends ApiBoundActivity {


    private ButtonAdapter buttonAdapter;
    private MediaPlayer mediaPlayer;
    private String lastTrackId;
    private Handler handler = new Handler();
    private Runnable offsetUpdater = new Runnable() {
        @Override
        public void run() {
            App.getInstance().getStore().dispatch(new Action("PLAYBACK_SET_OFFSET",getCurrentOffset()));
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setupMediaPlayer();

        buttonAdapter = new ButtonAdapter(this);


        Anvil.mount(findViewById(R.id.content_scenario_session), new Anvil.Renderable() {
            @Override
            public void view() {
                State state = App.getInstance().getStore().getState();

                DSL.linearLayout(() -> {
                    DSL.size(DSL.MATCH,DSL.MATCH);
                    DSL.orientation(LinearLayout.VERTICAL);

                    CardViewv7DSL.cardView(() -> {
                        DSL.size(DSL.MATCH,DSL.WRAP);
                        CardViewv7DSL.radius(0);
                        DSL.linearLayout(() -> {
                            DSL.size(DSL.MATCH,DSL.WRAP);
                            DSL.orientation(LinearLayout.HORIZONTAL);
                            DSL.padding(DSL.dip(15));


                            if(state.playback().state() == State.PLAYBACK_STATE.PLAYING){
                                DSL.button(() -> {
                                    DSL.text(R.string.playback_pause);
                                    DSL.onClick((v) -> {
                                        App.getInstance().getStore().dispatch(new Action("PLAYBACK_STOP",null));
                                    });
                                });
                            }

                            if(state.playback().state() == State.PLAYBACK_STATE.STOPPPED){
                                DSL.button(() -> {
                                    DSL.text(R.string.playback_play);
                                    DSL.onClick((v) -> {
                                        if(getCurrentTrack() != null){
                                            App.getInstance().getStore().dispatch(new Action("PLAYBACK_PLAY",getCurrentTrack().id()));
                                        }

                                    });
                                });
                            }

                            DSL.linearLayout(() -> {
                                DSL.size(DSL.MATCH,DSL.WRAP);
                                DSL.weight(1);
                                DSL.gravity(DSL.CENTER_VERTICAL);
                                DSL.padding(DSL.dip(15),0,0,0);
                                DSL.orientation(LinearLayout.VERTICAL);
                                DSL.textView(() -> {
                                    if(getCurrentTrack() != null){
                                        DSL.text(getString(R.string.playback_playing_status,getCurrentTrack().title()));
                                    }
                                    DSL.weight(1);
                                });

                                DSL.textView(() -> {
                                    if(getCurrentTrack() != null){
                                        DSL.text(getString(R.string.playback_playing_time,formatSeconds(state.playback().offset())));
                                    }
                                    DSL.weight(1);
                                });

                            });


                        });



                    });


                    Recycler.view(() -> {
                        DSL.size(DSL.MATCH,DSL.FILL);
                        Recycler.layoutManager(new LinearLayoutManager(PlaybackActivity.this));
                        Recycler.hasFixedSize(false);
                        Recycler.adapter(buttonAdapter);
                    });

                });


            }
        });
    }


    private void setupMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setScreenOnWhilePlaying(true);
        mediaPlayer.setLooping(false);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                App.getInstance().getStore().dispatch(new Action("PLAYBACK_FINISHED",null));
            }
        });
    }

    public String leadingZeroify(double nr){
        if(nr < 10){
            return "0"+Integer.toString((int) nr);
        }else{
            return Integer.toString((int) nr);
        }
    }

    public String formatSeconds(double ms){
        double hours = Math.floor(ms/3600000);
        ms = ms-hours*36000;
        double minutes = Math.floor(ms/60000);
        double seconds = Math.floor(ms-minutes*600000)/1000;
        return leadingZeroify(hours)+":"+leadingZeroify(minutes)+":"+leadingZeroify(seconds);
    }


    public int getCurrentOffset(){
        try{
            return mediaPlayer.getCurrentPosition();
        }catch (Exception e){
            return 0;
        }
    }



    public void startUpdater(){
        handler.postDelayed(offsetUpdater,0);
    }

    public void onButtonPress(String key){

        State state = App.getInstance().getStore().getState();

        ApiMiddleware.getApi().request(
                Endpoints.ScenarioSessionsStoreButtonPress(
                        ImmutableScenarioSessionsButtonPress
                                .builder()
                                .key(key)
                                .session(state.playback().scenarioSessionId())
                                .offset(getCurrentOffset())
                                .trackId(getCurrentTrack().id())
                                .build()
                        ,
                        new Endpoints.ScenarioSessionsStoreButtonPressCallback() {
                            @Override
                            public void result(Endpoints.ScenarioSessionsEmptyResult result) {

                            }

                            @Override
                            public void err(String errorCode, String description) {

                            }
                        }

                )
        );

        Log.d("PlaybackActivity.java","Button pressed: "+getCurrentTrack().id()+" - "+key);

    }

    public void playNext(){
        if(getCurrentTrack() != null){

            String baseUrl = new BaseUrlProvider(this).getBaseUrl();
            try {
                Log.d("URI","http://" + baseUrl + "/tracks/file/" + getCurrentTrack().id());
                setupMediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(this, Uri.parse("http://" + baseUrl + "/tracks/file/" + getCurrentTrack().id()));
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mediaPlayer.start();
                                startUpdater();
                                mediaPlayer.seekTo((int) getCurrentTrackResult().offset());
                                buttonAdapter.updateButtons(getCurrentTrack().buttons());

                            }
                        });


                    }
                });


                App.getInstance().getStore().dispatch(new Action("PLAYBACK_PLAYING",null));
            }catch (IOException e){
                throw new RuntimeException(e.getCause());
            }

        }
    }


    public Endpoints.ScenarioSessionsResultTrack getCurrentTrackResult(){
        State state =  App.getInstance().getStore().getState();

        String id = state.playback().scenarioSessionId();

        Endpoints.ScenarioSessionsScenarioSession session = state.scenarioSessions().items().get(id);

        if(session != null){

            for(Endpoints.ScenarioSessionsResultTrack resTrack: session.tracks()){

                if(!resTrack.played()){
                    String url = new BaseUrlProvider(PlaybackActivity.this).getBaseUrl();
                    Endpoints.TracksTrack track = state.tracks().items().get(resTrack.trackId());
                    if(track != null){
                        return resTrack;
                    }else{
                        return null;
                    }

                }

            }

        }

        return null;
    }

    public Endpoints.TracksTrack getCurrentTrack(){
        State state =  App.getInstance().getStore().getState();

        String id = state.playback().scenarioSessionId();

        Endpoints.ScenarioSessionsScenarioSession session = state.scenarioSessions().items().get(id);

        if(session != null){

            for(Endpoints.ScenarioSessionsResultTrack resTrack: session.tracks()){

                if(!resTrack.played()){
                    String url = new BaseUrlProvider(PlaybackActivity.this).getBaseUrl();
                    Endpoints.TracksTrack track = state.tracks().items().get(resTrack.trackId());
                    if(track != null){
                        return track;
                    }else{
                        return null;
                    }

                }

            }

        }

        return null;
    }

    @Override
    public void onEvent(Action action, State state) {
        switch (action.getType()){
            case "PLAYBACK_PLAY":

                playNext();


                break;

            case "PLAYBACK_STOP":

                buttonAdapter.updateButtons(new ArrayList<>());

                handler.removeCallbacks(offsetUpdater);

                try{
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    protected void onPause() {

        App.getInstance().getStore().dispatch(
                new Action("SCENARIO_SESSIONS_STOP_STREAM",null)
        );
        App.getInstance().getStore().dispatch(new Action("PLAYBACK_STOP",null));

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        App.getInstance().getStore().dispatch(
                new Action(
                        "SCENARIO_SESSIONS_STREAM_SINGLE",
                        App.getInstance().getStore().getState().playback().scenarioSessionId()
                )
        );

        App.getInstance().getStore().dispatch(new Action("PLAYBACK_CHECK_TRACKS",null));
    }
}
