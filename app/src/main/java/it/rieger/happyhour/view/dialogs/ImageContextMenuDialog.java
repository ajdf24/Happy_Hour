package it.rieger.happyhour.view.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.controller.callbacks.ReloadCallback;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.util.standard.CreateContextForResource;

/**
 * This class creates a context menu for a image (recycler view) because a context menu for recycler view needs api level 23.
 * Created by sebastian on 29.07.16.
 */
public class ImageContextMenuDialog extends DialogFragment {

    private final String LOG_TAG = getClass().getSimpleName();

    @Bind(R.id.image_dialog_list_view)
    ListView listView;

    private static final String URI = "imageKey";
    private static final String LOCATION = "location";

    String imageKey;

    Location location;

    static ReloadCallback callbackClass;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment sender_selection_dialog.
     */
    public static ImageContextMenuDialog newInstance(String uri, Location location, ReloadCallback callback) {

        ImageContextMenuDialog fragment = new ImageContextMenuDialog();
        Bundle args = new Bundle();
        args.putString(URI, uri);
        args.putSerializable(LOCATION, location);

        callbackClass = callback;
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageKey = getArguments().getString(URI);
        location = (Location) getArguments().getSerializable(LOCATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.dialog_fragment_image_dialog, container, false);

        ButterKnife.bind(this, fragmentView);

        String[] values = new String[]{
                CreateContextForResource.getStringFromID(R.string.image_dialog_delete)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragmentView.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch (position) {
                    case 0:
                        BackendDatabase.getInstance().removeImage(location, imageKey);
                        callbackClass.reload();
                        dismiss();
                        break;
                    default:
                        Toast.makeText(view.getContext(), "Noch nicht Implementiert", Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, "Menu Item not implemented");
                        break;
                }
            }
        });

        return fragmentView;
    }
}
