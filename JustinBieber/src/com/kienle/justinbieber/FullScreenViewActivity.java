package com.kienle.justinbieber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.kienle.justinbieber.adapter.FullScreenImageAdapter;
import com.kienle.justinbieber.helper.Utils;

public class FullScreenViewActivity extends Activity{

	private Utils mUtils;
	private FullScreenImageAdapter mAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mUtils = new Utils(this);

		Intent i = getIntent();
		int position = i.getIntExtra(GridViewActivity.POSITION, 0);

		mAdapter = new FullScreenImageAdapter(FullScreenViewActivity.this, mUtils.getFilePaths());
		mViewPager.setAdapter(mAdapter);

		// displaying selected image first
		mViewPager.setCurrentItem(position);
	}
}
