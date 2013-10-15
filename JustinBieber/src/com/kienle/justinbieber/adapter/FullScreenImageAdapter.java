package com.kienle.justinbieber.adapter;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kienle.justinbieber.R;
import com.kienle.justinbieber.helper.TouchImageView;

public class FullScreenImageAdapter extends PagerAdapter {

	private Activity mActivity;
	private ArrayList<String> mImagePaths;
	private LayoutInflater mInflater;

	// constructor
	public FullScreenImageAdapter(Activity activity,
			ArrayList<String> imagePaths) {
		this.mActivity = activity;
		this.mImagePaths = imagePaths;
		this.mInflater = LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		return this.mImagePaths.size();
	}

	@Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        final TextView setWall;
 
        View viewLayout = mInflater.inflate(R.layout.layout_fullscreen_image, container, false);
 
        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        setWall = (TextView) viewLayout.findViewById(R.id.tvSetWall);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        final Bitmap bitmap = BitmapFactory.decodeFile(mImagePaths.get(position), options);
        imgDisplay.setImageBitmap(bitmap);
        

        ((ViewPager) container).addView(viewLayout);
 
        imgDisplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (setWall.getVisibility() == View.INVISIBLE) {
					showSetWallPaper(setWall);
				} else {
					hideSetWallPaper(setWall);
				}
			}
		});
        
        setWall.setOnClickListener(new OnClickListener() {
			
			private WallpaperManager wallpaperManager;

			@Override
			public void onClick(View v) {
				wallpaperManager = WallpaperManager.getInstance(mActivity);
			    try {
					wallpaperManager.setBitmap(bitmap);
					Toast.makeText(mActivity, "Set wallpaper complete!", Toast.LENGTH_SHORT).show();
					hideSetWallPaper(setWall);
				} catch (IOException e) {
					Toast.makeText(mActivity, "Set wallpaper fail!", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        return viewLayout;
	}
	
	private void showSetWallPaper(TextView setWall) {
		setWall.setVisibility(View.VISIBLE);
		Animation animIn = new TranslateAnimation(0f, 0, 50, 0.0f);
		animIn.setDuration(500);
		setWall.startAnimation(animIn);
	}
	
	private void hideSetWallPaper(TextView setWall) {
		Animation animOut = new TranslateAnimation(0f, 0, 0, 50);
		animOut.setDuration(500);
		setWall.startAnimation(animOut);
		setWall.setVisibility(View.INVISIBLE);
	}
	
	@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
 
    }

}
