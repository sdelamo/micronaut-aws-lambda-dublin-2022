package example;

import io.micronaut.aws.cdk.function.MicronautFunction;
import io.micronaut.aws.cdk.function.MicronautFunctionFile;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import java.util.Arrays;
import java.util.Collections;
import software.amazon.awscdk.services.cognito.AutoVerifiedAttrs;
import software.amazon.awscdk.services.cognito.CognitoDomainOptions;
import software.amazon.awscdk.services.cognito.OAuthFlows;
import software.amazon.awscdk.services.cognito.OAuthScope;
import software.amazon.awscdk.services.cognito.OAuthSettings;
import software.amazon.awscdk.services.cognito.SignInAliases;
import software.amazon.awscdk.services.cognito.UserPool;
import software.amazon.awscdk.services.cognito.UserPoolClientOptions;
import software.amazon.awscdk.services.cognito.UserPoolDomainOptions;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Tracing;
import software.constructs.Construct;
import java.util.HashMap;
import java.util.Map;

public class AppStack extends Stack {

    public AppStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public AppStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        Map<String, String> environmentVariables = new HashMap<>();
        UserPool userPool = UserPool.Builder.create(this, "micronautapppool")
                .userPoolName("micronautapppool-name")
                .signInAliases(SignInAliases.builder()
                        .phone(false)
                        .username(false)
                        .email(true)
                        .build())
                .autoVerify(AutoVerifiedAttrs.builder()
                        .email(true)
                        .build())
                .selfSignUpEnabled(true)
                .build();
        userPool.addDomain("micronautapppool-domain", UserPoolDomainOptions.builder()
                        .cognitoDomain(CognitoDomainOptions.builder()
                                .domainPrefix("micronautapppool")
                                .build())
                .build());
        userPool.addClient("micronautapppool-client", UserPoolClientOptions.builder()
                .generateSecret(true)
                .userPoolClientName("micronautapppool-client")
                .oAuth(OAuthSettings.builder()
                        .scopes(Arrays.asList(OAuthScope.PROFILE,
                                OAuthScope.EMAIL,
                                OAuthScope.OPENID))
                        .flows(OAuthFlows.builder()
                                .authorizationCodeGrant(true)
                                .build())
                        .callbackUrls(Collections.singletonList("https://h6unj667qd.execute-api.eu-west-1.amazonaws.com/prod/oauth/callback/default"))
                        .logoutUrls(Collections.singletonList("https://h6unj667qd.execute-api.eu-west-1.amazonaws.com/prod/logout"))
                        .build())
                .build());
        Function function = MicronautFunction.create(ApplicationType.DEFAULT,
                false,
                this,
                "micronaut-function")
                .environment(environmentVariables)
                .code(Code.fromAsset(functionPath()))
                .timeout(Duration.seconds(10))
                .memorySize(512)
                .tracing(Tracing.ACTIVE)
                .build();
        LambdaRestApi api = LambdaRestApi.Builder.create(this, "micronaut-function-api")
                .handler(function)
                .build();
        CfnOutput.Builder.create(this, "ApiUrl")
                .exportName("ApiUrl")
                .value(api.getUrl())
                .build();
    }

    public static String functionPath() {
        return "../app/build/libs/" + functionFilename();
    }

    public static String functionFilename() {
        return MicronautFunctionFile.builder()
            .graalVMNative(false)
            .version("0.1")
            .archiveBaseName("app")
            .buildTool(BuildTool.GRADLE)
            .build();
    }
}