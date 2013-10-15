package com.kienle.justinbieber.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class Utils {

	private Activity mContext;

	// constructor
	public Utils(Activity context) {
		this.mContext = context;
	}

	/*
	 * Reading file paths from SDCard
	 */
	public ArrayList<String> getFilePaths() {
		ArrayList<String> filePaths = new ArrayList<String>();

		File directory = new File(Config.FOLDER_IMAGE);

		// check for directory
		if (directory.isDirectory()) {
			// getting list of file paths
			File[] listFiles = directory.listFiles();

			// Check for count
			if (listFiles.length > 0) {

				// loop through all files
				for (int i = 0; i < listFiles.length; i++) {

					// get file path
					String filePath = listFiles[i].getAbsolutePath();

					// check for supported file extension
					if (IsSupportedFile(filePath)) {
						// Add image path to array list
						filePaths.add(filePath);
					}
				}
			} else {
				// image directory is empty
				Toast.makeText(
						mContext,
						Config.FOLDER_IMAGE
								+ " is empty. Please load some images in it!",
						Toast.LENGTH_LONG).show();
			}

		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
			alert.setTitle("Error!");
			alert.setMessage(Config.APP_FOLDER + " directory path is not valid!");
			alert.setPositiveButton("OK", null);
			alert.show();
		}

		return filePaths;
	}

	/*
	 * Check supported file extensions
	 * 
	 * @returns boolean
	 */
	private boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
				filePath.length());

		if (AppConstant.FILE_EXTN
				.contains(ext.toLowerCase(Locale.getDefault())))
			return true;
		else
			return false;

	}

	/*
	 * getting screen width
	 */
	public int getScreenWidth() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics.widthPixels;
	}
}
