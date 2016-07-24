package it.rieger.happyhour.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;

/**
 * Created by sebastian on 05.07.16.
 */
public class HappyHourTimeViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.list_item_happy_hour_time_day)
    Spinner daySpinner;

    @Bind(R.id.list_item_happy_hour_time_time)
    EditText time;

    public HappyHourTimeViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public Spinner getDaySpinner() {
        return daySpinner;
    }

    public EditText getTime() {
        return time;
    }
}