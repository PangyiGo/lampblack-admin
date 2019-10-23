package com.osen.cloud.system.logging.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * User: PangYi
 * Date: 2019-10-23
 * Time: 17:41
 * Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
class IpVo implements Serializable {

    private String country;

    private String regionName;

    private String city;

    private String query;

    /**
     * success,fail
     */
    private String status;

}
