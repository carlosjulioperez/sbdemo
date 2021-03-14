package ec.carper.api.core.price;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Price {

  private int productId;
  private int priceId;
  private BigDecimal price;
  private BigDecimal offerPrice;

  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDate offerPriceInitialDate;
  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDate offerPriceFinalDate;

  private String serviceAddress;

  public Price() {
    this.productId = 0;
    this.priceId = 0;
    this.price = new BigDecimal("0");
    this.offerPrice = new BigDecimal("0");
    this.offerPriceInitialDate = null;
    this.offerPriceFinalDate = null;
    this.serviceAddress = null;

  }

  public Price(int productId, int priceId, BigDecimal price, BigDecimal offerPrice, LocalDate offerPriceInitialDate, LocalDate offerPriceFinalDate, String serviceAddress) {
    this.productId = productId;
    this.priceId = priceId;
    this.price = price;
    this.offerPrice = offerPrice;
    this.offerPriceInitialDate = offerPriceInitialDate;
    this.offerPriceFinalDate = offerPriceFinalDate;
    this.serviceAddress = serviceAddress;
  }

  public int getProductId() {
    return productId;
  }

  public int getPriceId() {
    return priceId;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getOfferPrice() {
    return offerPrice;
  }

  public LocalDate getOfferPriceInitialDate() {
    return offerPriceInitialDate;
  }

  public LocalDate getOfferPriceFinalDate() {
    return offerPriceFinalDate;
  }

  public String getServiceAddress() {
    return serviceAddress;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public void setPriceId(int priceId) {
    this.priceId = priceId;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public void setOfferPrice(BigDecimal offerPrice) {
    this.offerPrice = offerPrice;
  }

  public void setOfferPriceInitialDate(LocalDate offerPriceInitialDate) {
    this.offerPriceInitialDate = offerPriceInitialDate;
  }

  public void setOfferPriceFinalDate(LocalDate offerPriceFinalDate) {
    this.offerPriceFinalDate = offerPriceFinalDate;
  }

  public void setServiceAddress(String serviceAddress) {
    this.serviceAddress = serviceAddress;
  }
}
