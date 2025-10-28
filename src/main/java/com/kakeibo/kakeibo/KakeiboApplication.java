package com.kakeibo.kakeibo;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KakeiboApplication {
	public static void main(String[] args) {
		SpringApplication.run(KakeiboApplication.class, args);
	}

	@Bean
	public CommandLineRunner logDatasource(DataSource dataSource) {
		return args -> {
			try (Connection c = dataSource.getConnection()) {
				String url = c.getMetaData().getURL();
				// Mask password query param if present
				String masked = url.replaceAll("(?i)(password=)[^&]+", "$1****");
				System.out.println("JDBC URL at startup: " + masked);
			} catch (Exception e) {
				System.err.println("Failed to read JDBC URL: " + e.getMessage());
			}
		};
	}

}
