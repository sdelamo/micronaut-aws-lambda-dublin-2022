package example;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.validation.constraints.NotBlank;

@Requires(condition = CIAwsRegionProviderChainCondition.class)
@Requires(condition = CIAwsCredentialsProviderChainCondition.class)
@Requires(beans = { DynamoConfiguration.class, DynamoDbClient.class })
@Singleton
public class UserRepositoryImpl extends DynamoRepository implements UserRepository   {

    public UserRepositoryImpl(DynamoDbClient dynamoDbClient,
                              DynamoConfiguration dynamoConfiguration,
                              IdGenerator idGenerator) {
        super(dynamoDbClient, dynamoConfiguration, idGenerator);
    }

    @Override
    public void save(@NonNull @NotBlank String email) {
        User user = new User(idGenerator.generate(), email);
        putItem(user);
    }

}
