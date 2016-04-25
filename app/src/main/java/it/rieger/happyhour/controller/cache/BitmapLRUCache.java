package it.rieger.happyhour.controller.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Cache für images which are be loaded from the server.
 *
 * The cache was build as a singleton and is thread save.
 *
 * Created by sebastian on 25.04.16.
 */
public enum BitmapLRUCache {

    BITMAP_LRU_CACHE;

    private LruCache<String, Bitmap> bitmapLruCache;

    /**
     * constructor
     */
    BitmapLRUCache() {

        //get memory size
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        //set cache size
        final int cacheSize = maxMemory / 8;

        //initialize cache
        bitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    /**
     * Get the instance of the cache.
     *
     * <b>Note:</b> you can also use <code>BitmapLRUCache.BITMAP_LRU_CACHE</code> for getting the instance.
     *
     * @return the instance of the cache.
     */
    public static BitmapLRUCache getInstance(){

        return BITMAP_LRU_CACHE;
    }

    /**
     * Add a image to the cache.
     * @param key the key of the image
     * @param bitmap the bitmap of the image
     */
    public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            bitmapLruCache.put(key, bitmap);
        }
    }

    /**
     * Get a image from the cache or null, if the image is not in the cache.
     * @param key the key for the image
     * @return the image for the key or <code>null</code>, if the image is not in the cache
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return bitmapLruCache.get(key);
    }

}
