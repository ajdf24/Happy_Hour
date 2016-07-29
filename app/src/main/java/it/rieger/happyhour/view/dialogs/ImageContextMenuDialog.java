package it.rieger.happyhour.view.dialogs;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.cache.BitmapLRUCache;
import it.rieger.happyhour.util.standard.CreateContextForResource;

/**
 * This class creates a context menu for a image (recycler view) because a context menu for recycler view needs api level 23.
 * Created by sebastian on 29.07.16.
 */
public class ImageContextMenuDialog extends DialogFragment {

    private final String LOG_TAG = getClass().getSimpleName();

    @Bind(R.id.image_dialog_list_view)
    ListView listView;

    private static final String URI = "uri";

    String uri;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment sender_selection_dialog.
     */
    public static ImageContextMenuDialog newInstance(String uri) {

        ImageContextMenuDialog fragment = new ImageContextMenuDialog();
        Bundle args = new Bundle();
        args.putString(URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uri = getArguments().getString(URI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.dialog_fragment_image_dialog, container, false);

        ButterKnife.bind(this, fragmentView);

        String[] values = new String[]{CreateContextForResource.getStringFromID(R.string.image_dialog_save),
                CreateContextForResource.getStringFromID(R.string.image_dialog_share),
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
                        new SaveImageTask().execute(uri);
                        dismiss();
                        break;
                    case 1:
                        try {
                            String pathOfBmp = MediaStore.Images.Media.insertImage(CreateContextForResource.getContext().getContentResolver(), BitmapLRUCache.getInstance().getBitmapFromMemCache(uri), "title", null);
                            Uri bmpUri = Uri.parse(pathOfBmp);
                            final Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            shareIntent.setType("image/png");
                            startActivity(shareIntent);
                        } catch (NullPointerException e) {
                            Log.e(LOG_TAG, "Image not in cache");
                        }
                        dismiss();
                        break;
                    case 2:
                        Toast.makeText(CreateContextForResource.getContext(), R.string.general_not_implemented, Toast.LENGTH_SHORT).show();
                        dismiss();
                        break;
                    default:
                        Log.e(LOG_TAG, "Menu Item not implemented");
                        break;
                }
            }
        });

        return fragmentView;
    }

    private class SaveImageTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            for (String url : params) {
                URL newUrl = null;
                try {
                    newUrl = new URL(url);
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "String is not a url");
                }
                if (newUrl != null) {
                    String rootImageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    File imageDir = new File(rootImageDir + "/Happy_Hour");
                    System.out.println(rootImageDir);
                    imageDir.mkdirs();
                    String imageName = "Image-" + System.currentTimeMillis() + ".jpg";
                    File file = new File(imageDir, imageName);
                    if (file.exists()) file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        BitmapFactory.decodeStream(newUrl.openConnection().getInputStream()).compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "Can not load Image from URL");
                        Toast.makeText(CreateContextForResource.getContext(), R.string.image_dialog_permission_denied, Toast.LENGTH_LONG).show();
                    }
                }

            }
            return null;
        }
    }
}
