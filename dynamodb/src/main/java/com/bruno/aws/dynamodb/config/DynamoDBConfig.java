package com.bruno.aws.dynamodb.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.bruno.aws.dynamodb.repository")
public class DynamoDBConfig {
	@Value("${amazon.dynamodb.endpoint}")
	private String endpoint;

	@Value("${amazon.aws.accesskey}")
	private String accessKey;

	@Value("${amazon.aws.secretkey}")
	private String secretKey;

	public AWSCredentialsProvider amazonAWSCredentialsProvider() {
		return new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.accessKey, this.secretKey));
	}

	@Bean
	public DynamoDBMapperConfig dynamoDBMapperConfig() {
		return DynamoDBMapperConfig.DEFAULT;
	}

	@Bean
	public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
		return new DynamoDBMapper(amazonDynamoDB, config);
	}

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(this.endpoint, "")).build();
	}
}
