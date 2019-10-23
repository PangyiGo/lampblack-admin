package com.osen.cloud.system.data.util;

import com.osen.cloud.common.entity.dev_lampblack.DataDay;
import com.osen.cloud.common.entity.dev_lampblack.DataHistory;
import com.osen.cloud.common.entity.dev_lampblack.DataHour;
import com.osen.cloud.common.entity.dev_lampblack.DataMinute;
import com.osen.cloud.common.utils.ConstUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-12
 * Time: 15:03
 * Description: 数据表导出Excel工具类
 */
public class ExportExcelUtil {

    private static String[] header = {"数据日期时间", "油烟浓度值(mg/m³)", "颗粒物值(mg/m³)", "非甲烷总烃值(mg/m³)", "风机", "净化器"};

    /**
     * 导出实时数据Excel表格
     *
     * @param dataHistorys 数据
     * @param devicenames  工作表名
     * @return 工作簿
     */
    public static HSSFWorkbook createExcelToDataHistory(List<List<DataHistory>> dataHistorys, List<String> devicenames) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // 判断数据是否不为空
        if (dataHistorys.size() != 0 && dataHistorys != null) {
            for (int i = 0; i < dataHistorys.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(devicenames.get(i));

                // 设置表格头
                HSSFRow headrow = hssfSheet.createRow(0);
                for (int j = 0; j < header.length; j++) {
                    HSSFCell hssfCell = headrow.createCell(j);
                    hssfCell.setCellValue(header[j]);
                }

                // 一个工作表数据
                List<DataHistory> dataHistoryList = dataHistorys.get(i);
                int rowIndex = 1;
                for (DataHistory dataHistory : dataHistoryList) {
                    HSSFRow row = hssfSheet.createRow(rowIndex++);

                    HSSFCell cell_time = row.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    cell_time.setCellValue(df.format(dataHistory.getDateTime()));

                    HSSFCell cell_lampblack = row.createCell(1);
                    if (dataHistory.getLampblack() != null) {
                        cell_lampblack.setCellValue(dataHistory.getLampblack().toString());
                    } else {
                        cell_lampblack.setCellValue("0");
                    }

                    HSSFCell cell_pm = row.createCell(2);
                    if (dataHistory.getPm() != null) {
                        cell_pm.setCellValue(dataHistory.getPm().toString());
                    } else {
                        cell_pm.setCellValue("0");
                    }

                    HSSFCell cell_nmhc = row.createCell(3);
                    if (dataHistory.getNmhc() != null) {
                        cell_nmhc.setCellValue(dataHistory.getNmhc().toString());
                    } else {
                        cell_nmhc.setCellValue("0");
                    }

                    HSSFCell cell_fan = row.createCell(4);
                    if (dataHistory.getFanFlag() == null || dataHistory.getFanFlag().equals(ConstUtil.CLOSE_STATUS)) {
                        cell_fan.setCellValue("关闭");
                    } else if (dataHistory.getFanFlag().equals(ConstUtil.OPEN_STATUS)) {
                        cell_fan.setCellValue("开启");
                    }

                    HSSFCell cell_purifier = row.createCell(5);
                    if (dataHistory.getPurifierFlag() == null || dataHistory.getPurifierFlag().equals(ConstUtil.CLOSE_STATUS)) {
                        cell_purifier.setCellValue("关闭");
                    } else if (dataHistory.getPurifierFlag().equals(ConstUtil.OPEN_STATUS)) {
                        cell_purifier.setCellValue("开启");
                    }
                }

                // 设置默认表格列宽
                hssfSheet.setDefaultColumnWidth(30);
                hssfSheet.setDefaultRowHeight((short) (18.5 * 22));
                //列宽自适应
                for (int a = 0; a <= 5; a++) {
                    hssfSheet.autoSizeColumn(i);
                }
            }
        }
        return hssfWorkbook;
    }

    /**
     * 导出分钟数据Excel表格
     *
     * @param dataMinutes 数据
     * @param devicenames 工作表名
     * @return 工作簿
     */
    public static HSSFWorkbook createExcelToDataMinute(List<List<DataMinute>> dataMinutes, List<String> devicenames) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // 判断数据是否不为空
        if (dataMinutes.size() != 0 && dataMinutes != null) {
            for (int i = 0; i < dataMinutes.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(devicenames.get(i));

                // 设置表格头
                HSSFRow headrow = hssfSheet.createRow(0);
                for (int j = 0; j < header.length; j++) {
                    HSSFCell hssfCell = headrow.createCell(j);
                    hssfCell.setCellValue(header[j]);
                }

                // 一个工作表数据
                List<DataMinute> dataMinuteList = dataMinutes.get(i);
                int rowIndex = 1;
                for (DataMinute dataMinute : dataMinuteList) {
                    HSSFRow row = hssfSheet.createRow(rowIndex++);

                    HSSFCell cell_time = row.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    cell_time.setCellValue(df.format(dataMinute.getDateTime()));

                    HSSFCell cell_lampblack = row.createCell(1);
                    if (dataMinute.getLampblack() != null) {
                        cell_lampblack.setCellValue(dataMinute.getLampblack().toString());
                    } else {
                        cell_lampblack.setCellValue("0");
                    }

                    HSSFCell cell_pm = row.createCell(2);
                    if (dataMinute.getPm() != null) {
                        cell_pm.setCellValue(dataMinute.getPm().toString());
                    } else {
                        cell_pm.setCellValue("0");
                    }

                    HSSFCell cell_nmhc = row.createCell(3);
                    if (dataMinute.getNmhc() != null) {
                        cell_nmhc.setCellValue(dataMinute.getNmhc().toString());
                    } else {
                        cell_nmhc.setCellValue("0");
                    }

                    HSSFCell cell_fan = row.createCell(4);
                    if (dataMinute.getFanFlag() == null || dataMinute.getFanFlag().equals(ConstUtil.CLOSE_STATUS)) {
                        cell_fan.setCellValue("关闭");
                    } else if (dataMinute.getFanFlag().equals(ConstUtil.OPEN_STATUS)) {
                        cell_fan.setCellValue("开启");
                    }

                    HSSFCell cell_purifier = row.createCell(5);
                    if (dataMinute.getPurifierFlag() == null || dataMinute.getPurifierFlag().equals(ConstUtil.CLOSE_STATUS)) {
                        cell_purifier.setCellValue("关闭");
                    } else if (dataMinute.getPurifierFlag().equals(ConstUtil.OPEN_STATUS)) {
                        cell_purifier.setCellValue("开启");
                    }
                }

                // 设置默认表格列宽
                hssfSheet.setDefaultColumnWidth(30);
                hssfSheet.setDefaultRowHeight((short) (18.5 * 22));
                //列宽自适应
                for (int a = 0; a <= 5; a++) {
                    hssfSheet.autoSizeColumn(i);
                }
            }
        }
        return hssfWorkbook;
    }

    /**
     * 导出小时数据Excel表格
     *
     * @param dataHours   数据
     * @param devicenames 工作表名
     * @return 工作簿
     */
    public static HSSFWorkbook createExcelToDataHour(List<List<DataHour>> dataHours, List<String> devicenames) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // 判断数据是否不为空
        if (dataHours.size() != 0 && dataHours != null) {
            for (int i = 0; i < dataHours.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(devicenames.get(i));

                // 设置表格头
                HSSFRow headrow = hssfSheet.createRow(0);
                for (int j = 0; j < header.length; j++) {
                    HSSFCell hssfCell = headrow.createCell(j);
                    hssfCell.setCellValue(header[j]);
                }

                // 一个工作表数据
                List<DataHour> dataHourList = dataHours.get(i);
                int rowIndex = 1;
                for (DataHour dataHour : dataHourList) {
                    HSSFRow row = hssfSheet.createRow(rowIndex++);

                    HSSFCell cell_time = row.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    cell_time.setCellValue(df.format(dataHour.getDateTime()));

                    HSSFCell cell_lampblack = row.createCell(1);
                    if (dataHour.getLampblack() != null) {
                        cell_lampblack.setCellValue(dataHour.getLampblack().toString());
                    } else {
                        cell_lampblack.setCellValue("0");
                    }

                    HSSFCell cell_pm = row.createCell(2);
                    if (dataHour.getPm() != null) {
                        cell_pm.setCellValue(dataHour.getPm().toString());
                    } else {
                        cell_pm.setCellValue("0");
                    }

                    HSSFCell cell_nmhc = row.createCell(3);
                    if (dataHour.getNmhc() != null) {
                        cell_nmhc.setCellValue(dataHour.getNmhc().toString());
                    } else {
                        cell_nmhc.setCellValue("0");
                    }

                    HSSFCell cell_fan = row.createCell(4);
                    if (dataHour.getFanFlag() == null || dataHour.getFanFlag().equals(ConstUtil.CLOSE_STATUS)) {
                        cell_fan.setCellValue("关闭");
                    } else if (dataHour.getFanFlag().equals(ConstUtil.OPEN_STATUS)) {
                        cell_fan.setCellValue("开启");
                    }

                    HSSFCell cell_purifier = row.createCell(5);
                    if (dataHour.getPurifierFlag() == null || dataHour.getPurifierFlag().equals(ConstUtil.CLOSE_STATUS)) {
                        cell_purifier.setCellValue("关闭");
                    } else if (dataHour.getPurifierFlag().equals(ConstUtil.OPEN_STATUS)) {
                        cell_purifier.setCellValue("开启");
                    }
                }

                // 设置默认表格列宽
                hssfSheet.setDefaultColumnWidth(30);
                hssfSheet.setDefaultRowHeight((short) (18.5 * 22));
                //列宽自适应
                for (int a = 0; a <= 5; a++) {
                    hssfSheet.autoSizeColumn(i);
                }
            }
        }
        return hssfWorkbook;
    }

    /**
     * 导出小时数据Excel表格
     *
     * @param dataDays    数据
     * @param devicenames 工作表名
     * @return 工作簿
     */
    public static HSSFWorkbook createExcelToDataDay(List<List<DataDay>> dataDays, List<String> devicenames) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // 判断数据是否不为空
        if (dataDays.size() != 0 && dataDays != null) {
            for (int i = 0; i < dataDays.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(devicenames.get(i));

                // 设置表格头
                HSSFRow headrow = hssfSheet.createRow(0);
                for (int j = 0; j < header.length; j++) {
                    HSSFCell hssfCell = headrow.createCell(j);
                    hssfCell.setCellValue(header[j]);
                }

                // 一个工作表数据
                List<DataDay> dataDayList = dataDays.get(i);
                int rowIndex = 1;
                for (DataDay dataDay : dataDayList) {
                    HSSFRow row = hssfSheet.createRow(rowIndex++);

                    HSSFCell cell_time = row.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    cell_time.setCellValue(df.format(dataDay.getDateTime()));

                    HSSFCell cell_lampblack = row.createCell(1);
                    if (dataDay.getLampblack() != null) {
                        cell_lampblack.setCellValue(dataDay.getLampblack().toString());
                    } else {
                        cell_lampblack.setCellValue("0");
                    }

                    HSSFCell cell_pm = row.createCell(2);
                    if (dataDay.getPm() != null) {
                        cell_pm.setCellValue(dataDay.getPm().toString());
                    } else {
                        cell_pm.setCellValue("0");
                    }

                    HSSFCell cell_nmhc = row.createCell(3);
                    if (dataDay.getNmhc() != null) {
                        cell_nmhc.setCellValue(dataDay.getNmhc().toString());
                    } else {
                        cell_nmhc.setCellValue("0");
                    }

                    HSSFCell cell_fan = row.createCell(4);
                    if (dataDay.getFanFlag() == null || dataDay.getFanFlag().equals(ConstUtil.CLOSE_STATUS)) {
                        cell_fan.setCellValue("关闭");
                    } else if (dataDay.getFanFlag().equals(ConstUtil.OPEN_STATUS)) {
                        cell_fan.setCellValue("开启");
                    }

                    HSSFCell cell_purifier = row.createCell(5);
                    if (dataDay.getPurifierFlag() == null || dataDay.getPurifierFlag().equals(ConstUtil.CLOSE_STATUS)) {
                        cell_purifier.setCellValue("关闭");
                    } else if (dataDay.getPurifierFlag().equals(ConstUtil.OPEN_STATUS)) {
                        cell_purifier.setCellValue("开启");
                    }
                }

                // 设置默认表格列宽
                hssfSheet.setDefaultColumnWidth(30);
                hssfSheet.setDefaultRowHeight((short) (18.5 * 22));
                //列宽自适应
                for (int a = 0; a <= 5; a++) {
                    hssfSheet.autoSizeColumn(i);
                }
            }
        }
        return hssfWorkbook;
    }

}
