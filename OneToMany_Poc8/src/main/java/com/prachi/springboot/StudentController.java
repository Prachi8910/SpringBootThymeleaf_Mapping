package com.prachi.springboot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@Controller
//@RestController
@RequestMapping("/")
/**To run junit test case remove comment from RestController and to run MainApp remove comment from Controller
 * At a time only one annotation will work **/

public class StudentController {
	
	@Autowired
    private StudentRepository studentRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private StudentService service;
	
	private static final String PATH_1="listStudents";
	
	private static final String PATH_2="redirect:/";
	
	@GetMapping({"/","/students"})
    public String getAll(Model model) {
		List<Student> list = studentRepository.findAll(); 
		  model.addAttribute("students",list); 
		  return PATH_1;
    }
	
	
	
	@RequestMapping(value="add")
	public String addStudent(Model model) {
		model.addAttribute("Student",new Student());
		return "add-edit-Student";
	}

	 @GetMapping(path = {"/edit", "/add/{id}"})
		public String addProjectById(Model model, @PathVariable("id") Optional<Long> id,Project project) 
								throws RecordNotFoundException 
		{
			if (id.isPresent()) {
				Student entity = service.getStudentById(id.get());
				model.addAttribute("User", entity);
			} else {
				model.addAttribute("User", new Project());
			}
			return "add-Projects";
		}
	 @PostMapping(value="createStudent")
	    public String saveStudent(Student student, @RequestParam("image") MultipartFile multipartFile ,HttpServletRequest request,Project project,Optional<Long> id) throws  IOException {
		 String mf = multipartFile.getOriginalFilename();
		 if(mf == null) {
			  return null;
		  }
		 else {
		 
		 String fileName = StringUtils.cleanPath(mf);
		  student.setPhoto(fileName);
	    	String[] projectNames=request.getParameterValues("projectName");
	    	
	    	
	    	for(int i=0;i<projectNames.length;i++) {
	    		student.addProject(projectNames[i]);
	    	}
	    	 Student savedStudent = studentRepository.save(student);
	    	 String uploadDir = "student-photos/" + savedStudent.getId();
		        
		 
		        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	    	
	    	
	    	return PATH_2;
	    }
	 }
	
	 @PostMapping("/students/{id}/projects")
	 public String saveProject(@PathVariable Long id,@RequestParam String projectName) {
		 Student student= studentRepository.getOne(id);
		 Project p=new Project(projectName,student);
		 projectRepository.save(p);
		 return PATH_2;
		 
	 }
	 
	 @RequestMapping(path = "/delete/{id}")
		public String deleteUserById(Model model, @PathVariable("id") Long id) 
								throws RecordNotFoundException 
		{
			service.deleteUserById(id);
			return PATH_2;
		}
	
	 
	 @GetMapping("/student")
	 public String getStudentById(Model model,@RequestParam Long id) {
		 Student student=studentRepository.getOne(id);
		model.addAttribute("students", student);
		return PATH_1;
		 }
	 
	 @GetMapping("/testStudent")    
		public List<Student> getUsers() {
			return service.getUsers();
		}
	  @RequestMapping("/testStudent/{id}")
		public Optional<Student> getUser(@PathVariable Long id) {
			return service.getUser(id);
		}
	  @DeleteMapping(value = "/testStudent/{id}")
		public String deleteUser(@PathVariable Long id) {
			service.deleteUser(id);
			return "User deleted with id:"+id;
		}
	  
	  @PostMapping("/studentsTest")
		public ResponseEntity<Student> addUser(@RequestBody Student user) {
			service.addUser(user);
			return new ResponseEntity<Student>(user, HttpStatus.CREATED);
		}
		
	  @PostMapping("/projectTest")
		public ResponseEntity<Project> addProject(@RequestBody Project user) {
			//service.addProject(user);
		    projectRepository.save(user);
			return new ResponseEntity<Project>(user, HttpStatus.CREATED);
		}
		
	

}

