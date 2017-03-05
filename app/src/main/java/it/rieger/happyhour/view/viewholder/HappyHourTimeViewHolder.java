package it.rieger.happyhour.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;

/**
 * View holder for happy hour time
 * Created by sebastian on 05.07.16.
 */
public class HappyHourTimeViewHolder extends RecyclerView.ViewHolder {

    private final String LOG_TAG = getClass().getSimpleName();

    @Bind(R.id.list_item_happy_hour_spinner_day)
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
