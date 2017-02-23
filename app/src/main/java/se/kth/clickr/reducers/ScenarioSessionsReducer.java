package se.kth.clickr.reducers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.kth.clickr.Action;
import se.kth.clickr.ImmutableScenarioSessions;
import se.kth.clickr.ImmutableState;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Endpoints;

/**
 * Created by ElectricCookie on 29.08.2016.
 */
public class ScenarioSessionsReducer implements Store.Reducer<Action,State>{


    @Override
    public State reduce(Action action, State state) {

        State.ScenarioSessions scenarioSessions = state.scenarioSessions();

        switch (action.getType()){

            case "SCENARIO_SESSIONS_REQUEST_LOAD":


                scenarioSessions = ImmutableScenarioSessions.builder().from(scenarioSessions).state(State.GENERIC_STATE.LOADING).build();

                break;

            case "SCENARIO_SESSIONS_LOAD_FULFILLED":


                List<Endpoints.ScenarioSessionsScenarioSession> items = (List<Endpoints.ScenarioSessionsScenarioSession>) action.getPayload();

                Map<String,Endpoints.ScenarioSessionsScenarioSession> sessions = new HashMap<>();

                for(Endpoints.ScenarioSessionsScenarioSession item: items){
                    sessions.put(item.id(),item);
                }


                scenarioSessions = ImmutableScenarioSessions
                        .builder()
                        .from(scenarioSessions)
                        .state(State.GENERIC_STATE.FULFILLED)
                        .putAllItems(sessions)
                        .build();

                break;

            case "SCENARIO_SESSIONS_LOADED_SESSION":

                Endpoints.ScenarioSessionsScenarioSession session = (Endpoints.ScenarioSessionsScenarioSession) action.getPayload();

                scenarioSessions = ImmutableScenarioSessions
                        .builder()
                        .from(scenarioSessions)
                        .putItems(session.id(),session)
                        .build();

                break;

        }


        return ImmutableState.builder().from(state).scenarioSessions(scenarioSessions).build();
    }
}
