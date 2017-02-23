package se.kth.clickr.middleware;

import se.kth.clickr.Action;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Endpoints;
import se.kth.clickr.flood.ImmutableTracksIdContainer;

/**
 * Created by ElectricCookie on 30.08.2016.
 */
public class TracksMiddleware implements Store.Middleware<Action,State> {





    @Override
    public void dispatch(Store<Action, State> store, Action action, Store.NextDispatcher<Action> nextMiddleware) {

        switch (action.getType()){

            case "TRACKS_LOAD_SINGLE":


                Endpoints.TracksIdContainer params = ImmutableTracksIdContainer.builder().id((String) action.getPayload()).build();

                ApiMiddleware.getApi().request(Endpoints.TracksGet(params, new Endpoints.TracksGetCallback() {
                    @Override
                    public void result(Endpoints.TracksTrack result) {
                        store.dispatch(new Action("TRACKS_LOAD_SINGLE_FULFILLED",result));

                        store.dispatch(new Action("PLAYBACK_CHECK_TRACKS",result));
                    }

                    @Override
                    public void err(String errorCode, String description) {
                        store.dispatch(new Action("TRACKS_LOAD_SINGLE_REJECTED",errorCode));
                    }
                }));

                break;


        }


        nextMiddleware.next(action);
    }

}
