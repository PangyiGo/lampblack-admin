import cn.hutool.core.util.StrUtil;
import org.springframework.util.StringUtils;

/**
 * User: PangYi
 * Date: 2019-10-22
 * Time: 15:20
 * Description:
 */
public class TestMain {

    public static void main(String[] args) {
        String data = "##0297QN=20191122100900943 ;ST=22;CN=2011;PW=123456;MN= 2019061803100005;Flag=5;" +
                "CP=&&DataTime=20191122100900 ;a34004-Rtd=34.2,a34004-Flag=N ;a34002-Rtd=65.7,a34002-Flag=N;" +
                "a21026-Rtd=14.8,a21026-Flag=N;a21004- Rtd=20.9,a21004-Flag=N;O3-Rtd=185.1,O3-Flag=N;a21005-Rtd=2.5,a21005-Flag=N;VOC-Rtd=0.5,VOC-Flag=N&&CFC1";

        data = StringUtils.trimAllWhitespace(data);

        int index = StrUtil.indexOfIgnoreCase(data, "MN=");
        System.out.println(index);

        int index1 = StrUtil.indexOf(data, ';', index);
        System.out.println(index1);

        String sub = StrUtil.sub(data, index + 3, index1);
        System.out.println(sub);
    }
}
