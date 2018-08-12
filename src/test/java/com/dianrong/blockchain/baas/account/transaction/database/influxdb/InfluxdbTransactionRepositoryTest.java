package com.dianrong.blockchain.baas.account.transaction.database.influxdb;

import com.dianrong.blockchain.baas.account.transaction.domain.TransactionInfo;
import com.dianrong.blockchain.baas.account.transaction.domain.TransactionStatus;
import com.dianrong.blockchain.baas.account.transaction.domain.TransactionType;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class InfluxdbTransactionRepositoryTest {
    @Test
    public void testSaveTransaction() {
        InfluxdbConnection connection = new InfluxdbConnection(getInfluxdbInfo());
        connection.connect();

        InfluxdbTransactionRepository repo = new InfluxdbTransactionRepository(connection);

        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setTransactionId("123456");
        transactionInfo.setTransactionType(TransactionType.TRANSACTION_TYPE_RECHARGE);
        transactionInfo.setTransactionStatus(TransactionStatus.TRANSACTION_STATUS_SUCCESS);
        transactionInfo.setUserId(123);
        transactionInfo.setIncome(new BigDecimal(100));
        transactionInfo.setOutcome(new BigDecimal(0));
        transactionInfo.setVirtualBalance(new BigDecimal(100));
        transactionInfo.setCashBalance(new BigDecimal(100));
        transactionInfo.setTransactionTime(new Date());

        repo.saveTransaction(transactionInfo);

        InfluxdbQueryFilters filters = new InfluxdbQueryFilters();
        filters.addFilter("123456", InfluxdbQueryFilterType.TransactionFilterType.TRANSACTION_ID.getValue());
        List<TransactionInfo> savedTransactionInfos = repo.queryTransactions(filters);
        for (TransactionInfo info : savedTransactionInfos) {
            System.out.println(info.toString());
        }

        connection.disconnect();
    }


    @Test
    public void testQueryTransaction() {
        InfluxdbConnection connection = new InfluxdbConnection(getInfluxdbInfo());
        connection.connect();

        InfluxdbTransactionRepository repo = new InfluxdbTransactionRepository(connection);

//        TransactionInfo transactionInfo = new TransactionInfo();
//        transactionInfo.setTransactionId("123456");
//        transactionInfo.setTransactionType(TransactionType.TRANSACTION_TYPE_RECHARGE);
//        transactionInfo.setTransactionStatus(TransactionStatus.TRANSACTION_STATUS_SUCCESS);
//        transactionInfo.setUserId(123);
//        transactionInfo.setIncome(new BigDecimal(100));
//        transactionInfo.setOutcome(new BigDecimal(0));
//        transactionInfo.setVirtualBalance(new BigDecimal(100));
//        transactionInfo.setCashBalance(new BigDecimal(100));
//        transactionInfo.setTransactionTime(new Date());
//
//        repo.saveTransaction(transactionInfo);

        InfluxdbQueryFilters filters = new InfluxdbQueryFilters();
        filters.addFilter("1", InfluxdbQueryFilterType.TransactionFilterType.TRANSACTION_TYPE.getValue());
        List<TransactionInfo> savedTransactionInfos = repo.queryTransactions(filters);
        for (TransactionInfo info : savedTransactionInfos) {
            System.out.println(info.toString());
        }

        connection.disconnect();
    }

    private InfluxdbInfo getInfluxdbInfo() {
        InfluxdbInfo info = new InfluxdbInfo();
        info.setUrl("http://localhost:8086");
        info.setDatabase("baas");
        info.setUsername("root");
        info.setPassword("root");
        info.setRetentionPolicy("autogen");

        return info;
    }
}
