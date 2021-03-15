package ec.carper.microservices.core.price.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PriceRepository extends CrudRepository<PriceEntity, Integer> {

  @Transactional(readOnly = true)
  List<PriceEntity> findByProductId(int productId);
}
