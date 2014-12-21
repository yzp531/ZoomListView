package com.bettycc.zoomlistview.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.bettycc.zoomlistview.library.HeaderContainer;

/**
 * Created by ccheng on 12/19/14.
 */
public class ZoomListView extends ListView {

    private static final float SCALE_FACTOR = 0.4f;
    private final HeaderContainer mHeaderView;
    private int mActionIndexId;
    private float mLastMotionY;
    private float mLastBottom;

    public ZoomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeaderView = new HeaderContainer(getContext(), attrs);
        addHeaderView(mHeaderView);
    }

    public void setHeaderResource(int resId) {
        mHeaderView.setImageResource(resId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mHeaderView.isAnimating()) {
            mActionIndexId = -1;
            return true;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActionIndexId = ev.getPointerId(0);
                mLastMotionY = ev.getY(mActionIndexId);
                mLastBottom = mHeaderView.getBottom();
                break;
            case MotionEvent.ACTION_MOVE:
                int j = ev.findPointerIndex(mActionIndexId);
                if (j == -1) {
                    break;
                } else {
                    if (mHeaderView.getBottom() >= mHeaderView.getInitHeight()) {
                        float y = ev.getY(mActionIndexId);
                        if (y - mLastMotionY > 0) {
                            float scale = 1 + ((y - mLastMotionY) - (mHeaderView.getBottom() - mLastBottom)) / mHeaderView.getHeight() * SCALE_FACTOR;
                            mHeaderView.scale(scale);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mHeaderView.isScaled()) {
                    mHeaderView.restoreScale();
                }
        }

        return super.onTouchEvent(ev);
    }

    public HeaderContainer getHeaderView() {
        return mHeaderView;
    }


    public void setHeaderContentView(View view) {
        mHeaderView.addView(view);
    }
}