package com.hejia.serialport;

import java.io.File;
import java.math.BigInteger;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.hejia.fragment.FragmentMedia;
import com.hejia.media.Media;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

public class DataConvert {

	//将字节数组转换为十六进制字符串
	public  StringBuffer getHexStringBuffer(byte[] b) throws Exception {
		StringBuffer result = new StringBuffer();
		for (int i=0; i < b.length; i++) {
			result =  result.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
		}
		return result;
	}

	//将十六进制字符串转换为字节数组
	public  byte[] getByteArray(String hexString) {
		return new BigInteger(hexString,16).toByteArray();
	}

	public Spanned addUnlineToString(String str)
	{
		Spanned value = Html.fromHtml("<u>"+str+"</u>");
		return value;
	}


	//将两个字节表示的数值转为整形
	public long getCheckData(byte a,byte b)
	{
		long mcheckdata = 0;
		if((a>=0)&&(b>=0))
		{
			mcheckdata =a*16*16+b;
		}
		else if((a>=0)&&(b<0))
		{
			mcheckdata = a*16*16+(b&0x0FF);
		}
		else if((a<0)&&(b>=0))
		{
			mcheckdata = (a&0x0FF)*16*16+b;
		}
		else
		{
			mcheckdata = (a&0x0FF)*16*16+(b&0x0FF);
		}
		return mcheckdata;
	}

	/**

	 　　* @param start

	 　　* @param end

	 　　* @return >= start && <=end 的随机数

	 　　* end需大于start 否则返回-1

	 　　*/

	public  int getRandom(int start,int end)
	{

		if(start>end || start <0 || end <0){

			return -1;
		}

		return (int)(Math.random()*(end-start+1))+start;

	}

	//判断一个字符串是否含有数组里任何一个元素
	public Boolean isContains(String[] arrayStr, String str) {
		if(arrayStr==null)
		{
			return false;
		}
		for (int i = 0; i < arrayStr.length; i++) {
			if (str.contains(arrayStr[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	private int compare(String str, String target)
	{
		int d[][];              // 矩阵
		int n = str.length();
		int m = target.length();
		int i;                  // 遍历str的
		int j;                  // 遍历target的
		char ch1;               // str的
		char ch2;               // target的
		int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		if (n == 0) { return m; }
		if (m == 0) { return n; }
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++)
		{                       // 初始化第一列
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++)
		{                       // 初始化第一行
			d[0][j] = j;
		}

		for (i = 1; i <= n; i++)
		{                       // 遍历str
			ch1 = str.charAt(i - 1);
			// 去匹配target
			for (j = 1; j <= m; j++)
			{
				ch2 = target.charAt(j - 1);
				if (ch1 == ch2 || ch1 == ch2+32 || ch1+32 == ch2)
				{
					temp = 0;
				} else
				{
					temp = 1;
				}
				// 左边+1,上边+1, 左上角+temp取最小
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
			}
		}
		return d[n][m];
	}

	private int min(int one, int two, int three)
	{
		return (one = one < two ? one : two) < three ? one : three;
	}

	/**
	 * 获取两字符串的相似度
	 */

	public float getSimilarityRadio(String str, String target)
	{
		return 1 - (float) compare(str, target) / Math.max(str.length(), target.length());
	}

}

