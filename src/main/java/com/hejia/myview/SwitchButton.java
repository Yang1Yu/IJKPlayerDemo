package com.hejia.myview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.hejia.tp_launcher_v3.R;

public class SwitchButton extends View implements OnClickListener {
	private Bitmap mSwitchTrack, mSwitchThumb, mSwitchMask;
	private float mCurrentX = 0;// 当前的x坐标
	private boolean mSwitchOn = true;// 开关默认是开着的
	private int mMoveLength;// 最大移动距离
	private float mLastX = 0;// 第一次按下的有效区域

	private Rect mDest = null;// 绘制的目标区域大小
	private Rect mSrc = null;// 截取源图片的大小
	private int mDeltX = 0;// 移动的偏移量

	private int width, height;

	private Paint mPaint = null;
	private OnCheckedChangeListener mListener = null;
	private boolean mFlag = false;

	private float scaleWidth, scaleHeight;// 图片缩放的比例

	public SwitchButton(Context context) {
		this(context, null);
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	/**
	 * 初始化相关资源
	 */
	private void init(Context context, AttributeSet attrs) {
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);

		width = (int) mTypedArray.getDimension(R.styleable.SwitchButton_android_layout_width, 100);
		height = (int) mTypedArray.getDimension(R.styleable.SwitchButton_android_layout_height, 43);

		// 遮罩
		mSwitchMask = getSrcBitmap(mTypedArray, R.styleable.SwitchButton_mask);
		// 背景(轨道的背景)
		mSwitchTrack = getSrcBitmap(mTypedArray, R.styleable.SwitchButton_android_track);
		// 滑块
		mSwitchThumb = getSrcBitmap(mTypedArray, R.styleable.SwitchButton_android_thumb);

		// 计算缩放的比例
		scaleWidth = ((float) width / mSwitchMask.getWidth());
		scaleHeight = ((float) height / mSwitchMask.getHeight());

		mSwitchMask = getScaleBitmap(mSwitchMask);
		mSwitchTrack = getScaleBitmap(mSwitchTrack);
		mSwitchThumb = getScaleBitmap(mSwitchThumb);

		setOnClickListener(this);
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		// 可以移动的长度
		mMoveLength = mSwitchTrack.getWidth() - mSwitchMask.getWidth();
		// 创建一个矩形对象(目标区域的大小)，通过使用四个整数来初始化矩形左上角的横坐标、纵坐标以及矩形的高度、宽度
		mDest = new Rect(0, 0, mSwitchMask.getWidth(), mSwitchMask.getHeight());
		// 创建一个矩形对象，矩形左上角的横坐标、纵坐标以及矩形的宽度、高度均为零。这是默认的构造函数
		mSrc = new Rect();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);// 消除锯齿
		mPaint.setAlpha(255);// 设置透明度
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	}

	// 从资源中获取图片对象
	private Bitmap getSrcBitmap(TypedArray mTypedArray, int index) {
		BitmapDrawable tempBit;
		tempBit = (BitmapDrawable) mTypedArray.getDrawable(index);
		return tempBit.getBitmap();
	}

	// 将原图，按照布局文件中的控件长度来缩放
	private Bitmap getScaleBitmap(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		if (isInEditMode()) {
			return bitmap;
		}
		matrix.postScale(scaleWidth, scaleHeight);
		// bitmap =Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
		// bitmap.getHeight(), matrix, true) ;
		// return bitmap;
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	// 该方法指定该控件在屏幕上的大小,这里要计算一下控件的实际大小，然后调用setMeasuredDimension来设置
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mDeltX > 0 || mDeltX == 0 && mSwitchOn) {
			if (mSrc != null) {
				mSrc.set(mMoveLength - mDeltX, 0, mSwitchTrack.getWidth() - mDeltX, mSwitchMask.getHeight());
			}
		} else if (mDeltX < 0 || mDeltX == 0 && !mSwitchOn) {
			if (mSrc != null) {
				mSrc.set(-mDeltX, 0, mSwitchMask.getWidth() - mDeltX, mSwitchMask.getHeight());
			}
		}

		// 离屏缓冲，类似双缓冲机制吧
		int count = canvas.saveLayer(new RectF(mDest), null,
				Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
						| Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);

		canvas.drawBitmap(mSwitchTrack, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchTrack, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchMask, 0, 0, mPaint);
		canvas.drawBitmap(mSwitchThumb, mSrc, mDest, null);
		// 控件可以在布局文件中预览
		if (isInEditMode()) {
			return;
		}
		canvas.restoreToCount(count);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			mCurrentX = event.getX();
			mDeltX = (int) (mCurrentX - mLastX);
			// 如果开关开着向左滑动，或者开关关着向右滑动（这时候是不需要处理的）
			if ((mSwitchOn && mDeltX < 0) || (!mSwitchOn && mDeltX > 0)) {
				mFlag = true;
				mDeltX = 0;
			}

			if (Math.abs(mDeltX) > mMoveLength) {
				mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength;
			}
			invalidate();
			return true;
		case MotionEvent.ACTION_UP:
			if (Math.abs(mDeltX) > 0 && Math.abs(mDeltX) < mMoveLength / 2) {
				mDeltX = 0;
				invalidate();
				return true;
			} else if (Math.abs(mDeltX) > mMoveLength / 2 && Math.abs(mDeltX) <= mMoveLength) {
				mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength;
				mSwitchOn = !mSwitchOn;
				if (mListener != null) {
					mListener.onCheckedChanged(this, mSwitchOn);
				}
				invalidate();
				mDeltX = 0;
				return true;
			} else if (mDeltX == 0 && mFlag) {
				// 这时候得到的是不需要进行处理的，因为已经move过了
				mDeltX = 0;
				mFlag = false;
				return true;
			}
			return super.onTouchEvent(event);
		default:
			break;
		}
		invalidate();
		return super.onTouchEvent(event);
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mListener = listener;
	}

	public interface OnCheckedChangeListener {
		public void onCheckedChanged(SwitchButton button, boolean isChecked);
	}

	@Override
	public void onClick(View v) {
		mDeltX = mSwitchOn ? mMoveLength : -mMoveLength;
		mSwitchOn = !mSwitchOn;
		if (mListener != null) {
			mListener.onCheckedChanged(this, mSwitchOn);
		}
		invalidate();// 重绘
		mDeltX = 0;
	}

	public Boolean isChecked() {
		return mSwitchOn;
	}

	public void setChecked(Boolean checked) {
		mSwitchOn = checked;
		invalidate();
	}

}
