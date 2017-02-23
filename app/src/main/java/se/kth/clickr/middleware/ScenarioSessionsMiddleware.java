package se.kth.clickr.middleware;

import android.util.Pair;

import se.kth.clickr.Action;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Endpoints;
import se.kth.clickr.flood.ImmutableScenarioSessionsListSessionsParams;
import se.kth.clickr.flood.ImmutableScenarioSessionsSaveOffsetParams;
import se.kth.clickr.flood.ImmutableScenarioSessionsStreamSingleParams;
import se.kth.clickr.flood.Request;

public class ScenarioSessionsMiddleware implements Store.Middleware<Action,State>{

    private Request req;

    @Override
    public void dispatch(Store<Action, State> store, Action action, Store.NextDispatcher<Action> nextMiddleware) {

        switch (action.getType()){

            case "SCENARIO_SESSIONS_REQUEST_LOAD":

                Endpoints.ScenarioSessionsListSessionsParams params = ImmutableScenarioSessionsListSessionsParams.builder().build();

                ApiMiddleware.getApi().request(Endpoints.ScenarioSessionsListSessions(params, new Endpoints.ScenarioSessionsListSessionsCallback() {
                    @Override
                    public void result(Endpoints.ScenarioSessionsListSessionsResult result) {
                        store.dispatch(new Action("SCENARIO_SESSIONS_LOAD_FULFILLED",result.items()));
                    }

                    @Override
                    public void err(String errorCode, String description) {
                        store.dispatch(new Action("SCENARIO_SESSIONS_LOAD_REJECTED",errorCode));
                    }
                }));

                break;

            case "SCENARIO_SESSIONS_STREAM_SINGLE":


                Endpoints.ScenarioSessionsStreamSingleParams streamSingleParams = ImmutableScenarioSessionsStreamSingleParams.builder().id((String) action.getPayload()).build();


                 req = Endpoints.ScenarioSessionsStream(streamSingleParams, new Endpoints.ScenarioSessionsStreamCallback() {
                    @Override
                    public void result(Endpoints.ScenarioSessionsScenarioSession result) {
                        store.dispatch(new Action("SCENARIO_SESSIONS_LOADED_SESSION",result));
                        for(Endpoints.ScenarioSessionsResultTrack track: result.tracks()){
                            if(store.getState().tracks().items().get(track.trackId()) == null){
                                store.dispatch(new Action("TRACKS_LOAD_SINGLE",track.trackId()));
                            }
                        }
                        store.dispatch(new Action("PLAYBACK_CHECK_TRACKS",null));
                    }

                    @Override
                    public void err(String errorCode, String description) {
                        store.dispatch(new Action("SCENARIO_SESSIONS_LOADING_FAILED",errorCode));
                    }
                });


                ApiMiddleware.getApi().request(req);


                break;

            case "SCENARIO_SESSIONS_STOP_STREAM":


                ApiMiddleware.getApi().cancelRequest(req);

                break;


            case "SCENARIO_SESSIONS_SAVE_OFFSET":

                Endpoints.ScenarioSessionsSaveOffsetParams payload = (Endpoints.ScenarioSessionsSaveOffsetParams) action.getPayload();





                ApiMiddleware.getApi().request(Endpoints.ScenarioSessionsSaveOffset(payload,
                        new Endpoints.ScenarioSessionsSaveOffsetCallback() {
                            @Override
                            public void result(Endpoints.ScenarioSessionsNoResult result) {

                            }

                            @Override
                            public void err(String errorCode, String description) {

                            }
                        }
                ));

                break;

        }

        nextMiddleware.next(action);

    }
}
