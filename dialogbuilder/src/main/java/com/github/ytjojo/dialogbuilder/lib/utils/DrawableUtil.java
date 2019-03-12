package com.github.ytjojo.dialogbuilder.lib.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * Created by Administrator on 2015/12/2 0002.
 */
public class DrawableUtil {

    private static final int OUT_BMP_WIDTH = 200;
    private static final int OUT_BMP_HEIGHT = 200;

    /**
     * 用于按压变色的控件背景
     * @param defaultColor
     * @param pressColor
     * @param conerPx
     * @return
     */
    public static Drawable getCornerDrawable(int defaultColor, int pressColor, float conerPx) {
        GradientDrawable defaultDrawable = new GradientDrawable();
        defaultDrawable.setColor(defaultColor);
        defaultDrawable.setCornerRadius(conerPx);
        GradientDrawable pressDrawable = new GradientDrawable();
        pressDrawable.setColor(pressColor);
        pressDrawable.setCornerRadius(conerPx);
        StateListDrawable stateDrawable = new StateListDrawable();
        stateDrawable.addState(new int[]{android.R.attr.state_checked, android.R.attr.state_focused,android.R.attr.state_pressed},pressDrawable);
        stateDrawable.addState(new int[]{},defaultDrawable);
        return stateDrawable;
    }

    /**
     *纯色图片变色
     * @param drawable
     * @param color
     * @return
     */
    public static Drawable getTintDrawable(Drawable drawable, int color) {
        Drawable.ConstantState state = drawable.getConstantState();
        Drawable target = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
        target.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        DrawableCompat.setTint(target, color);
        return target;

    }

    /**
     * 可以仅用一张图片实现控件按压后颜色改变的效果
     * @param c
     * @param drawableId
     * @param colorId
     * @return
     */
    private static Drawable getStateListDraable(Context c, int drawableId, int colorId) {
        Drawable drawable = ContextCompat.getDrawable(c,drawableId);
        int[] colors = new int[] { ContextCompat.getColor(c,colorId), Color.TRANSPARENT};
        int[][] states = new int[2][];
        states[0] = new int[] { android.R.attr.state_pressed};
        states[1] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(states[0],drawable);//注意顺序
        stateListDrawable.addState(states[1],drawable);
        Drawable.ConstantState state = stateListDrawable.getConstantState();
        drawable = DrawableCompat.wrap(state == null ? stateListDrawable : state.newDrawable()).mutate();
        DrawableCompat.setTintList(drawable,colorList);
        return drawable;
    }

    /**
     * 圆角纯色Drawable
     * @param color
     * @param conerPx
     * @return
     */
    public static GradientDrawable getCornerDrawableNoState(int color, float conerPx) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(conerPx);
        return  drawable;
    }

    /**
     * 圆角纯色Drawable
     * @param color
     * @param
     * @return
     */
    public static GradientDrawable getCornerDrawableNoState(int color, float topLeft, float topRight,
                                                            float bottomLeft, float bottomRight) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        float[] corners = new float[]{topLeft,topLeft, topRight, topRight,bottomLeft,bottomLeft,bottomRight,
                bottomRight};
        ((GradientDrawable)drawable.mutate()).setCornerRadii(corners);
        return  drawable;
    }

    /**
     * 圆角纯色Drawable，带有边框
     * @param solidColor 填充颜色
     * @param strokeColor 边框颜色
     * @param strokeWidth 边框宽度
     * @param conerPx 圆角度数
     * @return
     */
    public static GradientDrawable getCornerDrawableNoState(int solidColor, int strokeColor, int strokeWidth,
                                                            float conerPx) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(solidColor);
        drawable.setCornerRadius(conerPx);
        drawable.setStroke(strokeWidth, strokeColor);
        return  drawable;
    }

    /**
     * 圆形纯色drawable
     * @param color
     * @return
     */
    public static Drawable getCircleDrawable(int color) {

        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setShape(GradientDrawable.OVAL);
        d.setSize(1, 1);
        return d;
    }

    public static GradientDrawable getCornerDrawableNoState(int color, float[] conerPxs) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadii(conerPxs);
        return  drawable;
    }

    public static Bitmap getCornerDrawableFromDrawable(Drawable inDrawable) {
        BitmapDrawable bd = (BitmapDrawable) inDrawable;
        Bitmap inBitmap = bd.getBitmap();
        return getCornerDrawableFromBmp(inBitmap);
    }

    public static Bitmap getCircleDrawableFromDrawable(Drawable inDrawable) {
        Bitmap inBitmap  = drawableToBitmap(inDrawable);
        return getCircleBitmap(inBitmap);
    }

    public static Bitmap getCornerDrawableFromBmp(Bitmap inBitmap) {
        inBitmap = zoomBitmap(inBitmap, OUT_BMP_WIDTH, OUT_BMP_WIDTH, true);
        int width = inBitmap.getWidth();
        int height = inBitmap.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//创建一个空的bitmap
        Canvas canvas = new Canvas(outBitmap);

        Path path = new Path();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        RectF r = new RectF();
        r.left = 0;
        r.right = (int)width;
        r.top = 0;
        r.bottom = (int)height;

        path.addRoundRect(r, 12, 12, Path.Direction.CCW);
        path.close();
        canvas.drawRoundRect(r, 12, 12, paint);

        canvas.clipPath(path, Region.Op.REPLACE);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(inBitmap, 0, 0, paint);

        return outBitmap;
    }

    public static Bitmap getCircleBitmap(Bitmap inBitmap) {
        int width = inBitmap.getWidth();
        int height = inBitmap.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//创建一个空的bitmap
        Canvas canvas = new Canvas(outBitmap);

        Path path = new Path();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        RectF r = new RectF();
        r.left = 0;
        r.right = (int)width;
        r.top = 0;
        r.bottom = (int)height;

        path.addCircle(width / 2, width / 2, width / 2, Path.Direction.CW);
        path.close();
        canvas.drawPath(path, paint);

        canvas.clipPath(path, Region.Op.REPLACE);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(inBitmap, 0, 0, paint);

        return outBitmap;
    }

    public static Bitmap zoomBitmap(Bitmap inBmp, int w, int h, Boolean scale) {
        int width = inBmp.getWidth();
        int height = inBmp.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth;
        float scaleHeight;
        if (scale) {
            // 如果要保持宽高比，那说明高度跟宽度的缩放比例都是相同的
            scaleWidth = ((float) w / width);
            scaleHeight = ((float) w / width);
        } else {
            // 如果不保持缩放比，那就根据指定的宽高度进行缩放
            scaleWidth = ((float) w / width);
            scaleHeight = ((float) h / height);
        }
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(inBmp, 0, 0, width, height,
                matrix, true);

        return newbmp;
    }
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
           return bitmapDrawable.getBitmap();
        }
        Bitmap bitmap = null;
        try {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            bitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
        } catch (Exception e) {
            // TODO: handle exception

        }

        return bitmap;
    }


    /**
     * 创建RoundedBitmapDrawable实例
     *
     * @param bitmapOri    原始Bitmap实例
     * @param circular     是否是圆形
     * @param strokeWidth  边框宽度
     * @param cornerRadius 圆角半径值
     * @return
     */
    public static RoundedBitmapDrawable gainRoundedBitmapDrawableWithStroke(
            Resources resources,
            Bitmap bitmapOri,
            boolean circular,
            @ColorInt int strokeColor,
            int strokeWidth,
            float cornerRadius) {
        //注意,要先执行Bitmap.copy(Config config, boolean isMutable),
        //因为下面代码 Canvas(bitmap)需要保证传入的Bitmap实例必须是mutable
        Bitmap bitmap = bitmapOri.copy(Bitmap.Config.ARGB_8888, true);
        //Construct a canvas with the specified bitmap to draw into.
        //The bitmap must be mutable.
        Canvas canvas = new Canvas(bitmap);
        Paint borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(strokeWidth);
        borderPaint.setColor(strokeColor);
        //
        RoundedBitmapDrawable roundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(resources, bitmap);
        if (circular) {
            canvas.drawCircle(
                    roundedBitmapDrawable.getIntrinsicWidth() / 2,
                    roundedBitmapDrawable.getIntrinsicWidth() / 2,
                    roundedBitmapDrawable.getIntrinsicWidth() / 2,
                    borderPaint);
            //设置RoundedBitmapDrawable实例绘制为圆形
            roundedBitmapDrawable.setCircular(true);
        } else {
            //设置RoundedBitmapDrawable实例的圆角值
            RectF rectF = new RectF();
            rectF.set(0, 0,
                    roundedBitmapDrawable.getIntrinsicWidth(),
                    roundedBitmapDrawable.getIntrinsicHeight());
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, borderPaint);
            roundedBitmapDrawable.setCornerRadius(cornerRadius);
        }
        return roundedBitmapDrawable;
    }


}
