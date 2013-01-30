package com.mobien.webservices;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class XmlNode {
	public static final int TEXT_NODE = 0;
	public static final int ELEMENT_NODE = 1;

	public int nodeType = 0;
	public String nodeName = null;
	public String nodeValue = null;
	public Vector<XmlNode> children = null;
	public Hashtable<String, String> attributes = null;

	public XmlNode(int nodeType) {
		this.nodeType = nodeType;
		this.children = new Vector<XmlNode>();
		this.attributes = new Hashtable<String, String>();
	}

	public String[] getAttributeNames() {
		String[] names = new String[attributes.size()];

		Enumeration<String> e = attributes.keys();

		int i = 0;

		while (e.hasMoreElements()) {
			names[i] = e.nextElement();

			i++;
		}
		return names;
	}

	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

	public String getAttribute(String key) {
		return attributes.get(key);
	}

	public void addChild(XmlNode child) {
		this.children.addElement(child);
	}

}
