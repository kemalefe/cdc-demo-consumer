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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.cdc.demo.Book;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=ContractRestClientApplicationTest.class)

public class ContractRestClientApplicationTest {
	
	RestTemplate restTemplate;

	@Before
	public void setUp(){
		restTemplate = new RestTemplate();
	}
	@Rule
	public StubRunnerRule stubRunnerRule = new StubRunnerRule()
		.downloadStub("com.cdc", "cdc-demo-provider", "1.0.0")
		.withPort(8100)
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
}
