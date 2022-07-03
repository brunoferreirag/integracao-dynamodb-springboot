package com.bruno.aws.dynamodb.repository;

import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bruno.aws.dynamodb.model.ProductInfo;

@EnableScan
@Repository
public interface ProductInfoRepository extends  PagingAndSortingRepository<ProductInfo, String> {
	Optional<ProductInfo> findById(String id);
}
