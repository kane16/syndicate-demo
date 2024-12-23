package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
  lambdaName = "hello_world",
  isPublishVersion = true,
  roleName = "hello_world-role",
  aliasName = "learn",
  logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class HelloWorld implements RequestHandler<Object, Map<String, Object>> {

  public Map<String, Object> handleRequest(Object request, Context context) {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("statusCode", 200);
    resultMap.put("message", "Hello from Lambda");
    return resultMap;
  }


}
