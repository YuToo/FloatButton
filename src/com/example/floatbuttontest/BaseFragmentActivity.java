package com.example.floatbuttontest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;

public abstract class BaseFragmentActivity extends FragmentActivity {
	private View mFloatButton;// 浮动按钮
	private Animation mAnim;// 浮动按钮隐藏动画
	private WindowManager mWm;// 窗口管理器
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mFloatButton = getFloatButton();
	}

	public View getFloatButton() {
		return null;
	}
	
	
}
