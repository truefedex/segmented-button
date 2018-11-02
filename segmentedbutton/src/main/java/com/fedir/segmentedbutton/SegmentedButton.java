package com.fedir.segmentedbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * <p>This class is used to create a multiple-exclusion scope for a set of custom
 * views. Checking one view that belongs to a sectioned button unchecks
 * any previously checked view within the same sectioned button.</p>
 */
public class SegmentedButton extends LinearLayout {
    private OnHierarchyChangeListener delegatedOnHierarchyChangeListener;
    protected int checkedId = NO_ID;
    protected float cornerRadiusX, cornerRadiusY;
    protected OnCheckedChangeListener checkedChangeListener;

    public SegmentedButton(Context context) {
        super(context);
        init(null);
    }

    public SegmentedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SegmentedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.SegmentedButton);
            this.checkedId = attributes.getResourceId(R.styleable.SegmentedButton_checkedButton, NO_ID);
            this.cornerRadiusX = dipToPixels(getContext(),
                    attributes.getFloat(R.styleable.SegmentedButton_cornerRadiusX, 10f));
            this.cornerRadiusY = dipToPixels(getContext(),
                    attributes.getFloat(R.styleable.SegmentedButton_cornerRadiusY, 10f));
            attributes.recycle();
        }

        super.setOnHierarchyChangeListener(this.hierarchyChangeListener);
    }

    public int getCheckedId() {
        return checkedId;
    }

    /**
     * <p>Sets the selection to the view whose identifier is passed in
     * parameter. Using -1 (or View.NO_ID) as the selection identifier clears the selection;</p>
     *
     * @param value the unique id of the view to select in this segmented button
     *
     * @see #getCheckedId()
     */
    public void setCheckedId(int value) {
        setCheckedId(value, false);
    }

    /**
     * <p>Returns the identifier of the selected view in this segmented button.
     * Upon empty selection, the returned value is -1.</p>
     *
     * @return the unique id of the selected view in this segmented button
     *
     * @see #setCheckedId(int)
     */
    private void setCheckedId(int value, boolean fromUser) {
        this.checkedId = value;
        this.updateChildViewsDecorations();
        if (checkedChangeListener != null) {
            checkedChangeListener.onCheckedChanged(this, checkedId, fromUser);
        }
    }

    public OnCheckedChangeListener getCheckedChangeListener() {
        return checkedChangeListener;
    }

    public void setCheckedChangeListener(OnCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        this.delegatedOnHierarchyChangeListener = listener;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        this.updateChildViewsDecorations();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.updateChildViewsDecorations();
    }

    private OnClickListener onChildClickListener = new OnClickListener() {
        public void onClick(View v) {
            setCheckedId(v.getId(), true);
        }
    };

    private OnHierarchyChangeListener hierarchyChangeListener = new OnHierarchyChangeListener() {
        public void onChildViewAdded(View parent, View child) {
            if (parent == SegmentedButton.this) {
                int id = child.getId();
                if (id == NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }

                child.setOnClickListener(onChildClickListener);
            }

            if (delegatedOnHierarchyChangeListener != null) {
                delegatedOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(View parent, View child) {
            if (parent == SegmentedButton.this) {
                child.setOnClickListener(null);
            }

            if (delegatedOnHierarchyChangeListener != null) {
                delegatedOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    };

    protected void updateChildViewsDecorations() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = this.getChildAt(i);
            boolean checked = child.getId() == this.checkedId;
            child.setActivated(checked);// use activated state since our custom view does not need to be checkable
            decorateOneSegmentView(child, checked, i, getChildCount());
        }
    }

    protected void decorateOneSegmentView(View child, boolean checked, int index, int count) {
        if (child.getBackground() != null && child.getBackground() instanceof StateListDrawable) {
            StateListDrawable stateList = (StateListDrawable)child.getBackground();
            if (stateList.getCurrent() instanceof GradientDrawable) {
                GradientDrawable shape = (GradientDrawable)stateList.getCurrent();
                float[] corners;
                if (getOrientation() == HORIZONTAL) {
                    corners = index == 0 ? // first view in segment
                            new float[]{cornerRadiusX, cornerRadiusY, 0.0F, 0.0F, 0.0F, 0.0F, cornerRadiusX, cornerRadiusY} :
                            (index == count - 1 ? // last view in segment
                                    new float[]{0.0F, 0.0F, cornerRadiusX, cornerRadiusY, cornerRadiusX, cornerRadiusY, 0.0F, 0.0F} :
                                    new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
                } else {
                    corners = index == 0 ?  // first view in segment
                            new float[]{cornerRadiusX, cornerRadiusY, cornerRadiusX, cornerRadiusY, 0.0F, 0.0F, 0.0F, 0.0F} :
                            (index == count - 1 ? // last view in segment
                                    new float[]{0.0F, 0.0F, 0.0F, 0.0F, cornerRadiusX, cornerRadiusY, cornerRadiusX, cornerRadiusY} :
                                    new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
                }
                shape.setCornerRadii(corners);
            }
        }
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * view changed in this segments group.</p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>Called when the checked view has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group the group in which the checked view has changed
         * @param checkedId the unique identifier of the newly checked view
         * @param fromUser is checked state changed by user UI interaction, otherwise it
         *                 changed programmatically
         */
        public void onCheckedChanged(SegmentedButton group, int checkedId, boolean fromUser);
    }
}
