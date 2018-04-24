package com.dao;

import com.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.Map;


/**
 * Created by jiale on 2017/10/9.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {

    User findByUserId(String userId);

    User findUserByUsername(String username);

    @Query("select u from User u join u.department dept where dept.departmentId = :deptId")
    Page<User> findByDeptId(@Param("deptId") String deptId, Pageable pageable);

    @Query("select u from User u join  u.roles role where role.roleId = :roleId")
    Page<User> findByRoleId(@Param("roleId") Long roleId, Pageable pageable);

    @Query("select count(u.userId) from User u join u.department dept where dept.departmentId = :deptId")
    int countByDeptId(@Param("deptId") String deptId);

    /*注意需要modifying*/
    @Modifying
    @Query("update User u set u.flag = :flag where u.userId = :userId")
    int updateUserFlag(@Param("flag") Integer flag, @Param("userId") String userId);

    @Query("select u from User u left join u.leaves l where l.leaveId=:leaveId")
    User findByLeaveId(@Param("leaveId") String leaveId);

    public static class Specs{

        /**
         * 根据查询条件构造动态查询sql
         * username
         * userId
         * deptId
         *
         * @param paramsMap
         * @return
         */
        public static Specification<User> buQueryParams(Map<String,String> paramsMap){

            return new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    // 创建 Predicate
                    Predicate predicate = criteriaBuilder.conjunction();

                    // 组装条件
                    if(StringUtils.isNotBlank(paramsMap.get("username"))){
                        predicate.getExpressions().add(criteriaBuilder.like(root.<String>get("username")
                                ,"%"+ paramsMap.get("username") + "%"));
                    }
                    if(StringUtils.isNotBlank(paramsMap.get("userId"))){
                        predicate.getExpressions().add(criteriaBuilder.equal(root.<String>get("userId"),paramsMap.get("userId")));
                    }
                    if(StringUtils.isNotBlank(paramsMap.get("deptId"))){
                        // 注意联表的问题
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("department").get("departmentId"),paramsMap.get("deptId")));
                    }
                    return predicate;
                }
            };

        }
    }





}

