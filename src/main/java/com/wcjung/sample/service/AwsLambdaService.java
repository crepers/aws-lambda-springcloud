package com.wcjung.sample.service;

import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public class AwsLambdaService {
	public String uppercase(final String input) {
		return input.toUpperCase(Locale.ENGLISH);
	}
}