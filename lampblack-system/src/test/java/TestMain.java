import com.osen.cloud.system.system_socket.utils.CRCValidationUtil;

/**
 * User: PangYi
 * Date: 2019-10-22
 * Time: 15:20
 * Description:
 */
public class TestMain {

    public static void main(String[] args) {
        boolean validateCRC = CRCValidationUtil.validateCRC("QN=20191029234700001;ST=22;CN=2011;PW=123456;MN=2019081903100008;Flag=5;CP=&&DataTime=20191029234700;LAMPBLACK-Rtd=0.0,LAMPBLACK-Flag=N&&", "0000");
        System.out.println(validateCRC);
    }
}
