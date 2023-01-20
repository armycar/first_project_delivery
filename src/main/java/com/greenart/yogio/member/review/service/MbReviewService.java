package com.greenart.yogio.member.review.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.greenart.yogio.member.entity.MbMemberInfoEntity;
import com.greenart.yogio.member.review.entity.MbOrderInfoEntity;
import com.greenart.yogio.member.review.entity.MbReviewEntity;
import com.greenart.yogio.member.review.entity.MbReviewImageEntity;
import com.greenart.yogio.member.review.repository.MbReviewImageRepository;
import com.greenart.yogio.member.review.repository.MbReviewRepository;
import com.greenart.yogio.member.review.vo.MbReviewVO;

import jakarta.servlet.http.HttpSession;

@Service
public class MbReviewService {
    @Autowired MbReviewRepository r_repo;
    @Autowired MbReviewImageRepository i_repo;
    public Map<String, Object> addReview(MbReviewVO data, HttpSession session) { //리뷰쓰기
     Map<String ,Object> resultMap = new LinkedHashMap<String, Object>();
     MbMemberInfoEntity loginUser = (MbMemberInfoEntity)session.getAttribute("loginUser");
     if(loginUser == null) {
        resultMap.put("status", false);
        resultMap.put("message", "로그인 후 사용가능합니다");
        resultMap.put("code",HttpStatus.BAD_REQUEST);
        return resultMap;
     }
    //  MbOrderInfoEntity order = MbOrderInfoEntity.builder().build();
     MbReviewEntity review = MbReviewEntity.builder()
     .reRegDt(data.getRegDt())
     .reScore(data.getScore())
     .reContent(data.getContent())
     .reOiSeq(data.getOiSeq())
     .reTasteScore(data.getTasteScore())
     .reQuantityScore(data.getQuantityScore())
     .reDeliveryScore(data.getDeliveryScore())
     .build();
     r_repo.save(review);

     MbReviewImageEntity image = MbReviewImageEntity.builder()
     .riName(data.getName())
     .riOrder(data.getOrder())
     .riReSeq(review)
     .build();
      i_repo.save(image);

      resultMap.put("status", true);
      resultMap.put("message", "리뷰가 등록되었습니다");
      resultMap.put("code", HttpStatus.CREATED);
      return resultMap;
    }
}