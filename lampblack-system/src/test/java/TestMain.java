import com.osen.cloud.common.utils.ConstUtil;

/**
 * User: PangYi
 * Date: 2019-10-22
 * Time: 15:20
 * Description:
 */
public class TestMain {

    public static void main(String[] args) {
        String data = "data_history_201912";

        boolean compareToTime = ConstUtil.compareToTime(9);
        System.out.println(compareToTime);
    }
}
