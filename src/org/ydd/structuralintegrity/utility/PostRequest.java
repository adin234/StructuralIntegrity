package org.ydd.structuralintegrity.utility;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

public class PostRequest extends AsyncTask<String, String, String> {
	public PostExecute postexecute;
	public List<NameValuePair> pairs;
    @Override
    protected String doInBackground(String... urls) {
            // TODO Auto-generated method stub
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urls[0]);
            try {
                // Add your data
            	Boolean hasImage = false;
            	MultipartEntity entity = new MultipartEntity();
            	
            	
            	for(int i = 0; i<pairs.size(); i++) {
            		if(pairs.get(i).getName().equalsIgnoreCase("image")) {
            			hasImage = true;
            			entity.addPart(pairs.get(i).getName(), new FileBody(new File(pairs.get(i).getValue())));
            		} else {
            			entity.addPart(pairs.get(i).getName(), new StringBody(pairs.get(i).getValue()));
            		}
            	}
            	
            	httpPost.setEntity(entity);
            	if(!hasImage) {
                    httpPost.setEntity(new UrlEncodedFormEntity(pairs));
            	}

                // Execute HTTP Post Request
                HttpResponse response = client.execute(httpPost);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                return out.toString();
                
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            
            return builder.toString();
    }
    
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
        	Log.d("DO AFTER", "DID?");
			postexecute.doAfter(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}