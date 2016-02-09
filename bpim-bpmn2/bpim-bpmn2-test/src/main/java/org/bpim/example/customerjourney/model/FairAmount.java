package org.bpim.example.customerjourney.model;

public class FairAmount extends ETollDataObject{
	
	private long price;
	private boolean isDiscounted;
	
	public FairAmount(){
		label = "Fair Amount";
	}
	
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public boolean isDiscounted() {
		return isDiscounted;
	}
	public void setDiscounted(boolean isDiscounted) {
		this.isDiscounted = isDiscounted;
	}

}
