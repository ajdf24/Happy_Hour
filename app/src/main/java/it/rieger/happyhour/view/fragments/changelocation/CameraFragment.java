package it.rieger.happyhour.view.fragments.changelocation;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.adapter.GalleryAdapter;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.controller.callbacks.ReloadCallback;
import it.rieger.happyhour.model.Image;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.listener.AnimationListener;
import it.rieger.happyhour.view.dialogs.ImageContextMenuDialog;
import it.rieger.happyhour.view.fragments.SlideshowDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CameraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends AbstractChangeLocationFragment implements ReloadCallback{

    private final String LOG_TAG = getClass().getSimpleName();

    private static final String ARG_LOCATION = "location";

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = (Location) getArguments().getSerializable(ARG_LOCATION);
        }



    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_camera, container, false);

        super.onCreateView(inflater, container, savedInstanceState);

        images = location.getImageKeyList();

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeGui() {
        galleryAdapter = new GalleryAdapter(view.getContext(), location);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(galleryAdapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeActiveElements() {

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
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
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

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(view.getContext(), recyclerView, new GalleryAdapter.ClickListener() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void onClick(View view, int position) {

                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.BUNDLE_CONTEXT_POSITION, position);

                FragmentManager fragmentManager = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance(images, location);
                newFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_container, newFragment, AppConstants.FragmentTags.FRAGMENT_SLIDE_SHOW).addToBackStack(AppConstants.FragmentTags.FRAGMENT_SLIDE_SHOW);
                fragmentTransaction.commit();
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void onLongClick(View view, int position) {

                FragmentTransaction ft = ((Activity)view.getContext()).getFragmentManager().beginTransaction();
                Fragment prev = ((Activity)view.getContext()).getFragmentManager().findFragmentByTag(AppConstants.FragmentTags.FRAGMENT_IMAGE_DIALOG);
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);


                DialogFragment newFragment = ImageContextMenuDialog.newInstance(images.get(position), location, CameraFragment.this);
                newFragment.show(ft, AppConstants.FragmentTags.FRAGMENT_IMAGE_DIALOG);
            }
        }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkReadyToSave() {
        return true;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
           Bundle extras = data.getExtras();
           Bitmap imageBitmap = (Bitmap) extras.get("data");

            Image image = new Image();
            image.setBitmapToImage(imageBitmap);

            BackendDatabase.getInstance().saveImage(image, location);


        }
        if(requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK){

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

                Image image = new Image();
                image.setBitmapToImage(bitmap);

                BackendDatabase.getInstance().saveImage(image, location);
                galleryAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reload() {
        galleryAdapter.notifyDataSetChanged();
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
