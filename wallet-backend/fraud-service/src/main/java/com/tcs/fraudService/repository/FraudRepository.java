package com.tcs.fraudService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcs.fraudService.bean.FraudRecord;

@Repository
public interface FraudRepository extends JpaRepository<FraudRecord, Long> {

}
