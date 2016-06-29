package com.mobyadvert.money.bean;

public class FreshBean {
	private String offerName;
	private String payoutCost;
	private Integer offerID;
	private String imageUrl;

	public FreshBean(String offerName, String payoutCost, Integer offerID, String imageUrl) {
		this.offerID = offerID;
		this.offerName = offerName;
		this.payoutCost = payoutCost;
		this.imageUrl = imageUrl;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public String getPayoutCost() {
		return payoutCost;
	}

	public void setPayoutCost(String payoutCost) {
		this.payoutCost = payoutCost;
	}

	public Integer getOfferID() {
		return offerID;
	}

	public void setOfferID(Integer offerID) {
		this.offerID = offerID;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *            the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
