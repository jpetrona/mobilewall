package com.mobyadvert.money.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilewall.app.R;
import com.mobyadvert.money.bean.FreshBean;

public class MobileArrayAdapter extends ArrayAdapter<FreshBean> {

	private final Context context;
	private final List<FreshBean> freshBeanList;

	public MobileArrayAdapter(Context context, int textViewResourceId, ArrayList<FreshBean> freshBeanList) {
		super(context, R.layout.row_item, freshBeanList);
		this.freshBeanList = freshBeanList;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.row_item, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.offer_name);
		TextView offerPoint = (TextView) rowView.findViewById(R.id.offer_point);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		FreshBean freshBean = freshBeanList.get(position);
		if (position % 2 == 0) {
			rowView.setBackgroundColor(context.getResources().getColor(R.color.white));
		}
		
		if (freshBean.getImageUrl() != null) {
			new ImageDownloadTask().execute(imageView, freshBean.getImageUrl());
		}
		
		textView.setText(freshBean.getOfferName());
		offerPoint.setText(freshBean.getPayoutCost());
		return rowView;
	}

	class ImageDownloadTask extends AsyncTask<Object, Void, Bitmap> {

		private ImageView imageView;
		private String path;

		/**
		 * This function will be executed to download the image in a background process.
		 * 
		 */
		@Override
		protected Bitmap doInBackground(Object... params) {
			try {
				imageView = (ImageView) params[0];
				path = (String) params[1];
				InputStream in = new java.net.URL(path).openStream();
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				return bitmap;
			} catch (Exception e) {
				Log.e("ImageDownload", e.getMessage());
			}
			return null;
		}

		/**
		 * This function will be called after the image download and attaches the bitmap to the ImageView.
		 * 
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			if (bitmap != null) {
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}
}
