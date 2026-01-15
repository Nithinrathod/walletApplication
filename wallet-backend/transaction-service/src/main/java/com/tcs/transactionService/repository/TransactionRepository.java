package com.tcs.transactionService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcs.transactionService.bean.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
        SELECT COUNT(t) FROM Transaction t
        WHERE t.senderUserId = :userId
        AND t.createdAt >= :fromTime
    """)
    int countRecentTransactions(
        @Param("userId") String userId,
        @Param("fromTime") LocalDateTime fromTime
    );

    @Query("""
        SELECT COUNT(t) FROM Transaction t
        WHERE t.senderUserId = :userId
        AND t.status = 'FAILED'
        AND t.createdAt >= :fromTime
    """)
    int countFailedTransactions(
        @Param("userId") String userId,
        @Param("fromTime") LocalDateTime fromTime
    );

    @Query("""
        SELECT AVG(t.amount) FROM Transaction t
        WHERE t.senderUserId = :userId
    """)
    Double findAverageAmount(@Param("userId") String userId);
    
    List<Transaction> findBySenderUserIdOrReceiverUserIdOrderByCreatedAtDesc(String senderId, String receiverId);
}
