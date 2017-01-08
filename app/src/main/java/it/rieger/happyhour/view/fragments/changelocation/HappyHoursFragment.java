package it.rieger.happyhour.view.fragments.changelocation;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.adapter.HappyHourAdapter;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.Location;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HappyHoursFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HappyHoursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HappyHoursFragment extends AbstractChangeLocationFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOCATION = "location";

    @Bind(R.id.fragment_happy_hours_cardList)
    RecyclerView happyHours;

    @Bind(R.id.fragment_happy_hours_new_happy_hour)
    FloatingActionButton fab;

    private Location location;

    private OnFragmentInteractionListener listener;

    public HappyHoursFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param location current location.
     * @return A new instance of fragment HappyHoursFragment.
     */
    public static HappyHoursFragment newInstance(Location location) {
        HappyHoursFragment fragment = new HappyHoursFragment();
        Bundle args = new Bundle();
        args.putSerializable(LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = (Location) getArguments().getSerializable(LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_happy_hours, container, false);

        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        happyHours.setLayoutManager(linearLayoutManager);

        final HappyHourAdapter happyHourAdapter = new HappyHourAdapter(location, view);
        happyHours.setAdapter(happyHourAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                happyHourAdapter.removeItem(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(happyHours);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.getHappyHours().add(new HappyHour());
                happyHourAdapter.notifyDataSetChanged();
                happyHours.scrollToPosition(happyHourAdapter.getItemCount() - 1);
            }
        });

        return view;
    }

    @Override
    protected void initializeGui() {

    }

    @Override
    protected void initializeActiveElements() {

    }

    @Override
    protected boolean checkReadyToSave() {
        return false;
    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        BackendDatabase.getInstance().saveLocation(location);

        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}
