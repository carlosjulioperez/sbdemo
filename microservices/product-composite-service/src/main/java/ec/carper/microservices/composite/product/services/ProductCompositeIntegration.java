package ec.carper.microservices.composite.product.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.carper.api.core.price.Price;
import ec.carper.api.core.price.PriceService;
import ec.carper.api.core.product.Product;
import ec.carper.api.core.product.ProductService;
import ec.carper.util.exceptions.InvalidInputException;
import ec.carper.util.exceptions.NotFoundException;
import ec.carper.util.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static org.springframework.http.HttpMethod.GET;

/**
 * @author : carper
 * @created : 2021-02-28
**/
@Component
public class ProductCompositeIntegration implements ProductService, PriceService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String priceServiceUrl;

    @Autowired
    public ProductCompositeIntegration(
        RestTemplate restTemplate,
        ObjectMapper mapper,

        @Value("${app.product-service.host}") String productServiceHost,
        @Value("${app.product-service.port}") int    productServicePort,

        @Value("${app.price-service.host}") String priceServiceHost,
        @Value("${app.price-service.port}") int    priceServicePort
        ) {

      this.restTemplate = restTemplate;
      this.mapper = mapper;

      productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
      priceServiceUrl   = "http://" + priceServiceHost + ":" + priceServicePort + "/price?productId=";
    }
    
    public Product getProduct(int productId) {

      try {
        String url = productServiceUrl + productId;
        LOG.debug("Will call getProduct API on URL: {}", url);

        Product product = restTemplate.getForObject(url, Product.class);
        LOG.debug("Found a product with id: {}", product.getProductId());

        return product;

      } catch (HttpClientErrorException ex) {

        switch (ex.getStatusCode()) {

          case NOT_FOUND:
            throw new NotFoundException(getErrorMessage(ex));

          case UNPROCESSABLE_ENTITY :
            throw new InvalidInputException(getErrorMessage(ex));

          default:
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
      }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
      try {
        return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
      } catch (IOException ioex) {
        return ex.getMessage();
      }
    }

    public List<Price> getPrices(int productId) {

      try {
        String url = priceServiceUrl + productId;

        LOG.debug("Will call getPrices API on URL: {}", url);
        List<Price> prices = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Price>>() {}).getBody();

        LOG.debug("Found {} prices for a product with id: {}", prices.size(), productId);
        return prices;

      } catch (Exception ex) {
        LOG.warn("Got an exception while requesting prices, return zero prices: {}", ex.getMessage());
        return new ArrayList<>();
      }
    }

}

