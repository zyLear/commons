package com.zylear.commons.util.excel;

import com.zylear.commons.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public final class ExcelUtils {


//    public static void exportObjects2Excel(HttpServletResponse response, List<?> data, Class clazz, String filename) {
//        try {
//            response.reset();
//            // 设置response的Header
//            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
//            OutputStream output = new BufferedOutputStream(response.getOutputStream());
//            response.setContentType("application/x-msdownload");
//            Workbook workbook = createWorkbook(data, clazz, true, filename, false);
//            workbook.write(output);
//            output.flush();
//            output.close();
//        } catch (Exception e) {
//            log.error("导出excel出现异常", e);
//        }
//    }

    /**
     * 根据数据生成Workbook
     *
     * @param data          待导出数据
     * @param clazz         映射对象Class
     * @param isWriteHeader 是否写入表头
     * @param sheetName     指定导出Excel的sheet名称
     * @param isXssf        导出的Excel是否为Excel2007及以上版本(默认是)
     * @throws Exception 异常
     * @author Crab2Died
     */
    public static Workbook createWorkbook(List<?> data, Class clazz, boolean isWriteHeader,
                                          String sheetName, boolean isXssf) {
        Workbook workbook = isXssf ? new XSSFWorkbook() : new HSSFWorkbook();
        Sheet sheet = !StringUtils.isEmpty(sheetName) ? workbook.createSheet(sheetName) : workbook.createSheet();

        List<ExcelHeader> headers = getHeaderList(clazz);
        if (isWriteHeader) {
            writeHeader(workbook, sheet, headers);
        }
        // 写数据
        Object data1;
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            data1 = data.get(i);
            for (int j = 0; j < headers.size(); j++) {
                row.createCell(j).setCellValue(getProperty(data1, headers.get(j).getField()));
            }
        }

        //设置列宽
//        for (int column = 0; column < headers.size(); column++) {
//            // 调整列宽
//            sheet.autoSizeColumn((short) column);
//            // 解决自动设置列宽中文失效的问题
////            sheet.setColumnWidth(column, sheet.getColumnWidth(column) * 17 / 10);
//        }
        return workbook;
    }

    private static void writeHeader(Workbook workbook, Sheet sheet, List<ExcelHeader> headers) {
        //title style
//        CellStyle cellStyle = workbook.createCellStyle();
//        Font font = workbook.createFont();
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        // 设置字体大小
//        font.setFontHeightInPoints((short) 12);
//        cellStyle.setFont(font);
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 写标题
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers.get(i).getTitle());
//            cell.setCellStyle(cellStyle);
        }
    }

    /**
     * 根据对象注解获取Excel表头信息
     *
     * @param clz
     * @return
     */
    private static List<ExcelHeader> getHeaderList(Class<?> clz) {
        List<ExcelHeader> headers = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazz = clz; clazz != Object.class; clazz = clazz.getSuperclass()) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }

        for (Field field : fields) {
            // 是否使用ExcelField注解
            if (field.isAnnotationPresent(ExcelField.class)) {
                ExcelField er = field.getAnnotation(ExcelField.class);
                headers.add(new ExcelHeader(er.title(), er.order(), field.getName(), field));
            }
        }
        Collections.sort(headers);
        return headers;
    }

    private static String getProperty(Object obj, Field field) {
        if (obj == null) {
            return null;
        }

        Object object = BeanUtil.getField(field, obj);
        return object == null ? "" : String.valueOf(object);
    }

    private static String getProperty(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }

        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), fieldName);
        if (propertyDescriptor == null) {
            return "";
        }
        Method method = propertyDescriptor.getReadMethod();

        if (method == null) {
            return "";
        }

        Object object = BeanUtil.invoke(method, obj);
        return object == null ? "" : String.valueOf(object);
    }

}
