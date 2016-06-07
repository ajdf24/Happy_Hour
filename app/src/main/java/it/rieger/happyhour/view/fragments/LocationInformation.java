package it.rieger.happyhour.view.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.widget.FavoriteButton;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.standard.CreateContextForResource;
import it.rieger.happyhour.view.LocationDetail;

/**
 * Fragment which is overlaying the map with information of a specific location
 */
public class LocationInformation extends Fragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.fragment_location_details_pictures_list_view)
    SliderLayout mDemoSlider;

    @BindView(R.id.fragment_information_button)
    ImageButton infoButton;

    @BindView(R.id.fragment_button_favorite)
    FavoriteButton favoriteButton;

    static Location currentLocation;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_information_fragment, container, false);

        ButterKnife.bind(this, view);

        HashMap<String,String> file_maps = new HashMap<String, String>();
        file_maps.put("Clubeins 1", "http://www.eventsofa.de/venue-images/534/ef0/534ef027b7605368076c4eeb-7262.jpg");
        file_maps.put("Clubeins 2", "http://www.afterworkclub-erfurt.de/wp-content/uploads/2014/11/IMG_6385-705x476.jpg");
        file_maps.put("Clubeins 3", "https://www.blitz-world.de/magazin/archiv/2014/1405/pix/t-club-clubeins2.jpg");

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(new CreateContextForResource().getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
//                    .setOnSliderClickListener(this);


            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

        infoButton.setColorFilter(Color.parseColor(new CreateContextForResource().getStringFromID(R.color.colorAccent)));
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_LOCATION, currentLocation);
                intent.setClass(new CreateContextForResource().getContext(), LocationDetail.class);
                startActivity(intent);
            }
        });

        favoriteButton.setLocation(currentLocation);

        return view;
    }

    /**
     * {@inheritDoc}
     * @param context the called context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * fragment listener
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
