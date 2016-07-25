package it.rieger.happyhour.util.listener;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * Only a helper class, which implements {@link RecyclerView.OnItemTouchListener}.
 * In the code there are now only the methods which are needed.
 * Created by sebastian on 25.07.16.
 */
public class OnItemTouchListener implements RecyclerView.OnItemTouchListener {
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
