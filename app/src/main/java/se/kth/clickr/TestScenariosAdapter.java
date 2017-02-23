package se.kth.clickr;

import android.app.Activity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se.kth.clickr.flood.Endpoints;

/**
 * Created by ElectricCookie on 29.08.2016.
 */



public class TestScenariosAdapter extends RecyclerView.Adapter<TestScenariosAdapter.ViewHolder>{

    private List<Endpoints.TestScenariosTestScenario> scenarios = new ArrayList<>();

    TestScenariosAdapter(Activity a){

    }


    public void updateValues(Map<String,Endpoints.TestScenariosTestScenario> updated){


        List<Endpoints.TestScenariosTestScenario> newScenarios = new ArrayList<>(updated.values());


        DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                new TestScenarioDiffer(scenarios,newScenarios)
        );

        scenarios = newScenarios;

        result.dispatchUpdatesTo(TestScenariosAdapter.this);
    }

    private class TestScenarioDiffer extends DiffUtil.Callback{


        private List<Endpoints.TestScenariosTestScenario> oldItems;
        private List<Endpoints.TestScenariosTestScenario> newItems;

        public TestScenarioDiffer(List<Endpoints.TestScenariosTestScenario> oldItems,List<Endpoints.TestScenariosTestScenario> newItems){
            this.oldItems = oldItems;
            this.newItems = newItems;
        }


        @Override
        public int getOldListSize() {
            return oldItems != null ? oldItems.size() : 0;
        }


        @Override
        public int getNewListSize() {
            return newItems != null ? newItems.size() : 0;
        }


        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldItems.get(oldItemPosition).id().equals(newItems.get(newItemPosition).id());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Endpoints.TestScenariosTestScenario oldScenario = oldItems.get(oldItemPosition);
            Endpoints.TestScenariosTestScenario newScenario = newItems.get(newItemPosition);

            boolean equal = (
                    oldScenario.title().equals(newScenario.title())
                    &&
                    oldScenario.description().equals(newScenario.description())
                    &&
                    oldScenario.tracks().equals(newScenario.tracks())
            );

            return equal;

        }
    }



    public TestScenariosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_test_scenario,parent,false);


        TestScenariosAdapter.ViewHolder vh = new ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(TestScenariosAdapter.ViewHolder holder, int position) {

        final Endpoints.TestScenariosTestScenario scenario = scenarios.get(position);

        holder.title.setText(scenario.title());
        holder.description.setText(scenario.description());


        holder.view.setOnClickListener(null);

        holder.view.setOnClickListener((v) -> {
            App.getInstance().getStore().dispatch(new Action("TEST_SCENARIOS_SHOW_DIALOG",scenario.id()));
        });

    }

    @Override
    public int getItemCount() {
        return scenarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView description;
        private CardView view;

        public ViewHolder(CardView view){
            super(view);

            this.view = view;

            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);

        }



    }

}