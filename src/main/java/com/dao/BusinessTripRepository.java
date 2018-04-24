package com.dao;

import com.entity.BusinessTrip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author:EdenJia
 * @Date：create in 11:26 2017/10/8
 * @Describe:
 */
@Repository
public interface BusinessTripRepository extends JpaRepository<BusinessTrip,Long>,JpaSpecificationExecutor<BusinessTrip> {

   // @Override
  //  Page<BusinessTrip> findAll(Pageable pageable);

    @Query("select b from BusinessTrip b join fetch b.users u where u.userId = :userId and b.businessId =:businessId")
    BusinessTrip seltectByUserIdAndBusinessId(@Param("userId")String userId,@Param("businessId") String businessId);

    BusinessTrip findByBusinessId(String businessId);


}
