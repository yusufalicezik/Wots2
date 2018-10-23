package com.yusufalicezik.wots.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yusufalicezik.wots.R;

public class UniversalImagLoader {

    int defaultImage= R.drawable.ic_person;
    private Context mContext;


    public UniversalImagLoader(Context context) {
        mContext = context;
    }




    public ImageLoaderConfiguration getConfig(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(400)).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        return configuration;
    }

    public static void setImage(String imgURL, ImageView image, final ProgressBar mProgressBar, String append){

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }


/*

    public UniversalImagLoader getConfig()
    {
        DisplayImageOptions defaultOptions= new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)

                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();



        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100*1024*1024).build();


    }


    public void setImage(String imgUrl, ImageView imageView, ProgressBar mProgressBar, String ilkKisim){

        ImageLoader imageLoader=ImageLoader.getInstance();
        imageLoader.displayImage(ilkKisim + imgUrl, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });


    }

    */
}



/*


class UniversalImageLoader(val mContext:Context) {


    val config:ImageLoaderConfiguration
        get() {

            val defaultOptions=DisplayImageOptions.Builder()
                    .showImageOnLoading(defaultImage)
                    .showImageForEmptyUri(defaultImage)
                    .showImageOnFail(defaultImage)
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(FadeInBitmapDisplayer(400))
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build()


            return ImageLoaderConfiguration.Builder(mContext)
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(WeakMemoryCache())
                    .diskCacheSize(100*1024*1024).build()
        }


    companion object {
        private val defaultImage= R.drawable.ic_profile

        fun setImage(imgUrl:String, imageView:ImageView,mProgressbar:ProgressBar?, ilkKisim:String)
        {
            val imageLoader=ImageLoader.getInstance()
            imageLoader.displayImage(ilkKisim+imgUrl,imageView,object :ImageLoadingListener{
                override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {

                    if(mProgressbar!=null)
                    {
                        mProgressbar.visibility=View.GONE

                    }

                }

                override fun onLoadingStarted(imageUri: String?, view: View?) {
                    if(mProgressbar!=null)
                    {
                        mProgressbar.visibility=View.VISIBLE

                    }
                }

                override fun onLoadingCancelled(imageUri: String?, view: View?) {
                    if(mProgressbar!=null)
                    {
                        mProgressbar.visibility=View.GONE

                    }
                }

                override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                    if(mProgressbar!=null)
                    {
                        mProgressbar.visibility=View.GONE

                    }
                }
            })
        }
    }
 */
