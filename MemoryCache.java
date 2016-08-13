package cn.mini.miniimageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by xuebing on 16/8/13.
 */
public class MemoryCache implements ImageCache {

    private LruCache<String,Bitmap> mMemeryCache;

    public MemoryCache(){
        //初始化LRU缓存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory / 4;
        mMemeryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(String url) {
        return mMemeryCache.get(url);
    }

    @Override
    public void put(String url, Bitmap bmp) {
        mMemeryCache.put(url,bmp);
    }
}
