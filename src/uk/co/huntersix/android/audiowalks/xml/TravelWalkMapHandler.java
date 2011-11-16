package uk.co.huntersix.android.audiowalks.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uk.co.huntersix.android.audiowalks.model.Placemark;
import uk.co.huntersix.android.audiowalks.model.PlacemarkPoint;
import uk.co.huntersix.android.audiowalks.model.TravelWalkMap;

public class TravelWalkMapHandler extends DefaultHandler {
	private boolean documentTag = false;
	private boolean nameTag = false;
	private boolean descriptionTag = false;
	private boolean placemarkTag = false;
	private boolean pointTag = false;
	private boolean coordTag = false;
	private String tempStringValue;

	private TravelWalkMap travelWalkMap;
	private Placemark placemark;
	private PlacemarkPoint placemarkPoint;

	public TravelWalkMap getParsedData() {
		return this.travelWalkMap;
	}

	@Override
	public void startDocument() throws SAXException {
		this.travelWalkMap = new TravelWalkMap();
	}

	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}

	/**
	 * Gets be called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (localName.equalsIgnoreCase("document")) {
			documentTag = true;
			
			// Start new travel walk map
			travelWalkMap = new TravelWalkMap();
		} 
		else if (documentTag && !placemarkTag) {
			if (localName.equalsIgnoreCase("name")) {
				nameTag = true;
				
				travelWalkMap.name = atts.getValue("name");
			} 
			else if (localName.equalsIgnoreCase("description")) {
				descriptionTag = true;
				
				travelWalkMap.description = atts.getValue("description");
			}
			else if (localName.equalsIgnoreCase("placemark")) {
				placemarkTag = true;

				// Start a new placemark
				placemark = new Placemark();
			}
		}
		else if (documentTag && placemarkTag) {
			if (localName.equalsIgnoreCase("name")) {
				nameTag = true;
				
				placemark.name = atts.getValue("name");
			} 
			else if (localName.equalsIgnoreCase("description")) {
				descriptionTag = true;
				
				placemark.description = atts.getValue("description");
			}
			else if (localName.equalsIgnoreCase("point")) {
				pointTag = true;
				
				placemarkPoint = new PlacemarkPoint();
			}
			else if (pointTag && localName.equalsIgnoreCase("coordinates")) {
				coordTag = true;
			}
		}
	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase("document")) {
			documentTag = false;
		} 
		else if (localName.equalsIgnoreCase("name")) {
			if (placemarkTag) {
				placemark.name = tempStringValue;
			}
			else {
				travelWalkMap.name = tempStringValue;
			}

			nameTag = false;
		} 
		else if (localName.equalsIgnoreCase("description")) {
			if (placemarkTag) {
				placemark.description = tempStringValue;
			}
			else {
				travelWalkMap.description = tempStringValue;
			}

			descriptionTag = false;
		} 
		else if (localName.equalsIgnoreCase("placemark")) {
			placemarkTag = false;
			
			travelWalkMap.placemarks.add(placemark);
			placemark = null;
		}
		else if (localName.equalsIgnoreCase("point")) {
			placemark.point = placemarkPoint;

			pointTag = false;
		}
		else if (localName.equalsIgnoreCase("coordinates")) {
			coordTag = false;
		}
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		if (nameTag || descriptionTag) {
			tempStringValue = new String(ch, start, length);
		}
		else if (coordTag) {
			try {
				String[] temp = new String(ch, start, length).split(",");
				placemarkPoint.coord1 = new Double(temp[1]) * 1E6;
				placemarkPoint.coord2 = new Double(temp[0]) * 1E6;
			}
			catch (Exception e) {
				// Sometimes an outofboundsexception occurs here but I can't reproduce it in debug mode
				// ignore - for now
			}
		}
	}
}
