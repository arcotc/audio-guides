package uk.co.huntersix.android.audiowalks;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class WalkViewActivity extends MapActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walk);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		setTitle(getTitle() + " - " + bundle.getString("travelWalk.title"));
		
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
	    GroundZeroItemizedOverlay itemizedoverlay = new GroundZeroItemizedOverlay(drawable);
	    
	    GeoPoint point = calculateGeoPoint(bundle.getString("travelWalk.latitude"), bundle.getString("travelWalk.longitude"));
	    OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
	    
	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);
	    
	    MapController mapController = mapView.getController();
	    mapController.setZoom(16);
	    mapController.setCenter(point);
	}
	
	public GeoPoint calculateGeoPoint(String sLatitude, String sLongitude) {
	    int latitude = new Double(Double.parseDouble(sLatitude) * 1E6).intValue();
	    int longitude = new Double(Double.parseDouble(sLongitude) * 1E6).intValue();
	    
	    return new GeoPoint(latitude, longitude);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
