package com.osen.cloud.system.web_security.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-03
 * Time: 13:58
 * Description: jwt信息结构体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TransferUserToJwt implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private List<String> authority = new ArrayList<>(0);
}
