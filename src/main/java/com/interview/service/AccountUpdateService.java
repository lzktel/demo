//package com.dianrong.blockchain.baas.account.account.service;
//
//import com.dianrong.blockchain.baas.account.account.database.mapper.AccountMapper;
//import com.dianrong.blockchain.baas.account.account.database.model.AccountEntity;
//import com.dianrong.blockchain.baas.account.account.domain.AccountInfo;
//import com.dianrong.blockchain.baas.account.account.domain.RechargeAccount;
//import com.dianrong.blockchain.baas.account.account.exception.AccountBalanceNotEnoughException;
//import com.dianrong.blockchain.baas.account.account.exception.AccountExistException;
//import com.dianrong.blockchain.baas.account.account.exception.AccountNotExistException;
//import com.dianrong.blockchain.baas.account.domain.AccountException;
//import com.dianrong.blockchain.baas.account.transaction.database.mysql.repository.MysqlTransactionRepository;
//import com.dianrong.blockchain.baas.account.transaction.domain.TransactionInfo;
//import com.dianrong.blockchain.baas.account.transaction.domain.TransactionMode;
//import com.dianrong.blockchain.baas.account.transaction.domain.TransactionStatus;
//import com.dianrong.blockchain.baas.account.transaction.domain.TransactionType;
//import com.dianrong.blockchain.baas.account.transaction.util.TransactionUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
//@Service
//@Transactional
//public class AccountUpdateService {
//    private static final String DEFAULT_ORIGINATOR = "平台";
//    private static final String DEFAULT_OPERATOR = "BAAS";
//
//    @Autowired
//    private AccountMapper accountMapper;
//
//    @Autowired
//    private AccountQueryService queryService;
//
//    @Autowired
//    private MysqlTransactionRepository transactionRepository;
//
//    public AccountInfo createAccount(long userId) throws AccountExistException {
//        AccountInfo accountInfo = new AccountInfo();
//        accountInfo.setUserId(userId);
//        accountInfo.setVirtualBalance(BigDecimal.ZERO);
//        accountInfo.setCashBalance(BigDecimal.ZERO);
//        accountInfo.setRemark("");
//
//        return this.createAccount(accountInfo);
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public AccountInfo createAccount(AccountInfo accountInfo) throws AccountExistException {
//        // validate: only one accountEntity for user
//        this.queryService.isAccountNotExist(accountInfo.getUserId());
//
//        // create: a new accountEntity in database
//        AccountEntity accountEntity = new AccountEntity();
//        accountEntity.setUserId(accountInfo.getUserId());
//        accountEntity.setVirtualBalance(accountInfo.getVirtualBalance());
//        accountEntity.setCashBalance(accountInfo.getCashBalance());
//        accountEntity.setCreditBalance(accountInfo.getCreditBalance());
//        accountEntity.setRemark(accountInfo.getRemark());
//        accountEntity.setOperator(DEFAULT_OPERATOR);
//        accountEntity.setOperateTime(new Date());
//
//        accountMapper.saveAccount(accountEntity);
//
//        return accountEntity.toAccountInfo();
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public TransactionInfo rechargeAccount(long userId, RechargeAccount rechargeAccount) throws AccountException {
//        // validate if the user exist or not
//        this.queryService.isAccountExist(userId);
//
//        // validate recharge account
//        if (rechargeAccount.getAmount().compareTo(BigDecimal.ZERO) < 0) {
//            throw new AccountException("Recharge amount should not be negative");
//        }
//
//        // query the accountEntity and lock it for further update
//        AccountEntity accountEntity = this.accountMapper.queryAccountByUserIdForUpdate(userId);
//        if (accountEntity == null) {
//            throw new AccountNotExistException(String.format("The account of user(%d) does not exist", userId));
//        }
//
//        // prepare transaction info
//        TransactionInfo transactionInfo = new TransactionInfo();
//        transactionInfo.setUserId(userId);
//        transactionInfo.setTransactionId(TransactionUtil.generateTransactionId());
//        transactionInfo.setTransactionMode(TransactionMode.TRANSACTION_MODE_NEW);
//        transactionInfo.setTransactionTime(new Date());
//        transactionInfo.setIncome(rechargeAccount.getAmount());
//        transactionInfo.setOutcome(BigDecimal.ZERO);
//        transactionInfo.setVirtualBalance(accountEntity.getVirtualBalance());
//        transactionInfo.setCashBalance(accountEntity.getCashBalance());
//        transactionInfo.setCreditBalance(accountEntity.getCreditBalance());
//        transactionInfo.setTransactionStatus(TransactionStatus.TRANSACTION_STATUS_SUCCESS);
//        transactionInfo.setOriginator(rechargeAccount.getOriginator());
//        transactionInfo.setReceiver(String.valueOf(userId));
//
//        if (rechargeAccount.getDetail() != null) {
//            transactionInfo.setDetail(rechargeAccount.getDetail());
//        }
//        if (rechargeAccount.getComment() != null) {
//            transactionInfo.setComment(rechargeAccount.getComment());
//        }
//
//        switch (rechargeAccount.getType()) {
//            case VIRTUAL:
//                transactionInfo.setTransactionType(TransactionType.TRANSACTION_TYPE_PRESENT);
//                transactionInfo.setVirtualBalance(accountEntity.getVirtualBalance().add(rechargeAccount.getAmount()));
//                break;
//            case CASH:
//                transactionInfo.setTransactionType(TransactionType.TRANSACTION_TYPE_RECHARGE);
//                transactionInfo.setCashBalance(accountEntity.getCashBalance().add(rechargeAccount.getAmount()));
//                break;
//            case CREDIT:
//                transactionInfo.setTransactionType(TransactionType.TRANSACTION_TYPE_CREDIT);
//                transactionInfo.setCreditBalance(accountEntity.getCreditBalance().add(rechargeAccount.getAmount()));
//                break;
//            default:
//                break;
//        }
//
//        // update accountEntity
//        Integer updated = -1;
//        switch (rechargeAccount.getType()) {
//            case VIRTUAL:
//                updated = this.accountMapper.updateAccountVirtualBalance(userId, accountEntity.getVirtualBalance().add(rechargeAccount.getAmount()), DEFAULT_OPERATOR);
//                break;
//            case CASH:
//                updated = this.accountMapper.updateAccountCashBalance(userId, accountEntity.getCashBalance().add(rechargeAccount.getAmount()), DEFAULT_OPERATOR);
//                break;
//            case CREDIT:
//                updated = this.accountMapper.updateAccountCreditBalance(userId, accountEntity.getCreditBalance().add(rechargeAccount.getAmount()), DEFAULT_OPERATOR);
//                break;
//            default:
//                break;
//        }
//
//        if (updated > 0) {
//            // save transaction
//            this.transactionRepository.saveTransaction(transactionInfo);
//            return transactionInfo;
//        }
//
//        throw new AccountException("Failed to recharge account");
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public TransactionInfo consumeAccount(long userId, BigDecimal amount, String operator) throws AccountException {
//        // validate if the user exist or not
//        this.queryService.isAccountExist(userId);
//
//        AccountEntity accountEntity = this.accountMapper.queryAccountByUserIdForUpdate(userId);
//        if (accountEntity == null) {
//            throw new AccountNotExistException(String.format("The account of user(%d) does not exist", userId));
//        }
//
//        // prepare transaction for present operation
//        TransactionInfo transactionInfo = new TransactionInfo();
//        transactionInfo.setUserId(userId);
//        transactionInfo.setTransactionId(TransactionUtil.generateTransactionId());
//        transactionInfo.setTransactionType(TransactionType.TRANSACTION_TYPE_CONSUMPTION);
//        transactionInfo.setTransactionMode(TransactionMode.TRANSACTION_MODE_NEW);
//        transactionInfo.setTransactionStatus(TransactionStatus.TRANSACTION_STATUS_SUCCESS);
//        transactionInfo.setTransactionTime(new Date());
//        transactionInfo.setIncome(BigDecimal.ZERO);
//        transactionInfo.setOutcome(amount);
//        transactionInfo.setVirtualBalance(accountEntity.getVirtualBalance());
//        transactionInfo.setCashBalance(accountEntity.getCashBalance());
//        transactionInfo.setCreditBalance(accountEntity.getCreditBalance());
//        transactionInfo.setOriginator(DEFAULT_ORIGINATOR);
//        transactionInfo.setReceiver(String.valueOf(userId));
//
//        Integer updated;
//        if (accountEntity.getVirtualBalance().compareTo(amount) >= 0) {
//            transactionInfo.setVirtualBalance(accountEntity.getVirtualBalance().subtract(amount));
//
//            updated = this.accountMapper.updateAccountVirtualBalance(userId, transactionInfo.getVirtualBalance(), operator);
//        } else {
//            BigDecimal cashBalance = accountEntity.getCashBalance().subtract(amount.subtract(accountEntity.getVirtualBalance()));
//            if (cashBalance.compareTo(BigDecimal.ZERO) < 0) {
//                throw new AccountBalanceNotEnoughException(String.format("Account (%d) balance is not enough", userId));
//            }
//
//            transactionInfo.setVirtualBalance(BigDecimal.ZERO);
//            transactionInfo.setCashBalance(cashBalance);
//
//            this.accountMapper.updateAccountVirtualBalance(userId, transactionInfo.getVirtualBalance(), operator);
//            updated = this.accountMapper.updateAccountCashBalance(userId, transactionInfo.getCashBalance(), operator);
//        }
//
//        if (updated > 0) {
//            this.transactionRepository.saveTransaction(transactionInfo);
//            return transactionInfo;
//        }
//
//        throw new AccountException("Failed to consume account");
//    }
//}
