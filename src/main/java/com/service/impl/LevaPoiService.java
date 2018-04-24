package com.service.impl;

import com.common.LeaveStatus;
import com.common.LeaveType;
import com.dao.LeaveRepository;
import com.dao.UserRepository;
import com.dto.LeaveDto;
import com.entity.Leave;
import com.entity.User;
import com.service.ILeavePoiService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

/**
 * create by zzping
 */
@Service
public class LevaPoiService implements ILeavePoiService {
    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private UserRepository userRepository;

    public void buildPoiByCondiction(Specification<Leave> spec, OutputStream os) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet xssfSheet = workbook.createSheet("请假导出");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        Font font = workbook.createFont();        //设置字体
        font.setFontHeightInPoints((short) 20);
        cellStyle.setFont(font);//把字体设到单元格风格
        xssfSheet.addMergedRegion(new CellRangeAddress(0, 3, 0, 7));//标题行,合并前三行
        XSSFRow row = xssfSheet.createRow(0);//第一行
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("本部门月度请假");//标题
        cell.setCellStyle(cellStyle);   //设置风格

        XSSFRow row1 = xssfSheet.createRow(4);//列字段
        row1.createCell(0,CellType.STRING).setCellValue("工号");
        row1.createCell(1, CellType.STRING).setCellValue("申请人");
        row1.createCell(2, CellType.STRING).setCellValue("部门");
        row1.createCell(3, CellType.STRING).setCellValue("类型");
        row1.createCell(4, CellType.STRING).setCellValue("原因");
        row1.createCell(5, CellType.STRING).setCellValue("开始时间");
        row1.createCell(6, CellType.STRING).setCellValue("结束时间");
        row1.createCell(7, CellType.STRING).setCellValue("部门意见");
        row1.createCell(8, CellType.STRING).setCellValue("人事意见");
        row1.createCell(9, CellType.STRING).setCellValue("状态");

        //查找符合条件的
        List<Leave> leaveList = leaveRepository.findAll(spec);
        int lastRowNum = xssfSheet.getLastRowNum();
        for (int i = 0; i < leaveList.size(); i++) {
            XSSFRow row2 = xssfSheet.createRow(i + lastRowNum + 1);
            Leave leave = leaveList.get(i);
            String leaveId = leave.getLeaveId();
            User user = userRepository.findByLeaveId(leaveId);
            String userName = user.getUsername();
            row2.createCell(0, CellType.STRING).setCellValue(user.getUserId());//工号
            row2.createCell(1, CellType.STRING).setCellValue(userName);//申请人
            row2.createCell(2, CellType.STRING).setCellValue(user.getDepartment().getDepartmentName());//部门
            row2.createCell(3, CellType.STRING).setCellValue(leaveTypeToMsg(leave.getTypes()));//申请类型
            row2.createCell(4, CellType.STRING).setCellValue(leave.getDescribe());//申请原因
            row2.createCell(5, CellType.STRING).setCellValue(leave.getFromTime());//开始时间
            row2.createCell(6, CellType.STRING).setCellValue(leave.getToTime());//结束时间
            row2.createCell(7, CellType.STRING).setCellValue(leave.getDepartmentOpinion());//部门意见
            row2.createCell(8, CellType.STRING).setCellValue(leave.getHrOpinion());//人事意见
            row2.createCell(9, CellType.STRING).setCellValue(flagToMsg(leave.getFlag()));//状态
        }

        workbook.write(os);
        os.close();
    }

    private static String flagToMsg(int flag) {
        for (LeaveStatus leaveStatus : LeaveStatus.values()) {
            if (leaveStatus.getStatus() == flag) {
                return leaveStatus.getMsg();

            }

        }
        return null;
    }

    private static String leaveTypeToMsg(String types) {
        for (LeaveType leaveType : LeaveType.values()) {
            if (leaveType.name().equals(types)) {
                return leaveType.getType();
            }

        }
        return null;
    }
}
