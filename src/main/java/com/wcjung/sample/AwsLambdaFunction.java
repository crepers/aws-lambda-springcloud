package com.wcjung.sample;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.wcjung.sample.domain.Request;
import com.wcjung.sample.domain.Response;
import com.wcjung.sample.service.AwsLambdaService;

@Component("awsLambdaFunction")
public class AwsLambdaFunction implements Function<Request, Response> {

    private final AwsLambdaService service;

    public AwsLambdaFunction(AwsLambdaService service) {
        this.service = service;
    }

    @Override
    public Response apply(final Request request) {
        final Response result = new Response();
        result.setResult(service.uppercase(request.getInput()));
        return result;
    }
}