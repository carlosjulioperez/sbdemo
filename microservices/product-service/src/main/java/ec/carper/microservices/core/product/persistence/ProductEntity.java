package ec.carper.microservices.core.product.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="products")
public class ProductEntity {

  @Id
  private String id;

  @Version
  private Integer version;

  @Indexed(unique = true)
  private int productId;
    
  private String name;

  private String description;

  public ProductEntity() {
  }

  public ProductEntity(int productId, String name, String description) {
    this.productId = productId;
    this.name = name;
    this.description = description;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Integer getVersion() {
    return version;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getProductId() {
    return productId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
