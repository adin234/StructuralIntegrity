package org.ydd.structuralintegrity.adapters;

import java.util.ArrayList;

import org.ydd.structuralintegrity.R;
import org.ydd.structuralintegrity.models.Comments;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter <Comments> {
	Context context;
	int layoutResourceId;
	ArrayList <Comments> data = new ArrayList <Comments> ();
	
	public CustomListViewAdapter(Context context, int layoutResourceId, ArrayList <Comments> data) {
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
			holder.comment = (TextView) row.findViewById(R.id.item_comment);
			holder.rate = (RatingBar) row.findViewById(R.id.item_rate);
			holder.images = (LinearLayout) row.findViewById(R.id.item_images);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		
		Comments item = data.get(position);
		holder.comment.setText(item.getComment());
		holder.rate.setRating(item.getRate());
		holder.images.removeAllViews();
		String images[] = item.getImages().split(",");
		holder.images.setVisibility(View.VISIBLE);
		//Log.d("images length", ""+images.length);
		if(images[0].equals("")) {
			holder.images.setVisibility(View.GONE);
		}
		for(int i = 0; i< images.length; i++) {
			String imageUrl = images[i];
			ImageLoader imageLoader = ImageLoader.getInstance();
			ImageView imgView = new ImageView(row.getContext());
			holder.images.addView(imgView, 50, 50);
			imageLoader.displayImage(imageUrl, imgView);
			//Log.d("adding position "+position, "here la"+i);
		};
		return row;
	}
	
	static class RecordHolder {
		TextView comment;
		RatingBar rate;
		LinearLayout images;
	}
}
