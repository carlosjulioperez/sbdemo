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

      productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product";
      priceServiceUrl   = "http://" + priceServiceHost + ":" + priceServicePort + "/price";
    }
    
    @Override
    public Product createProduct(Product body) {

      try {
        String url = productServiceUrl;
        LOG.debug("Will post a new product to URL: {}", url);

        Product product = restTemplate.postForObject(url, body, Product.class);
        LOG.debug("Created a product with id: {}", product.getProductId());

        return product;

      } catch (HttpClientErrorException ex) {
        throw handleHttpClientException(ex);
      }
    }

    public Product getProduct(int productId) {
      try {
        String url = productServiceUrl + "/" + productId;
        LOG.debug("Will call the getProduct API on URL: {}", url);

        Product product = restTemplate.getForObject(url, Product.class);
        LOG.debug("Found a product with id: {}", product.getProductId());

        return product;

      } catch (HttpClientErrorException ex) {
        throw handleHttpClientException(ex);
      }
    }

    @Override
    public void deleteProduct(int productId) {
      try {
        String url = productServiceUrl + "/" + productId;
        LOG.debug("Will call the deleteProduct API on URL: {}", url);

        restTemplate.delete(url);

      } catch (HttpClientErrorException ex) {
        throw handleHttpClientException(ex);
      }
    }

    @Override
    public void deletePrices(int productId) {
        try {
            String url = priceServiceUrl + "?productId=" + productId;
            LOG.debug("Will call the deletePrice API on URL: {}", url);

            restTemplate.delete(url);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    // private String getErrorMessage(HttpClientErrorException ex) {
      // try {
        // return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
      // } catch (IOException ioex) {
        // return ex.getMessage();
      // }
    // }

    @Override
    public Price createPrice(Price body) {

      try {
        String url = priceServiceUrl;
        LOG.debug("Will post a new price to URL: {}", url);

        Price price = restTemplate.postForObject(url, body, Price.class);
        LOG.debug("Created a price with id: {}", price.getProductId());

        return price;

      } catch (HttpClientErrorException ex) {
        throw handleHttpClientException(ex);
      }
    }

    public List<Price> getPrices(int productId) {
      try {
        String url = priceServiceUrl + "?productId=" + productId;

        LOG.debug("Will call the getPrices API on URL: {}", url);
        List<Price> prices = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Price>>() {}).getBody();

        LOG.debug("Found {} prices for a product with id: {}", prices.size(), productId);
        return prices;

      } catch (Exception ex) {
        LOG.warn("Got an exception while requesting prices, return zero prices: {}", ex.getMessage());
        return new ArrayList<>();
      }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
      switch (ex.getStatusCode()) {

        case NOT_FOUND:
          return new NotFoundException(getErrorMessage(ex));

        case UNPROCESSABLE_ENTITY :
          return new InvalidInputException(getErrorMessage(ex));

        default:
          LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
          LOG.warn("Error body: {}", ex.getResponseBodyAsString());
          return ex;
      }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
      try {
        return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
      } catch (IOException ioex) {
        return ex.getMessage();
      }
    }
}

