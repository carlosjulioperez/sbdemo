package ec.carper.microservices.core.price;

import ec.carper.api.core.price.Price;
import ec.carper.microservices.core.price.persistence.PriceEntity;
import ec.carper.microservices.core.price.services.PriceMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class MapperTests {

    private PriceMapper mapper = Mappers.getMapper(PriceMapper.class);

    @Test
    public void mapperTests() {

        assertNotNull(mapper);

        Price api = new Price(1, 2, new BigDecimal("5.55"), new BigDecimal("4.44"), LocalDate.parse("2021-01-01"), LocalDate.parse("2020-01-07"), "add");

        PriceEntity entity = mapper.apiToEntity(api);

        assertEquals(api.getProductId()             , entity.getProductId());
        assertEquals(api.getPriceId()               , entity.getPriceId());
        assertEquals(api.getPrice()                 , entity.getPrice());
        assertEquals(api.getOfferPrice()            , entity.getOfferPrice());
        assertEquals(api.getOfferPriceInitialDate() , entity.getOfferPriceInitialDate());
        assertEquals(api.getOfferPriceFinalDate()   , entity.getOfferPriceFinalDate());

        Price api2 = mapper.entityToApi(entity);

        assertEquals(api.getProductId()             , api2.getProductId());
        assertEquals(api.getPriceId()               , api2.getPriceId());
        assertEquals(api.getPrice()                 , api2.getPrice());
        assertEquals(api.getOfferPrice()            , api2.getOfferPrice());
        assertEquals(api.getOfferPriceInitialDate() , api2.getOfferPriceInitialDate());
        assertEquals(api.getOfferPriceFinalDate()   , api2.getOfferPriceFinalDate());
        
        assertNull(api2.getServiceAddress());
    }

    @Test
    public void mapperListTests() {

        assertNotNull(mapper);

        Price api = new Price(1, 2, new BigDecimal("5.55"), new BigDecimal("4.44"), LocalDate.parse("2021-01-01"), LocalDate.parse("2020-01-07"), "add");

        // Price api = new Price(1, 2, "a", 4, "C", "adr");
        List<Price> apiList = Collections.singletonList(api);

        List<PriceEntity> entityList = mapper.apiListToEntityList(apiList);
        assertEquals(apiList.size(), entityList.size());

        PriceEntity entity = entityList.get(0);

        assertEquals(api.getProductId(), entity.getProductId());
        assertEquals(api.getPriceId(), entity.getPriceId());
        assertEquals(api.getPrice(), entity.getPrice());
        assertEquals(api.getOfferPrice(), entity.getOfferPrice());
        assertEquals(api.getOfferPriceInitialDate(), entity.getOfferPriceInitialDate());
        assertEquals(api.getOfferPriceFinalDate(), entity.getOfferPriceFinalDate());

        List<Price> api2List = mapper.entityListToApiList(entityList);
        assertEquals(apiList.size(), api2List.size());

        Price api2 = api2List.get(0);

        assertEquals(api.getProductId(), api2.getProductId());
        assertEquals(api.getPriceId(), api2.getPriceId());
        assertEquals(api.getPrice(), api2.getPrice());
        assertEquals(api.getOfferPrice(), api2.getOfferPrice());
        assertEquals(api.getOfferPriceInitialDate(), api2.getOfferPriceInitialDate());
        assertEquals(api.getOfferPriceFinalDate(), api2.getOfferPriceFinalDate());
        assertNull(api2.getServiceAddress());
    }
}
