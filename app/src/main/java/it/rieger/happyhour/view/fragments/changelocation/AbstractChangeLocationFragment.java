package it.rieger.happyhour.view.fragments.changelocation;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.exception.NotSavedException;

/**
 * Created by sebastian on 31.12.16.
 */

/**
 * Abstract class for fragments, which change a location.
 * Most methods are given.
 * Before this class can detached the methods {@link AbstractChangeLocationFragment#readyToSave()}
 * and {@link AbstractChangeLocationFragment#saveLocation()} must be called first.
 *
 * All views should bind with the @Bind annotation from Butterknife
 */
public abstract class AbstractChangeLocationFragment extends Fragment {

    /**
     * interactionListener
     */
    private OnFragmentInteractionListener listener;

    /**
     * log tag
     */
    protected final String LOG_TAG = getClass().getSimpleName();

    /**
     * bundle key for location
     */
    protected static final String LOCATION = "Location";

    /**
     * current location
     */
    protected Location location;

    /**
     * is fragment ready to save
     */
    private boolean readyToSaveCalled = false;

    /**
     * is location saved
     */
    private boolean locationSaved = false;

    /**
     * delete changes anyway
     */
    private boolean deleteAnyway = false;

    /**
     * current view
     */
    protected View view;

    /**
     * current context
     */
    protected Context context;

    ProgressDialog progress;

    /**
     * check if the fragment is ready to save and to detach.
     * <note>{@link AbstractChangeLocationFragment#onDetach()} can only called after this call</note>
     * @return true if the fragment is ready to save, false otherwise
     */
    public boolean readyToSave(){
        boolean isReady = checkReadyToSave();

        readyToSaveCalled = true;

        return isReady;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = (Location) getArguments().getSerializable(LOCATION);
    }

    /**
     * creates the view for this fragment.
     * call the methods {@link ButterKnife#bind(Object, View)}, {@link AbstractChangeLocationFragment#initializeGui()},
     * {@link AbstractChangeLocationFragment#initializeActiveElements()}
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_general, container, false);

        context = view.getContext();

        ButterKnife.bind(this, view);

        initializeGui();

        initializeActiveElements();

        return view;
    }

    /**
     * initializes the gui with the values form {@link AbstractChangeLocationFragment#location}
     */
    protected abstract void initializeGui();

    /**
     * initializes active elements like buttons and so on
     */
    protected abstract void initializeActiveElements();

    /**
     * check if the current state of the ui is valid for save.
     * this method should ony called from the method {@link AbstractChangeLocationFragment#readyToSave()}
     * @return true if the ui is valid otherwise false.
     */
    protected abstract boolean checkReadyToSave();

    /**
     * save the current changes to the database.
     * this method must be called before {@link AbstractChangeLocationFragment#onDetach()} and after
     * {@link AbstractChangeLocationFragment#readyToSave()}
     */
    public void saveLocation(){

        if(!readyToSaveCalled){
            throw new RuntimeException("readyToSave() not called first!");
        }

        BackendDatabase.getInstance().saveLocation(location);

        locationSaved = true;
    }

    /**
     * this method can only be called
     *
     * {@inheritDoc}
     */
    @Override
    public void onDetach() {
        super.onDetach();

//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        // set title
//        alertDialogBuilder.setTitle("Please logout after ");
//        AlertDialog d = alertDialogBuilder.create();
//        d.show();


//        if(!locationSaved){
//            if(!deleteAnyway) {
//                throw new NotSavedException(context.toString() + " Location is not saved or deleteAnyway not set true");
//            }
//        }
    }

//    /**
//     * set the delete anyway flag. so the fragment can detached without save.
//     */
//    public void setDeleteAnyway() {
//        this.deleteAnyway = true;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
