package example;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.beans.BeanProperty;
import io.micronaut.core.beans.BeanWrapper;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class DynamoRepository {
    public final static String PK = "pk";
    public final static String SK = "sk";

    protected final DynamoDbClient dynamoDbClient;
    protected final DynamoConfiguration dynamoConfiguration;

    protected final IdGenerator idGenerator;

    protected DynamoRepository(DynamoDbClient dynamoDbClient,
                               DynamoConfiguration dynamoConfiguration,
                               IdGenerator idGenerator) {
        this.dynamoDbClient = dynamoDbClient;
        this.dynamoConfiguration = dynamoConfiguration;
        this.idGenerator = idGenerator;
    }

    protected static AttributeValue s(String str) {
        return AttributeValue.builder().s(str).build();
    }

    protected <T extends DynamoDbItem> PutItemResponse putItem(@NonNull T entity) {
        return dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(dynamoConfiguration.getTableName())
                .item(item(entity))
                .build());
    }

    @NonNull
    protected <T extends DynamoDbItem> Map<String, AttributeValue> item(@NonNull T item) {
        Map<String, AttributeValue> result = new HashMap<>();
        AttributeValue pk = s(item.partitionKey());
        AttributeValue sk = s(item.sortKey());
        result.put(PK, pk);
        result.put(SK, sk);
        result.put("CLASS_SIMPLE_NAME", s(item.getClass().getSimpleName()));
        final BeanWrapper<?> wrapper = BeanWrapper.getWrapper(item);
        for (BeanProperty<?, ?> property : wrapper.getBeanProperties()) {
            if (property.getType().equals(String.class)) {
                Optional<String> stringOptional =  wrapper.getProperty(property.getName(), String.class);
                if (stringOptional.isPresent()) {
                    String str = stringOptional.get();
                    if (str != null) {
                        result.put(property.getName(), s(str));
                    }
                }
            }
        }
        return result;
    }
}
