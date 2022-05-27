package example;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.ConfigurationProperties;
import javax.validation.constraints.NotBlank;

@Requires(property = "dynamodb.table-name")
@ConfigurationProperties("dynamodb")
public interface DynamoConfiguration {
    @NotBlank
    String getTableName();
}
