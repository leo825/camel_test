/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/10/11
 * Time: 10:24
 * To change this template use File | Settings | File Templates.
 */
package com.leo.camel.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/11.
 */
public class POIUtil {

    /**
     * 读取出filePath中的所有数据信息
     *
     * @param filePath excel文件的绝对路径
     */

    public static void getDataFromExcel2(String filePath) {
        Sheet sheet = checkSheetvalidity(filePath);
        if (sheet != null) {
            //获得所有数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                //获得第i行对象
                Row row = sheet.getRow(i);
                try {
                    System.out.println("应用名称：" + row.getCell(0) + ",接口名称：" + row.getCell(1) + ",接口地址：" + row.getCell(2) + ",接口描述：" + row.getCell(3) + ",返回值类型：" + row.getCell(4));
                } catch (Exception e) {
                    System.out.println("获取单元格错误");
                }
            }
        } else {
            System.out.println("exls读取数据异常");
        }
    }

    /***
     * 检查excel的合法性
     */
    public static Sheet checkSheetvalidity(String filePath) {
        Sheet sheet = null;
        FileInputStream fis = null;
        Workbook wookbook = null;
        //判断是否为excel类型文件
        if (filePath.endsWith(".xls") || filePath.endsWith(".xlsx")) {
            try {
                //获取一个绝对地址的流
                fis = new FileInputStream(filePath);
            } catch (Exception e) {
                return null;
            }
            try {
                //2003版本的excel，用.xls结尾
                wookbook = new HSSFWorkbook(fis);//得到工作簿
            } catch (Exception ex) {
                try {
                    //这里需要重新获取流对象，因为前面的异常导致了流的关闭—————————————————————————————加了这一行
                    fis = new FileInputStream(filePath);
                    //2007版本的excel，用.xlsx结尾
                    wookbook = new XSSFWorkbook(filePath);//得到工作簿
                } catch (IOException e) {
                    return null;
                }
            }
            sheet = wookbook.getSheetAt(0); //得到一个工作表
            Row rowHead = sheet.getRow(0);//获得表头
            if (rowHead.getPhysicalNumberOfCells() != 5 || sheet.getLastRowNum() == 0) {//判断表头是否合格,这里看你有多少列,或者表为空
                System.out.println("表头列数与规定的不符合，或者表里数据为空");
                return null;
            }
        } else {
            System.out.println("文件不是excel类型");
        }
        return sheet;

    }


    public static void main(String[] args) {
        getDataFromExcel2("E:\\upload\\api\\api.xlsx");
    }
}
