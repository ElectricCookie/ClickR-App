package se.kth.clickr;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trikita.anvil.DSL;
import trikita.anvil.RenderableView;
import trikita.anvil.recyclerview.Recycler;
import trikita.anvil.support.v4.Supportv4DSL;


public class TestScenariosView  extends Fragment{


    private TestScenariosAdapter adapter;

    private Store.Subscriber<State> subscriber;


    public TestScenariosView(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new TestScenariosAdapter(getActivity());

        subscriber = new Store.Subscriber<State>() {
            @Override
            public boolean shouldUpdate(State oldState, State newState) {
                return oldState.testScenarios() != newState.testScenarios();
            }

            @Override
            public void update(State newState) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateValues(newState.testScenarios().items());
                    }
                });
            }
        };

    }


    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().getStore().subscribe(subscriber);
        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().getStore().unsubscribe(subscriber);
    }

    private void refresh(){
        App.getInstance().getStore().dispatch(new Action("TEST_SCENARIOS_REQUEST_LOAD",null));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return new RenderableView(getActivity()) {
            @Override
            public void view() {
                State state = App.getInstance().getStore().getState();
                Supportv4DSL.swipeRefreshLayout(() -> {
                    Supportv4DSL.refreshing(state.testScenarios().state() == State.GENERIC_STATE.LOADING);
                    Supportv4DSL.onRefresh(TestScenariosView.this::refresh);

                    if(state.testScenarios().items().size() == 0){
                        DSL.textView(() -> {
                            DSL.padding(DSL.dip(15));
                            DSL.text(R.string.test_scenarios_no_scnearios);
                            DSL.gravity(DSL.CENTER);
                        });
                    }


                    Recycler.view(() -> {
                        DSL.size(DSL.MATCH,DSL.MATCH);
                        Recycler.layoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        Recycler.hasFixedSize(false);
                        Recycler.adapter(adapter);
                    });
                });
            }
        };
    }

}
