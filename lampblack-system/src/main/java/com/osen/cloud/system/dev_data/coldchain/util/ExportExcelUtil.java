package com.osen.cloud.system.dev_data.coldchain.util;

import com.osen.cloud.common.entity.dev_coldchain.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-11-04
 * Time: 15:25
 * Description: 数据表导出工具类
 */
public class ExportExcelUtil {

    /**
     * 数据表格导出工具，批量
     * 实时
     *
     * @param coldChains        导出的设备历史数据
     * @param deviceNames       设备名称
     * @param coldChainMonitors 冷链设备监控点
     * @return 工作簿
     */
    public static HSSFWorkbook historyExcelExport(List<List<ColdChainHistory>> coldChains, List<String> deviceNames, List<ColdChainMonitor> coldChainMonitors) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        if (coldChains.size() != 0 && coldChains != null) {
            // 生成表格
            for (int i = 0; i < coldChains.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(deviceNames.get(i));

                // 第一行表头
                HSSFRow headRow = hssfSheet.createRow(0);
                headRow.createCell(0).setCellValue("数据日期");
                headRow.createCell(1).setCellValue(coldChainMonitors.get(i).getM01());
                headRow.createCell(3).setCellValue(coldChainMonitors.get(i).getM02());
                headRow.createCell(5).setCellValue(coldChainMonitors.get(i).getM03());
                headRow.createCell(7).setCellValue(coldChainMonitors.get(i).getM04());
                // 合并单元格
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));

                // 第二行表头
                HSSFRow title = hssfSheet.createRow(1);
                title.createCell(1).setCellValue("温度℃");
                title.createCell(2).setCellValue("湿度%");
                title.createCell(3).setCellValue("温度℃");
                title.createCell(4).setCellValue("湿度%");
                title.createCell(5).setCellValue("温度℃");
                title.createCell(6).setCellValue("湿度%");
                title.createCell(7).setCellValue("温度℃");
                title.createCell(8).setCellValue("湿度%");

                // 数据表数据
                List<ColdChainHistory> chainHistories = coldChains.get(i);
                int rowIndex = 2;
                for (ColdChainHistory chainHistory : chainHistories) {
                    HSSFRow dataRow = hssfSheet.createRow(rowIndex++);

                    // 时间
                    HSSFCell time = dataRow.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time.setCellValue(df.format(chainHistory.getDateTime()));

                    // 监控点01
                    HSSFCell t01 = dataRow.createCell(1);
                    if (chainHistory.getT01() != null)
                        t01.setCellValue(chainHistory.getT01().toString());
                    else
                        t01.setCellValue("");
                    HSSFCell h01 = dataRow.createCell(2);
                    if (chainHistory.getH01() != null)
                        h01.setCellValue(chainHistory.getH01().toString());
                    else
                        h01.setCellValue("");

                    // 监控点02
                    HSSFCell t02 = dataRow.createCell(3);
                    if (chainHistory.getT02() != null)
                        t02.setCellValue(chainHistory.getT02().toString());
                    else
                        t02.setCellValue("");
                    HSSFCell h02 = dataRow.createCell(4);
                    if (chainHistory.getH02() != null)
                        h02.setCellValue(chainHistory.getH02().toString());
                    else
                        h02.setCellValue("");

                    // 监控点03
                    HSSFCell t03 = dataRow.createCell(5);
                    if (chainHistory.getT03() != null)
                        t03.setCellValue(chainHistory.getT03().toString());
                    else
                        t03.setCellValue("");
                    HSSFCell h03 = dataRow.createCell(6);
                    if (chainHistory.getH03() != null)
                        h03.setCellValue(chainHistory.getH03().toString());
                    else
                        h03.setCellValue("");

                    // 监控点04
                    HSSFCell t04 = dataRow.createCell(7);
                    if (chainHistory.getT04() != null)
                        t04.setCellValue(chainHistory.getT04().toString());
                    else
                        t04.setCellValue("");
                    HSSFCell h04 = dataRow.createCell(8);
                    if (chainHistory.getH04() != null)
                        h04.setCellValue(chainHistory.getH04().toString());
                    else
                        h04.setCellValue("");
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
     * @param coldChains        导出的设备历史数据
     * @param deviceNames       设备名称
     * @param coldChainMonitors 冷链设备监控点
     * @return 工作簿
     */
    public static HSSFWorkbook minuteExcelExport(List<List<ColdChainMinute>> coldChains, List<String> deviceNames, List<ColdChainMonitor> coldChainMonitors) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        if (coldChains.size() != 0 && coldChains != null) {
            // 生成表格
            for (int i = 0; i < coldChains.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(deviceNames.get(i));

                // 第一行表头
                HSSFRow headRow = hssfSheet.createRow(0);
                headRow.createCell(0).setCellValue("数据日期");
                headRow.createCell(1).setCellValue(coldChainMonitors.get(i).getM01());
                headRow.createCell(3).setCellValue(coldChainMonitors.get(i).getM02());
                headRow.createCell(5).setCellValue(coldChainMonitors.get(i).getM03());
                headRow.createCell(7).setCellValue(coldChainMonitors.get(i).getM04());
                // 合并单元格
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));

                // 第二行表头
                HSSFRow title = hssfSheet.createRow(1);
                title.createCell(1).setCellValue("温度℃");
                title.createCell(2).setCellValue("湿度%");
                title.createCell(3).setCellValue("温度℃");
                title.createCell(4).setCellValue("湿度%");
                title.createCell(5).setCellValue("温度℃");
                title.createCell(6).setCellValue("湿度%");
                title.createCell(7).setCellValue("温度℃");
                title.createCell(8).setCellValue("湿度%");

                // 数据表数据
                List<ColdChainMinute> chainMinutes = coldChains.get(i);
                int rowIndex = 2;
                for (ColdChainMinute coldChainMinute : chainMinutes) {
                    HSSFRow dataRow = hssfSheet.createRow(rowIndex++);

                    // 时间
                    HSSFCell time = dataRow.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time.setCellValue(df.format(coldChainMinute.getDateTime()));

                    // 监控点01
                    HSSFCell t01 = dataRow.createCell(1);
                    if (coldChainMinute.getT01() != null)
                        t01.setCellValue(coldChainMinute.getT01().toString());
                    else
                        t01.setCellValue("");
                    HSSFCell h01 = dataRow.createCell(2);
                    if (coldChainMinute.getH01() != null)
                        h01.setCellValue(coldChainMinute.getH01().toString());
                    else
                        h01.setCellValue("");

                    // 监控点02
                    HSSFCell t02 = dataRow.createCell(3);
                    if (coldChainMinute.getT02() != null)
                        t02.setCellValue(coldChainMinute.getT02().toString());
                    else
                        t02.setCellValue("");
                    HSSFCell h02 = dataRow.createCell(4);
                    if (coldChainMinute.getH02() != null)
                        h02.setCellValue(coldChainMinute.getH02().toString());
                    else
                        h02.setCellValue("");

                    // 监控点03
                    HSSFCell t03 = dataRow.createCell(5);
                    if (coldChainMinute.getT03() != null)
                        t03.setCellValue(coldChainMinute.getT03().toString());
                    else
                        t03.setCellValue("");
                    HSSFCell h03 = dataRow.createCell(6);
                    if (coldChainMinute.getH03() != null)
                        h03.setCellValue(coldChainMinute.getH03().toString());
                    else
                        h03.setCellValue("");

                    // 监控点04
                    HSSFCell t04 = dataRow.createCell(7);
                    if (coldChainMinute.getT04() != null)
                        t04.setCellValue(coldChainMinute.getT04().toString());
                    else
                        t04.setCellValue("");
                    HSSFCell h04 = dataRow.createCell(8);
                    if (coldChainMinute.getH04() != null)
                        h04.setCellValue(coldChainMinute.getH04().toString());
                    else
                        h04.setCellValue("");
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
     * @param coldChains        导出的设备历史数据
     * @param deviceNames       设备名称
     * @param coldChainMonitors 冷链设备监控点
     * @return 工作簿
     */
    public static HSSFWorkbook hourExcelExport(List<List<ColdChainHour>> coldChains, List<String> deviceNames, List<ColdChainMonitor> coldChainMonitors) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        if (coldChains.size() != 0 && coldChains != null) {
            // 生成表格
            for (int i = 0; i < coldChains.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(deviceNames.get(i));

                // 第一行表头
                HSSFRow headRow = hssfSheet.createRow(0);
                headRow.createCell(0).setCellValue("数据日期");
                headRow.createCell(1).setCellValue(coldChainMonitors.get(i).getM01());
                headRow.createCell(3).setCellValue(coldChainMonitors.get(i).getM02());
                headRow.createCell(5).setCellValue(coldChainMonitors.get(i).getM03());
                headRow.createCell(7).setCellValue(coldChainMonitors.get(i).getM04());
                // 合并单元格
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));

                // 第二行表头
                HSSFRow title = hssfSheet.createRow(1);
                title.createCell(1).setCellValue("温度℃");
                title.createCell(2).setCellValue("湿度%");
                title.createCell(3).setCellValue("温度℃");
                title.createCell(4).setCellValue("湿度%");
                title.createCell(5).setCellValue("温度℃");
                title.createCell(6).setCellValue("湿度%");
                title.createCell(7).setCellValue("温度℃");
                title.createCell(8).setCellValue("湿度%");

                // 数据表数据
                List<ColdChainHour> coldChainHours = coldChains.get(i);
                int rowIndex = 2;
                for (ColdChainHour coldChainHour : coldChainHours) {
                    HSSFRow dataRow = hssfSheet.createRow(rowIndex++);

                    // 时间
                    HSSFCell time = dataRow.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time.setCellValue(df.format(coldChainHour.getDateTime()));

                    // 监控点01
                    HSSFCell t01 = dataRow.createCell(1);
                    if (coldChainHour.getT01() != null)
                        t01.setCellValue(coldChainHour.getT01().toString());
                    else
                        t01.setCellValue("");
                    HSSFCell h01 = dataRow.createCell(2);
                    if (coldChainHour.getH01() != null)
                        h01.setCellValue(coldChainHour.getH01().toString());
                    else
                        h01.setCellValue("");

                    // 监控点02
                    HSSFCell t02 = dataRow.createCell(3);
                    if (coldChainHour.getT02() != null)
                        t02.setCellValue(coldChainHour.getT02().toString());
                    else
                        t02.setCellValue("");
                    HSSFCell h02 = dataRow.createCell(4);
                    if (coldChainHour.getH02() != null)
                        h02.setCellValue(coldChainHour.getH02().toString());
                    else
                        h02.setCellValue("");

                    // 监控点03
                    HSSFCell t03 = dataRow.createCell(5);
                    if (coldChainHour.getT03() != null)
                        t03.setCellValue(coldChainHour.getT03().toString());
                    else
                        t03.setCellValue("");
                    HSSFCell h03 = dataRow.createCell(6);
                    if (coldChainHour.getH03() != null)
                        h03.setCellValue(coldChainHour.getH03().toString());
                    else
                        h03.setCellValue("");

                    // 监控点04
                    HSSFCell t04 = dataRow.createCell(7);
                    if (coldChainHour.getT04() != null)
                        t04.setCellValue(coldChainHour.getT04().toString());
                    else
                        t04.setCellValue("");
                    HSSFCell h04 = dataRow.createCell(8);
                    if (coldChainHour.getH04() != null)
                        h04.setCellValue(coldChainHour.getH04().toString());
                    else
                        h04.setCellValue("");
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
     * @param coldChains        导出的设备历史数据
     * @param deviceNames       设备名称
     * @param coldChainMonitors 冷链设备监控点
     * @return 工作簿
     */
    public static HSSFWorkbook dayExcelExport(List<List<ColdChainDay>> coldChains, List<String> deviceNames, List<ColdChainMonitor> coldChainMonitors) {
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        if (coldChains.size() != 0 && coldChains != null) {
            // 生成表格
            for (int i = 0; i < coldChains.size(); i++) {
                // 创建工作表
                HSSFSheet hssfSheet = hssfWorkbook.createSheet(deviceNames.get(i));

                // 第一行表头
                HSSFRow headRow = hssfSheet.createRow(0);
                headRow.createCell(0).setCellValue("数据日期");
                headRow.createCell(1).setCellValue(coldChainMonitors.get(i).getM01());
                headRow.createCell(3).setCellValue(coldChainMonitors.get(i).getM02());
                headRow.createCell(5).setCellValue(coldChainMonitors.get(i).getM03());
                headRow.createCell(7).setCellValue(coldChainMonitors.get(i).getM04());
                // 合并单元格
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
                hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));

                // 第二行表头
                HSSFRow title = hssfSheet.createRow(1);
                title.createCell(1).setCellValue("温度℃");
                title.createCell(2).setCellValue("湿度%");
                title.createCell(3).setCellValue("温度℃");
                title.createCell(4).setCellValue("湿度%");
                title.createCell(5).setCellValue("温度℃");
                title.createCell(6).setCellValue("湿度%");
                title.createCell(7).setCellValue("温度℃");
                title.createCell(8).setCellValue("湿度%");

                // 数据表数据
                List<ColdChainDay> coldChainDays = coldChains.get(i);
                int rowIndex = 2;
                for (ColdChainDay coldChainDay : coldChainDays) {
                    HSSFRow dataRow = hssfSheet.createRow(rowIndex++);

                    // 时间
                    HSSFCell time = dataRow.createCell(0, CellType.STRING);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time.setCellValue(df.format(coldChainDay.getDateTime()));

                    // 监控点01
                    HSSFCell t01 = dataRow.createCell(1);
                    if (coldChainDay.getT01() != null)
                        t01.setCellValue(coldChainDay.getT01().toString());
                    else
                        t01.setCellValue("");
                    HSSFCell h01 = dataRow.createCell(2);
                    if (coldChainDay.getH01() != null)
                        h01.setCellValue(coldChainDay.getH01().toString());
                    else
                        h01.setCellValue("");

                    // 监控点02
                    HSSFCell t02 = dataRow.createCell(3);
                    if (coldChainDay.getT02() != null)
                        t02.setCellValue(coldChainDay.getT02().toString());
                    else
                        t02.setCellValue("");
                    HSSFCell h02 = dataRow.createCell(4);
                    if (coldChainDay.getH02() != null)
                        h02.setCellValue(coldChainDay.getH02().toString());
                    else
                        h02.setCellValue("");

                    // 监控点03
                    HSSFCell t03 = dataRow.createCell(5);
                    if (coldChainDay.getT03() != null)
                        t03.setCellValue(coldChainDay.getT03().toString());
                    else
                        t03.setCellValue("");
                    HSSFCell h03 = dataRow.createCell(6);
                    if (coldChainDay.getH03() != null)
                        h03.setCellValue(coldChainDay.getH03().toString());
                    else
                        h03.setCellValue("");

                    // 监控点04
                    HSSFCell t04 = dataRow.createCell(7);
                    if (coldChainDay.getT04() != null)
                        t04.setCellValue(coldChainDay.getT04().toString());
                    else
                        t04.setCellValue("");
                    HSSFCell h04 = dataRow.createCell(8);
                    if (coldChainDay.getH04() != null)
                        h04.setCellValue(coldChainDay.getH04().toString());
                    else
                        h04.setCellValue("");
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
