package com.maxtrain.prsspringboot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "RequestLine")
public class RequestLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name="RequestId")
	private Request request;
	
	@ManyToOne
	@JoinColumn(name="ProductId")
	private Product product;
	
	public RequestLine() {
	}

	public RequestLine(int id, int quantity, Request request, Product product) {
		this.id = id;
		this.quantity = quantity;
		this.request = request;
		this.product = product;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "RequestLine [id=" + id + ", quantity=" + quantity + ", request=" + request + ", product=" + product
				+ "]";
	}
}
