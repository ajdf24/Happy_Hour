package it.rieger.happyhour.controller.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.rieger.happyhour.R;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.view.viewholder.HappyHourViewHolder;

/**
 * Created by sebastian on 05.07.16.
 */
public class HappyHourAdapter extends RecyclerView.Adapter<HappyHourViewHolder> {

    private List<HappyHour> happyHours;

    private View view;

    private HappyHour lastDeletedHappyHour;

    private int lastDeletedPosition;

    public HappyHourAdapter(List<HappyHour> happyHours, View view) {
        this.happyHours = happyHours;
        this.view = view;
    }

    @Override
    public HappyHourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_happy_hour, parent, false);
        return new HappyHourViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HappyHourViewHolder holder, int position) {
        HappyHour happyHour = happyHours.get(position);

        holder.getDrink().setText(happyHour.getDrink());
        holder.getPrice().setText(happyHour.getPrice());
    }

    @Override
    public int getItemCount() {
        return happyHours.size();
    }

    public void removeItem(final int position){

        lastDeletedHappyHour = happyHours.get(position);
        happyHours.remove(position);
        notifyDataSetChanged();

        Snackbar snackbar = Snackbar.make(view, "Aktion rückgängig machen?", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happyHours.add(position, lastDeletedHappyHour);
                notifyDataSetChanged();
            }
        });
        snackbar.show();
    }
}
