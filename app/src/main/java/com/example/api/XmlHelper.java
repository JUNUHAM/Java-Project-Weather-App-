package com.example.api;

import org.w3c.dom.Node;
//텍스트 반환해주는 클래스 
public class XmlHelper {	
	public static String getNodeTextContent(Node node) {
        if (node != null && node.getFirstChild() != null) {
            return node.getFirstChild().getNodeValue();
        } else {
            return "";
        }
    }
}
