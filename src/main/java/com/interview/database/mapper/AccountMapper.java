//package com.dianrong.blockchain.baas.account.account.database.mapper;
//
//import com.dianrong.blockchain.baas.account.account.database.model.AccountEntity;
//import org.apache.ibatis.annotations.*;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//
//@Service
//@Mapper
//public interface AccountMapper {
//    @Insert("INSERT INTO ACCOUNT (userId, virtualBalance, cashBalance, remark, operator, operateTime) values (#{accountEntity.userId}, #{accountEntity.virtualBalance}, #{accountEntity.cashBalance}, #{accountEntity.remark}, #{accountEntity.operator}, CURRENT_TIMESTAMP)")
//    void saveAccount(@Param("accountEntity") AccountEntity accountEntity);
//
//    @Select("Select count(1) from ACCOUNT where userId = #{userId}")
//    long isAccountExist(@Param("userId") long userId);
//
//    @Select("Select * from ACCOUNT where userId = #{userId}")
//    AccountEntity queryAccountByUserId(@Param("userId") long userId);
//
//    @Select("Select * from ACCOUNT where userId = #{userId} for update")
//    AccountEntity queryAccountByUserIdForUpdate(@Param("userId") long userId);
//
//    @Update("UPDATE ACCOUNT set cashBalance=#{balance}, operator=#{operator}, operateTime=CURRENT_TIMESTAMP where userId = #{userId}")
//    Integer updateAccountCashBalance(@Param("userId") long userId , @Param("balance") BigDecimal balance, @Param("operator") String operator);
//
//    @Update("UPDATE ACCOUNT set virtualBalance=#{balance}, operator=#{operator}, operateTime=CURRENT_TIMESTAMP where userId = #{userId}")
//    Integer updateAccountVirtualBalance(@Param("userId") long userId , @Param("balance") BigDecimal balance, @Param("operator") String operator);
//
//    @Update("UPDATE ACCOUNT set creditBalance=#{balance}, operator=#{operator}, operateTime=CURRENT_TIMESTAMP where userId = #{userId}")
//    Integer updateAccountCreditBalance(@Param("userId") long userId , @Param("balance") BigDecimal balance, @Param("operator") String operator);
//}
