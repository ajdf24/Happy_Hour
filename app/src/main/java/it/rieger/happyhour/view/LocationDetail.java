package it.rieger.happyhour.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;

public class LocationDetail extends AppCompatActivity {

    @Bind(R.id.activity_location_details_fab)
    public FloatingActionButton fab;

    @Bind(R.id.activity_location_details_pictures_list_view)
    SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        ButterKnife.bind(this);

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
//                    .setOnSliderClickListener(this);


            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
//        mDemoSlider.addOnPageChangeListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
