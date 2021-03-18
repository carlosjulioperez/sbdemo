package ec.carper.microservices.composite.product;

import java.math.BigDecimal;
import java.time.LocalDate;

import ec.carper.api.composite.product.PriceSummary;
import ec.carper.api.composite.product.ProductAggregate;
import ec.carper.api.core.price.Price;
import ec.carper.api.core.product.Product;
import ec.carper.microservices.composite.product.services.ProductCompositeIntegration;
import ec.carper.util.exceptions.InvalidInputException;
import ec.carper.util.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class) // Reemplaza a: @RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
public class ProductCompositeServiceApplicationTests {

  private static final int PRODUCT_ID_OK = 1;
  private static final int PRODUCT_ID_NOT_FOUND = 2;
  private static final int PRODUCT_ID_INVALID = 3;

  @Autowired
  private WebTestClient client;

  @MockBean
  private ProductCompositeIntegration compositeIntegration;

  @BeforeEach
  public void setUp() {

    when(compositeIntegration.getProduct(PRODUCT_ID_OK)).
      thenReturn(new Product(PRODUCT_ID_OK, "name", "description", "mock-address"));
    when(compositeIntegration.getPrices(PRODUCT_ID_OK)).
      thenReturn(singletonList(new Price(PRODUCT_ID_OK, 1, new BigDecimal("5.55"), new BigDecimal("4.44"), LocalDate.parse("2021-01-01"), LocalDate.parse("2020-01-07"), "mock address")));

    when(compositeIntegration.getProduct(PRODUCT_ID_NOT_FOUND)).thenThrow(new NotFoundException("NOT FOUND: " + PRODUCT_ID_NOT_FOUND));

    when(compositeIntegration.getProduct(PRODUCT_ID_INVALID)).thenThrow(new InvalidInputException("INVALID: " + PRODUCT_ID_INVALID));
  }

  @Test
  public void contextLoads() {
  }

  @Test
  public void createCompositeProduct1() {

    ProductAggregate compositeProduct = new ProductAggregate(1, "name", "desc", null, null);

    postAndVerifyProduct(compositeProduct, OK);
  }

  @Test
  public void createCompositeProduct2() {
    ProductAggregate compositeProduct = new ProductAggregate(1, "name", "desc",
        singletonList(new PriceSummary(1, new BigDecimal("5.55"))), null);

    postAndVerifyProduct(compositeProduct, OK);
  }

  @Test
  public void deleteCompositeProduct() {
    ProductAggregate compositeProduct = new ProductAggregate(1, "name", "desc",
        singletonList(new PriceSummary(1, new BigDecimal("5.55"))), null);

    postAndVerifyProduct(compositeProduct, OK);

    deleteAndVerifyProduct(compositeProduct.getProductId(), OK);
    deleteAndVerifyProduct(compositeProduct.getProductId(), OK);
  }

  @Test
  public void getProductById() {

    getAndVerifyProduct(PRODUCT_ID_OK, OK)
      .jsonPath("$.productId").isEqualTo(PRODUCT_ID_OK)
      .jsonPath("$.prices.length()").isEqualTo(1);
  }

  @Test
  public void getProductNotFound() {

    getAndVerifyProduct(PRODUCT_ID_NOT_FOUND, NOT_FOUND)
      .jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_NOT_FOUND)
      .jsonPath("$.message").isEqualTo("NOT FOUND: " + PRODUCT_ID_NOT_FOUND);
  }

  @Test
  public void getProductInvalidInput() {

    getAndVerifyProduct(PRODUCT_ID_INVALID, UNPROCESSABLE_ENTITY)
      .jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_INVALID)
      .jsonPath("$.message").isEqualTo("INVALID: " + PRODUCT_ID_INVALID);
  }

  private WebTestClient.BodyContentSpec getAndVerifyProduct(int productId, HttpStatus expectedStatus) {
    return client.get()
      .uri("/product-composite/" + productId)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody();
  }

  private void postAndVerifyProduct(ProductAggregate compositeProduct, HttpStatus expectedStatus) {
    client.post()
      .uri("/product-composite")
      .body(just(compositeProduct), ProductAggregate.class)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus);
  }

  private void deleteAndVerifyProduct(int productId, HttpStatus expectedStatus) {
    client.delete()
      .uri("/product-composite/" + productId)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus);
  }
}
