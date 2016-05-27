package com.example.ceshi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ceshi.R;

public class Utils
{

	/**
	 * 安装apk文件
	 * 
	 * @author drowtram
	 * @param fileName
	 */
	public static void installApk(Context context, String fileName)
	{
		if (getUninatllApkInfo(context, fileName))
		{
			File updateFile = new File(fileName);
			try
			{
				String[] args2 = { "chmod", "604", updateFile.getPath() };
				Runtime.getRuntime().exec(args2);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			/*------------------------*/
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(updateFile), "application/vnd.android.package-archive");
			context.startActivity(intent);
			// File file = new File(fileName);
			// Intent intent = new Intent();
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// intent.setAction(Intent.ACTION_VIEW); //浏览网页的Action(动作)
			// String type = "application/vnd.android.package-archive";
			// intent.setDataAndType(Uri.fromFile(file), type); //设置数据类型
			// context.startActivity(intent);
		} else
		{
			Toast.makeText(context, "文件还没下载完成，请耐心等待。", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 判断apk文件是否可以安装
	 * 
	 * @param context
	 * @param filePath
	 * @return
	 */
	public static boolean getUninatllApkInfo(Context context, String filePath)
	{
		boolean result = false;
		try
		{
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
			if (info != null)
			{
				result = true;
			}
		} catch (Exception e)
		{
			result = false;
			Log.e("zhouchuan", "*****  解析未安装的 apk 出现异常 *****" + e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 下载apk文件进行安装
	 * @author drowtram
	 * @param context
	 * @param mHandler 更新显示进度的handler
	 * @param url
	 */
	public static void startDownloadApk(final Context context, final String url, final Handler mHandler){
		showToast(context, "正在后台下载，完成后提示安装...", R.drawable.toast_smile);
		new Thread(new Runnable() {
			@Override
			public void run() {
//				File file = new File(Constant.PUBLIC_DIR);
				String apkName = url.substring(url.lastIndexOf("/")+1);
//				File file = new File(context.getCacheDir(),apkName);
//				if(!file.exists()){
//					file.mkdirs();
//				}
				FileOutputStream fos = null;
				try {
					HttpGet hGet = new HttpGet(url.replaceAll(" ", "%20"));//替换掉空格字符串，不然下载不成功
					HttpResponse hResponse = new DefaultHttpClient().execute(hGet);
					if(hResponse.getStatusLine().getStatusCode() == 200){
						InputStream is = hResponse.getEntity().getContent();
						float downsize = 0;
						if(mHandler != null) {
							//获取下载的文件大小
							float size = hResponse.getEntity().getContentLength();
							mHandler.obtainMessage(1001, size).sendToTarget();//发消息给handler处理更新信息
						}
						fos = context.openFileOutput(apkName, Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
						byte[] buffer = new byte[8192];
						int count = 0;
						while ((count = is.read(buffer)) != -1) {
							if(mHandler != null) {
								downsize += count;
								mHandler.obtainMessage(1002, downsize).sendToTarget();//发消息给handler处理更新信息
							}
							fos.write(buffer, 0, count);
//							Log.d("zhouchuan", "下载进度"+(int)(downsize/size*100)+"%"+" downsize="+downsize+" size="+size);
					    }
						fos.close();
						is.close();
						installApk(context,"/data/data/com.shenma.tvlauncher/files/" + apkName);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	private static long start = 0;
	private static String mtext = "";
	/**
	 * 自定义Toast
	 * @param context
	 * @param text
	 * @param image
	 */
	public static void showToast(Context context,String text,int image){
		Long end = java.lang.System.currentTimeMillis();;
		if(end-start<1000 && text.equals(mtext)){
			return;
		}else if(end-start<2000 && text.equals(mtext)){
			View view = LayoutInflater.from(context).inflate(R.layout.tv_toast, null);
			TextView tv_toast = (TextView) view.findViewById(R.id.tv_smtv_toast);
			ImageView iv_toast = (ImageView) view.findViewById(R.id.iv_smtv_toast);
			tv_toast.setText("你也太无聊了吧...");
			iv_toast.setBackgroundResource(image);
			Toast toast = new Toast(context);
			toast.setView(view);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
			start = end;
		}else{
			start = end;
			View view = LayoutInflater.from(context).inflate(R.layout.tv_toast, null);
			TextView tv_toast = (TextView) view.findViewById(R.id.tv_smtv_toast);
			ImageView iv_toast = (ImageView) view.findViewById(R.id.iv_smtv_toast);
			tv_toast.setText(text);
			iv_toast.setBackgroundResource(image);
			Toast toast = new Toast(context);
			toast.setView(view);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		}
		mtext = text;
	}
	
	private static Toast toast;
	/**
	 * 显示土司
	 * fix the toast Repeat display by zhouchuan 
	 * @param context
	 * @param text
	 * @param image
	 */
	public static void showToast(String text,Context context,int image){
		View view = null;
		if(toast == null) {
			toast = new Toast(context);
			view = LayoutInflater.from(context).inflate(R.layout.tv_toast, null);
		} else {
			view = toast.getView();
		}
		TextView tv_toast = (TextView) view.findViewById(R.id.tv_smtv_toast);
		ImageView iv_toast = (ImageView) view.findViewById(R.id.iv_smtv_toast);
		tv_toast.setText(text);
		iv_toast.setBackgroundResource(image);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
	
	/**
	 * 显示土司
	 * fix the toast Repeat display by zhouchuan 
	 * @param context
	 * @param text
	 * @param image
	 */
	public static void showToast(Context context,int text,int image){
		View view = null;
		if(toast == null) {
			toast = new Toast(context);
			view = LayoutInflater.from(context).inflate(R.layout.tv_toast, null);
		} else {
			view = toast.getView();
		}
		TextView tv_toast = (TextView) view.findViewById(R.id.tv_smtv_toast);
		ImageView iv_toast = (ImageView) view.findViewById(R.id.iv_smtv_toast);
		tv_toast.setText(text);
		iv_toast.setBackgroundResource(image);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
}
