package it.rieger.happyhour.view.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.listener.OnPageChangeListener;
import it.rieger.happyhour.util.standard.CreateContextForResource;

public class SlideshowDialogFragment extends DialogFragment{

    private final String LOG_TAG = getClass().getSimpleName();

    private static List<String> imageList;
    private ViewPagerAdapter viewPagerAdapter;

    @Bind(R.id.slide_show_fragment_image_count)
    TextView lblCount;

    @Bind(R.id.viewpager)
    ViewPager viewPager;
    private int selectedPosition = 0;

    public static SlideshowDialogFragment newInstance(List<String> images) {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        imageList = images;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slideshow_dialog, container, false);

        ButterKnife.bind(this,v);

        selectedPosition = getArguments().getInt(AppConstants.BUNDLE_CONTEXT_POSITION);

        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + CreateContextForResource.getStringFromID(R.string.slideshowfragment_picture_of) + imageList.size());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //  adapter
    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public ViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            String image = imageList.get(position);

            Glide.with(getActivity()).load(image)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
