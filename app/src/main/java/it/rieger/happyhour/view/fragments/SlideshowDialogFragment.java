package it.rieger.happyhour.view.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.model.Image;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.ThumbnailHolderClass;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.listener.OnPageChangeListener;
import it.rieger.happyhour.util.standard.CreateContextForResource;
import it.rieger.happyhour.view.dialogs.ImageContextMenuDialog;
import it.rieger.happyhour.view.viewholder.ThumbnailViewHolder;

public class SlideshowDialogFragment extends DialogFragment{

    private final String LOG_TAG = getClass().getSimpleName();

    private static List<String> imageList;

    static Location currentLocation;

    @Bind(R.id.slide_show_fragment_image_count)
    TextView lblCount;

    @Bind(R.id.viewpager)
    ViewPager viewPager;
    private int selectedPosition = 0;

    public static SlideshowDialogFragment newInstance(List<String> images, Location location) {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        imageList = images;
        currentLocation = location;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slideshow_dialog, container, false);

        ButterKnife.bind(this,v);

        selectedPosition = getArguments().getInt(AppConstants.BUNDLE_CONTEXT_POSITION);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
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
        lblCount.setText(String.format(CreateContextForResource.getContext().getString(R.string.slideshowfragment_picture_of_placeholder_x_from), (position + 1), imageList.size()));
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
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            String image = imageList.get(position);

            ImageViewHolder imageViewHolder = new ImageViewHolder(imageViewPreview, image);

            new DownloadImage().execute(imageViewHolder);



            container.addView(view);

            imageViewPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = ((Activity)view.getContext()).getFragmentManager().beginTransaction();
                    Fragment prev = ((Activity)view.getContext()).getFragmentManager().findFragmentByTag(AppConstants.FragmentTags.FRAGMENT_IMAGE_DIALOG);
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);


                    DialogFragment newFragment = ImageContextMenuDialog.newInstance(imageList.get(position), currentLocation, null);
                    newFragment.show(ft, AppConstants.FragmentTags.FRAGMENT_IMAGE_DIALOG);
                }
            });

            return view;
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class ImageViewHolder{
        ImageView imageView = null;

        String image = null;

        public ImageViewHolder(ImageView imageView, String image) {
            this.imageView = imageView;
            this.image = image;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    private class DownloadImage extends AsyncTask<ImageViewHolder, Integer, ThumbnailViewHolder> {

        @Override
        protected ThumbnailViewHolder doInBackground(final ImageViewHolder... params) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference images = database.getReference(AppConstants.Firebase.IMAGES_PATH);

            images.orderByKey().equalTo(params[0].getImage()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Image image = dataSnapshot1.getValue(Image.class);
//                        params[0].getThumbnailViewHolder().progressBar.setVisibility(View.INVISIBLE);
                        params[0].getImageView().setImageBitmap(image.getImage());
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
