package com.hejia.speech;
/**
 * 数据格式化
 *
 */
public class IatDataFormat {

    //收音机频道过滤
    public String ToFreqFormat(String source) {
        // 1,去除不相干的文字信息
        char[] pt = source.toCharArray();
        StringBuilder strB = new StringBuilder();
        for (int i = 0; i < pt.length; i++) {
            char k = pt[i];
            if (k == '点' || k == '零' || k == '幺' || k == '一' || k == '二' || k == '三' || k == '四'
                    || k == '五' || k == '六' || k == '七' || k == '八' || k == '九') {

                switch (k) {
                    case '零':
                        k = '0';
                        break;
                    case '一':
                        k = '1';
                    case '幺':
                        k = '1';
                        break;
                    case '二':
                        k = '2';
                        break;
                    case '三':
                        k = '3';
                        break;
                    case '四':
                        k = '4';
                        break;
                    case '五':
                        k = '5';
                        break;
                    case '六':
                        k = '6';
                        break;
                    case '七':
                        k = '7';
                        break;
                    case '八':
                        k = '8';
                        break;
                    case '九':
                        k = '9';
                        break;
                    case '点':
                        k = '.';
                        break;
                }
                strB.append(k);
            }
            else if((k>='0'&&k<='9')||(k=='.'))
            {
                strB.append(k);
            }
        }


        return strB.toString();
    }


}
