package io.github.stickerkadai;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class HomeActivity extends Activity {

    String[] imageUrls;

    DisplayImageOptions options;

    protected AbsListView listView;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fr_image_grid);
        try {
            Constants.IMAGES = (String[]) new StickersListRetriever().execute(this).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        imageUrls = Constants.IMAGES;

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        listView = (GridView) findViewById(R.id.grid);
        ((GridView) listView).setAdapter(new ImageAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });

	}

    public void onBackPressed() {
        ImageLoader.getInstance().stop();
        super.onBackPressed();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
            case R.id.refresh:
                new StickersListRetriever().execute(this);
                listView.invalidateViews();
                break;
            case R.id.contactus:
                Intent contactIntent = new Intent(this, ContactUs.class);
                startActivity(contactIntent);
                break;
            default:
            return false;
        }
        return true;
	}



    public class ImageAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        Context baseContext;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            baseContext=context;
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_grid_image, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            ImageLoader.getInstance()
                    .displayImage(imageUrls[position], holder.imageView, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.progressBar.setVisibility(View.VISIBLE);
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

                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });

            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
    }

    protected void startImagePagerActivity(int position) {
        Intent intent = new Intent(this, SimpleImageActivity.class);
        intent.putExtra("image_position", position);
        startActivity(intent);
    }

}