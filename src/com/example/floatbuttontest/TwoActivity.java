package com.example.floatbuttontest;

import android.os.Bundle;

public class TwoActivity extends MainActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new FloatButton(this);
		new FloatButton(this);
		new FloatButton(this);
		new FloatButton(this);
		new FloatButton(this);
		new FloatButton(this);
	}
}
