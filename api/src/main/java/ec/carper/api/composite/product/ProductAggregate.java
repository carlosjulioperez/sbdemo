package ec.carper.api.composite.product;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : carper
 * @created : 2021-02-27
**/
public class ProductAggregate {
      
  private final int productId;
  private final String name;
  private final String description;
  private final List<PriceSummary> prices; 
  private final ServiceAddresses serviceAddress;

  public ProductAggregate(int productId, String name, String description, List<PriceSummary> prices, ServiceAddresses serviceAddress) {
    this.productId = productId;
    this.name = name;
    this.description = description;
    this.prices = prices;
    this.serviceAddress = serviceAddress;
  }

  public int getProductId() {
    return productId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public List<PriceSummary> getPrices() {
    return prices;
  }

  public ServiceAddresses getServiceAddress() {
    return serviceAddress;
  }
}
