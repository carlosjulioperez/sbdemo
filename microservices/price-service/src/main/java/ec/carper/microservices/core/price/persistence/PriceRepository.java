package ec.carper.microservices.core.price.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PriceRepository extends CrudRepository<PriceEntity, String> {
  List<PriceEntity> findByProductId(int productId);
}
