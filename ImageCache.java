package cn.mini.miniimageloader;

import android.graphics.Bitmap;

/**
 * Created by xuebing on 16/8/13.
 */
public interface ImageCache {

    public Bitmap get(String url);

    public void put(String url,Bitmap bmp);
}
