package it.rieger.happyhour.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.rieger.happyhour.R;

/**
 * Created by admin on 06.06.16.
 */
public class FirstFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.first_layout, container, false);
        return myView;
    }
}
