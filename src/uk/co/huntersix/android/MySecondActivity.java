package uk.co.huntersix.android;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MySecondActivity extends Activity {
	ProgressBar progressBar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        
        progressBar = (ProgressBar)findViewById(R.id.progressbar_Horizontal);
        progressBar.setProgress(0);
        
        new MyBackgroundTask().execute(1,100,1);
    }
    
    public void startSamplesActivity(View view) {
    	Intent myIntent = new Intent(MySecondActivity.this, MySamplesActivity.class);
    	MySecondActivity.this.startActivity(myIntent);
    }


    public void showButton(String result) {
    	Button finishedButton = (Button)findViewById(R.id.finishButton);
    	finishedButton.setVisibility(View.VISIBLE);

    	TextView resultTextView = (TextView)findViewById(R.id.resultText);
        CharSequence resultTextCS = result;
        resultTextView.setText(resultTextCS);
        resultTextView.setVisibility(View.VISIBLE);
    }
    
    private class MyBackgroundTask extends AsyncTask<Integer, Integer, String> {    	
		@Override
		protected String doInBackground(Integer... args) {
	    	int myProgress = 0;
	    	
        	String resultText = "";

        	try {
				String uri = "http://content.guardianapis.com/search?section=travel&format=json&api-key=pbpw3gnyu3kcna9eqe736aj6";
				HttpClient client = new DefaultHttpClient();
		        HttpGet request = new HttpGet();
		        request.setURI(new URI(uri));
		        
		        String json = client.execute(request, new BasicResponseHandler());
		        
		        System.out.println(json);
		        
		        JSONObject response = new JSONObject(json).getJSONObject("response");
		        String status = response.getString("status");
		        if ("ok".equalsIgnoreCase(status)) {
		        	JSONArray results = response.getJSONArray("results");
		        	boolean first = true;
		        	for (int i=0; i<results.length(); i++) {
		        		if (!first) resultText += ", ";
		        		JSONObject result = results.getJSONObject(i);
		        		resultText += result.getString("webTitle");
		        		System.out.println(String.format("webTitle: %s", result.getString("webTitle")));
		        	}
			    }
			}
			catch (Exception e) {
				System.out.println("****************");
				System.out.println(e.getMessage());
			}

//	        if (isCancelled()) break;

			publishProgress(myProgress++);
			
			return resultText;
		}

		@Override
		protected void onPostExecute(String resultText) {
			publishProgress(100);
			
			System.out.println("Inside onPostExecute");

			showButton(resultText);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
		}
    }
}
