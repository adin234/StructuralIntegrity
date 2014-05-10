package org.ydd.structuralintegrity.adapters;

import java.util.ArrayList;

import org.ydd.structuralintegrity.R;
import org.ydd.structuralintegrity.models.Structure;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class CustomGridViewAdapter extends ArrayAdapter <Structure> {
	Context context;
	int layoutResourceId;
	ArrayList <Structure> data = new ArrayList <Structure> ();
	
	public CustomGridViewAdapter(Context context, int layoutResourceId, ArrayList <Structure> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new RecordHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
			holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
			holder.rating = (RatingBar) row.findViewById(R.id.item_rating);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		
		Structure item = data.get(position);
		holder.txtTitle.setText(item.getName());
		holder.rating.setRating(item.getRate());
		String imageUrl = item.getImageSrc().split(",")[0];
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(imageUrl, holder.imageItem);
		return row;
	}
	static class RecordHolder {
		TextView txtTitle;
		ImageView imageItem;
		RatingBar rating;
	}
}
