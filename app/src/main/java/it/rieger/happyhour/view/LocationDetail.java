package it.rieger.happyhour.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    SliderLayout mDemoSlider;

    @Bind(R.id.activity_location_details_opening_time)
    TextView openingTimes;

    @Bind(R.id.activity_location_details_happy_hours)
    TextView happyHours;

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

        initializeGUI();

        currentLocation = (Location) this.getIntent().getSerializableExtra(AppConstants.BUNDLE_CONTEXT_LOCATION);
        String openingTimeSting = "";
        for(Time time : currentLocation.getOpeningTimes().getTimes()){
            openingTimeSting = openingTimeSting + Day.toString(time.day) + ": "+ time.getStartTime() + " bis " + time.getEndTime() + "\n" ;
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

    /**
     * fill the ui with information
     */
    private void initializeGUI(){
        HashMap<String,String> file_maps = new HashMap<String, String>();
        file_maps.put("Clubeins 1", "http://www.eventsofa.de/venue-images/534/ef0/534ef027b7605368076c4eeb-7262.jpg");
        file_maps.put("Clubeins 2", "http://www.afterworkclub-erfurt.de/wp-content/uploads/2014/11/IMG_6385-705x476.jpg");
        file_maps.put("Clubeins 3", "https://www.blitz-world.de/magazin/archiv/2014/1405/pix/t-club-clubeins2.jpg");

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);


            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
