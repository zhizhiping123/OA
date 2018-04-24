package com.service;

import com.entity.Leave;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.io.OutputStream;

public interface ILeavePoiService {
    void buildPoiByCondiction(Specification<Leave> spec, OutputStream os) throws IOException;
}
