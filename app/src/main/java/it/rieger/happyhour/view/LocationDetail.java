package it.rieger.happyhour.view;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.controller.widget.BitmapSlider;
import it.rieger.happyhour.controller.widget.FavoriteButton;
import it.rieger.happyhour.model.Day;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.Image;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.Time;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.listener.ValueEventListener;
import it.rieger.happyhour.util.standard.CreateContextForResource;
import it.rieger.happyhour.view.fragments.SlideshowDialogFragment;

/**
 * Activity which shows the details for the location
 */
public class LocationDetail extends AppCompatActivity  {

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

    @Bind(R.id.activity_location_details_button_favorite)
    FavoriteButton favoriteButton;

    Location currentLocation;

    /**
     * {@inheritDoc}
     * create the view
     *
     * @param savedInstanceState the saved instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        ButterKnife.bind(this);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getExtras();

        currentLocation = (Location) bundle.getSerializable(AppConstants.BUNDLE_CONTEXT_LOCATION);

        favoriteButton.setLocation(currentLocation);

        initializeGUI();
    }

    /**
     * fill the ui with information
     */
    private void initializeGUI() {
        final HashMap<String, String> file_maps = new HashMap<String, String>();
        int numberOfPicture = 1;
        for (String image : currentLocation.getImageKeyList()) {
            file_maps.put(currentLocation.getName() + " " + numberOfPicture, image);
            numberOfPicture++;
        }

        new DownloadImage().execute((String[]) currentLocation.getImageKeyList().toArray(new String[]{}));

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

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if(currentLocation.getRatedUser().contains(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Toast.makeText(LocationDetail.this, R.string.general_already_voted, Toast.LENGTH_SHORT).show();
                    ratingBar.setRating(currentLocation.getRating());
                }else {
                    currentLocation.setRating(calculateNewRating(currentLocation.getRating(), currentLocation.getRatedUser().size(), rating));
                    currentLocation.getRatedUser().add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                }

                BackendDatabase.getInstance().saveLocation(currentLocation);

            }
        });

        String openingTimeSting = "";
        for (Time time : currentLocation.getOpeningTimes().getTimes()) {
            openingTimeSting = openingTimeSting + Day.toString(time.getDay()) + ": " + time.getStartTime() + CreateContextForResource.getStringFromID(R.string.general_time_till) + time.getEndTime() + "\n";
        }

        openingTimes.setText(openingTimeSting);

        String happyHoursString = "";
        for (HappyHour happyHour : currentLocation.getHappyHours()) {
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
                finish();
            }
        });
    }

    /**
     * calculate new rating for location
     * @return the new rating
     */
    private float calculateNewRating(float currentRating, int numberOfRatings, float newRating){
        if(numberOfRatings == 0){
            return newRating;
        }else {
            float rating = currentRating * numberOfRatings;
            rating = rating + newRating;

            return rating / (numberOfRatings + 1);
        }
    }

    /**
     * task for downloading images
     */
    private class DownloadImage extends AsyncTask<String, Integer, Bitmap> {

        /**
         * {@inheritDoc}
         */
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
                            BitmapSlider textSliderView = new BitmapSlider(LocationDetail.this);
                            textSliderView
                                    .image(image.getImage())
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            slider.addSlider(textSliderView);
                            slider.getCurrentSlider().setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                }
                            });
                        }
                    }
                });
            }

            return null;
        }
    }
}
