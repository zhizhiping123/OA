package com.service;

import com.entity.BusinessTrip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.OutputStream;

/**
 * @Author:EdenJia
 * @Date：create in 10:28 2017/10/14
 * @Describe:
 */
public interface IBusinessTripPoiService {
    void buildPoiByDepartmentIdandTime(Specification<BusinessTrip> userSpecification, OutputStream outputStream);
}
