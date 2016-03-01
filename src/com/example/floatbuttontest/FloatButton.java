package com.example.floatbuttontest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

/**
 * 浮动按钮工具类
 * 
 * @author YuToo
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FloatButton {
	private View mFloatButton;// 浮动按钮
	private Animator mAnim;// 浮动按钮隐藏动画
	private WindowManager mWm;// 窗口管理器
	private Context context;
	private LayoutParams params;
	private OnClickListener listener;
	private SharedPreferences sp;
	private boolean isShow = false;

	private int x, y;

	// 动画相关
	private boolean isShowAnim = true;// 是否显示懂画
	private float mFinish = 0.3f;// 最后 透明度
	private long duration = 2000;// 动画时长

	public FloatButton(Context context) {
		this.context = context;
		init();
	}

	/**
	 * 需要回显和保存按钮位置的初始化方法
	 * 
	 * @param context
	 * @param sp_name
	 *            配置信息文件的名称，
	 */
	public FloatButton(Context context, String sp_name) {
		this.context = context;
		sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
		init();
	}

	/**
	 * 
	 * @param context
	 * @param sp_name
	 *            配置信息文件的名称，
	 * @param showAnim
	 *            是否显示透明动画
	 */
	public FloatButton(Context context, String sp_name, boolean showAnim) {
		this.context = context;
		sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
		this.isShowAnim = showAnim;
		init();
	}

	private void init() {
		mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		// 初始化需要添加view的布局参数
		params = new LayoutParams();
		params.type = LayoutParams.TYPE_APPLICATION;
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL;
		params.format = PixelFormat.TRANSLUCENT;
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.TOP | Gravity.LEFT;

		// 默认展示的图片
		ShapeDrawable sd = new ShapeDrawable();
		sd.setShape(new OvalShape());
		sd.setColorFilter(0xffff0000, Mode.SCREEN);
		sd.setIntrinsicHeight(60);
		sd.setIntrinsicWidth(60);
		Drawable bg = sd.getCurrent();

		TextView tv = new TextView(context);
		tv.setText("Home");
		tv.setBackgroundDrawable(bg);
		tv.setTextSize(50);
		tv.setTextColor(Color.WHITE);
		mFloatButton = tv;

	}

	private void showLastPlace() {
		params.x = x;
		params.y = y;
		if (sp != null) {
			params.x = sp.getInt("x", x);
			params.y = sp.getInt("y", y);
		}
	}

	private void initAnim() {
		if (!isShowAnim) {
			return;
		}
		mAnim = ValueAnimator.ofFloat(1.0f, mFinish);
		mAnim.setDuration(duration);
		((ValueAnimator) mAnim).addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				if (isShow) {// 如果现在状态是显示状态则更新状态，
					float value = (float) animation.getAnimatedValue();
					params.alpha = value;
					mWm.updateViewLayout(mFloatButton, params);
				}
			}
		});
		mAnim.start();
	}

	private void initEvent() {
		// 拖动按钮的监听器
		mFloatButton.setOnTouchListener(new OnTouchListener() {
			private int offX, offY;
			private int startX, startY;
			private int leftMargin, topMargin;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					offX = (int) (startX - params.x);
					offY = (int) (startY - params.y);
					// 不透明
					if (mAnim != null) {
						mAnim.cancel();
					}
					params.alpha = 1.0f;
					mWm.updateViewLayout(mFloatButton, params);
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
					mWm.updateViewLayout(mFloatButton, params);
					break;
				case MotionEvent.ACTION_UP:
					if (mAnim != null) {
						mAnim.start();
					}
					if (sp != null) {
						Editor edit = sp.edit();
						edit.putInt("x", params.x);
						edit.putInt("y", params.y);
						edit.commit();
					}

					if (Math.abs(startX - event.getRawX()) > 10 || Math.abs(startY - event.getRawY()) > 10) {
						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});

		mFloatButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(v);
				}
			}
		});
	}

	private void setWidthAndHeight(int width, int height) {
		params.width = width;
		params.height = height;
	}

	public void setOnClickListener(OnClickListener listener) {
		this.listener = listener;
	}

	public void setContentView(View view) {
		mFloatButton = view;
	}

	public void setContentView(int resource) {
		mFloatButton = View.inflate(context, resource, null);
	}

	public void setContentView(View view, int width, int height) {
		setContentView(view);
		setWidthAndHeight(width, height);
	}

	public void setContentView(int resource, int width, int height) {
		setContentView(resource);
		setWidthAndHeight(width, height);
	}

	/** 是否显示动画 */
	public void setHasAnimation(boolean isShow) {
		this.isShowAnim = isShow;
	}

	/** 设置初始位置 */
	public void setStartPlace(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/** 设置动画展示时长 */
	public void setAnimDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * 设置动画结束时的透明度 范围：1.0-0.0 1.0：完全不透明 0.0：完全透明
	 */
	public void setFinishAlpha(float alpha) {
		mFinish = alpha;
	}

	public void show() {
		if (!isShow) {
			// 回显位置
			showLastPlace();
			mWm.addView(mFloatButton, params);
			initAnim();
			initEvent();
			isShow = true;
		}
	}

	public void dismiss() {
		if (isShow) {
			mWm.removeView(mFloatButton);
			isShow = false;
		}
	}

}
