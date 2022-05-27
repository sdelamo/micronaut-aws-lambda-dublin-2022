package example;
import io.micronaut.function.aws.MicronautRequestHandler;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionRequestHandler extends MicronautRequestHandler<S3EventNotification, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(FunctionRequestHandler.class);
    @Override
    public Void execute(S3EventNotification input) {
        LOG.info("s3 event {}", input.toString());
        return null;
    }
}
