package com.bank.transactions.mode;

public class Transaction {
	private final String id;
	private final double amount;
	private final String date;
	private String status;
	
	public Transaction(String id, double amount, String date, String status) {
		this.id = id;
		this.amount = amount;
		this.date = date;
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
}
