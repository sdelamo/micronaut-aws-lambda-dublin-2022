package example;

import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public interface UserRepository {

    void save(@NonNull @NotBlank @Email String email);
}
