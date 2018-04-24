package com.service;

import com.common.ServerResponse;
import com.dto.FindPwdDto;

public interface ILoginService {

    ServerResponse findPwd(FindPwdDto pwdDto);

    ServerResponse resetPwd(String username,String newPassword);

    ServerResponse buildLoginSuccessData(String userId);

}
