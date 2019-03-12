
package com.github.ytjojo.dialogbuilder.lib.showtipsview;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;


/**
 * @author SamLeung
 * @e-mail samlssplus@gmail.com
 * @github https://github.com/samlss
 * @description
 */
public class OvalShape extends LighterShape {

    /**
     * Construct a circle shape object.
     *
     * Will call {@link #OvalShape(float)} and pass the parameter is (15);
     * */
    public OvalShape() {
        super(15);
    }

    /**
     * Construct a oval shape object.
     * */
    public OvalShape(float blurRadius){
        super(blurRadius);
    }

    @Override
    public void setViewRect(Rect rect) {
        super.setViewRect(rect);

        if (!isViewRectEmpty()) {
            path.reset();
            RectF rectF =  new RectF(rect);
//            rectF.inset(0,-rect.height()*0.207f);
            path.addOval(rectF, Path.Direction.CW);
        }
    }
}