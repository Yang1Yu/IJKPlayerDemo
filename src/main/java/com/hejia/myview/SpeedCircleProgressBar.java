package com.hejia.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class SpeedCircleProgressBar extends View {
	// 圆弧线宽
	private float circleBorderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
	// 内边距
	private float circlePadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
	// 字体大小
	private float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 00, getResources().getDisplayMetrics());
	// 绘制圆周画笔
	private Paint backCirclePaint;
	// 圆周白色分割线画笔
	private Paint linePaint;
	// 绘制文字
	private Paint textPaint;
	// 百分比
	private int percent = 0;
	// 渐变颜色
	private int[] gradientColorArray = new int[]{Color.GREEN, Color.parseColor("#fe751a"), Color.parseColor("#13be23"), Color.GREEN};
	private Paint gradientCirclePaint;

	public SpeedCircleProgressBar(Context context) {
		super(context);
		init();
	}

	public SpeedCircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SpeedCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		backCirclePaint = new Paint();
		backCirclePaint.setStyle(Paint.Style.STROKE);
		backCirclePaint.setAntiAlias(true);
		backCirclePaint.setColor(Color.LTGRAY);
		backCirclePaint.setStrokeWidth(circleBorderWidth);
//        backCirclePaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.OUTER));

		gradientCirclePaint = new Paint();
		gradientCirclePaint.setStyle(Paint.Style.STROKE);
		gradientCirclePaint.setAntiAlias(true);
		gradientCirclePaint.setColor(Color.LTGRAY);
		gradientCirclePaint.setStrokeWidth(circleBorderWidth);

		linePaint = new Paint();
		linePaint.setColor(Color.WHITE);
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(1);

		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(textSize);
		textPaint.setColor(Color.BLACK);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(Math.min(measureWidth, measureHeight), Math.min(measureWidth, measureHeight));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 颜色渐变圆环
		LinearGradient linearGradient = new LinearGradient(circlePadding, circlePadding,
				getMeasuredWidth() - circlePadding,
				getMeasuredHeight() - circlePadding,
				gradientColorArray, null, Shader.TileMode.MIRROR);
		gradientCirclePaint.setShader(linearGradient);
		gradientCirclePaint.setShadowLayer(10, 10, 10, Color.RED);
		canvas.drawArc(
				new RectF(circlePadding * 2, circlePadding * 2,
						getMeasuredWidth() - circlePadding * 2, getMeasuredHeight() - circlePadding * 2), -90, 360, false, gradientCirclePaint);
		// 灰色背景圆环
		canvas.drawArc(
				new RectF(circlePadding * 2, circlePadding * 2,
						getMeasuredWidth() - circlePadding * 2, getMeasuredHeight() - circlePadding * 2), -223, (float) (percent / 100.0) * 360, false, backCirclePaint);

		// 半径
		float radius = (getMeasuredWidth() - circlePadding * 3) / 2;
		// x轴中坐标
		int centerX = getMeasuredWidth() / 2;

		// 切分圆弧线段
		for (float i = 0; i < 360; i += 18) {
			double rad = i * Math.PI / 180;
			float startX = (float) (centerX + (radius - circleBorderWidth) * Math.sin(rad));
			float startY = (float) (centerX + (radius - circleBorderWidth) * Math.cos(rad));

			float stopX = (float) (centerX + radius * Math.sin(rad) + 1);
			float stopY = (float) (centerX + radius * Math.cos(rad) + 1);

			canvas.drawLine(startX, startY, stopX, stopY, linePaint);
		}

		// 绘制文字
		float textWidth = textPaint.measureText(percent + "%");
		int textHeight = (int) (Math.ceil(textPaint.getFontMetrics().descent - textPaint.getFontMetrics().ascent) + 2);
		canvas.drawText(percent + "%", centerX - textWidth / 2, centerX + textHeight / 4, textPaint);
	}

	// 外留方法 传入当前速度
	public void setSpeed(int speed) {
		if (speed < 0) {
			speed = 240;
		} else if (speed > 240) {
			speed = 0;
		} else {
			speed = 240 - speed;
		}
		this.percent = (speed * 77) / 240 + 23;
		invalidate();
	}
}
