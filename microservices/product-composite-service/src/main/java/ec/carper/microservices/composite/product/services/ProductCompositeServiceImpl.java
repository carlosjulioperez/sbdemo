package ec.carper.microservices.composite.product.services;

import java.util.List;
import java.util.stream.Collectors;

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

  private final ServiceUtil serviceUtil;
  private ProductCompositeIntegration integration;

  @Autowired
  public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
    this.serviceUtil = serviceUtil;
    this.integration = integration;
  }

  @Override
  public ProductAggregate getProduct(int productId) {

    Product product = integration.getProduct(productId);
    if (product == null) throw new NotFoundException("No product found for productId: " + productId);

    List<Price> prices = integration.getPrices(productId);

    return createProductAggregate(product, prices, serviceUtil.getServiceAddress());
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
