package se.kth.clickr.reducers;

import android.util.Log;
import android.util.Pair;

import java.io.File;

import se.kth.clickr.Action;
import se.kth.clickr.Home;
import se.kth.clickr.ImmutablePlayback;
import se.kth.clickr.ImmutableState;
import se.kth.clickr.State;
import se.kth.clickr.Store;

/**
 * Created by ElectricCookie on 30.08.2016.
 */
public class PlaybackReducer implements Store.Reducer<Action,State>{




    @Override
    public State reduce(Action action, State state) {


        State.Playback playback  = state.playback();

        switch (action.getType()){

            case "PLAYBACK_SHOW":

                playback = ImmutablePlayback.builder().from(playback).scenarioSessionId((String) action.getPayload()).build();

                break;

            case "PLAYBACK_TRACKS_READY":

                playback = ImmutablePlayback.builder().from(playback).state(State.PLAYBACK_STATE.STOPPPED).build();

                break;

            case "PLAYBACK_PLAY":

                playback = ImmutablePlayback.builder().from(playback).currentTrackId((String) action.getPayload()).state(State.PLAYBACK_STATE.PLAYING).build();

                break;

            case "PLAYBACK_STOP":

                playback = ImmutablePlayback.builder().from(playback).state(State.PLAYBACK_STATE.STOPPPED).build();

                break;

            case "PLAYBACK_SET_OFFSET":

                playback = ImmutablePlayback.builder()
                        .from(playback)
                        .offset((int) action.getPayload())
                        .build();

                break;

            case "PLAYBACK_FINISHED":

                playback = ImmutablePlayback.builder()
                        .from(playback)
                        .state(State.PLAYBACK_STATE.STOPPPED)
                        .build();

                break;







            default:
                return state;
        }



        return ImmutableState.builder().from(state).playback(playback).build();
    }
}
