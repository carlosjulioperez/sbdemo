package ec.carper.api.composite.product;

public class ServiceAddresses {
    private final String cmp;
    private final String pro;
    private final String pri;

    public ServiceAddresses() {
        cmp = null;
        pro = null;
        pri = null;
    }

    public ServiceAddresses(String compositeAddress, String productAddress, String priceAddress) {
        this.cmp = compositeAddress;
        this.pro = productAddress;
        this.pri = priceAddress;
    }

    public String getCmp() {
        return cmp;
    }

    public String getPro() {
        return pro;
    }

    public String getPri() {
        return pri;
    }

}
