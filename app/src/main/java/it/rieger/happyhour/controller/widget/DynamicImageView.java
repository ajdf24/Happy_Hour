package it.rieger.happyhour.controller.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by sebastian on 07.04.16.
 * Image view, which can scale a image to a special size.
 * The view shows the image in the size of the view.
 * <b>Note:</b> When a image is upscaling the quality then the quality is poor.
 */
public class DynamicImageView extends ImageView {

    private final String LOG_TAG = getClass().getSimpleName();

    /**
     * {@inheritDoc}
     * call the super constructor
     */
    public DynamicImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@inheritDoc}
     * Scale the image to the correct size
     */
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final Drawable d = this.getDrawable();

        if (d != null) {
            final int width = MeasureSpec.getSize(widthMeasureSpec);
            final int height = (int) Math.ceil(width * (float) d.getIntrinsicHeight() / d.getIntrinsicWidth());
            this.setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
