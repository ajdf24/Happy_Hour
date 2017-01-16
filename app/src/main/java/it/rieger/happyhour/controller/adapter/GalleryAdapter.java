package it.rieger.happyhour.controller.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import it.rieger.happyhour.R;
import it.rieger.happyhour.model.Image;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.ThumbnailHolderClass;
import it.rieger.happyhour.util.listener.OnItemTouchListener;
import it.rieger.happyhour.view.viewholder.ThumbnailViewHolder;

/**
 * Created by Admin on 08.07.2016.
 */
public class GalleryAdapter extends RecyclerView.Adapter<ThumbnailViewHolder> {

    private final String LOG_TAG = getClass().getSimpleName();

    private List<String> images;
    private Context context;

    Location location;

    public GalleryAdapter(Context context, Location location) {
        this.context = context;
        this.location = location;
        this.images = location.getImageKeyList();
    }

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail, parent, false);

        return new ThumbnailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
        String image = images.get(position);

        ThumbnailHolderClass thumbnailHolderClass = new ThumbnailHolderClass(holder, image);

        new DownloadImage().execute(thumbnailHolderClass);

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener extends OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(final Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)) {
                clickListener.onClick(child, recyclerView.getChildAdapterPosition(child));
            }
            return false;
        }

    }

    private class DownloadImage extends AsyncTask<ThumbnailHolderClass, Integer, ThumbnailViewHolder>{

        @Override
        protected ThumbnailViewHolder doInBackground(final ThumbnailHolderClass... params) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference images = database.getReference("images");

            images.orderByKey().equalTo(params[0].getImageKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Image image = dataSnapshot1.getValue(Image.class);
                        params[0].getThumbnailViewHolder().progressBar.setVisibility(View.INVISIBLE);
                        params[0].getThumbnailViewHolder().thumbnail.setImageBitmap(image.getImage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return null;
        }
    }
}
