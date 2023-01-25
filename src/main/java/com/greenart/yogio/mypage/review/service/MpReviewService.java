package com.greenart.yogio.mypage.review.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.greenart.yogio.member.entity.MbMemberInfoEntity;
import com.greenart.yogio.mypage.review.entity.MpMypageReviewViewEntity;
import com.greenart.yogio.mypage.review.entity.MpReviewInfoEntity;
import com.greenart.yogio.mypage.review.repository.MpMypageReviewViewRepository;
import com.greenart.yogio.mypage.review.repository.MpReviewInfoRepository;
import com.greenart.yogio.mypage.review.repository.MpStoreReviewCountVIewRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class MpReviewService {
  @Autowired MpStoreReviewCountVIewRepository reviewCntRepo;
  @Autowired
  MpMypageReviewViewRepository reviewViewRepo;
  @Autowired
  MpReviewInfoRepository reivewRepo;
  
  // review 리스트 출력 (등록일 내림차순)
  public Map<String, Object> showReviewList(MbMemberInfoEntity memberInfo, HttpSession session,
  @PageableDefault(size=8, sort = "reRegDt", direction = Sort.Direction.DESC) Pageable pageable) {
    Map<String, Object> map = new LinkedHashMap<>();
    MbMemberInfoEntity member = (MbMemberInfoEntity) session.getAttribute("loginUser");
    if (member == null) {
      map.put("status", false);
      map.put("message", "로그인 후 이용하실 수 있습니다.");
    } else {
      map.put("status", true);
      Page<MpMypageReviewViewEntity> review = reviewViewRepo.findAll(pageable);
      map.put("myReview", review.getContent());
      map.put("reviewCnt", review.getTotalElements());
      map.put("totalPage", review.getTotalPages());
      map.put("currentPage", review.getNumber());
    }
    return map;
  }
   
  // review 수 출력 
  public Map<String, Object> showReviewCnt(Long siSeq) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("reviewCnt", reviewCntRepo.findBySiSeq(siSeq));
    return map;
  }
  
  @Transactional
  public Map<String, Object> deleteReview(HttpSession session, MbMemberInfoEntity memberInfo, Long reSeq) {
    Map<String, Object> map = new LinkedHashMap<>();
    MbMemberInfoEntity member = (MbMemberInfoEntity) session.getAttribute("loginUser");
    MpReviewInfoEntity review = reivewRepo.findByReSeq(reSeq);

    Date day = new Date();
    Calendar today = Calendar.getInstance();
    // 오늘 날짜를 calendar로 매핑하기
    today.setTime(day);
    // 비교할 날짜 구하기
    Calendar regDt = Calendar.getInstance();
    // 댓글 작성일을 calendar로 매핑하기
    regDt.setTime(review.getReRegDt());
    // 두 날짜간의 차이를 계산 (천분의 일초 단위라서 1000으로 나눠 초 단위로 변환)
    Long diffSec = (today.getTimeInMillis() - regDt.getTimeInMillis())/1000;
    // 초단위를 다시 일 단위로 변환 (하루가 몇 초인지로 나눔)
    Long diffDay = diffSec / (24 * 60 * 60);

    if (member == null) {
      map.put("status", false);
      map.put("message", "로그인 후 이용하실 수 있습니다.");
    }
    else {
      if (review.getOrder().getMember().getMiSeq() != member.getMiSeq()) {
        map.put("status", false);
        map.put("message", "댓글은 작성자만 삭제 가능합니다.");
      }
      else {
        if (diffDay > 14) {
          map.put("status", false);
          map.put("message", "작성한 후 2주 내의 댓글만 삭제 가능합니다.");
        }
        // 댓글 등록일이 현재로부터 2주 내에 작성되었다면 삭제
        else {
          map.put("status", true);
          map.put("message", "댓글이 삭제되었습니다.");
          reivewRepo.deleteByReSeq(review.getReSeq());
        }
      }
    }
    return map;
  }
}
