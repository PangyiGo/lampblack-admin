package com.osen.cloud.system.db_config;

import com.osen.cloud.common.utils.ConstUtil;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-09-18
 * Time: 15:51
 * Description:
 */
public class Testmain {

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.of(2019, 10, 31, 23, 59, 0);
        localDateTime = localDateTime.plusMonths(1);
        System.out.println(localDateTime.getMonthValue());

        System.out.println(ConstUtil.createNextTableName(ConstUtil.REALTIME_TB));
    }
}
