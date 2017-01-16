package it.rieger.happyhour.model;

import it.rieger.happyhour.view.viewholder.ThumbnailViewHolder;

/**
 * Created by sebastian on 16.01.17.
 */

public class ThumbnailHolderClass {

    private ThumbnailViewHolder thumbnailViewHolder = null;

    private String imageKey = null;

    public ThumbnailHolderClass(ThumbnailViewHolder thumbnailViewHolder, String imageKey) {
        this.thumbnailViewHolder = thumbnailViewHolder;
        this.imageKey = imageKey;
    }

    public ThumbnailViewHolder getThumbnailViewHolder() {
        return thumbnailViewHolder;
    }

    public void setThumbnailViewHolder(ThumbnailViewHolder thumbnailViewHolder) {
        this.thumbnailViewHolder = thumbnailViewHolder;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }
}
