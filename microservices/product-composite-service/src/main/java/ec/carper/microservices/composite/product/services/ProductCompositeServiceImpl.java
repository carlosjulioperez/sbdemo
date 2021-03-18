package ec.carper.microservices.composite.product.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ec.carper.api.composite.product.PriceSummary;
import ec.carper.api.composite.product.ProductAggregate;
import ec.carper.api.composite.product.ProductCompositeService;
import ec.carper.api.composite.product.ServiceAddresses;
import ec.carper.api.core.price.Price;
import ec.carper.api.core.product.Product;
import ec.carper.util.exceptions.NotFoundException;
import ec.carper.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {

  private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

  private final ServiceUtil serviceUtil;
  private ProductCompositeIntegration integration;

  @Autowired
  public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
    this.serviceUtil = serviceUtil;
    this.integration = integration;
  }


  @Override
  public void createCompositeProduct(ProductAggregate body) {

    try {

      LOG.debug("createCompositeProduct: creates a new composite entity for productId: {}", body.getProductId());

      Product product = new Product(body.getProductId(), body.getName(), body.getDescription(), null);
      integration.createProduct(product);

/*
      if (body.getPrices() != null) {
        body.getPrices().forEach(r -> {
          PriceSummary price = new PriceSummary(body.getProductId(), r.getActualPrice());
          integration.createPrice(price);
        });
      }
*/

      LOG.debug("createCompositeProduct: composite entites created for productId: {}", body.getProductId());

    } catch (RuntimeException re) {
      LOG.warn("createCompositeProduct failed", re);
      throw re;
    }
  }

  @Override
  public ProductAggregate getCompositeProduct(int productId) {
    LOG.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);
    Product product = integration.getProduct(productId);
    if (product == null) throw new NotFoundException("No product found for productId: " + productId);

    List<Price> prices = integration.getPrices(productId);
    LOG.debug("getCompositeProduct: aggregate entity found for productId: {}", productId);

    return createProductAggregate(product, prices, serviceUtil.getServiceAddress());
  }
    
  @Override
  public void deleteCompositeProduct(int productId) {
    LOG.debug("deleteCompositeProduct: Deletes a product aggregate for productId: {}", productId);
    integration.deleteProduct(productId);
    integration.deletePrices(productId);
    LOG.debug("getCompositeProduct: aggregate entities deleted for productId: {}", productId);
  }

  private ProductAggregate createProductAggregate(Product product, List<Price> prices, String serviceAddress) {

    List<PriceSummary> priceSummaries = (prices == null) ? null :
      prices.stream()
      .map(r -> new PriceSummary(r.getPriceId(), r.getPrice()))
      .collect(Collectors.toList());

    String productAddress = product.getServiceAddress();
    String priceAddress = (prices != null && prices.size() > 0) ? prices.get(0).getServiceAddress() : "";
    ServiceAddresses serviceAddresses = new ServiceAddresses (serviceAddress, productAddress, priceAddress);

    return new ProductAggregate(
        product.getProductId(),
        product.getName(),
        product.getDescription(),
        priceSummaries,
        serviceAddresses
        );
  }
}
