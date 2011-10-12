package uk.co.huntersix.android.audiowalks.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uk.co.huntersix.android.audiowalks.model.Placemark;
import uk.co.huntersix.android.audiowalks.model.TravelWalkMap;

public class TravelWalkMapHandler extends DefaultHandler {
	private boolean documentTag = false;
	private boolean nameTag = false;
	private boolean descriptionTag = false;
	private boolean placemarkTag = false;
	private boolean in_mytag = false;

	private TravelWalkMap travelWalkMap;
	private Placemark placemark;

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
				in_mytag = true;
				
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
		}
	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase("document")) {
			this.documentTag = false;
		} else if (localName.equalsIgnoreCase("name")) {
			this.nameTag = false;
		} else if (localName.equalsIgnoreCase("description")) {
			this.descriptionTag = false;
		} else if (localName.equalsIgnoreCase("placemark")) {
			this.placemarkTag = false;
			
			travelWalkMap.placemarks.add(placemark);
			placemark = null;
		}
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
//	@Override
//	public void characters(char ch[], int start, int length) {
//		if (this.in_mytag) {
//			travelWalkMap.setExtractedString(new String(ch, start, length));
//		}
//	}
}
