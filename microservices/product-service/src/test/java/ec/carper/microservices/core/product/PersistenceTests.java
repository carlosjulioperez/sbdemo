package ec.carper.microservices.core.product;

import ec.carper.microservices.core.product.persistence.ProductEntity;
import ec.carper.microservices.core.product.persistence.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.dao.OptimisticLockingFailureException;

//import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;

//import static java.util.stream.IntStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.data.domain.Sort.Direction.ASC;
@ExtendWith(SpringExtension.class) // Reemplaza a: @RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {

    @Autowired
    private ProductRepository repository;

    private ProductEntity savedEntity;

    @BeforeEach
    public void setupDb() {
        repository.deleteAll();

        ProductEntity entity = new ProductEntity(1, "n", "d");
        savedEntity = repository.save(entity);

        assertEqualsProduct(entity, savedEntity);
    }

    @Test
    public void create() {

        ProductEntity newEntity = new ProductEntity(2, "n", "d");
        repository.save(newEntity);

        ProductEntity foundEntity = repository.findById(newEntity.getId()).get();
        assertEqualsProduct(newEntity, foundEntity);

        assertEquals(2, repository.count());
    }

    @Test
    public void update() {
        savedEntity.setName("n2");
        repository.save(savedEntity);

        ProductEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (long) foundEntity.getVersion());
        assertEquals("n2", foundEntity.getName());
    }

    @Test
    public void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    public void getByProductId() {
        Optional<ProductEntity> entity = repository.findByProductId(savedEntity.getProductId());

        assertTrue(entity.isPresent());
        assertEqualsProduct(savedEntity, entity.get());
    }

    private void assertEqualsProduct(ProductEntity expectedEntity, ProductEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
        assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
        assertEquals(expectedEntity.getName(), actualEntity.getName());
        assertEquals(expectedEntity.getDescription(), actualEntity.getDescription());
    }
}