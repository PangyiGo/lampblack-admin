package com.osen.cloud.system.system_socket.utils;

/**
 * User: PangYi
 * Date: 2019-09-05
 * Time: 8:51
 * Description: HJ212协议数据格式校验
 */
public class CRCValidationUtil {

    /**
     * HJ212污染监测 CRC16校验算法
     *
     * @param dataSegment 数据段
     * @param crc         CRC校验码
     * @return 验证CRC校验码是否一致准确
     */
    public static boolean validateCRC(String dataSegment, String crc) {
        Integer[] regs = new Integer[dataSegment.length()];
        for (int i = 0; i < dataSegment.length(); i++) {
            regs[i] = (int) dataSegment.charAt(i);
        }
        int por = 0xFFFF;
        for (Integer reg : regs) {
            por = por >> 8;
            por ^= reg;
            for (int i = 0; i < 8; i++) {
                if ((por & 0x01) == 1) {
                    por = por >> 1;
                    por = por ^ 0xa001;
                } else
                    por = por >> 1;
            }
        }
        String toHexString = Integer.toHexString(por).toUpperCase();

        boolean flag = toHexString.equalsIgnoreCase(crc);
        if (!flag) {
            if (toHexString.length() == 3) {
                toHexString = "0" + toHexString;
            } else if (toHexString.length() == 2) {
                toHexString = "00" + toHexString;
            } else if (toHexString.length() == 1) {
                toHexString = "000" + toHexString;
            }
            flag = toHexString.equalsIgnoreCase(crc);
        }
        return flag;
    }
}
