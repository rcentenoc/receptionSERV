package pe.mm.reception;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class ReceptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceptionApplication.class, args);
	}

	@Value("${app.redis.url}")
	String url;
	// url traera el valor de la variable app.redis.url del archivo
	// application.properties
	@Value("${app.redis.password}")
	String password;
	// password traera el valor de la variable app.redis.password del archivo
	// application.properties

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
				.setPassword(password).setAddress(url);

		RedissonClient redissonClient = Redisson.create(config);
		return redissonClient;
		// REdisson sirve para manejar la conexion con Redis
		// Redis es una base de datos en memoria

	}
}
