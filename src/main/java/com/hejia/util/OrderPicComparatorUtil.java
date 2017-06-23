package com.hejia.util;

import com.hejia.bean.PicFileBean;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * 文件名：OrderPicComparatorUtil
 * 作者：韩秋宇
 * 时间：2017/5/28
 * 功能描述：给图片文件按照后缀，和首字母排序
 */

public class OrderPicComparatorUtil implements Comparator<PicFileBean> {
    @Override
    public int compare(PicFileBean o1, PicFileBean o2) {
        Collator instance = Collator.getInstance(Locale.CHINA);
        int num;
        if (o1.getFilePath().contains("jpg") && (o2.getFilePath().contains("jpg"))) {
            num = instance.compare(o1.getFilePath(), o2.getFilePath());
            return num == 0 ? new Integer(o1.getFilePath()
                    .compareTo(o2.getFilePath())) : num;
        } else if (o1.getFilePath().contains("bmp") && (o2.getFilePath().contains("bmp"))) {
            num = instance.compare(o1.getFilePath(), o2.getFilePath());
            return num == 0 ? new Integer(o1.getFilePath()
                    .compareTo(o2.getFilePath())) : num;
        } else if (o1.getFilePath().contains("png") && (o2.getFilePath().contains("png"))) {
            num = instance.compare(o1.getFilePath(), o2.getFilePath());
            return num == 0 ? new Integer(o1.getFilePath()
                    .compareTo(o2.getFilePath())) : num;
        } else if(o1.getFilePath().contains("bmp") &&(o2.getFilePath().contains("png"))){
            return -1;
        }else if(o1.getFilePath().contains("bmp") &&(o2.getFilePath().contains("jpg"))){
            return -1;
        }else if(o1.getFilePath().contains("png") &&(o2.getFilePath().contains("jpg"))){
            return -1;
        }else {
            return 1;
        }

    }
}