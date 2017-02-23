package se.kth.clickr.reducers;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.kth.clickr.Action;
import se.kth.clickr.ImmutableState;
import se.kth.clickr.ImmutableTestScenarios;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Endpoints;

/**
 * Created by ElectricCookie on 26.08.2016.
 */
public class TestScenarioReducer implements Store.Reducer<Action,State> {


    @Override
    public State reduce(Action action, State state) {

        State.TestScenarios testScenarios = state.testScenarios();

        switch (action.getType()){


            case "TEST_SCENARIOS_LOAD_FULFILLED":

                HashMap<String,Endpoints.TestScenariosTestScenario> res = new HashMap<>();

                List<Endpoints.TestScenariosTestScenario> items = (List<Endpoints.TestScenariosTestScenario>) action.getPayload();

                for(Endpoints.TestScenariosTestScenario item: items){
                    res.put(item.id(),item);
                }


                testScenarios =  ImmutableTestScenarios.builder()
                        .from(testScenarios)
                        .items(res)
                        .state(State.GENERIC_STATE.FULFILLED)
                        .build();

                break;

            case "TEST_SCENARIOS_LOAD_SINGLE_FULFILLED":


                Endpoints.TestScenariosTestScenario scenario = (Endpoints.TestScenariosTestScenario) action.getPayload();




                testScenarios = ImmutableTestScenarios.builder().from(testScenarios).putItems(scenario.id(),scenario).build();





                break;

            case "TEST_SCENARIOS_LOAD_REJECTED":

                testScenarios = ImmutableTestScenarios.builder().from(testScenarios).state(State.GENERIC_STATE.REJECTED).build();

                break;

            case "TEST_SCENARIOS_REQUEST_LOAD":

                testScenarios = ImmutableTestScenarios.builder().from(testScenarios).state(State.GENERIC_STATE.LOADING).build();

                break;


            case "TEST_SCENARIOS_HIDE_DIALOG":

                testScenarios = ImmutableTestScenarios.builder().from(testScenarios).showJoinDialog(false).build();

                break;

            case "TEST_SCENARIOS_SHOW_DIALOG":


                testScenarios = ImmutableTestScenarios.builder()
                        .from(testScenarios)
                        .joinId((String) action.getPayload())
                        .showJoinDialog(true)
                        .build();


                break;


            default:
                return state;

        }




        return ImmutableState.builder().from(state).testScenarios(testScenarios).build();
    }
}
