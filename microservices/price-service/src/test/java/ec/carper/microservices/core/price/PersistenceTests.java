package ec.carper.microservices.core.price;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import ec.carper.microservices.core.price.persistence.PriceEntity;
import ec.carper.microservices.core.price.persistence.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) // Reemplaza a: @RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {

    @Autowired
    private PriceRepository repository;

    private PriceEntity savedEntity;

    @BeforeEach
   	public void setupDb() {
   		repository.deleteAll();

        PriceEntity entity = new PriceEntity(1, 2, new BigDecimal("5.55"), new BigDecimal("4.44"), LocalDate.parse("2021-01-01"), LocalDate.parse("2020-01-07"));
        savedEntity = repository.save(entity);

        assertEqualsPrice(entity, savedEntity);
    }


    @Test
   	public void create() {

        PriceEntity newEntity = new PriceEntity(1, 3, new BigDecimal("6.66"), new BigDecimal("5.55"), LocalDate.parse("2021-01-01"), LocalDate.parse("2020-01-07"));
        repository.save(newEntity);

        PriceEntity foundEntity = repository.findById(newEntity.getId()).get();
        assertEqualsPrice(newEntity, foundEntity);

        assertEquals(2, repository.count());
    }

    /*
    @Test
   	public void update() {
        savedEntity.setAuthor("a2");
        repository.save(savedEntity);

        PriceEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (long)foundEntity.getVersion());
        assertEquals("a2", foundEntity.getAuthor());
    }
    */

    @Test
   	public void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
   	public void getByProductId() {
        List<PriceEntity> entityList = repository.findByProductId(savedEntity.getProductId());

        assertThat(entityList, hasSize(1));
        assertEqualsPrice(savedEntity, entityList.get(0));
    }

    private void assertEqualsPrice(PriceEntity expectedEntity , PriceEntity actualEntity) {
        assertEquals(expectedEntity.getId()                    , actualEntity.getId());
        assertEquals(expectedEntity.getVersion()               , actualEntity.getVersion());
        assertEquals(expectedEntity.getProductId()             , actualEntity.getProductId());
        assertEquals(expectedEntity.getPriceId()               , actualEntity.getPriceId());
        assertEquals(expectedEntity.getPrice()                 , actualEntity.getPrice());
        assertEquals(expectedEntity.getOfferPrice()            , actualEntity.getOfferPrice());
        assertEquals(expectedEntity.getOfferPriceInitialDate() , actualEntity.getOfferPriceInitialDate());
        assertEquals(expectedEntity.getOfferPriceFinalDate()   , actualEntity.getOfferPriceFinalDate());
    }
}
