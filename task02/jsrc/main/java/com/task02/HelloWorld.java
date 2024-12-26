package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;

import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@LambdaHandler(
  lambdaName = "hello_world",
  isPublishVersion = true,
  roleName = "hello_world-role",
  aliasName = "learn",
  logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig(
    authType = AuthType.NONE,
    invokeMode = InvokeMode.BUFFERED
)
public class HelloWorld implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

  private final Map<String, String> responseHeaders = Map.of("Content-Type", "application/json");

  public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent requestEvent,
      Context context) {
    var method = requestEvent.getRequestContext().getHttp().getMethod();
    var path = requestEvent.getRequestContext().getHttp().getPath();
    if (method.equals("GET") && path.equals("/hello")) {
      return APIGatewayV2HTTPResponse.builder()
          .withStatusCode(200)
          .withHeaders(responseHeaders)
          .withBody("{"
              + "\"message\": \"Hello from Lambda\""
              + "\"statusCode\": " + 200
              + "}")
          .build();
    } else {
      return APIGatewayV2HTTPResponse.builder()
          .withStatusCode(400)
          .withHeaders(responseHeaders)
          .withBody(
              String.format("{"
                  + "\"message\": \"Bad request syntax or unsupported method. Request path: %s. HTTP method: %s\""
                  + "\"statusCode\": " + 400
                  + "}", path, method)
          )
          .build();
    }
  }

}
