package demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.cdc.demo.Book;
import com.cdc.demo.IdObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContractRestClientApplicationTest.class)

public class ContractRestClientApplicationTest {

	RestTemplate restTemplate;

	@Before
	public void setUp() {

		// given:
		restTemplate = new RestTemplate();
	}

	@Rule
	public StubRunnerRule stubRunnerRule = new StubRunnerRule().downloadStub("com.cdc", "cdc-demo-provider", "1.0.1").withPort(8100)
			.stubsMode(StubRunnerProperties.StubsMode.LOCAL);

	@Test
	public void get_book_from_service_contract() {

		// when:
		ResponseEntity<Book> bookResponseEntity = restTemplate.getForEntity("http://localhost:8100/book-service/books/1", Book.class);

		// then:
		BDDAssertions.then(bookResponseEntity.getStatusCodeValue()).isEqualTo(200);
		BDDAssertions.then(bookResponseEntity.getBody().getId()).isEqualTo(1L);
		BDDAssertions.then(bookResponseEntity.getBody().getName()).isEqualTo("Nutuk");
		BDDAssertions.then(bookResponseEntity.getBody().getAuthor()).isEqualTo("Gazi M.Kemal Atatürk");
		assertThat(bookResponseEntity.getBody().getPrice()).isEqualTo(20d);

	}

	@Test
	public void insert_book_from_service_contract() {

		Book book = new Book(2L, "Fi", "Azra Kohen", 18.00d);
		// when:
		ResponseEntity<IdObject> idResponseEntity = restTemplate.postForEntity("http://localhost:8100/book-service/books", book, IdObject.class);

		// then:
		BDDAssertions.then(idResponseEntity.getStatusCodeValue()).isEqualTo(201);
		BDDAssertions.then(idResponseEntity.getBody().getId()).isEqualTo(2L);

	}

	@Test
	public void update_book_from_service_contract() {

		Book book = new Book(3L, "1984", "George Orwell", 40.00d);
		// when:
		ResponseEntity<Book> bookResponseEntity = restTemplate.exchange("http://localhost:8100/book-service/books/3", HttpMethod.PUT,
				new HttpEntity<Object>(book), Book.class);

		// then:
		BDDAssertions.then(bookResponseEntity.getStatusCodeValue()).isEqualTo(200);
		BDDAssertions.then(bookResponseEntity.getBody().getId()).isEqualTo(3L);
		BDDAssertions.then(bookResponseEntity.getBody().getName()).isEqualTo("1984");
		BDDAssertions.then(bookResponseEntity.getBody().getAuthor()).isEqualTo("George Orwell");
		assertThat(bookResponseEntity.getBody().getPrice()).isEqualTo(40.00d);

	}
}
