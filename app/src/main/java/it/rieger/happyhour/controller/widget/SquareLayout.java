package it.rieger.happyhour.controller.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Layout, which is parted in squares.
 * This layout be used like a {@link RelativeLayout}.
 * Created by Admin on 08.07.2016.
 */
public class SquareLayout extends RelativeLayout {

    private final String LOG_TAG = getClass().getSimpleName();

    /**
     * constructor
     * @param context current context
     */
    public SquareLayout(Context context) {
        super(context);
    }

    /**
     * constructor
     * @param context current context
     * @param attrs additional attributes
     */
    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * constructor
     * @param context current context
     * @param attrs additional attributes
     * @param defStyleAttr style attributes
     */
    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * constructor
     * @param context current context
     * @param attrs additional attributes
     * @param defStyleAttr style attributes
     * @param defStyleRes style references
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
