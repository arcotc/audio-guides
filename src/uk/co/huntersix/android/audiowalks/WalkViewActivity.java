package uk.co.huntersix.android.audiowalks;

import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uk.co.huntersix.android.audiowalks.model.TravelWalkMap;
import uk.co.huntersix.android.audiowalks.xml.TravelWalkMapHandler;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class WalkViewActivity extends MapActivity {
	private final String MY_DEBUG_TAG = "WeatherForcaster";
	String x = "";
	
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
	    
	    processGeoPointsFromMap(bundle.getString("travelWalk.mapUrl"));
	    
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

	private void processGeoPointsFromMap(String mapUrl) {
		// Load map
        try {
            /* Create a URL we want to load some xml-data from. */
            URL url = new URL(mapUrl);

            /* Get a SAXParser from the SAXPArserFactory. */
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            /* Get the XMLReader of the SAXParser we created. */
            XMLReader xr = sp.getXMLReader();
            /* Create a new ContentHandler and apply it to the XML-Reader*/
            TravelWalkMapHandler myExampleHandler = new TravelWalkMapHandler();
            xr.setContentHandler(myExampleHandler);
           
            /* Parse the xml-data from our URL. */
            xr.parse(new InputSource(url.openStream()));
            /* Parsing has finished. */

            /* Our ExampleHandler now provides the parsed data to us. */
            TravelWalkMap parsedTravelWalkMap = myExampleHandler.getParsedData();

            /* Set the result to be displayed in our GUI. */
            x += parsedTravelWalkMap.toString();
           
	    } catch (Exception e) {
	            /* Display any Error to the GUI. */
	            x += "Error: " + e.getMessage();
	            Log.e(MY_DEBUG_TAG, "WeatherQueryError", e);
	    }
		
		// Create overlays from map data
		
	}
}
