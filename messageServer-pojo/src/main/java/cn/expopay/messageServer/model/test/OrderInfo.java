package cn.expopay.messageServer.model.test;

import java.io.Serializable;

public class OrderInfo implements Serializable {
	
	private String id;
	private String cardNum;
	private double monery;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public double getMonery() {
		return monery;
	}
	public void setMonery(double monery) {
		this.monery = monery;
	}
	
	@Override
	public String toString() {
		return "OrderInfo [id=" + id + ", cardNum=" + cardNum + ", monery=" + monery + "]";
	}
	
}
