package com.app.piterp.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.piterp.model.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry, Integer> {

}
