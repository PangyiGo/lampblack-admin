package com.osen.cloud.system.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-09-24
 * Time: 13:59
 * Description: 请求分页参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageUserVo {

    private Integer pageNumber;

    private String company;
}
