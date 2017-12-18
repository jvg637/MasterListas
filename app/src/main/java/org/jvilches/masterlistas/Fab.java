package org.jvilches.masterlistas;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import com.gordonwong.materialsheetfab.AnimatedFab;

/**
 * Created by jvg63 on 03/11/2017.
 */

public class Fab extends FloatingActionButton implements AnimatedFab {
    public Fab(Context context) {
        super(context);
    }

    public Fab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Fab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Shows the FAB.
     */
    @Override
    public void show() {
        show(0, 0);
    }

    @Override
    public void show(float translationX, float translationY) {
        show(translationX, translationY);
    }


}
