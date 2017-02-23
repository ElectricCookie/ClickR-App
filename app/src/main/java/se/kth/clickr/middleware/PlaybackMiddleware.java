package se.kth.clickr.middleware;

import android.util.Log;
import android.util.Pair;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import se.kth.clickr.Action;
import se.kth.clickr.App;
import se.kth.clickr.Home;
import se.kth.clickr.ImmutableState;
import se.kth.clickr.ImmutableTestScenarios;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Endpoints;
import se.kth.clickr.flood.ImmutableScenarioSessionsSaveOffsetParams;

/**
 * Created by ElectricCookie on 30.08.2016.
 */
public class PlaybackMiddleware implements Store.Middleware<Action,State> {



    @Override
    public void dispatch(Store<Action, State> store, Action action, Store.NextDispatcher<Action> nextMiddleware) {

        State state = store.getState();
        State.Playback playback = store.getState().playback();




        switch (action.getType()){


            case "PLAYBACK_CHECK_TRACKS":


                Endpoints.ScenarioSessionsScenarioSession session = state.scenarioSessions().items().get(state.playback().scenarioSessionId());

                if(session != null){
                    boolean loaded =true;
                    for(Endpoints.ScenarioSessionsResultTrack track: session.tracks()){
                        if(state.tracks().items().get(track.trackId()) == null){
                            loaded =false;

                            break;
                        }
                    }
                    if(loaded){
                        store.dispatch(new Action("PLAYBACK_TRACKS_READY",null));
                    }

                }

                break;

            case "PLAYBACK_STOP":


                if(state.playback().currentTrackId() != null){

                    store.dispatch(
                            new Action("SCENARIO_SESSIONS_SAVE_OFFSET",
                                    ImmutableScenarioSessionsSaveOffsetParams
                                            .builder()
                                            .id(state.playback().scenarioSessionId())
                                            .trackId(state.playback().currentTrackId())
                                            .offset(state.playback().offset())
                                            .build()
                            )
                    );

                }

                break;

            case "PLAYBACK_FINISHED":

                if(state.playback().currentTrackId() != null){

                    store.dispatch(
                            new Action("SCENARIO_SESSIONS_SAVE_OFFSET",
                                    ImmutableScenarioSessionsSaveOffsetParams
                                            .builder()
                                            .id(state.playback().scenarioSessionId())
                                            .trackId(state.playback().currentTrackId())
                                            .offset(-1)
                                            .build()
                            )
                    );

                }





                break;


        }


        nextMiddleware.next(action);
    }
}
