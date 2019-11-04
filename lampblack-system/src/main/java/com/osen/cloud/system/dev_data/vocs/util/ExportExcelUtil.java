package com.osen.cloud.system.dev_data.vocs.util;

import com.osen.cloud.common.entity.dev_vocs.VocDay;
import com.osen.cloud.common.entity.dev_vocs.VocHistory;
import com.osen.cloud.common.entity.dev_vocs.VocHour;
import com.osen.cloud.common.entity.dev_vocs.VocMinute;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-04
 * Time: 16:57
 * Description: 数据表导出Excel工具类
 */
public class ExportExcelUtil {

    /**
     * 数据表格导出工具，批量
     * 实时
     *
     * @param vocHistories 导出的设备历史数据
     * @param deviceNames  设备名称
     * @return 工作簿
     */
    public static HSSFWorkbook historyExcelExport(List<List<VocHistory>> vocHistories, List<String> deviceNames) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        if (vocHistories != null && vocHistories.size() != 0) {
            for (int i = 0; i < vocHistories.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(deviceNames.get(i));

                // 表头
                HSSFRow head = hssfSheet.createRow(0);
                head.createCell(0).setCellValue("数据日期");
                head.createCell(1).setCellValue("TVOC");
                head.createCell(2).setCellValue("流量");
                head.createCell(3).setCellValue("流速");
                head.createCell(4).setCellValue("气压");
                head.createCell(5).setCellValue("温度");

                // 数据表数据
                List<VocHistory> vocHistoryList = vocHistories.get(i);
                int rowIndex = 1;
                for (VocHistory vocHistory : vocHistoryList) {
                    HSSFRow dataRow = hssfSheet.createRow(rowIndex++);

                    // 时间
                    HSSFCell time = dataRow.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time.setCellValue(df.format(vocHistory.getDateTime()));

                    HSSFCell tovc = dataRow.createCell(1);
                    if (vocHistory.getVoc() != null)
                        tovc.setCellValue(vocHistory.getVoc().toString());
                    else
                        tovc.setCellValue("");

                    HSSFCell flow = dataRow.createCell(2);
                    if (vocHistory.getFlow() != null)
                        flow.setCellValue(vocHistory.getFlow().toString());
                    else
                        flow.setCellValue("");

                    HSSFCell speed = dataRow.createCell(3);
                    if (vocHistory.getSpeed() != null)
                        speed.setCellValue(vocHistory.getSpeed().toString());
                    else
                        speed.setCellValue("");

                    HSSFCell pressure = dataRow.createCell(4);
                    if (vocHistory.getPressure() != null)
                        pressure.setCellValue(vocHistory.getPressure().toString());
                    else
                        pressure.setCellValue("");

                    HSSFCell temp = dataRow.createCell(5);
                    if (vocHistory.getTemp() != null)
                        temp.setCellValue(vocHistory.getTemp().toString());
                    else
                        temp.setCellValue("");
                }

                // 设置默认表格列宽
                hssfSheet.setDefaultColumnWidth(30);
                hssfSheet.setDefaultRowHeight((short) (18.5 * 22));
                //列宽自适应
                for (int a = 0; a <= 8; a++) {
                    hssfSheet.autoSizeColumn(i);
                }
            }
        }
        return hssfWorkbook;
    }

    /**
     * 数据表格导出工具，批量
     * 分钟
     *
     * @param vocHistories 导出的设备历史数据
     * @param deviceNames  设备名称
     * @return 工作簿
     */
    public static HSSFWorkbook minuteExcelExport(List<List<VocMinute>> vocHistories, List<String> deviceNames) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        if (vocHistories != null && vocHistories.size() != 0) {
            for (int i = 0; i < vocHistories.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(deviceNames.get(i));

                // 表头
                HSSFRow head = hssfSheet.createRow(0);
                head.createCell(0).setCellValue("数据日期");
                head.createCell(1).setCellValue("TVOC");
                head.createCell(2).setCellValue("流量");
                head.createCell(3).setCellValue("流速");
                head.createCell(4).setCellValue("气压");
                head.createCell(5).setCellValue("温度");

                // 数据表数据
                List<VocMinute> vocMinuteList = vocHistories.get(i);
                int rowIndex = 1;
                for (VocMinute vocMinute : vocMinuteList) {
                    HSSFRow dataRow = hssfSheet.createRow(rowIndex++);

                    // 时间
                    HSSFCell time = dataRow.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time.setCellValue(df.format(vocMinute.getDateTime()));

                    HSSFCell tovc = dataRow.createCell(1);
                    if (vocMinute.getVoc() != null)
                        tovc.setCellValue(vocMinute.getVoc().toString());
                    else
                        tovc.setCellValue("");

                    HSSFCell flow = dataRow.createCell(2);
                    if (vocMinute.getFlow() != null)
                        flow.setCellValue(vocMinute.getFlow().toString());
                    else
                        flow.setCellValue("");

                    HSSFCell speed = dataRow.createCell(3);
                    if (vocMinute.getSpeed() != null)
                        speed.setCellValue(vocMinute.getSpeed().toString());
                    else
                        speed.setCellValue("");

                    HSSFCell pressure = dataRow.createCell(4);
                    if (vocMinute.getPressure() != null)
                        pressure.setCellValue(vocMinute.getPressure().toString());
                    else
                        pressure.setCellValue("");

                    HSSFCell temp = dataRow.createCell(5);
                    if (vocMinute.getTemp() != null)
                        temp.setCellValue(vocMinute.getTemp().toString());
                    else
                        temp.setCellValue("");
                }

                // 设置默认表格列宽
                hssfSheet.setDefaultColumnWidth(30);
                hssfSheet.setDefaultRowHeight((short) (18.5 * 22));
                //列宽自适应
                for (int a = 0; a <= 8; a++) {
                    hssfSheet.autoSizeColumn(i);
                }
            }
        }
        return hssfWorkbook;
    }

    /**
     * 数据表格导出工具，批量
     * 小时
     *
     * @param vocHistories 导出的设备历史数据
     * @param deviceNames  设备名称
     * @return 工作簿
     */
    public static HSSFWorkbook hourExcelExport(List<List<VocHour>> vocHistories, List<String> deviceNames) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        if (vocHistories != null && vocHistories.size() != 0) {
            for (int i = 0; i < vocHistories.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(deviceNames.get(i));

                // 表头
                HSSFRow head = hssfSheet.createRow(0);
                head.createCell(0).setCellValue("数据日期");
                head.createCell(1).setCellValue("TVOC");
                head.createCell(2).setCellValue("流量");
                head.createCell(3).setCellValue("流速");
                head.createCell(4).setCellValue("气压");
                head.createCell(5).setCellValue("温度");

                // 数据表数据
                List<VocHour> vocHourList = vocHistories.get(i);
                int rowIndex = 1;
                for (VocHour vocHour : vocHourList) {
                    HSSFRow dataRow = hssfSheet.createRow(rowIndex++);

                    // 时间
                    HSSFCell time = dataRow.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time.setCellValue(df.format(vocHour.getDateTime()));

                    HSSFCell tovc = dataRow.createCell(1);
                    if (vocHour.getVoc() != null)
                        tovc.setCellValue(vocHour.getVoc().toString());
                    else
                        tovc.setCellValue("");

                    HSSFCell flow = dataRow.createCell(2);
                    if (vocHour.getFlow() != null)
                        flow.setCellValue(vocHour.getFlow().toString());
                    else
                        flow.setCellValue("");

                    HSSFCell speed = dataRow.createCell(3);
                    if (vocHour.getSpeed() != null)
                        speed.setCellValue(vocHour.getSpeed().toString());
                    else
                        speed.setCellValue("");

                    HSSFCell pressure = dataRow.createCell(4);
                    if (vocHour.getPressure() != null)
                        pressure.setCellValue(vocHour.getPressure().toString());
                    else
                        pressure.setCellValue("");

                    HSSFCell temp = dataRow.createCell(5);
                    if (vocHour.getTemp() != null)
                        temp.setCellValue(vocHour.getTemp().toString());
                    else
                        temp.setCellValue("");
                }

                // 设置默认表格列宽
                hssfSheet.setDefaultColumnWidth(30);
                hssfSheet.setDefaultRowHeight((short) (18.5 * 22));
                //列宽自适应
                for (int a = 0; a <= 8; a++) {
                    hssfSheet.autoSizeColumn(i);
                }
            }
        }
        return hssfWorkbook;
    }

    /**
     * 数据表格导出工具，批量
     * 天
     *
     * @param vocHistories 导出的设备历史数据
     * @param deviceNames  设备名称
     * @return 工作簿
     */
    public static HSSFWorkbook dayExcelExport(List<List<VocDay>> vocHistories, List<String> deviceNames) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        if (vocHistories != null && vocHistories.size() != 0) {
            for (int i = 0; i < vocHistories.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(deviceNames.get(i));

                // 表头
                HSSFRow head = hssfSheet.createRow(0);
                head.createCell(0).setCellValue("数据日期");
                head.createCell(1).setCellValue("TVOC");
                head.createCell(2).setCellValue("流量");
                head.createCell(3).setCellValue("流速");
                head.createCell(4).setCellValue("气压");
                head.createCell(5).setCellValue("温度");

                // 数据表数据
                List<VocDay> vocDayList = vocHistories.get(i);
                int rowIndex = 1;
                for (VocDay vocDay : vocDayList) {
                    HSSFRow dataRow = hssfSheet.createRow(rowIndex++);

                    // 时间
                    HSSFCell time = dataRow.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time.setCellValue(df.format(vocDay.getDateTime()));

                    HSSFCell tovc = dataRow.createCell(1);
                    if (vocDay.getVoc() != null)
                        tovc.setCellValue(vocDay.getVoc().toString());
                    else
                        tovc.setCellValue("");

                    HSSFCell flow = dataRow.createCell(2);
                    if (vocDay.getFlow() != null)
                        flow.setCellValue(vocDay.getFlow().toString());
                    else
                        flow.setCellValue("");

                    HSSFCell speed = dataRow.createCell(3);
                    if (vocDay.getSpeed() != null)
                        speed.setCellValue(vocDay.getSpeed().toString());
                    else
                        speed.setCellValue("");

                    HSSFCell pressure = dataRow.createCell(4);
                    if (vocDay.getPressure() != null)
                        pressure.setCellValue(vocDay.getPressure().toString());
                    else
                        pressure.setCellValue("");

                    HSSFCell temp = dataRow.createCell(5);
                    if (vocDay.getTemp() != null)
                        temp.setCellValue(vocDay.getTemp().toString());
                    else
                        temp.setCellValue("");
                }

                // 设置默认表格列宽
                hssfSheet.setDefaultColumnWidth(30);
                hssfSheet.setDefaultRowHeight((short) (18.5 * 22));
                //列宽自适应
                for (int a = 0; a <= 8; a++) {
                    hssfSheet.autoSizeColumn(i);
                }
            }
        }
        return hssfWorkbook;
    }
}
