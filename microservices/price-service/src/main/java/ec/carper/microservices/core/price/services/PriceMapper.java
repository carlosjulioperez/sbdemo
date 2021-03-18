package ec.carper.microservices.core.price.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ec.carper.api.core.price.Price;
import ec.carper.microservices.core.price.persistence.PriceEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PriceMapper {

   @Mappings({
     @Mapping(target = "serviceAddress", ignore = true)
   })
   Price entityToApi(PriceEntity entity);

   @Mappings({
     @Mapping(target = "id", ignore = true),
     @Mapping(target = "version", ignore = true)
   })
   PriceEntity apiToEntity(Price api);

   List<Price> entityListToApiList(List<PriceEntity> entity);
   List<PriceEntity> apiListToEntityList(List<Price> api);
}
