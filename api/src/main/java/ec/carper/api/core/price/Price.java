package ec.carper.api.core.price;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Price {

    private final int productId;
    private final int priceId;
    private final BigDecimal price;
    private final BigDecimal offerPrice;

    @JsonFormat(pattern="yyyy-MM-dd")
    private final LocalDate offerPriceInitialDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private final LocalDate offerPriceFinalDate;

    private final String serviceAddress;

    public Price() {
      this.productId = 0;
      this.priceId = 0;
      this.price = new BigDecimal(0);
      this.offerPrice = new BigDecimal(0);
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

}
