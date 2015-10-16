package org.bpim.objects.model;

import org.bpim.objects.ETollDataObject;

public class ImageProcessingResult extends ETollDataObject{
	private int confidenceRate;
	private String plateNumber;
	private Object normalizedImage;
	private boolean reviewedByHuman;
	private boolean successful;
	
	
	public int getConfidenceRate() {
		return confidenceRate;
	}
	public void setConfidenceRate(int confidenceRate) {
		this.confidenceRate = confidenceRate;
	}
	public Object getNormalizedImage() {
		return normalizedImage;
	}
	public void setNormalizedImage(Object normalizedImage) {
		this.normalizedImage = normalizedImage;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public boolean isReviewedByHuman() {
		return reviewedByHuman;
	}
	public void setReviewedByHuman(boolean reviewedByHuman) {
		this.reviewedByHuman = reviewedByHuman;
	}
	public boolean isSuccessful() {
		return successful;
	}
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

}
