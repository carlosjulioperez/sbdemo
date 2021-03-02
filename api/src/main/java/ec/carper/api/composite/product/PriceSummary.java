package ec.carper.api.composite.product;

import java.math.BigDecimal;

/**
 * @author : carper
 * @created : 2021-03-01
**/
public class PriceSummary {

  private final int priceId;
  private final BigDecimal actualPrice;

  public int getPriceId() {
    return priceId;
  }

  public BigDecimal getActualPrice() {
    return actualPrice;
  }

  public PriceSummary(int priceId, BigDecimal actualPrice) {
    this.priceId = priceId;
    this.actualPrice = actualPrice;
  }
}