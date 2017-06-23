package com.hejia.bean;

/**
 * 文件名：PicFileBean
 * 作者：韩秋宇
 * 时间：2017/5/26
 * 功能描述：图片文件的实体
 */

public class PicFileBean {

    private int fileId;
    private String fileName;
    private String filePath;
    private boolean isImg = false;
    private boolean isChecked = false;

    public PicFileBean(int fileId, String fileName, String filePath, boolean isImg, boolean isChecked) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.isImg = isImg;
        this.isChecked = isChecked;
    }

    public PicFileBean() {
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isImg() {
        return isImg;
    }

    public void setImg(boolean img) {
        isImg = img;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

//    @Override
//    public boolean equals(Object obj) {
//        PicFileBean s=(PicFileBean)obj;
//        return fileName.equals(s.fileName) || filePath.equals(s.filePath);
//    }
//    @Override
//    public int hashCode() {
//        String in = fileName;
//        return in.hashCode();
//    }
}
