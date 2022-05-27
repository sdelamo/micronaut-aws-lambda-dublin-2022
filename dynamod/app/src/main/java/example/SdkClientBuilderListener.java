package example;

import com.amazonaws.xray.interceptors.TracingInterceptor;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.client.builder.SdkClientBuilder;

/**
 * Configures X-Ray Tracing Interceptor for all sdk client builders.
 * @see <a href="https://aws.amazon.com/blogs/developer/x-ray-support-for-the-aws-sdk-for-java-v2/">X-Ray support for the AWS SDK for Java 2</a>
 *
 * @author Pavol Gressa
 * @since 3.2.0
 */
@Requires(classes = SdkClientBuilder.class)
@Singleton
public class SdkClientBuilderListener implements BeanCreatedEventListener<SdkClientBuilder<?, ?>> {
    private static final Logger LOG = LoggerFactory.getLogger(SdkClientBuilderListener.class);

    /**
     * Add {@link TracingInterceptor} to {@link SdkClientBuilder}.
     *
     * @param event bean created event
     * @return sdk client builder
     */
    @Override
    public SdkClientBuilder<?, ?> onCreated(BeanCreatedEvent<SdkClientBuilder<?, ?>> event) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Registering x-ray tracing interceptor to {}", event.getBean().getClass().getSimpleName());
        }
        return event.getBean().overrideConfiguration(builder ->
                builder.addExecutionInterceptor(new TracingInterceptor()));
    }
}

