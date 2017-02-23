package se.kth.clickr;

import android.app.Activity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se.kth.clickr.flood.Endpoints;

/**
 * Created by ElectricCookie on 30.08.2016.
 */
public class ScenarioSessionsAdapter extends RecyclerView.Adapter<ScenarioSessionsAdapter.ViewHolder> {


    private Activity activity;
    private List<Endpoints.ScenarioSessionsScenarioSession> sessions = new ArrayList<>();

    ScenarioSessionsAdapter(Activity activity){
        this.activity = activity;
    }

    public void updateValues(Map<String,Endpoints.ScenarioSessionsScenarioSession> updatedValues){

        List<Endpoints.ScenarioSessionsScenarioSession> items = new ArrayList<>(updatedValues.values());

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                new ScenarioSessionDiffer(sessions,items)
        );

        sessions = items;

        result.dispatchUpdatesTo(ScenarioSessionsAdapter.this);


    }


    private class ScenarioSessionDiffer extends DiffUtil.Callback{


        private List<Endpoints.ScenarioSessionsScenarioSession> oldItems;
        private List<Endpoints.ScenarioSessionsScenarioSession> newItems;

        public ScenarioSessionDiffer(List<Endpoints.ScenarioSessionsScenarioSession> oldItems,List<Endpoints.ScenarioSessionsScenarioSession> newItems){
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

            return false;

        }
    }



    public ScenarioSessionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_scenario_session,parent,false);


        ScenarioSessionsAdapter.ViewHolder vh = new ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(ScenarioSessionsAdapter.ViewHolder holder, int position) {

        Endpoints.ScenarioSessionsScenarioSession session = sessions.get(position);


        holder.title.setText(session.title());
        holder.description.setText(session.description());

        int total = session.tracks().size();
        boolean found = false;
        int done = 0;
        for(Endpoints.ScenarioSessionsResultTrack resultTrack: session.tracks()){
            if(resultTrack.played()){
                done++;
            }
        }



        holder.progress.setProgress(done/total+1);
        holder.progress.setIndeterminate(false);



        holder.itemView.setOnClickListener((v) -> {
            App.getInstance().getStore().dispatch(new Action("PLAYBACK_SHOW",session.id()));
        });

    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView description;
        private ProgressBar progress;

        public ViewHolder(CardView view){
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            progress = (ProgressBar) view.findViewById(R.id.progressBar);

        }



    }

}
