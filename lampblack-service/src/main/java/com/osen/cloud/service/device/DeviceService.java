package com.osen.cloud.service.device;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.system_device.Device;

import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-16
 * Time: 15:05
 * Description: 设备服务接口
 */
public interface DeviceService extends IService<Device> {

    /**
     * 设备添加
     *
     * @param device 设备信息
     * @param userId 用户ID
     * @return 信息
     */
    boolean addDevice(Device device, Integer userId);

    /**
     * 通过设备号查询设备信息
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    Device findDeviceNo(String deviceNo);

    /**
     * 通过设备号更新设备
     *
     * @param isLive   是否在线，1不在线，2在线
     * @param deviceNo 设备号
     * @return 信息
     */
    boolean updateDeviceStatus(Integer isLive, String deviceNo);

    /**
     * 分页查询指定用户的设备列表信息
     *
     * @param params 参数
     * @param type   设备类型
     * @return 信息
     */
    Map<String, Object> findDeviceByUserAccount(Map<String, Object> params, String type);

    /**
     * 分页查询系统所有设备信息列表
     *
     * @param params 参数
     * @return 信息
     */
    Map<String, Object> findAllDeviceToSystem(Map<String, Object> params);

    /**
     * 根据设备号删除指定用户
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    Map<String, Object> deleteToDeviceNo(String deviceNo);

    /**
     * 添加设备
     *
     * @param device 设备
     * @return 信息
     */
    boolean addDevice(Device device);

    /**
     * 统计查询指定用户设备状态
     *
     * @param account 账号
     * @param type    设备类型
     * @return 信息
     */
    Map<String, Integer> findDeviceStatusToUser(String account, String type);

    /**
     * 查询指定用户下的所有设备列表信息
     *
     * @param account 账号
     * @return 信息
     */
    Map<String, Object> finaAllDeviceToUser(String account);

    /**
     * 设备信息修改
     *
     * @param device 设备
     * @return 信息
     */
    boolean updateDeviceInfo(Device device);

    /**
     * 不分页查询指定类型的设备信息
     *
     * @param type 设备类型
     * @return 信息
     */
    List<Device> findAllToMap(String type);

}