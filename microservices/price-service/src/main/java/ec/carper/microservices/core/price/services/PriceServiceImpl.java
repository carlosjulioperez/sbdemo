package ec.carper.microservices.core.price.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.carper.microservices.core.price.persistence.PriceEntity;
import ec.carper.microservices.core.price.persistence.PriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;

import ec.carper.api.core.price.Price;
import ec.carper.api.core.price.PriceService;
import ec.carper.util.exceptions.InvalidInputException;
import ec.carper.util.http.ServiceUtil;

import java.util.List;

/**
 * @author : carper
 * @created : 2021-02-26
**/

@RestController
public class PriceServiceImpl implements PriceService{

  private static final Logger LOG = LoggerFactory.getLogger(PriceServiceImpl.class);

  private final PriceRepository repository;
  private final PriceMapper mapper;
  private final ServiceUtil serviceUtil;

  @Autowired
  public PriceServiceImpl(PriceRepository repository, PriceMapper mapper, ServiceUtil serviceUtil) {
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
  }

  @Override
  public Price createPrice(Price body) {
    try {
      PriceEntity entity = mapper.apiToEntity(body);
      PriceEntity newEntity = repository.save(entity);

      LOG.debug("createPrice: created a price entity: {}/{}", body.getProductId(), body.getPriceId());
      return mapper.entityToApi(newEntity);

    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Price Id:" + body.getPriceId());
    }

  }

  @Override
  public List<Price> getPrices(int productId) {

    if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

    if (productId == 113) {
      LOG.debug("No prices found for productId: {}", productId);
      return  new ArrayList<>();
    }

    List<Price> list = new ArrayList<>();
    list.add(new Price(productId, 1, new BigDecimal("5.55"), new BigDecimal("4.44"), LocalDate.parse("2021-01-01"), LocalDate.parse("2020-01-07"), serviceUtil.getServiceAddress()));
    list.add(new Price(productId, 2, new BigDecimal("7.77"), new BigDecimal("6.66"), LocalDate.parse("2021-01-08"), LocalDate.parse("2020-01-14"), serviceUtil.getServiceAddress()));
    list.add(new Price(productId, 3, new BigDecimal("9.99"), new BigDecimal("8.88"), LocalDate.parse("2021-01-15"), LocalDate.parse("2020-01-21"), serviceUtil.getServiceAddress())); 

    LOG.debug("/price response size: {}", list.size());

    return list;
  }

  @Override
    public void deletePrices(int productId) {
        LOG.debug("deletePrices: tries to delete prices for the product with productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId));
    }


}
