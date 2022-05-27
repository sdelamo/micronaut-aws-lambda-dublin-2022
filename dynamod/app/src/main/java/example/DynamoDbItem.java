package example;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

public interface DynamoDbItem {

    @NonNull
    String partitionKey();

    @Nullable
    String sortKey();
}
