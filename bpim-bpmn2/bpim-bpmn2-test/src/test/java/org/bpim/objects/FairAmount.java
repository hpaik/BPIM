package org.bpim.objects;

public class FairAmount extends ETollDataObject{
	
	private long price;
	private boolean isDiscounted;
	
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
