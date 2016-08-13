package cn.mini.miniimageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xuebing on 16/8/13.
 */
public class ImageLoader {
    //图片缓存
    ImageCache mImageCache = new MemoryCache();
    //线程池，数量为CPU的数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //注入缓存实现
    public void setImageCache(ImageCache cache){
        mImageCache = cache;
    }

    public void displayImage(String imageUrl, ImageView imageView){
        Bitmap bitmap = mImageCache.get(imageUrl);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }

        //图片没缓存，提交到线程池中下载图片
        submitLoadRequest(imageUrl,imageView);
    }

    private void submitLoadRequest(final String imageUrl,final ImageView imageView){
        imageView.setTag(imageUrl);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(imageUrl);
                if (bitmap == null){
                    return;
                }

                if (imageView.getTag().equals(imageUrl)){
                    imageView.setImageBitmap(bitmap);
                }
                mImageCache.put(imageUrl,bitmap);
            }
        });
    }

    private Bitmap downloadImage(String imageUrl){
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
