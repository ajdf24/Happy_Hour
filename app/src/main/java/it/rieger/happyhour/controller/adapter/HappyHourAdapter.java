package it.rieger.happyhour.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.view.viewholder.HappyHourViewHolder;

/**
 * Created by sebastian on 05.07.16.
 */
public class HappyHourAdapter extends RecyclerView.Adapter<HappyHourViewHolder> {

    private List<HappyHour> happyHours;

    public HappyHourAdapter(List<HappyHour> happyHours) {
        this.happyHours = happyHours;
    }

    @Override
    public HappyHourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(HappyHourViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
