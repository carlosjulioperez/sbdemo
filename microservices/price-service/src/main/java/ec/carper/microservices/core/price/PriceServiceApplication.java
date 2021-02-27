package ec.carper.microservices.core.price;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ec.carper")
public class PriceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceServiceApplication.class, args);
	}

}
