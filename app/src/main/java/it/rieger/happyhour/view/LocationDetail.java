package it.rieger.happyhour.view;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.database.firebase.Firebase;
import it.rieger.happyhour.controller.database.firebase.LocationsLoaded;
import it.rieger.happyhour.model.Day;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.Time;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.standard.CreateContextForResource;
import it.rieger.happyhour.view.fragments.SlideshowDialogFragment;

/**
 * Activity which shows the details for the location
 */
public class LocationDetail extends AppCompatActivity implements LocationsLoaded {

    private final String LOG_TAG = getClass().getSimpleName();

    @Bind(R.id.activity_location_details_fab)
    public FloatingActionButton fab;

    @Bind(R.id.activity_location_details_pictures_list_view)
    SliderLayout slider;

    @Bind(R.id.activity_location_details_opening_time)
    TextView openingTimes;

    @Bind(R.id.activity_location_details_happy_hours)
    TextView happyHours;

    @Bind(R.id.activity_location_details_ratingBar)
    RatingBar ratingBar;

    Location currentLocation;

    /**
     * {@inheritDoc}
     * create the view
     * @param savedInstanceState the saved instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();

        currentLocation = (Location) bundle.getSerializable(AppConstants.BUNDLE_CONTEXT_LOCATION);

        initializeGUI();


//        DatabaseReference mDatabase;
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//            Query postsRef = mDatabase.child("posts").orderByChild("id").equalTo("test");
//            postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    currentLocation = (dataSnapshot.getChildren().iterator().next().getValue(Location.class));
//                    initializeGUI();
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

    }

    /**
     * fill the ui with information
     */
    private void initializeGUI(){
        HashMap<String,String> file_maps = new HashMap<String, String>();
        int numberOfPicture = 1;
        for(String image : currentLocation.getImageKeyList()){
            file_maps.put(currentLocation.getName() + " " + numberOfPicture, image);
            numberOfPicture++;
        }

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            slider.addSlider(textSliderView);
            slider.getCurrentSlider().setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                }
            });
        }

        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);

        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.BUNDLE_CONTEXT_POSITION, 0);

                FragmentManager fragmentManager = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance(currentLocation.getImageKeyList(), currentLocation);
                newFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_container, newFragment, AppConstants.FragmentTags.FRAGMENT_SLIDE_SHOW).addToBackStack(AppConstants.FragmentTags.FRAGMENT_SLIDE_SHOW);
                fragmentTransaction.commit();
            }
        });

        ratingBar.setRating(currentLocation.getRating());

        String openingTimeSting = "";
        for(Time time : currentLocation.getOpeningTimes().getTimes()){
            openingTimeSting = openingTimeSting + Day.toString(time.getDay()) + ": "+ time.getStartTime() + CreateContextForResource.getStringFromID(R.string.general_time_till) + time.getEndTime() + "\n" ;
        }

        openingTimes.setText(openingTimeSting);

        String happyHoursString = "";
        for(HappyHour happyHour : currentLocation.getHappyHours()){
            happyHoursString = happyHoursString + happyHour.getDrink() + CreateContextForResource.getStringFromID(R.string.general_for) + happyHour.getPrice() + "\n";
        }

        happyHours.setText(happyHoursString);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_LOCATION, currentLocation);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(LocationDetail.this, ChangeLocationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void locationsLoaded(List<Location> locations) {
        currentLocation = locations.get(0);
        initializeGUI();
    }
}
