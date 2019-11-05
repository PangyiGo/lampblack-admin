package com.osen.cloud.system.dev_data.coldchain.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-11-05
 * Time: 17:41
 * Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressVO {

    // 经度
    private String longitude;

    // 纬度
    private String latitude;
}
