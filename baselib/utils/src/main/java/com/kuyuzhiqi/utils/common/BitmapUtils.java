package com.kuyuzhiqi.utils.common;

import android.content.res.Resources;
import android.graphics.*;
import android.view.View;
import android.widget.ScrollView;

import java.io.*;

/**
 * Bitmap相关的工具类
 * @author chengkai
 *
 */
// TODO: 2017/8/21 已经移动到module_util
public class BitmapUtils {

	private BitmapUtils() {
	}

	/**
	 * 获取圆形Bitmap
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap bitmap) {
		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final int color = Color.RED;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		bitmap.recycle();

		return output;
	}

	/**
	 * 将图片覆盖保存
	 */
	public static boolean saveBitmapToFile(Bitmap bm, String savePath) {
		if (bm == null)
			return false;
		InputStream is = null;
		FileOutputStream outputStream = null;
		ByteArrayOutputStream baos = null;
		try {
			File file = new File(savePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
			is = new ByteArrayInputStream(baos.toByteArray());
			outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while (len != -1) {
				len = is.read(buffer);
				if (len > 0) {
					outputStream.write(buffer, 0, len);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				baos = null;
			}

			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				is = null;
			}

			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				outputStream = null;
			}
		}
		return true;
	}
	
	/*
	 * 截取scrollview的屏幕
	 */
	public static Bitmap getBitmapFromScrollView(ScrollView scrollView) {
		int h = 0;
		Bitmap bitmap = null;
		// 获取scrollview实际高度
		for (int i = 0; i < scrollView.getChildCount(); i++) {
			h += scrollView.getChildAt(i).getHeight();
			scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
		}
		// 创建对应大小的bitmap
		bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
				Bitmap.Config.RGB_565);
		final Canvas canvas = new Canvas(bitmap);
		scrollView.draw(canvas);
		return bitmap;
	}
	
	/**
	 * 获取view上面的内容
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view){
		Bitmap bitmap=null;
        // 设置是否可以进行绘图缓存  
        view.setDrawingCacheEnabled(true); 
        // 清空缓存
        view.destroyDrawingCache();
        // 如果绘图缓存无法，强制构建绘图缓存  
        view.buildDrawingCache();  
        // 返回这个缓存视图   
        bitmap=view.getDrawingCache();  
        int width = view.getWidth();  
        int height = view.getHeight();  
        // 根据坐标点和需要的宽和高创建bitmap  
        bitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height);
        return bitmap;
	}
	
	/**
	 * 回收Bitmap资源
	 * @param bitmap
	 */
	public static void recycleBitmap(Bitmap bitmap){
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		} 
	}

	/**
	 * 缩放Bitmap到指定大小
	 * @param source
	 * @param reqWidth
	 * @param reqHeight
     * @return
     */
	public static Bitmap createScaledBitmap(Bitmap source, int reqWidth, int reqHeight) {
		if (reqWidth > 0 && reqHeight > 0) {
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, reqWidth, reqHeight, false);
			if (scaledBitmap != source) {
				recycleBitmap(source);
			}
			return scaledBitmap;
		}
		return source;
	}

	/**
	 * 从File中获Bitmap,并缩放至指定大小
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeScaledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
		
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    // 缩放至指定大小
    	Bitmap tempBitmap = BitmapFactory.decodeFile(pathName, options);
		float pScaleX = (float) reqWidth / tempBitmap.getWidth();
		float pScaleY = (float) reqHeight / tempBitmap.getHeight();
		//获取较小的放大倍数
		float pMinScale = pScaleX < pScaleY ? pScaleX : pScaleY;
		//
		Bitmap resultBitmap = null;
		resultBitmap = Bitmap.createScaledBitmap(tempBitmap, (int)(pMinScale * tempBitmap.getWidth()), (int)(pMinScale * tempBitmap.getHeight()), false);
		//如果bitmap大小改变,需要释放资源
		if(tempBitmap != null && tempBitmap.getWidth() == resultBitmap.getWidth() && tempBitmap.getHeight() == resultBitmap.getHeight()){
			tempBitmap.recycle();
			tempBitmap = null;
		} 
    	return resultBitmap;
	}

	/**
	 * 根据给出的长宽,从File中获取合适大小的Bitmap
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @param inPreferredConfig 图片质量格式,参见 Bitmap.Config
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight , Bitmap.Config inPreferredConfig ) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    //set the config
	    options.inPreferredConfig = inPreferredConfig;
	    
	    return BitmapFactory.decodeFile(pathName, options);
	}
	
	/**
	 * 根据给出的长宽,从File中获取合适大小的Bitmap
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(pathName, options);
	}
	
	/**
	 * 根据给出的长宽,从Resource中获取合适大小的Bitmap
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	/**
	 * 根据给出的长,宽,计算出合适的inSampleSize
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;//设置inSampleSize为2的幂是因为decoder最终还是会对非2的幂的数进行向下处理，获取到最靠近2的幂的数。
			}
		}
		return inSampleSize;
	}

	/**
	 * 获取图片的原始大小
	 * @param imagePath
	 * @return
	 */
	public static Point getImageRawSize(String imagePath){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);
		return new Point(options.outWidth, options.outHeight);
	}

	public static void closeBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	/**
	 * 将图片按照指定的角度进行旋转
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
		if (bitmap == null)
			return null;
		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		if (newBitmap != bitmap) {
			BitmapUtils.closeBitmap(bitmap);
		}
		return newBitmap;
	}
}
