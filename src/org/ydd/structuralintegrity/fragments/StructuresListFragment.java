package org.ydd.structuralintegrity.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ydd.structuralintegrity.ListStructures;
import org.ydd.structuralintegrity.R;
import org.ydd.structuralintegrity.adapters.CustomGridViewAdapter;
import org.ydd.structuralintegrity.models.Structure;
import org.ydd.structuralintegrity.utility.GetRequest;
import org.ydd.structuralintegrity.utility.PostExecute;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class StructuresListFragment extends Fragment {
		GridView gridView;
		CustomGridViewAdapter customGridAdapter;
		FragmentManager fragmentManager;
		ArrayList<Structure> gridArray;
		ProgressDialog pDialog;
		
		/**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
		public static StructuresListFragment newInstance(int sectionNumber, FragmentManager fmanager) {
        	StructuresListFragment fragment = new StructuresListFragment(fmanager);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public StructuresListFragment(FragmentManager fmanager) {
        	this.fragmentManager = fmanager;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	pDialog = new ProgressDialog(container.getContext());
        	pDialog.setTitle("Loading Data");
        	pDialog.setMessage("Please wait...");
        	pDialog.show();
            final View rootView = inflater.inflate(R.layout.fragment_list_structures, container, false);
            gridArray = new ArrayList<Structure>();
            GetRequest get = new GetRequest();
            get.postexecute = new PostExecute() {
    			
    			@Override
    			public void doAfter(String string) throws JSONException {
    				JSONObject json = new JSONObject(string);
    				JSONArray data = json.getJSONArray("data");
    				
    				for(int i =0; i < data.length(); i++) {
    					JSONObject obj = data.getJSONObject(i);
    					gridArray.add(new Structure(
    							obj.getString("_id"),
    							obj.getString("image"), 
    							obj.getString("name"), 
    							obj.getString("description"), 
    							Float.parseFloat(obj.getString("rating"))));
    				}
    				
    				gridView = (GridView) rootView.findViewById(R.id.gridView1);
    	        	customGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, gridArray);
    	        	gridView.setAdapter(customGridAdapter);
    	        	gridView.setOnItemClickListener(new OnItemClickListener() {
    	        		@Override 
    	        		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) { 
    	        			Toast.makeText(rootView.getContext(), gridArray.get(position).getName(), Toast.LENGTH_SHORT)
    	        				.show();
    	        			Fragment fragment = new StructureDetailFragment(fragmentManager, gridArray.get(position));
    	        			fragmentManager.beginTransaction()
    		                    .replace(R.id.container, fragment)
    		                    .commit();
    	        		}
    				});
    	        	pDialog.hide();
    			}
    		};
    		
    		
            get.execute("http://ec2-54-214-176-172.us-west-2.compute.amazonaws.com/structure");
        	
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((ListStructures) activity).onSectionAttached(
                    1);
        }
}