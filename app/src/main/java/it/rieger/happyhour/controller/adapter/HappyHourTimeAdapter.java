package it.rieger.happyhour.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import it.rieger.happyhour.model.HappyHourTime;
import it.rieger.happyhour.view.viewholder.HappyHourTimeViewHolder;

/**
 * Created by sebastian on 05.07.16.
 */
public class HappyHourTimeAdapter extends RecyclerView.Adapter<HappyHourTimeViewHolder> {

    private List<HappyHourTime> happyHourTimes;

    public HappyHourTimeAdapter(List<HappyHourTime> happyHourTimes) {
        this.happyHourTimes = happyHourTimes;
    }

    @Override
    public HappyHourTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(HappyHourTimeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
