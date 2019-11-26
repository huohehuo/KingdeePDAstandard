package com.fangzuo.assist.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.fangzuo.assist.R;

/**
 * Created by Administrator on 2019/11/25.
 */

public class IconNmuberView extends View {
    Bitmap bitmap;
    String number = "0";

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int x = getWidth() / 2;
        int y = getHeight() / 2;

        Paint paint = new Paint();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notic);
        //在画布上画上图标作为背景，不需动态加载的话可以省略这部
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, paint);
        }

        //数字为0直接返回
        if (number.equals("0")) {
            return;
        }

        //设置画笔为红色
        paint.setColor(Color.RED);
        //计算小圆形的圆心图标，半径取大图标半径的四分之一
        canvas.drawCircle((float) (x + Math.sqrt(x * x / 2)), (float) (x - Math.sqrt(x * x / 2)), (float) (x / 4), paint);
        paint.setColor(Color.WHITE);
        //为适应各种屏幕分辨率，字体大小取半径的3.5分之一，具体根据项目需要调节
        paint.setTextSize((float) (x / 3.5));
        //去除锯齿效果
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        //字体加粗
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //字体位置设置为以圆心为中心
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(number, (float) (x + Math.sqrt(x * x / 2)), (float) (x - Math.sqrt(x * x / 2)) + x / 9, paint);
    }

    //设置图标
    public void setIcon(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    //设置数字
    public void setNumber(String number) {
        this.number = number;
    }

    public IconNmuberView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
