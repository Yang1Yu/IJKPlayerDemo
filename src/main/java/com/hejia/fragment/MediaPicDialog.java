package com.hejia.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hejia.bean.PicFileBean;
import com.hejia.media.MyTextView;
import com.hejia.tp_launcher_v3.R;
import com.hejia.util.ImageScaleUtil;
import com.hejia.util.LruCacheUtil;
import com.hejia.util.PicFilePerateUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 文件名：MediaPicDialog
 * 作者：韩秋宇
 * 时间：2017/5/26
 * 功能描述：读取文件、选择文件
 */


public class MediaPicDialog extends Dialog {

    //GridView中可见的第一张图片的下标
    private int mFirstVisibleItem = -1;
    //GridView中可见的图片的数量
    private int mVisibleItemCount;
    //记录是否是第一次进入该界面
    private boolean isFirstEnterThisActivity = true;

    private PicFilePerateUtil filePerate;
    private PicFileBean fileBean;//图片文件的实体类
    private List<PicFileBean> fileBeanList = new ArrayList<>();//全部文件信息
    private List<PicFileBean> fileInBeanList;//点击文件是暂时保存该路径下的文件信息
    private GridView img_media_grid_view;
    private TextView img_media_empty_text;
    private TextView img_media_nousb_text;
    private Button btn_img_media_back;//返回按钮
    private Button btn_img_media_all;//全选按钮
    private Button btn_img_media_ok;//确定按钮
    private Button img_media_grid_cancel;//取消按钮
    private Context mContext;
    private FileImgAdapter fileImgAdapter;
    private List<PicFileBean> collectPicList = new ArrayList<>();//保存选中的图片集合
    private boolean isCollect = false;//全选按钮点击状态
    private boolean isAllCollect = false;//终止全部选中或者全部清除
    private LinearLayout ll_media_reading;
    private RelativeLayout rl_pic_grid;
    private LinearLayout ll_pic_sd_card;
    private  ImageView iv_pic_up_card;
    private ImageView iv_pic_readinganim;
    private AnimationDrawable anim;
    private RelativeLayout rl_dialog_top;

    private OnNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private OnYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    private Message message;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (message.what){
                case 1:
                    btn_img_media_all.setText("全选");
                    ll_media_reading.setVisibility(View.GONE);
                    anim.stop();
                    rl_dialog_top.setVisibility(View.VISIBLE);
                    if(null!=fileBeanList&&fileBeanList.size()>0){
                        if(null == fileImgAdapter){
                            fileImgAdapter = new FileImgAdapter();
                            img_media_grid_view.setAdapter(fileImgAdapter);
                        }else {
                            fileImgAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case 2:
                    LruCacheUtil.getInstance().evictAll();
                    fileBeanList.clear();
                    dismiss();
                    break;
            }


        }
    };

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface OnYesOnclickListener {
        void onYesClick(List<PicFileBean> collectPicList);
    }

    public interface OnNoOnclickListener {
        void onNoClick();
    }

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(OnNoOnclickListener onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(OnYesOnclickListener onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener;
    }

    public MediaPicDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.imagefile_dialog_gridview);
        initView();
        /**
         * 取消事件监听
         */
        img_media_grid_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });


        /**
         * 确认按钮的事件监听
         */
        btn_img_media_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick(collectPicList);
                }
            }
        });

        //点击usb图片事件监听
        iv_pic_up_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //先获取一个文件的操作对象
                filePerate = new PicFilePerateUtil();

                if(!filePerate.getExtSDCardPathList().contains("/mnt/media_rw/udisk")){
//            img_media_nousb_text.setVisibility(View.VISIBLE);
//            img_media_empty_text.setVisibility(View.GONE);
//            img_media_grid_view.setVisibility(View.GONE);
//            btn_img_media_all.setVisibility(View.GONE);
                    dismiss();
                }else{
                    ll_pic_sd_card.setVisibility(View.GONE);
                    rl_pic_grid.setVisibility(View.VISIBLE);

                    if(filePerate.getAllFile("/mnt/media_rw/udisk").isEmpty()){
                        img_media_empty_text.setVisibility(View.VISIBLE);
                        img_media_grid_view.setVisibility(View.GONE);
                        btn_img_media_all.setVisibility(View.GONE);
                    }else{
                        rl_dialog_top.setVisibility(View.GONE);
                        ll_media_reading.setVisibility(View.VISIBLE);
                        anim = (AnimationDrawable) iv_pic_readinganim.getBackground();
                        anim.start();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                fileBeanList.clear();
                                for(int i=0;i<filePerate.getAllFile("/mnt/media_rw/udisk").size();i++){
                                    if(filePerate.getExtSDCardPathList().contains("/mnt/media_rw/udisk")){
                                        fileBean = new PicFileBean();
                                        fileBean.setFilePath(filePerate.pathFile("/mnt/media_rw/udisk").get(i));
                                        fileBean.setFileName(filePerate.getAllFile("/mnt/media_rw/udisk").get(i));
                                        fileBean.setImg(filePerate.checkIsImageFile(filePerate.getAllFile("/mnt/media_rw/udisk").get(i)));
                                        fileBeanList.add(fileBean);
                                    }else {
                                        dismiss();
                                    }

                                }
                                initLoadBitmaps();
                                message = Message.obtain();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }
                }
            }
        });

        /**
         * 返回按钮事件监听
         */
        btn_img_media_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCollect = false;//切换新的文件夹全选状态改变
                isAllCollect = false;//非全选状态
                if(ll_pic_sd_card.getVisibility() == View.VISIBLE){
                    dismiss();
                } else if(!filePerate.getExtSDCardPathList().contains("/mnt/media_rw/udisk")){
//                    img_media_nousb_text.setVisibility(View.VISIBLE);
//                    img_media_empty_text.setVisibility(View.GONE);
//                    img_media_grid_view.setVisibility(View.GONE);
//                    btn_img_media_all.setVisibility(View.GONE);
                    dismiss();
                }else{
                    img_media_nousb_text.setVisibility(View.GONE);
                    img_media_grid_view.setVisibility(View.VISIBLE);
                    btn_img_media_all.setVisibility(View.VISIBLE);
                    // filePerate.getCurrentPath();//当前文件夹目录
                    final String prePath = filePerate.getParentFolder(filePerate.getCurrentPath());//上一级文件夹目录
                    if("/mnt/media_rw".equals(prePath)){
                        ll_pic_sd_card.setVisibility(View.VISIBLE);
                        rl_pic_grid.setVisibility(View.GONE);
                        btn_img_media_all.setText("");
                    }else {
                        //更新页面状态
                        btn_img_media_all.setText("全选");
                        img_media_empty_text.setVisibility(View.GONE);
                        img_media_grid_view.setVisibility(View.VISIBLE);
                        btn_img_media_all.setVisibility(View.VISIBLE);
                        rl_dialog_top.setVisibility(View.GONE);
                        ll_media_reading.setVisibility(View.VISIBLE);
                        anim = (AnimationDrawable) iv_pic_readinganim.getBackground();
                        anim.start();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                fileBeanList.clear();
                                for(int i=0;i<filePerate.getAllFile(prePath).size();i++){
                                    if(filePerate.getExtSDCardPathList().contains("/mnt/media_rw/udisk")){
                                        fileBean = new PicFileBean();
                                        fileBean.setFilePath(filePerate.pathFile(prePath).get(i));
                                        fileBean.setFileName(filePerate.getAllFile(prePath).get(i));
                                        fileBean.setImg(filePerate.checkIsImageFile(filePerate.getAllFile(prePath).get(i)));
                                        fileBeanList.add(fileBean);
                                    }else {
                                        dismiss();
                                    }

                                }
                                initLoadBitmaps();
                                message = Message.obtain();
                                message.what = 1;
                                handler.sendMessage(message);;
                            }
                        }).start();
                    }
                }
            }
        });

        //gridview滑动监听
        img_media_grid_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == SCROLL_STATE_IDLE) {
                        loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
                    }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mFirstVisibleItem = firstVisibleItem;
                mVisibleItemCount = visibleItemCount;
                if (isFirstEnterThisActivity && visibleItemCount > 0) {
                    loadBitmaps(firstVisibleItem, visibleItemCount);
                    isFirstEnterThisActivity = false;
                }
            }
        });

        /**
         * gridview 条目点击事件实现页面刷新
         */
        img_media_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                isCollect = false;
                isAllCollect = false;//改变全选状态，实现页面更新
                if(!filePerate.getExtSDCardPathList().contains("/mnt/media_rw/udisk")){
//                    img_media_nousb_text.setVisibility(View.VISIBLE);
//                    img_media_empty_text.setVisibility(View.GONE);
//                    img_media_grid_view.setVisibility(View.GONE);
//                    btn_img_media_all.setVisibility(View.GONE);
                }else{
                    img_media_nousb_text.setVisibility(View.GONE);
                    img_media_grid_view.setVisibility(View.VISIBLE);
                    btn_img_media_all.setVisibility(View.VISIBLE);
                    //如果点击的文件是图片
                    if(fileBeanList.get(position).isImg()){
                        if(fileBeanList.get(position).isChecked()){
                            fileBeanList.get(position).setChecked(false);
                            collectPicList.remove(fileBeanList.get(position));
                        }else {
                            fileBeanList.get(position).setChecked(true);
                            collectPicList.add(fileBeanList.get(position));
                        }

                        if(null == fileImgAdapter){
                            fileImgAdapter = new FileImgAdapter();
                            img_media_grid_view.setAdapter(fileImgAdapter);
                        }else {
                            fileImgAdapter.notifyDataSetChanged();
                        }
                    }else{//如果点击的是文件夹
                        if(filePerate.getAllFile(fileBeanList.get(position).getFilePath()).isEmpty()){
                            fileBeanList.clear();
                            img_media_empty_text.setVisibility(View.VISIBLE);
                            img_media_grid_view.setVisibility(View.GONE);
                            btn_img_media_all.setVisibility(View.GONE);
                        }else{
                            rl_dialog_top.setVisibility(View.GONE);
                            btn_img_media_all.setText("全选");
                            ll_media_reading.setVisibility(View.VISIBLE);
                            anim = (AnimationDrawable) iv_pic_readinganim.getBackground();
                            anim.start();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if(null == fileInBeanList){
                                        fileInBeanList = new ArrayList<>();
                                    }else {
                                        fileInBeanList.clear();
                                    }
                                    fileInBeanList.addAll(fileBeanList);
                                    fileBeanList.clear();
                                    for(int i=0;i<filePerate.getAllFile(fileInBeanList.get(position).getFilePath()).size();i++){
                                        if(filePerate.getExtSDCardPathList().contains("/mnt/media_rw/udisk")){
                                            fileBean = new PicFileBean();
                                            fileBean.setFilePath(filePerate.pathFile(fileInBeanList.get(position).getFilePath()).get(i));
                                            fileBean.setFileName(filePerate.getAllFile(fileInBeanList.get(position).getFilePath()).get(i));
                                            fileBean.setImg(filePerate.checkIsImageFile(filePerate.getAllFile(fileInBeanList.get(position).getFilePath()).get(i)));
                                            fileBeanList.add(fileBean);
                                        }else {
                                            dismiss();
                                        }

                                    }
                                    initLoadBitmaps();
                                    message = Message.obtain();
                                    message.what = 1;
                                    handler.sendMessage(message);;
                                }
                            }).start();
                        }
                    }
                }
            }

        });

        /**
         * 全选按钮事件监听
         */
        btn_img_media_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllCollect = true;//这是全选按钮发过来的状态

                if(!filePerate.getExtSDCardPathList().contains("/mnt/media_rw/udisk")){
//                    img_media_nousb_text.setVisibility(View.VISIBLE);
//                    img_media_empty_text.setVisibility(View.GONE);
//                    img_media_grid_view.setVisibility(View.GONE);
//                    btn_img_media_all.setVisibility(View.GONE);
                }else {
                    if(isCollect){
                        isCollect = false;

                    }else {
                        isCollect = true;

                    }
                    if(isCollect){
                        btn_img_media_all.setText("取消全选");
                    }else{
                        btn_img_media_all.setText("全选");
                    }
                    if(null == fileImgAdapter){
                        fileImgAdapter = new FileImgAdapter();
                        img_media_grid_view.setAdapter(fileImgAdapter);
                    }else {
                        fileImgAdapter.notifyDataSetChanged();
                    }
                    for(int i=0;i<fileBeanList.size();i++){
                        if(fileBeanList.get(i).isImg()){
                            if(isCollect){//如果全选按钮选中页面刷新
                                if(isAllCollect){
                                    collectPicList.add(fileBeanList.get(i));
                                    fileBeanList.get(i).setChecked(true);
                                }
                            }else{//取消全选按钮选中页面刷新
                                if(isAllCollect){
                                    collectPicList.remove(fileBeanList.get(i));
                                    fileBeanList.get(i).setChecked(false);
                                }
                            }
                        }
                    }
                }

            }
        });
    }

    private void initView() {
        img_media_grid_view = (GridView) findViewById(R.id.img_media_grid_view);
        img_media_empty_text = (TextView) findViewById(R.id.img_media_empty_text);
        btn_img_media_all = (Button) findViewById(R.id.btn_img_media_all);
        btn_img_media_all.setText("");
        btn_img_media_back = (Button) findViewById(R.id.btn_img_media_back);
        btn_img_media_ok = (Button) findViewById(R.id.btn_img_media_ok);
        img_media_grid_cancel = (Button) findViewById(R.id.btn_img_media_cancel);
        img_media_nousb_text = (TextView) findViewById(R.id.img_media_nousb_text);
        ll_media_reading = (LinearLayout) findViewById(R.id.ll_media_reading);
        rl_pic_grid = (RelativeLayout) findViewById(R.id.rl_pic_grid);
        ll_pic_sd_card = (LinearLayout) findViewById(R.id.ll_pic_sd_card);
        iv_pic_up_card = (ImageView) findViewById(R.id.iv_pic_up_card);
        iv_pic_readinganim = (ImageView) findViewById(R.id.iv_pic_readinganim);
        rl_dialog_top = (RelativeLayout) findViewById(R.id.rl_dialog_top);
    }



    /**
     * 文件名：PictureAdapter
     * 作者：韩秋宇
     * 时间：2017/5/25
     * 功能描述：图片列表适配器
     */

    public class FileImgAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return fileBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return fileBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext,R.layout.imgfile_grid_item, null);
                holder.iv_imgitem_fileIcon = (ImageView) convertView.findViewById(R.id.iv_imgitem_fileIcon);
                holder.tv_imgitem_fileName = (MyTextView) convertView.findViewById(R.id.tv_imgitem_fileName);
                holder.checkbox_imgitem_folder = (ImageView) convertView.findViewById(R.id.checkbox_imgitem_folder);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(null!=fileBeanList&&fileBeanList.size()>0){
                if(fileBeanList.get(position).isImg()){
                    setImageForImageView(fileBeanList.get(position).getFilePath(),holder.iv_imgitem_fileIcon);
                    if(fileBeanList.get(position).isChecked()){
                        holder.checkbox_imgitem_folder.setVisibility(View.VISIBLE);
                    }else {
                        holder.checkbox_imgitem_folder.setVisibility(View.GONE);
                    }

                }else {
                    holder.iv_imgitem_fileIcon.setImageResource(R.drawable.pic_media_file);
                    holder.checkbox_imgitem_folder.setVisibility(View.GONE);
                }
                holder.tv_imgitem_fileName.setText(fileBeanList.get(position).getFileName());
            }else {
                holder.iv_imgitem_fileIcon.setImageResource(R.drawable.pic_media_file);
                holder.checkbox_imgitem_folder.setVisibility(View.GONE);
                holder.tv_imgitem_fileName.setText("文件夹为空");
            }

            return convertView;


        }

        class ViewHolder {
            ImageView iv_imgitem_fileIcon;
            MyTextView tv_imgitem_fileName;
            ImageView checkbox_imgitem_folder;
        }
    }


    private void initLoadBitmaps(){
        try {
            int j = 0;
            if(fileBeanList.size()>30){
                j = 30;
            }else {
                j=fileBeanList.size();
            }
            for(int i=0;i<j;i++){
                if(filePerate.checkIsImageFile(fileBeanList.get(i).getFilePath())){
                    String imagePath = fileBeanList.get(i).getFilePath();
                    Bitmap bitmap = (Bitmap) LruCacheUtil.getInstance().get(imagePath);
                    if (bitmap == null) {
//                    bitmap=BitmapFactory.decodeResource(getResources(),Integer.parseInt(imagePath),options);
                        bitmap= ImageScaleUtil.ImageSrc(fileBeanList.get(i).getFilePath(),60,60);
                       // bitmap = ImageScaleUtil.getSmallBitmap(fileBeanList.get(i).getFilePath());
                        //将从SDCard读取的图片添加到LruCache中
                        LruCacheUtil.getInstance().put(imagePath, bitmap);
                    }

                }

            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 为GridView的item加载图片
     * @param firstVisibleItem GridView中可见的第一张图片的下标
     * @param visibleItemCount GridView中可见的图片的数量
     */
    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        try {
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                if(filePerate.checkIsImageFile(fileBeanList.get(i).getFilePath())){
                    String imagePath = fileBeanList.get(i).getFilePath();
                    Bitmap bitmap = (Bitmap) LruCacheUtil.getInstance().get(imagePath);
                    if (bitmap == null) {
                        bitmap=ImageScaleUtil.ImageSrc(fileBeanList.get(i).getFilePath(),60,60);
                        //bitmap = ImageScaleUtil.getSmallBitmap(fileBeanList.get(i).getFilePath());
                        //将从SDCard读取的图片添加到LruCache中
                        LruCacheUtil.getInstance().put(imagePath, bitmap);

                    }
                }
            }
            fileImgAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 为ImageView设置图片(Image)
     * 1 从缓存中获取图片
     * 2 若图片不在缓存中则为其设置默认图片
     */
    private void setImageForImageView(String imagePath, ImageView imageView) {
        Bitmap bitmap = (Bitmap) LruCacheUtil.getInstance().get(imagePath);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.img_media_file);
        }
    }

}
