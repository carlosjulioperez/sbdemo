package ec.carper.microservices.core.price;

import ec.carper.api.core.price.Price;
import ec.carper.microservices.core.price.persistence.PriceRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
public class PriceServiceApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private PriceRepository repository;

	@BeforeEach
	public void setupDb() {
		repository.deleteAll();
	}

	@Test
	public void getPricesByProductId() {

		int productId = 1;

		postAndVerifyPrice(productId, 1, OK);
		postAndVerifyPrice(productId, 2, OK);
		postAndVerifyPrice(productId, 3, OK);

		assertEquals(3, repository.findByProductId(productId).size());

		getAndVerifyPricesByProductId(productId, OK)
			.jsonPath("$.length()").isEqualTo(3)
			.jsonPath("$[2].productId").isEqualTo(productId)
			.jsonPath("$[2].priceId").isEqualTo(3);
	}

	@Test
	public void duplicateError() {

		int productId = 1;
		int priceId = 1;

		postAndVerifyPrice(productId, priceId, OK)
			.jsonPath("$.productId").isEqualTo(productId)
			.jsonPath("$.priceId").isEqualTo(priceId);

		assertEquals(1, repository.count());

		postAndVerifyPrice(productId, priceId, UNPROCESSABLE_ENTITY)
			.jsonPath("$.path").isEqualTo("/price")
			.jsonPath("$.message").isEqualTo("Duplicate key, Product Id: 1, Price Id:1");

		assertEquals(1, repository.count());
	}

	@Test
	public void deletePrices() {

		int productId = 1;
		int priceId = 1;

		postAndVerifyPrice(productId, priceId, OK);
		assertEquals(1, repository.findByProductId(productId).size());

		deleteAndVerifyPricesByProductId(productId, OK);
		assertEquals(0, repository.findByProductId(productId).size());

		deleteAndVerifyPricesByProductId(productId, OK);
	}

	@Test
	public void getPricesMissingParameter() {

		getAndVerifyPricesByProductId("", BAD_REQUEST)
			.jsonPath("$.path").isEqualTo("/price")
			.jsonPath("$.message").isEqualTo("Required int parameter 'productId' is not present");
	}

	@Test
	public void getPricesInvalidParameter() {

		getAndVerifyPricesByProductId("?productId=no-integer", BAD_REQUEST)
			.jsonPath("$.path").isEqualTo("/price")
			.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getPricesNotFound() {

		getAndVerifyPricesByProductId("?productId=113", OK)
			.jsonPath("$.length()").isEqualTo(0);
	}

	@Test
	public void getPricesInvalidParameterNegativeValue() {

		int productIdInvalid = -1;

		getAndVerifyPricesByProductId("?productId=" + productIdInvalid, UNPROCESSABLE_ENTITY)
			.jsonPath("$.path").isEqualTo("/price")
			.jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
	}

	private WebTestClient.BodyContentSpec getAndVerifyPricesByProductId(int productId, HttpStatus expectedStatus) {
		return getAndVerifyPricesByProductId("?productId=" + productId, expectedStatus);
	}

	private WebTestClient.BodyContentSpec getAndVerifyPricesByProductId(String productIdQuery, HttpStatus expectedStatus) {
		return client.get()
			.uri("/price" + productIdQuery)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody();
	}

	private WebTestClient.BodyContentSpec postAndVerifyPrice(int productId, int priceId, HttpStatus expectedStatus) {
		// Price price = new Price(productId, priceId, "Author " + priceId, priceId, "Content " + priceId, "SA");
    Price price = new Price(productId, priceId, new BigDecimal("5.55"), new BigDecimal("4.44"), LocalDate.parse("2021-01-01"), LocalDate.parse("2020-01-07"), "SA");
		return client.post()
			.uri("/price")
			.body(just(price), Price.class)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody();
	}

	private WebTestClient.BodyContentSpec deleteAndVerifyPricesByProductId(int productId, HttpStatus expectedStatus) {
		return client.delete()
			.uri("/price?productId=" + productId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectBody();
	}

}
