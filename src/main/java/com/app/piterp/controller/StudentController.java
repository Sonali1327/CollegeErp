package com.app.piterp.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.app.piterp.dto.ResponseDto;
import com.app.piterp.model.Material;
import com.app.piterp.model.Respose;
import com.app.piterp.model.StudentInfo;
import com.app.piterp.service.MaterialRepository;
import com.app.piterp.service.ResponseRepository;
import com.app.piterp.service.StudentInfoRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;






@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	ResponseRepository rrepo;
	@Autowired
	StudentInfoRepository srepo;
	@Autowired
	MaterialRepository mrepo;
	
	@GetMapping("/studenthome")
	public String showStudentHome(HttpSession session,HttpServletResponse response)
	{
		try {
			response.setHeader("cache-control","no-cache, no-store,must-revalidate");
			
			if(session.getAttribute("userid")!= null) {
				return"/student/studenthome";
			}
			else {
				return"redirect:/stulogin";
			}
		
		
	}
	catch(Exception e){
		return"redirect:/stulogin";
	}
		
	}
	//logout
	
	@GetMapping("stulogout")
	public String stuLogout(HttpSession session) {
		
		session.invalidate();
		return "redirect:/stulogin";
		
		}
	
	//change password
	
		@GetMapping("/changepassword")
		public String showChangePassword(HttpSession session,HttpServletResponse response)
		{
			
			try {
				response.setHeader("cache-control","no-cache, no-store,must-revalidate");
				
				if(session.getAttribute("userid")!= null) {
					return"/student/changepassword";
				}
				else {
					return"redirect:/stulogin";
				}
			
			
		}
		catch(Exception e){
			return"redirect:/stulogin";
		}
					
				
		}
		
		@PostMapping("/changepassword")
		public String ChangePassword(HttpSession session,HttpServletResponse response,HttpServletRequest request, RedirectAttributes redirectAttributes)
		{
			
			try {
				response.setHeader("cache-control","no-cache, no-store, must-revalidate");
				
				if(session.getAttribute("userid")!= null) {
					
					String oldPassword= request.getParameter("oldpassword");
					String newPassword = request.getParameter("newpassword");
					String confirmPassword = request.getParameter("confirmpassword");
					if(!newPassword.equals(confirmPassword))
					{
						redirectAttributes.addFlashAttribute("msg", "Newpassword And Confirmpassword Are Not Matched");
						return "redirect:/student/changepassword";
						}
					 StudentInfo reg = srepo.getById(Long.parseLong(session.getAttribute("userid").toString()));
					 if(!oldPassword.equals(reg.getPassword()))
					 {
						 redirectAttributes.addFlashAttribute("msg", "Oldpassword is not matched");
						 return"redirect:/student/changepassword";
					 }
					 
					     else
					     {
						 reg.setPassword(newPassword);
						 srepo.save(reg);
						 return"redirect:/student/stulogout";
						 
					 }
				}
				else 
				{
					
					return"redirect:/stulogin";
				}
			
			
			}
		catch(Exception e){
			return"redirect:/stulogin";
		}
					
				
		}
		
	@GetMapping("/giveresponse")
		public String showGiveResponse(HttpSession session,HttpServletResponse response,Model model)
		{
			try {
				response.setHeader("cache-control","no-cache, no-store,must-revalidate");
				
				if(session.getAttribute("userid")!= null) {
					ResponseDto dto =new ResponseDto();
					model.addAttribute("dto", dto);
					
					return"/student/giveresponse";
				}
				else {
					return"redirect:/stulogin";
				}
			
			
		}
		catch(Exception e){
			return"redirect:/stulogin";
		}
			
		}
		@PostMapping("/giveresponse")
		public String giveResponse( HttpSession session , Model model,@ModelAttribute HttpServletResponse response,ResponseDto responseDto,RedirectAttributes redirectAttributes)
		{
			
				//response.setHeader("cache-control","no-cache, no-store,must-revalidate");
				
				if(session.getAttribute("userid")!= null) {
					StudentInfo reg=srepo.getById(Long.parseLong(session.getAttribute("userid").toString()));
					model.addAttribute("dto",reg.getName());
					Respose res =new Respose();
					res.setEnrollmentno(reg.getEnrollmentno());
					res.setName(reg.getName());
					res.setProgram(reg.getProgram());
					res.setBranch(reg.getBranch());
					res.setYear(reg.getYear());
					res.setResponsetype(responseDto.getResponsetype());
					res.setResponsetext(responseDto.getResponsetext());
					Date dt=new Date();
					SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy");
					String posteddate = df.format(dt);
					res.setPosteddate(posteddate);
					rrepo.save(res);
					redirectAttributes.addFlashAttribute("msg", "Response is submitted");
					return"redirect:/student/giveresponse";
				    
				}
				else {
					return"/student/stulogin";
				}
				
				
			
			
			
		
			
		}
		@GetMapping("/viewstudymaterial")
		public String showStudyMaterial(HttpSession session, HttpServletResponse response, Model model) {
			try {
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				if (session.getAttribute("userid") != null) {
					StudentInfo studentInfo=srepo.findById(Long.parseLong(session.getAttribute("userid").toString())).get();
					List<Material> mat=mrepo.getMaterial(studentInfo.getProgram(),studentInfo.getBranch(),studentInfo.getYear(),"mat");
					model.addAttribute("mat", mat);
					return "/student/viewstudymaterial";
				} else {
					return "redirect:/stulogin";
				}

			} catch (Exception e) {
				return "redirect:/stulogin";
			}
		}
		@GetMapping("/viewassignment")
		public String showAssignment(HttpSession session, HttpServletResponse response, Model model) {
			try {
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				if (session.getAttribute("userid") != null) {
					StudentInfo s=srepo.findById(Long.parseLong(session.getAttribute("userid").toString())).get();
					List<Material> assign=mrepo.getMaterial(s.getProgram(), s.getBranch(), s.getYear(), "assign");
					model.addAttribute("assign", assign);
					return "/student/viewassignment";
				} else {
					return "redirect:/stulogin";
				}

			} catch (Exception e) {
				return "redirect:/stulogin";
			}
		}
}
