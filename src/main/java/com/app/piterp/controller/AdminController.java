package com.app.piterp.controller;

import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.piterp.dto.MaterialDto;
import com.app.piterp.model.Enquiry;
import com.app.piterp.model.Material;
import com.app.piterp.model.Respose;
import com.app.piterp.model.StudentInfo;
import com.app.piterp.service.EnquiryRepository;
import com.app.piterp.service.MaterialRepository;
import com.app.piterp.service.ResponseRepository;
import com.app.piterp.service.StudentInfoRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	
@Autowired
StudentInfoRepository srepo;
@Autowired
EnquiryRepository erepo;
@Autowired
ResponseRepository rrepo;
@Autowired
MaterialRepository mrepo;
	
	@GetMapping("/adminhome")
	public String ShowAdminHome(HttpSession session , HttpServletResponse response)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store,must-revalidate");
				if (session.getAttribute("adminid")!=null)
			{
			return "/admin/adminhome";	
			}
			else {
				return "/admin/adminhome";
				
			}
			} catch (Exception e) {																																																																																															
			return "redirect:/adminlogin";
		}
	
	}
	//logout
	
		@GetMapping("logout")
		public String stuLogout(HttpSession session) {
			
			session.invalidate();
			return "redirect:/adminlogin";
			
			}
	@GetMapping("/managestudent")
	public String ShowStudent(HttpSession session , HttpServletResponse response,Model model)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store,must-revalidate");
				if (session.getAttribute("adminid")!=null)
			{
					List<StudentInfo>silist=srepo.findAll();
					model.addAttribute("silist", silist);
			return "/admin/managestudent";	
			}
			else {
				return "/admin/adminhome";
				
			}
			} catch (Exception e) {
			return "redirect:/adminlogin";
		}
	
	}
	
	@GetMapping("/manageenquiries")
	public String Showenquiries(HttpSession session , HttpServletResponse response,Model model)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store,must-revalidate");
				if (session.getAttribute("adminid")!=null)
			{
				List<Enquiry>enq=erepo.findAll();
				model.addAttribute("enq", enq);
			return "/admin/manageenquiries";	
			}
			else {
				return "/admin/adminhome";
				
			}
			} catch (Exception e) {
			return "redirect:/adminlogin";
		}
	
	}
	@GetMapping("/delenq")
	public String ShowAdminHome(HttpSession session , HttpServletResponse response,@RequestParam int id)
	{
		try {
			response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
				if (session.getAttribute("adminid")!=null)
			{
			Enquiry enq=erepo.getById(id);
			erepo.delete(enq);
			return "/admin/manageenquiries";	
			}
			else {
				return "/admin/adminhome";
				
			}
			} catch (Exception e) {																																																																																															
			return "redirect:/adminlogin";
		}
	
	}
	@GetMapping("/managefeedback")
	public String Showfeedback(HttpSession session , HttpServletResponse response,Model model)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store,must-revalidate");
				if (session.getAttribute("adminid")!=null)
			{
			List<Respose>listfeedback=rrepo.findbyResponsetype("feedback");	
			model.addAttribute("listfeedback", listfeedback);
			return "/admin/managefeedback";	
			}
			else {
				return "/admin/adminhome";
				
			}
			} catch (Exception e) {																																																																																															
			return "redirect:/adminlogin";
		}
	
	}
	@GetMapping("/managecomplaint")
	public String Showcomplaint(HttpSession session , HttpServletResponse response,Model model)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store,must-revalidate");
				if (session.getAttribute("adminid")!=null)
			{
			List<Respose> listcomp=rrepo.findbyResponsetype("complaint");
			model.addAttribute("listcomp", listcomp);
			return "/admin/managecomplaint";	
			}
			else {
				return "/admin/adminhome";
				
			}
			} catch (Exception e) {																																																																																															
			return "redirect:/adminlogin";
		}
	
	}
	@GetMapping("/addstudymaterial")
	public String ShowAddStudyMaterial(HttpSession session , HttpServletResponse response,Model model)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store,must-revalidate");
				if (session.getAttribute("adminid")!=null)
			{
				MaterialDto dto=new MaterialDto();
				model.addAttribute("dto", dto);
			return "/admin/addstudymaterial";	
			}
			else {
				return "/admin/adminhome";
				
			}
			} catch (Exception e) {																																																																																															
			return "redirect:/adminlogin";
		}
	
	}
	@PostMapping("/addstudymaterial")
	public String SaveAddStudyMaterial(HttpSession session, HttpServletResponse response, @ModelAttribute MaterialDto dto, RedirectAttributes redirectAttributes) {
	    try {
	        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	        
	        if (session.getAttribute("adminid") != null) {
	            MultipartFile uploadfile = dto.getUploadfile();
	            String storageFileName = new Date().getTime() + "_" + uploadfile.getOriginalFilename();
	            String uploadDir = "public/mat/";
	            Path uploadPath = Paths.get(uploadDir);
	            
	            if (!Files.exists(uploadPath)) {
	                Files.createDirectories(uploadPath);
	            }
	            
	            try (InputStream inputStream = uploadfile.getInputStream()) {
	                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
	            }
	            
	            Material m = new Material();
	            m.setProgram(dto.getProgram());
	            m.setBranch(dto.getBranch());
	            m.setYear(dto.getYear());
	            m.setMaterialtype(dto.getMaterialtype());
	            m.setSubject(dto.getSubject());
	            m.setTopic(dto.getTopic());
	            
	            Date dt = new Date();
	            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	            String posteddate = df.format(dt);
	            m.setPosteddate(posteddate);
	            
	            // Set the uploaded file name or path to the material (this seems more appropriate)
	            m.setFilename(storageFileName);
	            
	            // Save the material
	            mrepo.save(m);
	            redirectAttributes.addFlashAttribute("msg", "Material is Uploaded");
	            
	            return "redirect:/admin/addstudymaterial";    
	        } else {
	            return "redirect:/admin/adminhome";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();  // Consider logging the error as well for troubleshooting
	        return "redirect:/adminlogin";
	    }
	}

	@GetMapping("/managestudymaterial")
	public String Showmangestudymaterial(HttpSession session , HttpServletResponse response,Model model)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store,must-revalidate");
				if (session.getAttribute("adminid")!=null)
			{
					List<Material> mat=mrepo.findAll();
					model.addAttribute("mat", mat);
			       return "/admin/managestudymaterial";	
			}
			else {
				return "/admin/adminhome";
				
			}
			} catch (Exception e) {																																																																																															
			return "redirect:/adminlogin";
		}
	
	}
	@GetMapping("/delmat")
	public String delMaterial(HttpSession session , HttpServletResponse response,@RequestParam int id)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store,must-revalidate");
				if (session.getAttribute("adminid")!=null)
			{
			Material m=mrepo.findById(id).get();
			Path Filepath=Paths.get("public/mat/"+m.getFilename());
			try {
				Files.delete(Filepath);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			mrepo.delete(m);
			return "redirect:/admin/managestudymaterial";	
			}
			else {
				return "/admin/adminhome";
				
			}
			} catch (Exception e) {																																																																																															
			return "redirect:/adminlogin";
		}
	
	}
	
}
