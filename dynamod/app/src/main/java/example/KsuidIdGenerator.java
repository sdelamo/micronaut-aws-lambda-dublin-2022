package example;

import com.amirkhawaja.Ksuid;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.UUID;

@Singleton
public class KsuidIdGenerator implements IdGenerator {

    @Override
    @NonNull
    public String generate() {
        try {
            return new Ksuid().generate();
        } catch (IOException e) {
            return UUID.randomUUID().toString();
        }
    }
}
