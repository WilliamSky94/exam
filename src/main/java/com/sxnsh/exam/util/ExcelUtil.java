package com.sxnsh.exam.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author yxz
 * excel处理工具类  提供方法判断当前行是否为空  获取当前单元格的数据
 */
@Slf4j
public class ExcelUtil {

    /**判断当前行的数据是否符合预期 是否为空**/
    public static int CheckRowNull(HSSFRow hssfRow,int rowNum){
        int num = 0;
        Iterator<Cell> cellItr =hssfRow.iterator();
        while(cellItr.hasNext() && num < rowNum){
            Cell c =cellItr.next();
            if(c.getCellTypeEnum() == CellType.BLANK){
                num++;
            }
        }
        return num;
    }

    /***获取当前cell的数据**/
    public static String getCellValue(HSSFCell cell) {
        String cellValue = "";
        DecimalFormat df = new DecimalFormat("0.00");
        if(cell == null){
            return "";
        }
        switch (cell.getCellTypeEnum()) {
            case STRING:
                cellValue =cell.getRichStringCellValue().getString().trim();
                break;
            case NUMERIC:
                cellValue =df.format(cell.getNumericCellValue()).toString();
                break;
            case BOOLEAN:
                cellValue =String.valueOf(cell.getBooleanCellValue()).trim();
                break;
            case FORMULA:
                cellValue =cell.getCellFormula();
                break;
            default:
                cellValue = "";
        }
        return cellValue;
    }

    /**校验传入的row 在前n位是否都为空 如果是 这条数据不符合录入标准**/
    public static boolean isNeedNumNull(HSSFRow row, int n){

        for (int i = 0; i < n; i++) {
            if(row.getCell(i).getCellTypeEnum() == CellType.BLANK){
                return true;
            }
        }

        return false;

    }

    /**传入文件路径，返回所有的xls文件**/
    public static List<String> fileCollect(String filePath){
        List<String> fileList = new ArrayList<String>();
//        this.findExcel(filePath,fileList);
        return fileList;

    }

    /**内部函数-用于递归获取excel文件**/
    private static void findExcel(String filePath, List<String> files){
        File excelDic = new File(filePath);
        File[] tempFileList = excelDic.listFiles();

        for (int k = 0; k < tempFileList.length; k++) {
            /**对是文件的记录文件名**/
            if(tempFileList[k].isFile()){
                String tempFileName = tempFileList[k].toString();
                if(tempFileName.indexOf(".xls") > 0){
                    files.add(tempFileName.substring(tempFileName.lastIndexOf("\\") +1));
                }else{
                    log.error("The  " + tempFileName + "is not a xls file");
                }

            }
            if(tempFileList[k].isDirectory()){
                findExcel(tempFileList[k].toString(),files);
                continue;
            }
        }

    }


}
