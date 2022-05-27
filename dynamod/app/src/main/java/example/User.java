package example;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Introspected
public class User implements DynamoDbItem {
    @NonNull
    @NotBlank
    private final String id;

    @NonNull
    @NotBlank
    @Email
    private final String email;

    public User(@NonNull String id, @NonNull String email) {
        this.id = id;
        this.email = email;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @Override
    @NonNull
    public String partitionKey() {
        return "USER#" + id;
    }

    @Override
    @Nullable
    public String sortKey() {
        return "USER#" + id;
    }
}
