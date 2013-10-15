package com.kienle.justinbieber;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.kienle.justinbieber.adapter.GridViewImageAdapter;
import com.kienle.justinbieber.helper.AppConstant;
import com.kienle.justinbieber.helper.Config;
import com.kienle.justinbieber.helper.DialogUtil;
import com.kienle.justinbieber.helper.FileUtils;
import com.kienle.justinbieber.helper.StringUtil;
import com.kienle.justinbieber.helper.Utils;

public class GridViewActivity extends Activity {
	public static final String POSITION = "position";
	
	private Utils mUtils;
	private ArrayList<String> mImagePaths = new ArrayList<String>();
	private GridViewImageAdapter mAdapter;
	private GridView mGridView;
	private int mColumnWidth;
	private SharedPreferences mPrefs;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_view);

		mGridView = (GridView) findViewById(R.id.grid_view);

		mPrefs = getSharedPreferences("com.kienle.justinbieber", MODE_PRIVATE);
		mProgressDialog = DialogUtil.createProgressDialog(this, StringUtil.getString(R.string.copying_data));
		
		if (mPrefs.getBoolean("first_run", true)) {
            
            // copy data to sdcard
            String appFolder = FileUtils.getSdcardDir();
            CopyDataTask copyTask = new CopyDataTask();
            copyTask.execute(appFolder);

            mPrefs.edit().putBoolean("first_run", false).commit();
        } else {
        	initData();
        }
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// on selecting grid view image
				// launch full screen activity
				Intent i = new Intent(GridViewActivity.this, FullScreenViewActivity.class);
				i.putExtra(POSITION, position);
				startActivity(i);
			}
		});
	}
	
	private void initData() {
		mUtils = new Utils(this);

		// Initilizing Grid View
		initilizeGridLayout();

		// loading all image paths from SD card
		mImagePaths = mUtils.getFilePaths();

		// Gridview adapter
		mAdapter = new GridViewImageAdapter(GridViewActivity.this, mImagePaths, mColumnWidth);

		// setting grid view adapter
		mGridView.setAdapter(mAdapter);
	}

	private void initilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				AppConstant.GRID_PADDING, r.getDisplayMetrics());

		mColumnWidth = (int) ((mUtils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

		mGridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
		mGridView.setColumnWidth(mColumnWidth);
		mGridView.setStretchMode(GridView.NO_STRETCH);
		mGridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		mGridView.setHorizontalSpacing((int) padding);
		mGridView.setVerticalSpacing((int) padding);
	}
	
	private class CopyDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String appFolder = params[0];
            boolean copyComplete = false;
            try {
                copyComplete = FileUtils.copyDataFromAssetToSd(GridViewActivity.this, Config.DATA_ZIP_FILE, appFolder);
            } catch (IOException e) {
                Log.d("KienLT", "[MainActivity] copy data to sdcard error: " + e.getMessage());
                copyComplete = false;
            }
            return copyComplete;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            hideDialog();
            initData();
            if (mAdapter != null) {
            	mAdapter.notifyDataSetChanged();
            }
        } 

        @Override
        protected void onCancelled() {
            hideDialog();
        }
    };

    private void showDialog() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void hideDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialogConfirmExit();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void showDialogConfirmExit() {
        DialogUtil.createConfirmExistDialog(this, confirmExitListenner, R.string.confirm_exit);
    }
    
    DialogInterface.OnClickListener confirmExitListenner = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            moveTaskToBack(true);
            finish();
        }
    };

}
