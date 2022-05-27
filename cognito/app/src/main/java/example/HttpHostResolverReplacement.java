package dev.contactvault.host;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.HttpHostResolver;
import jakarta.inject.Singleton;

@Replaces(HttpHostResolver.class)
@Singleton
public class HttpHostResolverReplacement implements HttpHostResolver {
    
    @Override
    @NonNull
    public String resolve(HttpRequest request) {
        return "https://h6unj667qd.execute-api.eu-west-1.amazonaws.com/prod/";
    }
}
