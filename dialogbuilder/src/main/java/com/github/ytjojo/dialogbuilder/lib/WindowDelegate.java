package com.github.ytjojo.dialogbuilder.lib;

import android.app.Activity;
import android.view.View;

public interface WindowDelegate {


     void attachToWindow();

     void detachFromWindow();

     void onCreate(Activity activity,View contentView);

     boolean isShowing();

     void setOnKeyListener(OnKeyListener l);
}
