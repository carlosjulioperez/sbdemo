package ec.carper.api.core.product;

public class Product{

    private final int productId;
    private final String nombre;
    private final String descripcion;
    private final String serviceAddress;

    public Product() {
		this.productId = 0;
		this.nombre = null;
		this.descripcion = null;
		this.serviceAddress = null;
	}

	public Product(int productId, String nombre, String descripcion, String serviceAddress) {
		this.productId = productId;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.serviceAddress = serviceAddress;
	}

	public int getProductId() {
		return productId;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

}
