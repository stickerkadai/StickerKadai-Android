package io.github.stickerkadai;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

public class SimpleImageActivity extends Activity {

    String[] imageUrls;

    DisplayImageOptions options;

    ViewPager pager;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_image_pager);

        imageUrls = Constants.IMAGES;

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ImageAdapter(this));
        pager.setCurrentItem(getIntent().getIntExtra("image_position", 0));

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                File stickerFile= com.nostra13.universalimageloader.utils.DiskCacheUtils.findInCache(Constants.IMAGES[pager.getCurrentItem()], ImageLoader.getInstance().getDiskCache());
                Uri stickerUri= Uri.fromFile(stickerFile);
                shareIntent.putExtra(Intent.EXTRA_STREAM, stickerUri);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, "Send via"));
                break;
            default:
                return false;
        }
        return true;
    }


    public void onBackPressed() {
        ImageLoader.getInstance().stop();
        super.onBackPressed();
    }

    private class ImageAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        Context baseContext;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            baseContext=context;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

            ImageLoader.getInstance().displayImage(imageUrls[position], imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:message = "Input/Output error";break;
                        case DECODING_ERROR:message = "Image can't be decoded";break;
                        case NETWORK_DENIED:message = "Downloads are denied";break;
                        case OUT_OF_MEMORY:message = "Out Of Memory error";break;
                        case UNKNOWN:message = "Unknown error";break;
                    }
                    Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });

            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }


}