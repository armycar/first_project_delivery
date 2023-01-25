package com.greenart.yogio.mypage.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greenart.yogio.mypage.order.entity.MpOrderInfoEntity;
import com.greenart.yogio.mypage.order.entity.MpPlusMenuChoiceEntity;

@Repository
public interface MpPlusMenuChoiceRepository extends JpaRepository<MpPlusMenuChoiceEntity, Long> {
  Page<MpPlusMenuChoiceEntity> findByOrder(MpOrderInfoEntity order, Pageable pageable);
}