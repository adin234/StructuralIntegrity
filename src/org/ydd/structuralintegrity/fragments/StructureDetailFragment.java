package org.ydd.structuralintegrity.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ydd.structuralintegrity.ListStructures;
import org.ydd.structuralintegrity.R;
import org.ydd.structuralintegrity.adapters.CustomListViewAdapter;
import org.ydd.structuralintegrity.models.Comments;
import org.ydd.structuralintegrity.models.Structure;
import org.ydd.structuralintegrity.utility.GetRequest;
import org.ydd.structuralintegrity.utility.PostExecute;
import org.ydd.structuralintegrity.utility.PostRequest;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class StructureDetailFragment extends Fragment {
		Structure structure;
		FragmentManager fragmentManager;
		CustomListViewAdapter customListAdapter;
		public String imguploadsrc = "";
		ArrayList<Comments> comments;
		View rootView;
		Fragment sdetail;
		ProgressDialog pDialog;
		
		/**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public StructureDetailFragment(FragmentManager fmanager, Structure structure) {
        	this.fragmentManager = fmanager;
        	this.structure = structure;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	pDialog = new ProgressDialog(container.getContext());
        	pDialog.setTitle("Loading Data");
        	pDialog.setMessage("Please wait...");
        	imguploadsrc = "";
        	sdetail = this;
            rootView = inflater.inflate(R.layout.fragment_structure_detail, container, false);
            ((TextView) rootView.findViewById(R.id.text_title)).setText(structure.getName());
            ((TextView) rootView.findViewById(R.id.text_description)).setText(structure.getDescription());
            ((RatingBar) rootView.findViewById(R.id.ratingBar1)).setRating(structure.getRate());
            comments = new ArrayList<Comments>();
            GetRequest get = new GetRequest();
            pDialog.show();
            get.postexecute = new PostExecute() {
    			
    			@Override
    			public void doAfter(String string) throws JSONException {
    				JSONArray data = null;
    				Boolean skip = false;
    				try {
	    				JSONObject json = new JSONObject(string);
	    				data = json.getJSONArray("data");
    				} catch (Exception e) { 
    					((TextView) rootView.findViewById(R.id.textView1))
    						.setText("No Comments Yet");
    					skip = true;
    				}
    				
    				if(!skip) {
	    				for(int i =0; i < data.length(); i++) {
	    					JSONObject obj = data.getJSONObject(i);
	    					String imageString = "";
	        				try {
	        					imageString = obj.getString("image");
	        				} catch(Exception e) {}
	    					comments.add(new Comments(
	    							obj.getString("_id"),
	    							obj.getString("comment"),
	    							"05/06/2014",
	    							imageString, 
	    							Float.parseFloat(obj.getString("rating"))));
	    				}
	    				
	    				
	    				Log.d("Comments", ""+comments.size());
	    				ListView listView = (ListView) rootView.findViewById(R.id.comments);
	    	            customListAdapter = new CustomListViewAdapter(getActivity(), R.layout.row_list, comments);
	    	        	listView.setAdapter(customListAdapter);
    				}
    	        	pDialog.hide();
    			}
    		};
    		
            get.execute("http://ec2-54-214-176-172.us-west-2.compute.amazonaws.com/structure/comments?structure_id="+structure.getId());
        	
            ImageView imageView = (ImageView) rootView.findViewById(R.id.image_primary);
    		String imageUrl = structure.getImageSrc().split(",")[0];
    		ImageLoader imageLoader = ImageLoader.getInstance();
    		imageLoader.init(ImageLoaderConfiguration.createDefault(rootView.getContext()));
    		imageLoader.displayImage(imageUrl, imageView);
    		

			((Button) rootView.findViewById(R.id.addComment)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(rootView.getContext());
					dialog.setContentView(R.layout.dialog_comment);
					dialog.setTitle("Post a Comment");
		 
					Button submitButton = (Button) dialog.findViewById(R.id.buttonSubmit);
					// if button is clicked, close the custom dialog
					submitButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							postComment(dialog);
						}
					});
					
					Button cancelButton = (Button) dialog.findViewById(R.id.buttonCancel);
					// if button is clicked, close the custom dialog
					cancelButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					
					Button selectImage = (Button) dialog.findViewById(R.id.selectImage);
					selectImage.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							openGallery(5);
						}
					});
		 
					dialog.show();
				}
			});
    		
            return rootView;
        }
        
        public void openGallery(int req_code){
        	Intent intent = new Intent();
        	intent.setType("image/*");
        	intent.setAction(Intent.ACTION_GET_CONTENT);
        	startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        	if (resultCode == Activity.RESULT_OK) {
        		Uri selectedImageUri = data.getData();
        		imguploadsrc = getPath(selectedImageUri);
        	}
        }

        public String getPath(Uri uri) {
        	String[] projection = { MediaStore.Images.Media.DATA };
        	Cursor cursor = ((Activity) (rootView).getContext()).managedQuery(uri, projection, null, null, null);
        	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        	cursor.moveToFirst();
        	return cursor.getString(column_index);
        }
        
        public void postComment(final Dialog dialog) {
			dialog.dismiss();
        	pDialog.show();
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("comment", ((TextView) dialog.findViewById(R.id.text_comment)).getText().toString()));
            nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("structure_id", ""+structure.getId()));
            nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("rating", ""+((RatingBar) dialog.findViewById(R.id.rating_rate)).getRating()));
    		
            sdetail = new StructureDetailFragment(fragmentManager, structure);
            
            Log.d("POST DO", "MUST DO");
            PostRequest postRequest = new PostRequest();
            postRequest.postexecute = new PostExecute() {
				@Override
				public void doAfter(String string) throws JSONException {
		            JSONObject json = new JSONObject(string);
		            String id = json.getString("id");
					if(!imguploadsrc.equals("")) {
						Log.d("POST DO", "MUST DO");
			            PostRequest postRequest = new PostRequest();
			            postRequest.postexecute = new PostExecute() {
							@Override
							public void doAfter(String string) throws JSONException {
					            Log.d("POST DO", "isdoing");
								Log.d("WHAT WRONG LA", "asdf "+string);
								((TextView) rootView.findViewById(R.id.textView1))
									.setText("Comments");
								pDialog.hide();
								fragmentManager.beginTransaction()
			                    .replace(R.id.container, sdetail)
			                    .commit();
							}
						};
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			            nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("image", imguploadsrc));
			            nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("id", id));
			            Log.d("set id to ", ""+id);
			            postRequest.pairs = nameValuePairs;
						postRequest.execute("http://ec2-54-214-176-172.us-west-2.compute.amazonaws.com:8080/upload");
			            //postRequest.execute("http://10.0.1.182/tests/raven.php");
					} else {
						((TextView) rootView.findViewById(R.id.textView1))
							.setText("Comments");
						pDialog.hide();
						fragmentManager.beginTransaction()
		                    .replace(R.id.container, sdetail)
		                    .commit();
					}
				}
			};
			
			postRequest.pairs = nameValuePairs;
			postRequest.execute("http://ec2-54-214-176-172.us-west-2.compute.amazonaws.com/structure/comments");
            
		
//			nameValuePairs = new ArrayList<NameValuePair>(3);
//            nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("image", ));
//			postRequest.pairs = nameValuePairs;
//			postRequest.execute("http://10.0.1.182/tests/raven.php");
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((ListStructures) activity).onSectionAttached(1);
        }
}