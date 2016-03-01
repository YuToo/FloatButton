package com.example.floatbuttontest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnTouchListener  {
	private TextView tv;
	LayoutParams params;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final FloatButton button = new FloatButton(this,"config");
		button.setFinishAlpha(0.4f);
		button.setAnimDuration(4000);
		ImageView iv = new ImageView(this);
		iv.setImageResource(R.drawable.ic_launcher);
		button.setContentView(iv);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				button.dismiss();
				Toast.makeText(MainActivity.this, "...", 0).show();
			}
		});
		button.setStartPlace(100, 400);
		button.show();
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//		params = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params = new LayoutParams();
		
		
		
//		
//		params.type = LayoutParams.TYPE_APPLICATION;// 所有程序窗口的“基地”窗口，其他应用程序窗口都显示在它上面。  
//        params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL  ;
//        params.format = PixelFormat.TRANSLUCENT;// 不设置这个弹出框的透明遮罩显示为黑色  
//        params.gravity = Gravity.TOP | Gravity.LEFT;  
		params.type = LayoutParams.TYPE_APPLICATION;
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL;
		params.format = PixelFormat.TRANSLUCENT;
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.TOP | Gravity.LEFT;
		
		
		tv = new TextView(this);
		tv.setFocusable(true);
		tv.setClickable(true);
		tv.setText("hahha");
		tv.setTextSize(60);
		tv.setOnTouchListener(this);
		
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, TwoActivity.class));
			}
		});
		
		
		wm.addView(tv, params);
	}


	private int offX, offY;
	private int startX, startY;
	private int leftMargin, topMargin;
	private WindowManager wm;
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) event.getRawX();
			startY = (int) event.getRawY();
			offX = (int) (startX - params.x);
			offY = (int) (startY - params.y);
			// 不透明
			tv.clearAnimation();
			break;
		case MotionEvent.ACTION_MOVE:
			leftMargin = (int) event.getRawX() - offX;
			topMargin = (int) event.getRawY() - offY;
			if (leftMargin < 0) {
				leftMargin = 0;
			}
			if (topMargin < 0) {
				topMargin = 0;
			}
			params.y = topMargin;
			params.x = leftMargin;
			wm.updateViewLayout(tv, params);
			break;
		case MotionEvent.ACTION_UP:



			if (Math.abs(startX - event.getRawX()) > 10 || Math.abs(startY - event.getRawY()) > 10) {
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
}
