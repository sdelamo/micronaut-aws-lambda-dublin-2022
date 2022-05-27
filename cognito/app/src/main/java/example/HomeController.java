package example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;

@Controller
public class HomeController {

    @Secured(SecurityRule.IS_ANONYMOUS)
    @View("home.html")
    @Get
    public Map<String, Object> index() {
        return Collections.singletonMap("message", "Hello World");
    }
}
