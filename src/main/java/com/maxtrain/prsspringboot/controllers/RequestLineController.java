package com.maxtrain.prsspringboot.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxtrain.prsspringboot.entities.Product;
import com.maxtrain.prsspringboot.entities.Request;
import com.maxtrain.prsspringboot.entities.RequestLine;
import com.maxtrain.prsspringboot.repositories.RequestLineRepository;
import com.maxtrain.prsspringboot.repositories.RequestRepository;

@RestController
@RequestMapping("/request-lines")
public class RequestLineController {

	@Autowired
	private RequestLineRepository requestLineRepo;
	
	@Autowired
	private RequestRepository requestRepo;
	
	
	@GetMapping("")
	public List<RequestLine> getAll() {
		List<RequestLine> requestLines = requestLineRepo.findAll();
		
		return requestLines;
	}
	
	@GetMapping("/{id}")
	public RequestLine getById(@PathVariable int id) {
		RequestLine requestLine = new RequestLine();
		Optional<RequestLine> optionalRequestLine = requestLineRepo.findById(id);
		
		if (optionalRequestLine.isPresent()) {
			requestLine = optionalRequestLine.get();
		}
		
		return requestLine;
	}
	
	@PostMapping("")
	public RequestLine create(@RequestBody RequestLine newRequestLine) {
		RequestLine requestLine = new RequestLine();
		
		boolean requestLineExists = requestLineRepo.findById(newRequestLine.getId()).isPresent();
		
		if (!requestLineExists) {
			requestLine = requestLineRepo.save(newRequestLine);
			//Optional<RequestLine> optionalRequestLine = requestLineRepo.findById(requestLine.getId());
			//requestLine = optionalRequestLine.get();
			recalculateTotal(requestLine.getRequest());
		}
		return requestLine;
	}
	
	@PutMapping("")
	public RequestLine update(@RequestBody RequestLine updatedRequestLine) {
		RequestLine requestLine = new RequestLine();
	
		boolean requestLineExists = requestLineRepo.findById(updatedRequestLine.getId()).isPresent();
	
		if (requestLineExists) {
			requestLine = requestLineRepo.save(updatedRequestLine);
			recalculateTotal(requestLine.getRequest());
		}
		
		return requestLine;
	}
	
	@DeleteMapping("/{id}")
	public RequestLine deleteById(@PathVariable int id) {
		RequestLine requestLine = new RequestLine();
		Optional<RequestLine> optionalRequestLine = requestLineRepo.findById(id);
		
		if (optionalRequestLine.isPresent()) {
			requestLine = optionalRequestLine.get();
			Request request = requestLine.getRequest();
			requestLineRepo.deleteById(id);
			recalculateTotal(request);
		}
		
		return requestLine;
	}
	
	@GetMapping("/lines-for-request/{id}")
	public List<RequestLine> getByRequestId(@PathVariable int id) {
		List<RequestLine> requestLines = new ArrayList<RequestLine>();
		
		Optional<Request> optionalRequest = requestRepo.findById(id);
		
		if (optionalRequest.isPresent()) {
			Request request = optionalRequest.get();
			
			requestLines = requestLineRepo.findByRequest(request);
		}
		
		return requestLines;
	}
	
	private void recalculateTotal (Request request) {
		
		List<RequestLine> requestLines = requestLineRepo.findByRequest(request);

		double total = 0;
		
		for (RequestLine reqLine : requestLines) {
			
			Product product = reqLine.getProduct();
			
			total += product.getPrice() * reqLine.getQuantity();
		}
		
		request.setTotal(total);
		requestRepo.save(request);
	}
}