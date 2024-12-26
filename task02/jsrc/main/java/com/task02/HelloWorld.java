package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaLayer;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.ArtifactExtension;
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
  layers = {"sdk-layer"},
  aliasName = "learn",
  logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaLayer(
    layerName = "sdk-layer",
    libraries = {"lib/commons-lang3-3.14.0.jar", "lib/gson-2.10.1.jar"},
    artifactExtension = ArtifactExtension.ZIP
)
@LambdaUrlConfig(
    authType = AuthType.NONE,
    invokeMode = InvokeMode.BUFFERED
)
public class HelloWorld implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

  private final Map<String, String> responseHeaders = Map.of("Content-Type", "application/json");
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent requestEvent,
      Context context) {
    var method = requestEvent.getRequestContext().getHttp().getMethod();
    var path = requestEvent.getRequestContext().getHttp().getPath();
    if (method.equals("GET") && path.equals("/hello")) {
      return APIGatewayV2HTTPResponse.builder()
          .withStatusCode(200)
          .withHeaders(responseHeaders)
          .withBody(gson.toJson(new Response("Hello from Lambda", 200)))
          .build();
    } else {
      return APIGatewayV2HTTPResponse.builder()
          .withStatusCode(400)
          .withHeaders(responseHeaders)
          .withBody(
              gson.toJson(new Response(
                  String.format("Bad request syntax or unsupported method. Request path: %s. HTTP method: %s", path, method),
                  400
                  )
              )
          )
          .build();
    }
  }

  private record Response(String message, int statusCode) {
  }

}
