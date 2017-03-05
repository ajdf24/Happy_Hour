package it.rieger.happyhour.util.listener;

import android.widget.SearchView;

/**
 * Only a helper class, which implements {@link SearchView.OnQueryTextListener}.
 * In the code there are now only the methods which are needed.
 * Created by sebastian on 28.07.16.
 */
public class OnQueryTextListener implements SearchView.OnQueryTextListener {
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
