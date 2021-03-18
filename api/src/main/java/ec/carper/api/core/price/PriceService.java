package ec.carper.api.core.price;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PriceService {

  /**
   * Sample usage:
   *
   * curl -X POST $HOST:$PORT/price \
   *   -H "Content-Type: application/json" --data \
   *   '{"productId":123,"priceId":456,"price":11.5,"offerPrice":10.5,"offerPriceInitialDate":2021-03-16,"offerPriceFinalDate":"2021-03-18"}'
   *
   * @param body
   * @return
   */
  @PostMapping(
    value    = "/price",
    consumes = "application/json",
    produces = "application/json")
  Price createPrice(@RequestBody Price body);

  /**
   * Sample usage: curl $HOST:$PORT/price?productId=1
   *
   * @param productId
   * @return
   */
  @GetMapping(
    value    = "/price",
    produces = "application/json")
  List<Price> getPrices(@RequestParam(value = "productId", required = true) int productId);

  /**
   * Sample usage:
   *
   * curl -X DELETE $HOST:$PORT/price?productId=1
   *
   * @param productId
   */
  @DeleteMapping(value = "/price")
  void deletePrices(@RequestParam(value = "productId", required = true)  int productId);
}
