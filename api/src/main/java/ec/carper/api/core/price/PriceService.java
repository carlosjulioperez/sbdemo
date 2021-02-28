package ec.carper.api.core.price;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface PriceService {

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

}
