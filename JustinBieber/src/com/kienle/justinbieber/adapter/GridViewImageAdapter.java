package com.kienle.justinbieber.adapter;


import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.kienle.justinbieber.helper.ImageLoader;

public class GridViewImageAdapter extends BaseAdapter {

	private Activity _activity;
	private ArrayList<String> _filePaths = new ArrayList<String>();
	private int imageWidth;
	public ImageLoader mImageLoader;
	
	public GridViewImageAdapter(Activity activity, ArrayList<String> filePaths,
			int imageWidth) {
		this._activity = activity;
		this._filePaths = filePaths;
		this.imageWidth = imageWidth;
		this.mImageLoader = new ImageLoader(activity);
	}

	@Override
	public int getCount() {
		return this._filePaths.size();
	}

	@Override
	public Object getItem(int position) {
		return this._filePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(_activity);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
		
		mImageLoader.displayImage(_filePaths.get(position), imageView);

		return imageView;
	}

}
