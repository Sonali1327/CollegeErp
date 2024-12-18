package com.app.piterp.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.piterp.model.Respose;

public interface ResponseRepository extends JpaRepository<Respose, Integer> {

	@Query("select r from Respose r where r.responsetype=:responsetype")
	List<Respose> findbyResponsetype(@Param("responsetype") String responsetype);

}
