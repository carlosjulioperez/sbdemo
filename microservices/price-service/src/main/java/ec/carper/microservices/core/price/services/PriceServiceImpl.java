package ec.carper.microservices.core.price.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;

import ec.carper.api.core.price.Price;
import ec.carper.api.core.price.PriceService;
import ec.carper.util.exceptions.InvalidInputException;
import ec.carper.util.http.ServiceUtil;

/**
 * @author : carper
 * @created : 2021-02-26
**/

@RestController
public class PriceServiceImpl implements PriceService{

  private static final Logger LOG = LoggerFactory.getLogger(PriceServiceImpl.class);

  private final ServiceUtil serviceUtil;

  @Autowired
  public PriceServiceImpl(ServiceUtil serviceUtil) {
    this.serviceUtil = serviceUtil;
  }

  @Override
  public List<Price> getPrices(int productId) {

    if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

    if (productId == 113) {
      LOG.debug("No prices found for productId: {}", productId);
      return  new ArrayList<>();
    }

    List<Price> list = new ArrayList<>();
    list.add(new Price(productId, 1, new BigDecimal(5.55), new BigDecimal(4.44), "2021-01-01", "2020-01-31", serviceUtil.getServiceAddress()));
    list.add(new Price(productId, 2, new BigDecimal(7.77), new BigDecimal(6.66), "2021-01-01", "2020-01-31", serviceUtil.getServiceAddress()));
    list.add(new Price(productId, 3, new BigDecimal(9.99), new BigDecimal(8.88), "2021-01-01", "2020-01-31", serviceUtil.getServiceAddress())); 

    LOG.debug("/price response size: {}", list.size());

    return list;
  }


}
