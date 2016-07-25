package it.rieger.happyhour.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.model.Day;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.Time;
import it.rieger.happyhour.util.AppConstants;

/**
 * Activity which shows the details for the location
 */
public class LocationDetail extends AppCompatActivity {

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

        currentLocation = (Location) this.getIntent().getSerializableExtra(AppConstants.BUNDLE_CONTEXT_LOCATION);

        initializeGUI();

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
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);


            slider.addSlider(textSliderView);
        }

        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);

        ratingBar.setRating(currentLocation.getRating());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String openingTimeSting = "";
        for(Time time : currentLocation.getOpeningTimes().getTimes()){
            openingTimeSting = openingTimeSting + Day.toString(time.getDay()) + ": "+ time.getStartTime() + " bis " + time.getEndTime() + "\n" ;
        }

        openingTimes.setText(openingTimeSting);

        String happyHoursString = "";
        for(HappyHour happyHour : currentLocation.getHappyHours()){
            happyHoursString = happyHoursString + happyHour.getDrink() + " für " + happyHour.getPrice() + "\n";
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
}
