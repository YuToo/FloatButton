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

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FloatButton fb = new FloatButton(this,"config");//创建浮动按钮对象
		//设置初始化参数
		fb.setAnimDuration(4000);//设置动画时长
		fb.setFinishAlpha(0.4f);//设置最终透明度
		fb.setHasAnimation(true);//是否展示动画
		fb.setStartPlace(100, 100);//初始化位置
		fb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "单击了。。。", Toast.LENGTH_SHORT).show();
			}
		});
		fb.show();//展示
	}
}
