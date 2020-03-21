package com.vin.vertx.model;

public class Tracker {
	
	private String state;
	private String country;
	private int confirmed;
	private int deaths;
	private int recovered;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	public int getRecovered() {
		return recovered;
	}
	public void setRecovered(int recovered) {
		this.recovered = recovered;
	}
	@Override
	public String toString() {
		return "Tracker [state=" + state + ", country=" + country + ", confirmed=" + confirmed + ", deaths=" + deaths
				+ ", recovered=" + recovered + "]";
	}
	
}
