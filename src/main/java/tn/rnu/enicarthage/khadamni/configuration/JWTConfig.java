package tn.rnu.enicarthage.khadamni.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {
    private String Secret;
    private Short TokenLifeTime;
    private Short RefreshTokenLength;
    private Short RefreshTokenLifeTime;
}
