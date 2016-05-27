package com.example.ceshi;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyOnFocusAnimListener implements AnimationListener
{

	private int paramInt;
	private ImageView[] rebgs;
	private TextView[] tvs;
	public MyOnFocusAnimListener(int paramInt,ImageView[] rebg,TextView[] tvs) {
		this.paramInt = paramInt;
		this.rebgs = rebg;
		this.tvs = tvs;
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		rebgs[paramInt].setVisibility(View.VISIBLE);
		// Animation localAnimation =animEffect
		// .alphaAnimation(0.0F, 1.0F, 150L, 0L);
		// localImageView.startAnimation(localAnimation);
		if (paramInt >= 0) {
			tvs[paramInt].setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

}
