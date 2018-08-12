//package com.dianrong.blockchain.baas.account.account.database.model;
//
//import com.dianrong.blockchain.baas.account.account.domain.AccountInfo;
//import lombok.*;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
//@Getter
//@Setter
//@ToString
//@NoArgsConstructor
//public class AccountEntity {
//    private long userId;
//    private BigDecimal virtualBalance;
//    private BigDecimal cashBalance;
//    private BigDecimal creditBalance;
//    private String remark;
//    private String operator;
//    private Date operateTime;
//    private Date createTime;
//
//    public void fromAccountInfo(AccountInfo accountInfo) {
//        this.userId = accountInfo.getUserId();
//        this.virtualBalance = accountInfo.getVirtualBalance();
//        this.cashBalance = accountInfo.getCashBalance();
//        this.creditBalance = accountInfo.getCreditBalance();
//        this.remark = accountInfo.getRemark();
//        this.operator = accountInfo.getOperator();
//        this.operateTime = accountInfo.getOperateTime();
//        this.createTime = accountInfo.getCreateTime();
//    }
//
//    public AccountInfo toAccountInfo() {
//        AccountInfo accountInfo = new AccountInfo();
//        accountInfo.setUserId(this.userId);
//        accountInfo.setVirtualBalance(this.virtualBalance);
//        accountInfo.setCashBalance(this.cashBalance);
//        accountInfo.setCreditBalance(this.creditBalance);
//        accountInfo.setRemark(this.remark);
//        accountInfo.setOperator(this.operator);
//        accountInfo.setOperateTime(this.operateTime);
//        accountInfo.setCreateTime(this.createTime);
//
//        return accountInfo;
//    }
//}
