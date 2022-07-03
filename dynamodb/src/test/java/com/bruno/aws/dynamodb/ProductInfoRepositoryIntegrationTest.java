package com.bruno.aws.dynamodb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.bruno.aws.dynamodb.model.ProductInfo;
import com.bruno.aws.dynamodb.repository.ProductInfoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = { 
  "amazon.dynamodb.endpoint=http://localhost:8080", 
  "amazon.aws.accesskey=test1", 
  "amazon.aws.secretkey=test231" })
public class ProductInfoRepositoryIntegrationTest {

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    ProductInfoRepository repository;

    private static final String EXPECTED_COST = "20";
   
    @Before
    public void setup() throws Exception {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        
        CreateTableRequest tableRequest = dynamoDBMapper
          .generateCreateTableRequest(ProductInfo.class);
        tableRequest.setProvisionedThroughput(
          new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);
        
    }
    
    @After
    public void cleanUp() {
    	dynamoDBMapper.batchDelete(
    	          (List<ProductInfo>)repository.findAll());
    }

    @Test
    public void givenItemWithExpectedCost_whenRunFindAll_thenItemIsFound() { 
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCost(EXPECTED_COST);
        productInfo.setId("1");
        productInfo.setMsrp("");
        repository.save(productInfo); 
        List<ProductInfo> result = (List<ProductInfo>) repository.findAll();

        assertTrue(result.get(0).getCost().equals(EXPECTED_COST)); 
    }
}
