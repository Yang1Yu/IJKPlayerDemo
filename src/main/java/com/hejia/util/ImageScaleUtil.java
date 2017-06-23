package com.hejia.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 文件名：ImageScaleUtil
 * 作者：韩秋宇
 * 时间：2017/5/28
 * 功能描述：压缩图片文件的工具
 */


public class ImageScaleUtil {

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * 根据文件的路径和宽高尺寸来压缩图片文件
     *
     * @param imagePath
     */
    public static Bitmap ImageSrc(String imagePath , int maxWidth , int maxHeight) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = getImageScale(imagePath,maxWidth,maxHeight);
        Bitmap bm = BitmapFactory.decodeFile(imagePath, option);
        return bm;
    }


    /**
     * 根据文件的路径和宽高尺寸来确定文件的压缩倍数
     *
     * @param imagePath
     * @return
     */
    private static int getImageScale(String imagePath , int maxWidth ,int maxHeight) {
        BitmapFactory.Options option = new BitmapFactory.Options();

        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, option);

        int scale = 1;
        while (option.outWidth / scale >= maxWidth || option.outHeight / scale >= maxHeight) {
            scale *= 3;
        }
        return scale;
    }


}
