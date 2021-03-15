package ec.carper.microservices.core.price.persistence;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "prices", indexes = { @Index(name = "prices_unique_idx", unique = true, columnList = "productId,priceId") })
public class PriceEntity {

  @Id @GeneratedValue
  private int id;

  @Version
  private int version;

  private int productId;
  private int priceId;
  private BigDecimal price;
  private BigDecimal offerPrice;

  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDate offerPriceInitialDate;
  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDate offerPriceFinalDate;

  public PriceEntity() {
  }

  public PriceEntity(int id, int version, int productId, int priceId, BigDecimal price, BigDecimal offerPrice, LocalDate offerPriceInitialDate, LocalDate offerPriceFinalDate) {
    this.productId = productId;
    this.priceId = priceId;
    this.price = price;
    this.offerPrice = offerPrice;
    this.offerPriceInitialDate = offerPriceInitialDate;
    this.offerPriceFinalDate = offerPriceFinalDate;
  }


  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getProductId() {
    return productId;
  }

  public void setPriceId(int priceId) {
    this.priceId = priceId;
  }

  public int getPriceId() {
    return priceId;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setOfferPrice(BigDecimal offerPrice) {
    this.offerPrice = offerPrice;
  }

  public BigDecimal getOfferPrice() {
    return offerPrice;
  }

  public void setOfferPriceInitialDate(LocalDate offerPriceInitialDate) {
    this.offerPriceInitialDate = offerPriceInitialDate;
  }

  public LocalDate getOfferPriceInitialDate() {
    return offerPriceInitialDate;
  }

  public void setOfferPriceFinalDate(LocalDate offerPriceFinalDate) {
    this.offerPriceFinalDate = offerPriceFinalDate;
  }

  public LocalDate getOfferPriceFinalDate() {
    return offerPriceFinalDate;
  }
}
