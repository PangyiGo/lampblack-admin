import com.osen.cloud.system.system_socket.utils.CRCValidationUtil;

/**
 * User: PangYi
 * Date: 2019-10-22
 * Time: 15:20
 * Description:
 */
public class TestMain {

    public static void main(String[] args) {
        boolean validateCRC = CRCValidationUtil.validateCRC("QN=20191022052400001;ST=22;CN=2011;PW=123456;MN=2019081903100008;Flag=5;CP=&&DataTime=20191022052400;LAMPBLACK-Rtd=0.1,LAMPBLACK-Flag=N&&", "03C0");
        System.out.println(validateCRC);
    }
}
