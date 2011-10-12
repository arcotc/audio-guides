package uk.co.huntersix.android.audiowalks;

import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uk.co.huntersix.android.audiowalks.model.Placemark;
import uk.co.huntersix.android.audiowalks.model.TravelWalkMap;
import uk.co.huntersix.android.audiowalks.xml.TravelWalkMapHandler;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class WalkViewActivity extends MapActivity {
	private final String MY_DEBUG_TAG = "WeatherForcaster";
	private ProgressBar progressBar;
    private GeoPoint basePoint = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walk);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		setTitle(getTitle() + " - " + bundle.getString("travelWalk.title"));
    	basePoint = calculateGeoPoint(bundle.getString("travelWalk.latitude"), bundle.getString("travelWalk.longitude"));
		
        progressBar = (ProgressBar)findViewById(R.id.progressbar_Horizontal);
        progressBar.setProgress(0);

//		MapView mapView = (MapView) findViewById(R.id.mapview);
//	    mapView.setBuiltInZoomControls(true);
	    
//	    List<Overlay> mapOverlays = mapView.getOverlays();
//	    Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
//	    GroundZeroItemizedOverlay itemizedoverlay = new GroundZeroItemizedOverlay(drawable);
//	    
//	    GeoPoint point = calculateGeoPoint(bundle.getString("travelWalk.latitude"), bundle.getString("travelWalk.longitude"));
//	    OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
	    
//	    TravelWalkMap travelWalkMap = processGeoPointsFromMap(bundle.getString("travelWalk.mapUrl"));
        new LoadMapContentTask().execute(bundle.getString("travelWalk.mapUrl"));
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

	public void showResults(TravelWalkMap travelWalkMap) {
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);

	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
	    GroundZeroItemizedOverlay itemizedoverlay = new GroundZeroItemizedOverlay(drawable);
	    
	    GeoPoint point = null;
	    if ((travelWalkMap != null) && (travelWalkMap.placemarks.size() > 0)) {
	    	for (Placemark placemark : travelWalkMap.placemarks) {
			    GeoPoint p = new GeoPoint(placemark.point.coord1.intValue(), placemark.point.coord2.intValue());
			    
			    if (point == null) {
			    	point = p;
			    }
			    
			    OverlayItem overlay = new OverlayItem(p, placemark.name, placemark.description);
			    itemizedoverlay.addOverlay(overlay);
	    	}
	    }

	    if (point == null) {
	    	point = basePoint;
	    }
	    
//	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);
	    
	    MapController mapController = mapView.getController();
	    mapController.setZoom(16);
	    mapController.setCenter(point);
		
		progressBar.setVisibility(View.INVISIBLE);
	}


    private class LoadMapContentTask extends AsyncTask<String, Integer, TravelWalkMap> {    	
		@Override
		protected TravelWalkMap doInBackground(String... args) {
	    	int myProgress = 0;
	    	
	        TravelWalkMapHandler travelWalkMapHandler = new TravelWalkMapHandler();

	        // Load map
	        try {
	            /* Create a URL we want to load some xml-data from. */
	            URL url = new URL(args[0].replaceAll("&amp;", "&") + "&output=kml");

	            /* Get a SAXParser from the SAXPArserFactory. */
	            SAXParserFactory spf = SAXParserFactory.newInstance();
	            SAXParser sp = spf.newSAXParser();

	            /* Get the XMLReader of the SAXParser we created. */
	            XMLReader xr = sp.getXMLReader();
	            /* Create a new ContentHandler and apply it to the XML-Reader*/
	            xr.setContentHandler(travelWalkMapHandler);
	           
	            /* Parse the xml-data from our URL. */
	            xr.parse(new InputSource(url.openStream()));
	            /* Parsing has finished. */

		    } catch (Exception e) {
	            Log.e(MY_DEBUG_TAG, e.getMessage(), e);
		    }
			
	        return travelWalkMapHandler.getParsedData();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(TravelWalkMap travelWalkMap) {
			publishProgress(100);
			
			showResults(travelWalkMap);
		}
    }	
}
