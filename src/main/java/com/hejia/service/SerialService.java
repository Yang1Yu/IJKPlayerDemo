package com.hejia.service;

import android.util.Log;

import java.io.UnsupportedEncodingException;

public class SerialService {
    private static final String TAG = "SerialService";

    // 加载本地库，__open,__close,__recv()和__send()的实现通过JNI在本地代码实现
    static {
//        Log.d(TAG, "System.loadLibrary()");
        System.loadLibrary("serial_runtime");
    }

    // wyn构造方法，为开启SerialService类作标记
    // 构造器，用于打开串口
    public SerialService() {
        Log.d(TAG, "SerialService()");
        // 如果需要一些初始化，可以在这里实现
    }

    // java 中没有析构函数,目前会在hal层uart_device_open打开设备的时候有一个84字节的内存在进程没退出前一直占用着
    // serial_service_exit函数是实现释放84字节资源的，只有在正常库被去加载的时候才会调用
    // public~SerialService(){ Log.d(TAG, "~SerialService()"); _exit(); }
    // wyn对外程序保留的打开接口
    public int open(String path, int baudrate) {
        Log.d(TAG, "open()");
        int fd;

        fd = __open(path, baudrate);
        // wyn判断fd，确定是否打开
        if (fd < 0) {
            Log.e(TAG, "open() error");
            return -1;
        }
        return fd;
    }

    // 关闭串口，本应该在析构函数中实现，由于java中没有析构函数，单独实现
    public int close(int fd) {
        Log.d(TAG, "close()");
        int ret;
        // wyn判断fd，确定是否蓝牙设备已开启
        if (fd < 0) {
            Log.d(TAG, "close() error fd is not open");
            return -1;
        }
        ret = __close(fd);
        return ret;
    }

    public String recv(int fd, int size) {
        int iRet;
        // Log.d(TAG, "recv()");
        if (fd < 0)
            return "fd is not right";

        if (size > 128)
            return "recv size is too big";
        // 由于 Java String是双字节字符，而串口数据是原始字节流，所以要进行转化
        byte[] data = new byte[128];
        // 从驱动读取字节流
        iRet = __recv(fd, data, 128, 0);
        if (iRet < 0) {
            // Log.e(TAG, "暂无命令接收");
            return "";
        }

        String ret;
        try {
            // 转化为Java String字符
            ret = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            return null;
        }
        return ret;
    }

    public int send(int fd, String str) {
        // Log.d(TAG, "send()");
        int len;
        if (fd < 0) {
            Log.d(TAG, "send() error");
            return -1;
        }
        try {
            // 将Java String字符串转码成字节流后，写入串口
            len = __send(fd, str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            return -1;
        }
        return len;
    }

    // 串口接收
    public byte[] recvByte(int fd, int size) {
        int iRet;
        // Log.d(TAG, "recv()");
        if (fd < 0) {
            Log.e("SerialService", "fd is not right");
        }

        // 由于 Java String是双字节字符，而串口数据是原始字节流，所以要进行转化
        byte[] data = new byte[1024];
        // 从驱动读取字节流
        iRet = __recv(fd, data, 1024, 1);
        SerialPortReadService.receBufferSize = iRet;

        if (iRet < 0) {
            // Log.e(TAG, "暂无命令接收"+fd);
            return data;
        }
        return data;
    }

    // 串口发送
    public int send(int fd, byte[] data) {
        // Log.d(TAG, "send()");
        int len = 0;
        if (fd < 0) {
            Log.d(TAG, "send() error");
            return -1;
        }
        // 写入串口
        if (fd > 0) {
            len = __send(fd, data);
        }
        return len;
    }

    // 以下函数必须被引用
    private static native int __open(String path, int baudrate);

    private static native int __close(int iFd);

    private static native int __recv(int iFd, byte[] data, int len, int style);

    private static native int __send(int iFd, byte[] data);
}
