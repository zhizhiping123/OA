package com.entity;
import org.hibernate.annotations.Fetch;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @Author:EdenJia
 * @Date：create in 10:17 2017/9/29
 * @Describe: 用户实体
 */
@Entity
@Table(name="oa_user")
public class User  implements Serializable,UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
    @Column(name = "user_id",length = 11)
    private String userId;
    @Column(name = "username",length = 20,nullable = false)
    private String username;
    @Column(name = "password",length = 100,nullable = false)
    private String password;
    @Column(name = "answer1",length = 50)
    private String answer1;
    @Column(name = "answer2",length = 50)
    private String answer2;
    @Column(name = "email",length = 20)
    private String email;
    @Column(name = "phone_num",length = 11)
    private String phoneNum;
    @Column(name = "flag")
    private int flag; //标志符 0 离职，1在职
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;


    //用户与角色多对多
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns =
            @JoinColumn(name ="user_id",referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "role_id")
       )
    private List<Role> roles;

    //用户与部门多对一
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dept_id",referencedColumnName = "dept_id")
    private Department department;

    //用户与外出表 多对多
    @ManyToMany(mappedBy = "users")
    private Set<BusinessTrip> businessTrips;

    //用户与请假 多对多
    @ManyToMany(mappedBy = "users",fetch = FetchType.LAZY)
    private Set<Leave>  leaves;

    //用户与加班 多对多
    @ManyToMany(mappedBy = "users")
    private Set<OverTime> overTimes;

    //用户与报餐 多对多
    @ManyToMany(mappedBy = "users")
    private Set<Eating> eatings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String encodePasswd = encoder.encodePassword(password,"");
        this.password = encodePasswd;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<BusinessTrip> getBusinessTrips() {
        return businessTrips;
    }

    public void setBusinessTrips(Set<BusinessTrip> businessTrips) {
        this.businessTrips = businessTrips;
    }

    public Set<Leave> getLeaves() {
        return leaves;
    }

    public void setLeaves(Set<Leave> leaves) {
        this.leaves = leaves;
    }

    public Set<OverTime> getOverTimes() {
        return overTimes;
    }

    public void setOverTimes(Set<OverTime> overTimes) {
        this.overTimes = overTimes;
    }

    public Set<Eating> getEatings() {
        return eatings;
    }

    public void setEatings(Set<Eating> eatings) {
        this.eatings = eatings;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //  需将 List<Authority> 转成 List<SimpleGrantedAuthority>，否则前端拿不到角色列表名称
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (Role role: this.roles){
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        return simpleGrantedAuthorities;
    }


    public void setEncodePassword(String password) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String encodePasswd = encoder.encodePassword(password,"");
        this.password = encodePasswd;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
