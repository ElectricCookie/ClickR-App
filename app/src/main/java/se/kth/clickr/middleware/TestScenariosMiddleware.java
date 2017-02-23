package se.kth.clickr.middleware;

import android.util.Pair;
import android.widget.Toast;

import se.kth.clickr.Action;
import se.kth.clickr.Home;
import se.kth.clickr.R;
import se.kth.clickr.State;
import se.kth.clickr.Store;
import se.kth.clickr.flood.Endpoints;
import se.kth.clickr.flood.ImmutableScenarioSessionsGenerateSessionParams;
import se.kth.clickr.flood.ImmutableTestScenariosGetOpenScenariosParams;
import se.kth.clickr.flood.ImmutableTestScenariosIdContainer;

/**
 * Created by ElectricCookie on 26.08.2016.
 */
public class TestScenariosMiddleware  implements Store.Middleware<Action,State>{

    @Override
    public void dispatch(Store<Action, State> store, Action action, Store.NextDispatcher<Action> nextDispatcher) {

        switch (action.getType()){


            case "TEST_SCENARIOS_REQUEST_LOAD":

                Endpoints.TestScenariosGetOpenScenariosParams params = ImmutableTestScenariosGetOpenScenariosParams.builder().build();

                ApiMiddleware.getApi().request(Endpoints.TestScenariosGetOpenScenarios(params, new Endpoints.TestScenariosGetOpenScenariosCallback() {
                    @Override
                    public void result(Endpoints.TestScenariosGetOpenScenariosResult result) {
                        store.dispatch(new Action("TEST_SCENARIOS_LOAD_FULFILLED",result.openScenarios()));
                    }

                    @Override
                    public void err(String errorCode, String description) {
                        store.dispatch(new Action("TEST_SCENARIOS_LOAD_REJECTED",errorCode));
                    }
                }));


                break;

            case "TEST_SCENARIO_LOAD":

                Endpoints.TestScenariosIdContainer container = ImmutableTestScenariosIdContainer.builder().id((String) action.getPayload()).build();

                ApiMiddleware.getApi().request(Endpoints.TestScenariosGet(container, new Endpoints.TestScenariosGetCallback() {
                    @Override
                    public void result(Endpoints.TestScenariosTestScenario result) {
                        store.dispatch(new Action("TEST_SCENARIOS_LOAD_SINGLE_FULFILLED",result));
                    }

                    @Override
                    public void err(String errorCode, String description) {
                        store.dispatch(new Action("TEST_SCENARIOS_LOAD_SINGLE_REJECTED",errorCode));
                    }
                }));


                break;


            case "TEST_SCENARIOS_JOIN":


                Home activity = (Home) action.getPayload();

                Endpoints.ScenarioSessionsGenerateSessionParams generateSessionParams =
                        ImmutableScenarioSessionsGenerateSessionParams
                                .builder().scenarioId(store.getState().testScenarios().joinId()
                        ).build();


                ApiMiddleware.getApi().request(Endpoints.ScenarioSessionsGenerateSession(generateSessionParams, new Endpoints.ScenarioSessionsGenerateSessionCallback() {
                    @Override
                    public void result(Endpoints.ScenarioSessionsGenerateSessionResult result) {

                        store.dispatch(new Action("TEST_SCENARIOS_HIDE_DIALOG",null));


                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                store.dispatch(new Action("PLAYBACK_SHOW",result.id()));
                                Toast.makeText(activity, R.string.test_scenarios_joined_scenario,Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void err(String errorCode, String description) {

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, R.string.test_scenarios_failed_joined_scenario,Toast.LENGTH_SHORT).show();
                            }
                        });

                        store.dispatch(new Action("TEST_SCENARIOS_HIDE_DIALOG",null));


                    }
                }));


                break;

        }


        nextDispatcher.next(action);
    }
}
