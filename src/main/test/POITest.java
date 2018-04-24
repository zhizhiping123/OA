import com.service.impl.LeaveService;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class POITest {


    @Test
    public void test() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        FileOutputStream os=new FileOutputStream(new File("C:\\Users\\33709\\Desktop\\poi1.xlsx"));
        XSSFSheet xssfSheet=workbook.createSheet("请假导出");
        CellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        Font font=workbook.createFont();        //设置字体
        font.setFontHeightInPoints((short)20);
        cellStyle.setFont(font);//把字体设到单元格风格
        xssfSheet.addMergedRegion(new CellRangeAddress(0,3,0,7));//标题行,合并前三行
        xssfSheet.setColumnWidth(0,25000);
        XSSFRow row = xssfSheet.createRow(0);//第一行
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("本部门月度请假");//标题
        cell.setCellStyle(cellStyle);   //设置风格

        XSSFRow row1 = xssfSheet.createRow(4);//列字段
        row1.createCell(0).setCellValue("申请人");
        row1.createCell(1).setCellValue("类型");
        row1.createCell(2).setCellValue("原因");
        row1.createCell(3).setCellValue("开始时间");
        row1.createCell(4).setCellValue("部门意见");
        row1.createCell(5).setCellValue("人事意见");
        row1.createCell(6).setCellValue("开始时间");
        row1.createCell(7).setCellValue("结束时间");

        System.out.println(xssfSheet.getLastRowNum());
        


        workbook.write(os);
        workbook.close();

    }





}
