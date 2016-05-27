package com.example.ceshi;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        showweb();
       // overridePendingTransition(enterAnim, exitAnim);
    }
    private void showweb()
    {
    	try
		{
			Intent intent_category = new Intent("com.wasutv.action.webbrowser");
			String link_url = "http://bs3-epg.wasu.tv/teleplay.shtml";
			intent_category.putExtra("Url", link_url);
			startActivity(intent_category);
		} catch (Exception e)
		{
			Utils.showToast(this, "播放失败", R.drawable.toast_smile);
			e.printStackTrace();
		}
    }
    public boolean isChinese() {  
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
		return true;
		else
		return false;
    }
    private ImageView whiteBorder;// 白色背景框
    /**
	 * 背景框平移
	 * @param paramInt1 边框的宽
	 * @param paramInt2 边框的高
	 * @param paramFloat1 边框相对左边的边距
	 * @param paramFloat2 边框相对上边的边距
	 */
	private void flyWhiteBorder(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
	  {
	    if (this.whiteBorder == null)
	      return;
	    int i = this.whiteBorder.getWidth();
	    int j = this.whiteBorder.getHeight();
	    ViewPropertyAnimator localViewPropertyAnimator1 = this.whiteBorder.animate();
	    localViewPropertyAnimator1.setDuration(150L);
	    float f1 = paramInt1;
	    float f2 = i;
	    float f3 = f1 / f2;
	    localViewPropertyAnimator1.scaleX(f3);
	    float f4 = paramInt2;
	    float f5 = j;
	    float f6 = f4 / f5;
	    localViewPropertyAnimator1.scaleY(f6);
	    localViewPropertyAnimator1.x(paramFloat1);
	    localViewPropertyAnimator1.y(paramFloat2);
	    localViewPropertyAnimator1.start();
	  }
}
