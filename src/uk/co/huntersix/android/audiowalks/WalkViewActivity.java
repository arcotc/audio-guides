package uk.co.huntersix.android.audiowalks;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class WalkViewActivity extends MapActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walk);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		setTitle(getTitle() + " - " + bundle.get("travelWalk.title"));
		
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
