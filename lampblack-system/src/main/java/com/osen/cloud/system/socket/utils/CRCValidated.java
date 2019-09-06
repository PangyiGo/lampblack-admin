package com.osen.cloud.system.socket.utils;

/**
 * User: PangYi
 * Date: 2019-09-05
 * Time: 8:51
 * Description: CRC合法性校验码
 */
public class CRCValidated {

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
        System.out.println(toHexString);
        return toHexString.equalsIgnoreCase(crc);
    }

    public static void main(String[] args) {
        String dataSegement = "##0186QN=20190813155800001;ST=22;CN=2011;PW=123456;MN=2019051703100020;Flag=5;" +
                "CP=&&DataTime=20190813155800;LAMPBLACK-Rtd=3.13,LAMPBLACK-Flag=N;PM-Rtd=2.16,PM-Flag=N;NMHC-Rtd=4.12,NMHC-Flag=N&&D641";

        System.out.println(("QN=20190813155800001;ST=22;CN=2011;PW=123456;MN=2019051703100020;Flag=5;" + "CP=&&DataTime=20190813155800;LAMPBLACK-Rtd=3.13,LAMPBLACK-Flag=N;PM-Rtd=2.16,PM-Flag=N;NMHC-Rtd=4" + ".12,NMHC-Flag=N&&").length());

        System.out.println(validateCRC("QN=20190813155800001;ST=22;CN=2011;PW=123456;MN=2019051703100020;Flag=5;" +
                "CP=&&DataTime=20190813155800;LAMPBLACK-Rtd=3.13,LAMPBLACK-Flag=N;PM-Rtd=2.16,PM-Flag=N;NMHC-Rtd=4" + ".12,NMHC-Flag=N&&", "D641"));

        System.out.println(("QN=20190813155800001;ST=22;CN=2011;PW=123456;MN=2019051703100020;Flag=5;" +
                "CP=&&DataTime=20190813155800;LAMPBLACK-Rtd=0.0,LAMPBLACK-Flag=N&&").length());

        System.out.println(validateCRC("QN=20190813155800001;ST=22;CN=2011;PW=123456;MN=2019051703100020;Flag=5;" +
                "CP=&&DataTime=20190813155800;LAMPBLACK-Rtd=0.0,LAMPBLACK-Flag=N&&", "5880"));
    }
}
