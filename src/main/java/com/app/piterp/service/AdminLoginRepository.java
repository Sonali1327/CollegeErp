package com.app.piterp.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.piterp.model.AdminLogin;

public interface AdminLoginRepository extends JpaRepository<AdminLogin, String> {

}
