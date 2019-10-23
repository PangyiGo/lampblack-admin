package com.osen.cloud.system.user.vo;

import com.osen.cloud.common.entity.system_user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-24
 * Time: 14:00
 * Description: 分页结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnPageVo {

    private Long totalNumber;

    private List<User> users = new ArrayList<>(0);
}
