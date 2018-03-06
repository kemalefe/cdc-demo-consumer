package com.cdc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ContractRestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContractRestClientApplication.class, args);
	}
}

@RestController
class MessageRestController {

	private final RestTemplate restTemplate;

	MessageRestController(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@RequestMapping("/print/book/{bookId}")
	String getMessage(@PathVariable("bookId") Long bookId) {
		Book book = this.restTemplate.getForObject("http://localhost:8000/book-service/books/{id}", Book.class, bookId);
		return "Book details -> id:" + book.getId() + ", name:" + book.getName() + ", author:" + book.getAuthor() + ", price: " + book.getPrice();
	}

}
