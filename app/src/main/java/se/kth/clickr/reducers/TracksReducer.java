package se.kth.clickr.reducers;

import se.kth.clickr.Action;
import se.kth.clickr.ImmutableState;
import se.kth.clickr.ImmutableTracks;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Endpoints;

/**
 * Created by ElectricCookie on 30.08.2016.
 */
public class TracksReducer implements Store.Reducer<Action,State>{


    @Override
    public State reduce(Action action, State state) {


        State.Tracks tracks = state.tracks();

        switch (action.getType()){


            case "TRACKS_LOAD_SINGLE_FULFILLED":

                Endpoints.TracksTrack track = (Endpoints.TracksTrack) action.getPayload();



                tracks = ImmutableTracks.builder().from(tracks).putItems(track.id(),track).build();

                break;



            default:

                return state;
        }


        return ImmutableState.builder().from(state).tracks(tracks).build();

    }
}
