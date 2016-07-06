package it.rieger.happyhour.view.viewholder;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;

/**
 * Created by sebastian on 05.07.16.
 */
public class HappyHourViewHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.list_item_happy_hour_button_save)
    FloatingActionButton fab;

    @Bind(R.id.list_item_happy_hour_drink)
    EditText drink;

    @Bind(R.id.list_item_happy_hour_price)
    EditText price;

    public HappyHourViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public EditText getDrink() {
        return drink;
    }

    public EditText getPrice() {
        return price;
    }
}
