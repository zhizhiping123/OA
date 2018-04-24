package com.service.impl;

import com.common.ServerResponse;
import com.dao.DepartmentRepository;
import com.dto.BusinessTripDto;
import com.dto.PageDto;
import com.dto.UserDto;
import com.entity.BusinessTrip;
import com.entity.Department;
import com.entity.Role;
import com.entity.User;
import com.service.IBusinessTripPoiService;
import com.service.IBusinessTripService;
import com.service.IDepartmentService;
import com.service.impl.BusinessTripService;
import com.service.impl.DepartmentService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * @Author:EdenJia
 * @Date：create in 15:11 2017/10/13
 * @Describe:外出生成报表
 */
@Service
public class BusinessTripPoiService implements IBusinessTripPoiService{

   @Autowired
    private  IBusinessTripService iBusinessTripService;
   @Autowired
    private IDepartmentService iDepartmentService;
    //根据每部们每月外出报表

    /**
     *
     */
    public  void buildPoiByDepartmentIdandTime(Specification<BusinessTrip> userSpecification, OutputStream outputStream){
        Workbook wb = new XSSFWorkbook();
        //生成一个sheet
        Sheet sheet = wb.createSheet("sheet1");
        sheet.autoSizeColumn(1);

        //sheet 的第一行
        Row row = sheet.createRow(0);
        Cell cell_0 = row.createCell(0, CellType.STRING);
        cell_0.setCellValue("出行报表");
        row.setHeight(Short.parseShort("10"));

        //生产报表，每一行的内容
        Row row2 = sheet.createRow(1);

        Cell cell1 = row2.createCell(0,CellType.STRING);
        cell1.setCellValue("姓名");
        Cell cell2 = row2.createCell(1,CellType.STRING);
        cell2.setCellValue("工号");
        Cell cell3 = row2.createCell(2,CellType.STRING);
        cell3.setCellValue("部门");
        Cell cell4 = row2.createCell(3,CellType.STRING);
        cell4.setCellValue("职位");
        Cell cell5 = row2.createCell(4,CellType.STRING);
        cell5.setCellValue("外出时间");
        Cell cell6 = row2.createCell(5,CellType.STRING);
        cell6.setCellValue("人数");
        Cell cell7 = row2.createCell(6,CellType.STRING);
        cell7.setCellValue("出发地点");
        Cell cell8 = row2.createCell(7,CellType.STRING);
        cell8.setCellValue("目的地");
        Cell cell9 = row2.createCell(8,CellType.STRING);
        cell9.setCellValue("联系电话");
        Cell cell10 = row2.createCell(9,CellType.STRING);
        cell10.setCellValue("返回时间");
        Cell cell11 = row2.createCell(10,CellType.STRING);
        cell11.setCellValue("描述");
        Cell cell12 = row2.createCell(11,CellType.STRING);
        cell12.setCellValue("领导审批");
        Cell cell13 = row2.createCell(12,CellType.STRING);
        cell13.setCellValue("人事审批");
        Cell cell14 = row2.createCell(13,CellType.STRING);
        cell14.setCellValue("车辆类型");
        Cell cell15 = row2.createCell(14,CellType.STRING);
        cell15.setCellValue("状态");
        Cell cell16 = row2.createCell(15,CellType.STRING);
        cell16.setCellValue("创建时间");
        Cell cell17 = row2.createCell(16,CellType.STRING);
        cell17.setCellValue("修改时间");

        //生成数据
        ServerResponse serverResponse = iBusinessTripService.findByCondiction(userSpecification,null);
      //  PageDto pageDto = (PageDto)serverResponse.getData();
        List<BusinessTripDto> businessTripDtos =(List<BusinessTripDto>) serverResponse.getData();
        for (int i = 0;i<businessTripDtos.size();i++){
            BusinessTripDto businessTripDto = businessTripDtos.get(i);
            row2 = sheet.createRow(i+2);
            Cell cell0 = row2.createCell(0,CellType.STRING);

            Cell cell_1 = row2.createCell(1,CellType.STRING);
            Cell cell_2 = row2.createCell(3,CellType.STRING);
            for (UserDto userDto: businessTripDto.getUsers()){
                cell0.setCellValue(userDto.getUsername());
                cell_1.setCellValue(userDto.getUserId());
                cell_2.setCellValue(userDto.getRoleName().get(0));
            }
            Cell cell = row2.createCell(2,CellType.STRING);
            Department department = iDepartmentService.findDepartmentById(businessTripDto.getDepartmentId());
            cell.setCellValue(department.getDepartmentName());
            Cell cell_3 = row2.createCell(4,CellType.STRING);
            cell_3.setCellValue(businessTripDto.getFromTime());
            Cell cell_4 = row2.createCell(5,CellType.STRING);
            cell_4.setCellValue(businessTripDto.getPersonNum());
            Cell cell_5 = row2.createCell(6,CellType.STRING);
            cell_5.setCellValue(businessTripDto.getFromLocation());

            Cell cell_6 = row2.createCell(7,CellType.STRING);
            cell_6.setCellValue(businessTripDto.getToLocation());
            Cell cell_7 = row2.createCell(8,CellType.STRING);
            cell_7.setCellValue(businessTripDto.getPhoneNum());
            Cell  cell_8 = row2.createCell(9,CellType.STRING);
            cell_8.setCellValue(businessTripDto.getToTime());
            Cell cell_9 = row2.createCell(10,CellType.STRING);
            cell_9.setCellValue(businessTripDto.getDescribe());
            Cell cell_10 = row2.createCell(11,CellType.STRING);
            cell_10.setCellValue(businessTripDto.getDepartmentOpinion());
            Cell cell_11 = row2.createCell(12,CellType.STRING);
            cell_11.setCellValue(businessTripDto.getHrOpinion());
            Cell cell_12 = row2.createCell(13,CellType.STRING);
            cell_12.setCellValue(businessTripDto.getCarType());
            Cell cell_13 = row2.createCell(14,CellType.STRING);
            if (businessTripDto.getFlag() == 1){
                cell_13.setCellValue("审核通过");
            }
            else{
                cell_13.setCellValue("审核未通过");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Cell cell_14 = row2.createCell(15,CellType.STRING);
            cell_14.setCellValue(sdf.format(businessTripDto.getCreateTime()));
            Cell cell_15 = row2.createCell(16,CellType.STRING);
            cell_15.setCellValue(sdf.format(businessTripDto.getCreateTime()));
        }
        for (int i = 0;i<row2.getLastCellNum();i++){
            sheet.setColumnWidth(i,30*256);
        }
        try {
            wb.write(outputStream);
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
