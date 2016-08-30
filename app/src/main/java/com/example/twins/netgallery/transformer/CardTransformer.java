package com.example.twins.netgallery.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Twins on 30.08.2016.
 */

public class CardTransformer implements ViewPager.PageTransformer {

    private final float scalingStart;

    public CardTransformer(float scalingStart) {
        super();
        this.scalingStart = 1 - scalingStart;
    }

    @Override
    public void transformPage(View page, float position) {

        if (position >= 0) {
            final int w = page.getWidth();
            float scaleFactor = 1 - scalingStart * position;

            page.setAlpha(1 - position);
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setTranslationX(w * (1 - position) - w);
        }
    }
}
