package it.rieger.happyhour.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import it.rieger.happyhour.R;

/**
 * Created by Admin on 08.07.2016.
 */
public class ThumbnailViewHolder extends RecyclerView.ViewHolder {
    public ImageView thumbnail;

    public ThumbnailViewHolder(View view) {
        super(view);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
    }
}
