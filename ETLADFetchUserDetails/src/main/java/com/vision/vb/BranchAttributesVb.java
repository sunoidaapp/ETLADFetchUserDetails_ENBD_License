package com.vision.vb;

public class BranchAttributesVb {
	private String attribute = "";
	private String attributeValue = "";
	
	public BranchAttributesVb() {}
	
	public BranchAttributesVb(String attribute, String attributeValue) {
		super();
		this.attribute = attribute;
		this.attributeValue = attributeValue;
	}
	
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
}
