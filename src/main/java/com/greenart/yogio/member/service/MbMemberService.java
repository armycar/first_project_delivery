package com.greenart.yogio.member.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.greenart.yogio.member.entity.MbMemberInfoEntity;
import com.greenart.yogio.member.repository.MbMemberInfoRepository;
import com.greenart.yogio.member.vo.MbLoginVO;
import com.greenart.yogio.member.vo.MbMemberVO;
import com.greenart.yogio.utils.MbAESAlgorithm;

@Service
public class MbMemberService {
   @Autowired MbMemberInfoRepository m_repo;
   public Map<String, Object> addMember(MbMemberInfoEntity data) {
    Map<String ,Object> resultMap = new LinkedHashMap<String, Object>();
    if(m_repo.countByMiId(data.getMiId())==1) {
        resultMap.put("status", false);
        resultMap.put("message",data.getMiId()+"은/는 이미 등록된 사용자 입니다");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else if(data.getMiPwd().length()<8) {
      resultMap.put("status", false);
      resultMap.put("message", "비밀번호는 8자리 이상입니다");
      resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else {
      try {
        String encPwd = MbAESAlgorithm.Encrypt(data.getMiPwd());
        data.setMiPwd(encPwd);
      }  catch(Exception e) {e.printStackTrace();}
      m_repo.save(data);
      resultMap.put("status", true);
      resultMap.put("message", "회원이 등록되었습니다");
      resultMap.put("code", HttpStatus.CREATED);
    }
    return resultMap;
}
public Map<String, Object> loginMember(MbLoginVO data) {
    Map<String ,Object> resultMap = new LinkedHashMap<String, Object>();
    MbMemberInfoEntity loginUser = null; 
    try {
      loginUser = m_repo.findByMiIdAndMiPwd(
      data.getMiId(), MbAESAlgorithm.Encrypt(data.getMiPwd())
      );
    }catch(Exception e) {e.printStackTrace();}
    if(loginUser == null) {
      resultMap.put("status", false);
      resultMap.put("message", "아이디 또는 비밀번호 오류입니다");
      resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else {
      resultMap.put("status", true);
      resultMap.put("message", "로그인 되었습니다");
      resultMap.put("code", HttpStatus.ACCEPTED);
      resultMap.put("loginUser", loginUser);
    }

    return resultMap;
  }

  public Map<String, Object> searchMemberId(MbMemberVO data) {
    Map<String ,Object> resultMap = new LinkedHashMap<String, Object>();
    // 사용자 전화번호 받아서 리스트에 있는 것과 비교하여 해당 전화번호에 맞는 아이디 찾기
    MbMemberInfoEntity User = null; 
    User = m_repo.findByMiPhone(data.getMiPhone());
    if(User == null) {
      resultMap.put("status", false);
      resultMap.put("message", "해당하는 정보가 없습니다");
      resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else{
      resultMap.put("status", true);
      resultMap.put("message", "고객님의 아이디를 찾았습니다");
      resultMap.put("code", HttpStatus.ACCEPTED);
      resultMap.put("UserId", User.getMiId());
    }
    return resultMap;
  }

  public Map<String, Object> searchMemberPwd(MbMemberVO data) throws Exception{
    Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
    MbMemberInfoEntity User = null;
    try {
    User = m_repo.findByMiIdAndMiPhone(data.getMiId(),data.getMiPhone());
   }catch(Exception e) {e.printStackTrace();}
    if(User == null) {
      resultMap.put("status", false);
      resultMap.put("message", "해당하는 정보가 없습니다");
      resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else{
      resultMap.put("status", true);
      resultMap.put("message", "고객님의 비밀번호를 찾았습니다");
      resultMap.put("code", HttpStatus.ACCEPTED);
      resultMap.put("UserPwd", MbAESAlgorithm.Decrypt(User.getMiPwd()));
    }
    return resultMap;
  }
}