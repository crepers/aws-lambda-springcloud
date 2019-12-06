package com.wcjung.sample.handler;

import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

import com.wcjung.sample.domain.Request;
import com.wcjung.sample.domain.Response;

public class AwsLambdaFunctionHandler extends SpringBootRequestHandler<Request, Response> { 

} 