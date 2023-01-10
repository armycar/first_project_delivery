package com.greenart.yogio.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review_img")
public class MbReviewImageEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ri_seq") private Long riSeq; 
  @Column(name = "ri_re_seq") private Long riReSeq; 
  @Column(name = "ri_name") private String riName; 
  @Column(name = "ri_order") private Integer riOrder; 
  @ManyToOne
  @JoinColumn(name = "ri_re_seq") MbReviewEntity review;
}