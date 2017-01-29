package it.rieger.happyhour.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Created by sebastian on 29.01.17.
 */

public class BitmapSlider extends TextSliderView {

    private Bitmap mBitmap;
    private String mUrl;
    private File mFile;
    private int mRes;

    protected OnSliderClickListener mOnSliderClickListener;

    private boolean mErrorDisappear;

    private ImageLoadListener mLoadListener;

    private String mDescription;

    private ScaleType mScaleType = ScaleType.Fit;

    public BitmapSlider(Context context) {
        super(context);
    }

    public BaseSliderView image(Bitmap bitmap) {
        if (mUrl != null || mFile != null || mBitmap != null) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mBitmap = bitmap;
        return this;
    }

    public void setOnImageLoadListener(ImageLoadListener l){
        mLoadListener = l;
    }

    protected void bindEventAndShow(final View v, ImageView targetImageView) {
        final BaseSliderView me = this;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSliderClickListener != null) {
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });

        mLoadListener.onStart(me);

        Picasso p = Picasso.with(mContext);
        RequestCreator rq = null;
        if (mUrl != null) {
            rq = p.load(mUrl);
        } else if (mFile != null) {
            rq = p.load(mFile);
        } else if (mBitmap != null) {
            targetImageView.setImageBitmap(mBitmap);
            if (v.findViewById(com.daimajia.slider.library.R.id.loading_bar) != null) {
                v.findViewById(com.daimajia.slider.library.R.id.loading_bar).setVisibility(View.INVISIBLE);
            }
        } else if (mRes != 0) {
            rq = p.load(mRes);
        } else {
            return;
        }

        if (rq == null) {
            return;
        }

        if (getEmpty() != 0) {
            rq.placeholder(getEmpty());
        }

        if (getError() != 0) {
            rq.error(getError());
        }

        switch (mScaleType) {
            case Fit:
                rq.fit();
                break;
            case CenterCrop:
                rq.fit().centerCrop();
                break;
            case CenterInside:
                rq.fit().centerInside();
                break;
        }

        rq.into(targetImageView, new Callback() {
            @Override
            public void onSuccess() {
                if (v.findViewById(com.daimajia.slider.library.R.id.loading_bar) != null) {
                    v.findViewById(com.daimajia.slider.library.R.id.loading_bar).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError() {
                if(mLoadListener != null){
                    mLoadListener.onEnd(false,me);
                }
            }
        });
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(com.daimajia.slider.library.R.layout.render_type_text,null);
        ImageView target = (ImageView)v.findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);
        TextView description = (TextView)v.findViewById(com.daimajia.slider.library.R.id.description);
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}

