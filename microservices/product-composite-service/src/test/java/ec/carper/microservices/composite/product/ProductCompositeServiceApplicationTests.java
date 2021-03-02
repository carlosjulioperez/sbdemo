package ec.carper.microservices.composite.product;

import java.math.BigDecimal;
import java.time.LocalDate;

import ec.carper.api.core.price.Price;
import ec.carper.api.core.product.Product;
import ec.carper.microservices.composite.product.services.ProductCompositeIntegration;
import ec.carper.util.exceptions.InvalidInputException;
import ec.carper.util.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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
    public void getProductById() {

      client.get()
        .uri("/product-composite/" + PRODUCT_ID_OK)
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.productId").isEqualTo(PRODUCT_ID_OK)
        .jsonPath("$.prices.length()").isEqualTo(1);
    }

    @Test
    public void getProductNotFound() {

      client.get()
        .uri("/product-composite/" + PRODUCT_ID_NOT_FOUND)
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound()
        .expectHeader().contentType(APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_NOT_FOUND)
        .jsonPath("$.message").isEqualTo("NOT FOUND: " + PRODUCT_ID_NOT_FOUND);
    }

    @Test
    public void getProductInvalidInput() {

      client.get()
        .uri("/product-composite/" + PRODUCT_ID_INVALID)
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
        .expectHeader().contentType(APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_INVALID)
        .jsonPath("$.message").isEqualTo("INVALID: " + PRODUCT_ID_INVALID);
    }
  }

