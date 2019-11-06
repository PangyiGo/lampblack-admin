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
        String data = "##0395QN=20191106110100001;ST=22;CN=2011;PW=123456;MN=2019110507100002;Flag=5;CP=&&DataTime=20191106110100;a01001-Rtd=27.1,a01001-Flag=N;a01002-Rtd=43.5,a01002-Flag=N;T02-Rtd=27.1,T02-Flag=N;H02-Rtd=44.0,H02-Flag=N;T03-Rtd=27.2,T03-Flag=N;H03-Rtd=43.4,H03-Flag=N;T04-Rtd=27.2,T04-Flag=N;H04-Rtd=43.4,H04-Flag=N;a01002-Rtd=43.5,a01002-Flag=N;lng-Rtd=113.840340,lng-Flag=N;lat-Rtd=22.700447,lat-Flag=N&&6480";

        DataSegmentParseUtil dataSegmentParseUtil = new DataSegmentParseUtil();
        Map<String, Object> map = dataSegmentParseUtil.parseDataTOMap(data);

        System.out.println(map);
    }
}
