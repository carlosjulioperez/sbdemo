package ec.carper.api.composite.product;

import java.math.BigDecimal;

/**
 * @author : carper
 * @created : 2021-02-27
**/
public class ProductAggregate {
      
  private final int productId;
  private final String name;
  private final String description;
  private final int priceId;
  private final BigDecimal actualPrice;
  private final boolean offer;
  private final String serviceAddress;

  public ProductAggregate(int productId, String name, String description, int priceId, BigDecimal actualPrice, boolean offer, String serviceAddress) {
    this.productId = productId;
    this.name = name;
    this.description = description;
    this.priceId = priceId;
    this.actualPrice = actualPrice;
    this.offer = offer;
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

  public int getPriceId() {
    return priceId;
  }

  public BigDecimal getActualPrice() {
    return actualPrice;
  }

  public boolean getOffer() {
    return offer;
  }

  public String getServiceAddress() {
    return serviceAddress;
  }
}
