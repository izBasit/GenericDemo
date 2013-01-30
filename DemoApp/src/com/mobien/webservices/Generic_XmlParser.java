package com.mobien.webservices;


import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;

public class Generic_XmlParser {
	public Vector<String> elements = new Vector<String>();

	public XmlNode parseXML(XmlPullParser parser, boolean ignoreWhitespaces)
			throws Exception {
		parser.next();
		return _parse(parser, ignoreWhitespaces);
	}

	XmlNode _parse(XmlPullParser parser, boolean ignoreWhitespaces)
			throws Exception {
		XmlNode node = new XmlNode(XmlNode.ELEMENT_NODE);

		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new Exception("Illegal XML state: " + parser.getName() + ", "
					+ parser.getEventType());
		} else {
			node.nodeName = parser.getName();
			for (int i = 0; i < parser.getAttributeCount(); i++) {
				node.setAttribute(parser.getAttributeName(i),
						parser.getAttributeValue(i));
			}
			parser.next();
			while (parser.getEventType() != XmlPullParser.END_TAG) {
				if (parser.getEventType() == XmlPullParser.START_TAG) {
					node.addChild(_parse(parser, ignoreWhitespaces));
					// System.out.println("1. "+parser.getName());
				} else if (parser.getEventType() == XmlPullParser.TEXT) {
					// System.out.println("2. "+parser.getName());
					if (!ignoreWhitespaces || !parser.isWhitespace()) {
						XmlNode child = new XmlNode(XmlNode.TEXT_NODE);

						child.nodeValue = parser.getText();
						// System.out.println("Got value :"+child.nodeValue);
						elements.addElement(child.nodeValue);
						node.addChild(child);
					}
				}
				parser.next();
			}
		}
		return node;
	}
	
	public String[] getElements(){ 	
		String[] result = new String[ elements.size() ];
		System.out.println("Elements Size ------>"+elements.size());
		if( elements.size()>0 ) 
		{
			for(int loop=0; loop<elements.size(); loop++)
			{
				result[loop] = elements.elementAt(loop);
			}
		}

		return result;

	}
	
	public void setEntity_figure(String entity_figure1){
		elements.addElement(entity_figure1);
	}
}
