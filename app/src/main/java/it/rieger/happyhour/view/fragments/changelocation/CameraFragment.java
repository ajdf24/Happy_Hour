package it.rieger.happyhour.view.fragments.changelocation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.adapter.GalleryAdapter;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.listener.AnimationListener;
import it.rieger.happyhour.view.fragments.SlideshowDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CameraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();

    private static final String ARG_LOCATION = "location";

    private Location location;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_PHOTO = 2;

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_button_takepicture)
    FloatingActionButton takePictureIntent;

    @Bind(R.id.fragment_button_pick)
    FloatingActionButton photoPickerIntent;

    @Bind(R.id.fragment_button_show_buttons)
    FloatingActionButton showButton;

    @Bind(R.id.fragment_camera_imageview)
    ImageView mImageView;

    @Bind(R.id.fragment_camera_image_recycler_view)
    RecyclerView recyclerView;

    private boolean isButtonsShow = false;

    private List<String> images;
    private GalleryAdapter galleryAdapter;



    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CameraFragment.
     */
    public static CameraFragment newInstance(Location param1) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = (Location) getArguments().getSerializable(ARG_LOCATION);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, view);

        takePictureIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        photoPickerIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isButtonsShow) {
                    Animation expandIn = AnimationUtils.loadAnimation(getActivity(), R.anim.expand_in);
                    Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_clock_whise);
                    showButton.startAnimation(rotate);
                    takePictureIntent.startAnimation(expandIn);
                    photoPickerIntent.startAnimation(expandIn);
                    expandIn.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            takePictureIntent.setVisibility(View.VISIBLE);
                            photoPickerIntent.setVisibility(View.VISIBLE);
                            isButtonsShow = true;
                        }
                    });
                }else {
                    Animation expandOut = AnimationUtils.loadAnimation(getActivity(), R.anim.expand_out);
                    Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_un_clock_whise);
                    showButton.startAnimation(rotate);
                    takePictureIntent.startAnimation(expandOut);
                    photoPickerIntent.startAnimation(expandOut);
                    expandOut.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            takePictureIntent.setVisibility(View.INVISIBLE);
                            photoPickerIntent.setVisibility(View.INVISIBLE);
                            isButtonsShow = false;
                        }
                    });
                }
            }
        });

        showButton.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                return false;
            }
        });


        images = location.getImageKeyList();
        galleryAdapter = new GalleryAdapter(view.getContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(galleryAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(view.getContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.BUNDLE_CONTEXT_POSITION, position);

                FragmentManager fragmentManager = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance(images);
                newFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_container, newFragment, AppConstants.FragmentTags.FRAGMENT_SLIDE_SHOW).addToBackStack(AppConstants.FragmentTags.FRAGMENT_SLIDE_SHOW);
                fragmentTransaction.commit();
            }

            @Override
            public void onLongClick(View view, int position) {
                //TODO: delete action
            }
        }));

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
           Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //TODO: Upload to Server Show in Galery

        }
        if(requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                //TODO: Upload to Server Show in Galery
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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
