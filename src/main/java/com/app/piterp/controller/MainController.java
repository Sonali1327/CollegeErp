package com.app.piterp.controller;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.piterp.api.SmsSender;
import com.app.piterp.dto.AdminLoginDto;
import com.app.piterp.dto.EnquiryDto;
import com.app.piterp.dto.StudentInfoDto;
import com.app.piterp.model.AdminLogin;
import com.app.piterp.model.Enquiry;
import com.app.piterp.model.StudentInfo;
import com.app.piterp.service.AdminLoginRepository;
import com.app.piterp.service.EnquiryRepository;
import com.app.piterp.service.StudentInfoRepository;

import jakarta.servlet.http.HttpSession;


@Controller
public class MainController {
	@Autowired
	AdminLoginRepository arepo;
	@Autowired
	StudentInfoRepository srepo;
	@Autowired
	EnquiryRepository erepo;
	
	
	
	@GetMapping("/")
	public String showIndex()

	{
		return "index";
	}
	@GetMapping("/aboutus")
	public String showAboutUs()
	{
		return "aboutus";
	}
	@GetMapping("/life@pgi")
	public String showlifepgi()
	{
		return "life@pgi";
	}
	
	@GetMapping("/registration")
	public String showRegistration(Model model)
	{
		StudentInfoDto dto= new StudentInfoDto();
		model.addAttribute("dto", dto);
		return "registration";
	}

	@PostMapping("/registration")
	public String SubmitRegistration(@ModelAttribute StudentInfoDto dto,RedirectAttributes redirectAttributes)
	{
		StudentInfo s =new StudentInfo();
		s.setEnrollmentno(dto.getEnrollmentno());
		s.setName(dto.getName());
		s.setFname(dto.getFname());
		s.setMname(dto.getMname());
		s.setGender(dto.getGender());
		s.setAddress(dto.getAddress());
		s.setProgram(dto.getProgram());
		s.setBranch(dto.getBranch());
		s.setYear(dto.getYear());
		s.setContactno(dto.getContactno());
		s.setEmailaddress(dto.getEmailaddress());
		s.setPassword(dto.getPassword());
		Date dt =new Date();
		SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy");
		String regdate = df.format(dt);
		s.setRegdate(regdate);
		srepo.save(s);
		
		return "redirect:/stulogin";
	}
	
	
	 @GetMapping("/stulogin")
	    public String showStuLogin() {
	        return "stulogin"; // Ensure this matches the JSP name without the .jsp extension
	    }
	 @PostMapping("/stulogin")
		public String SubmitStulogin(@ModelAttribute StudentInfoDto dto,HttpSession session,RedirectAttributes redirectAttributes)
		{
			
	
		
		 StudentInfo studentInfo=srepo.getById(dto.getEnrollmentno());
			if (studentInfo.getPassword().equals( dto.getPassword()))
				
			{
			session.setAttribute("userid", dto.getEnrollmentno());
			return "redirect:/student/studenthome";
			}
			redirectAttributes.addFlashAttribute("message", "Student not register");
			return "redirect:/stulogin";
		}
	@GetMapping("/adminlogin")
	public String showAdminLogin()
	{
		return "adminlogin";
	}
	
	@PostMapping("/adminlogin")
	public String saveAdminLogin(@ModelAttribute AdminLoginDto dto,HttpSession session,RedirectAttributes redirectAttributes)
	{
		AdminLogin adminLogin = arepo.getById(dto.getUserid());
		if (adminLogin.getPassword().equals(dto.getPassword())) {
			session.setAttribute("adminid", dto.getPassword());
			return"redirect:/admin/adminhome";
		}
		redirectAttributes.addFlashAttribute("message","user does not exist" );
		return"redirect:/adminlogin";
	}
	@GetMapping("/contactus")
	public String showContactUs(Model model)
	{
		EnquiryDto dto=new EnquiryDto();
		model.addAttribute("dto",dto);		
		return "contactus";
	}
	@PostMapping("/contactus")
	public String saveEnquiry(@ModelAttribute EnquiryDto dto, RedirectAttributes redirectAttributes )
	{
		Enquiry e= new Enquiry();
		e.setName(dto.getName());
		e.setContactno(dto.getContactno());
		e.setEmailaddress(dto.getEmailaddress());
		e.setEnquirytext(dto.getEnquirytext());
		Date dt = new Date();
		SimpleDateFormat df= new SimpleDateFormat("dd//MM//yyyy");
	    String posteddate= df.format(dt);
		e.setPosteddate(posteddate);
		erepo.save(e);
		SmsSender ss=new SmsSender();
		ss.sendSms(dto.getContactno());
		redirectAttributes.addFlashAttribute("msg", "Enquiry is saved successfully");
		
		return"redirect:/contactus";
	}
	
	
	
}

