package ec.carper.microservices.core.price.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="prices")
@CompoundIndex(name = "prod-pri-id", unique = true, def = "{'productId': 1, 'priceId' : 1}")
public class PriceEntity {

  @Id
  private String id;

  @Version
  private Integer version;

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

  public PriceEntity(int productId, int priceId, BigDecimal price, BigDecimal offerPrice, LocalDate offerPriceInitialDate, LocalDate offerPriceFinalDate) {
    this.productId = productId;
    this.priceId = priceId;
    this.price = price;
    this.offerPrice = offerPrice;
    this.offerPriceInitialDate = offerPriceInitialDate;
    this.offerPriceFinalDate = offerPriceFinalDate;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Integer getVersion() {
    return version;
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
