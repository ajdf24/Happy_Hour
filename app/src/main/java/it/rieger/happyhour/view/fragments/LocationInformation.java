package it.rieger.happyhour.view.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.widget.FavoriteButton;
import it.rieger.happyhour.model.Image;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.standard.CreateContextForResource;
import it.rieger.happyhour.controller.widget.BitmapSlider;
import it.rieger.happyhour.view.LocationDetail;

/**
 * Fragment which is overlaying the map with information of a specific location
 */
public class LocationInformation extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();

    @Bind(R.id.fragment_location_details_pictures_list_view)
    SliderLayout demoSlider;

    @Bind(R.id.fragment_location_information_information_button)
    ImageButton infoButton;

    @Bind(R.id.fragment_location_information_button_favorite)
    FavoriteButton favoriteButton;

    static Location currentLocation;

    View view;

    /**
     * constructor, which is required
     */
    public LocationInformation() {
    }

    /**
     *  create a new instance of this fragment
     * @return A new instance of fragment {@link LocationInformation}.
     */
    public static LocationInformation newInstance(Location location) {
        LocationInformation fragment = new LocationInformation();
        currentLocation = location;
        return fragment;
    }

    /**
     * {@inheritDoc}
     * create the ui
     * @param savedInstanceState saved instance of the fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * create the view of the fragment
     * @param inflater the inflater
     * @param container the container
     * @param savedInstanceState saved instance of the fragment
     * @return the created view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location_information_fragment, container, false);

        ButterKnife.bind(this, view);


        new DownloadImage().execute((String[]) currentLocation.getImageKeyList().toArray(new String[]{}));



        demoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        demoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        demoSlider.setCustomAnimation(new DescriptionAnimation());
        demoSlider.setDuration(4000);

        infoButton.setColorFilter(Color.parseColor(new CreateContextForResource().getStringFromID(R.color.colorAccent)));
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_LOCATION, currentLocation);
                intent.setClass(getActivity(), LocationDetail.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        favoriteButton.setLocation(currentLocation);
        
        return view;
    }

    /**
     * internal task for downloading images
     */
    private class DownloadImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(final String... params) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference images = database.getReference(AppConstants.Firebase.IMAGES_PATH);

            for (String key : params) {
                images.orderByKey().equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Image image = dataSnapshot1.getValue(Image.class);
                            BitmapSlider textSliderView = new BitmapSlider(LocationInformation.this.getActivity());
                            textSliderView
                                    .image(image.getImage())
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            demoSlider.addSlider(textSliderView);
                            demoSlider.getCurrentSlider().setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            return null;
        }
    }

    /**
     * {@inheritDoc}
     * @param context the called context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

        demoSlider = (SliderLayout) view.findViewById(R.id.fragment_location_details_pictures_list_view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
        demoSlider = null;
    }

    /**
     * fragment listener
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
