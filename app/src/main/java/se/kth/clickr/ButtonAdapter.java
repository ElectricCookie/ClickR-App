package se.kth.clickr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import se.kth.clickr.flood.Endpoints;

/**
 * Created by ElectricCookie on 31.08.2016.
 */
public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ViewHolder> {

    PlaybackActivity activity;

    private List<Endpoints.TracksButton> buttons = new ArrayList<>();

    ButtonAdapter(PlaybackActivity activity){
        this.activity = activity;
    }

    public void updateButtons(List<Endpoints.TracksButton> buttons){
        this.buttons = buttons;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.button.setText(buttons.get(position).label());

        holder.button.setOnClickListener((v) -> {
            activity.onButtonPress(buttons.get(position).key());
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_button,parent,false));

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private Button button;

        ViewHolder(View v){
            super(v);

            button = (Button) v.findViewById(R.id.button);

        }

    }

}
