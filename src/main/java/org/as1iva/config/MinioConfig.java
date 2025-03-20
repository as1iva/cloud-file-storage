package org.as1iva.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    @Bean
    public MinioClient minioClient(
            @Value("${spring.minio.url}") String url,
            @Value("${spring.minio.access-key}") String user,
            @Value("${spring.minio.secret-key}") String password) {

        return MinioClient.builder()
                .endpoint(url)
                .credentials(user, password)
                .build();
    }
}
