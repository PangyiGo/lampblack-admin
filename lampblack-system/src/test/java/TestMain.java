import com.osen.cloud.system.system_socket.utils.DataSegmentParseUtil;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-10-22
 * Time: 15:20
 * Description:
 */
public class TestMain {

    public static void main(String[] args) {
        String data = "##0137QN=20191106104800001;ST=22;CN=2011;PW=123456;MN=2019081903100008;Flag=5;CP=&&DataTime=20191106104800;LAMPBLACK-Rtd=0.1,LAMPBLACK-Flag=N&&F001";

        DataSegmentParseUtil dataSegmentParseUtil = new DataSegmentParseUtil();
        Map<String, Object> map = dataSegmentParseUtil.parseDataTOMap(data);

        System.out.println(map);
    }
}
