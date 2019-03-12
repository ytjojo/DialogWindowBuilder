package com.github.ytjojo.dialogbuilder;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.ytjojo.dialogbuilder.lib.DialogFragmentDelegateImpl;
import com.github.ytjojo.dialogbuilder.lib.WindowBuilder;
import com.github.ytjojo.dialogbuilder.lib.showtipsview.CircleShape;
import com.github.ytjojo.dialogbuilder.lib.showtipsview.OvalShape;
import com.github.ytjojo.dialogbuilder.lib.showtipsview.RectShape;
import com.github.ytjojo.dialogbuilder.lib.showtipsview.ShowTipsHelper;
import com.github.ytjojo.dialogbuilder.lib.showtipsview.TipViewInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ScrollingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = WindowBuilder.newBuilder(ScrollingActivity.this)
                        .setContentView(R.layout.dialog_content)
                        .windowDelegate(new DialogFragmentDelegateImpl())
                        .setRelativePosition(v,WindowBuilder.RELATIVE_LEFT_TO_LEFTOF|WindowBuilder.RELATIVE_RIGHT_TO_LEFTOF,WindowBuilder.RELATIVE_BOTTOM_TO_TOPOF)
                        .setWindowBackgroudColor(0x88000000)
                        .show();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(ScrollingActivity.this);
//                builder.setMessage("确定删除吗？");
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.setTitle("提示");
//                builder.show();

                builder = WindowBuilder.newBuilder(ScrollingActivity.this)
                        .setContentView(R.layout.dialog_content)
                        .windowDelegate(new DialogFragmentDelegateImpl())
                        .setRelativePosition(view,WindowBuilder.RELATIVE_RIGHT_TO_LEFTOF,WindowBuilder.RELATIVE_BOTTOM_TO_TOPOF)
                        .setWindowBackgroudColor(0x88000000)
                        .show();


//                builder = WindowBuilder.newBuilder(ScrollingActivity.this)
//                        .setContentView(R.layout.dialog_content)
//                        .windowDelegate(new DialogFragmentDelegateImpl())
//                        .setLeftRightMargin()
//                        .setLayoutVerticalBias(1f)
//                        .setGravity(Gravity.CENTER)
//                        .setWindowBackgroudColor(0x88000000)
//                        .show();

            }
        });

        ShowTipsHelper.create(ScrollingActivity.this)
                .addTipViewInfo(TipViewInfo.create()
                    .setHightLightedView(findViewById(R.id.fab))
                        .setTipView(new TextView(ScrollingActivity.this))
                        .setInset(-10,-10)
                        .setShape(new CircleShape(15))


                ).addTipViewInfo(
                TipViewInfo.create()
                        .setHightLightedView(findViewById(R.id.tv_click))
                        .setTipView(new TextView(ScrollingActivity.this))
                        .setInset(-10,-10)
                        .setShape(new RectShape())
        )
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(ScrollingActivity.this);
//            builder.setMessage("确定删除吗？");
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            builder.show();
//
//            AppCompatDialog dialog = new AppCompatDialog(ScrollingActivity.this);
//            dialog.show();;
//            dialog.setContentView(R.layout.dialog_content);

            builder = WindowBuilder.newBuilder(ScrollingActivity.this)
                    .setContentView(R.layout.dialog_content)
                    .windowDelegate(new DialogFragmentDelegateImpl())
                    .setLeftRightMargin()
                    .setLayoutVerticalBias(1f)
                    .setGravity(Gravity.CENTER)
                    .setWindowBackgroudColor(0x88000000)
                    .show();



            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    WindowBuilder builder;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }
}
