package com.emard.jms.jmsfundamentals.messagestructure;

import java.io.Serializable;

public class Patient implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	
	public Patient(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Patient [id=" + id + ", name=" + name + "]";
	}
	
}
