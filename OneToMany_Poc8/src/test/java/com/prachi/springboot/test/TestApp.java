package com.prachi.springboot.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prachi.springboot.*;


@WebMvcTest(value = StudentController.class)
class TestApp {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StudentRepository repo;
	
	@MockBean
	private ProjectRepository proRepo;
	
	@MockBean
	private StudentService service;
	
	
	ObjectMapper om = new ObjectMapper();
	
	public static List<Student> setUp() throws IOException  {
		Student user = new Student();
		user.setId(1L);
		user.setFname("steve");
		user.setLname("huj");
		user.setContact(8909990000L);
		user.setEmailId("st@gmail.com");
		user.setPhoto("stud1.png");
		user.addProject("ML");
		
		String uploadDir = "student-photos/" + user.getId();
		MultipartFile multipartFile = new MockMultipartFile("sourceFile.tmp", "Hello World".getBytes());
			FileUploadUtil.saveFile(uploadDir, "stud1.png",multipartFile);
		
		
		Student user1 = new Student();
		user1.setId(2L);
		user1.setFname("Mathew");
		user1.setLname("uhhu");
		user1.setContact(8909990000L);
		user1.setEmailId("mw@gmail.com");
		user1.setPhoto("stud2.png");
		user1.addProject("AI");
		
		List<Student> listUser = new ArrayList<Student>();
		listUser.add(user);
		listUser.add(user1);
		
		
		user1.addProject("Security");
		
		return listUser;
	}
	
	public static List<Project> setUp1() {
		Student user = new Student();
		user.setId(3L);
		user.setFname("steve");
		user.setLname("huj");
		user.setContact(8909990000L);
		user.setEmailId("st@gmail.com");
		user.setPhoto("stud1.png");
		user.addProject("Mobile");
		
		
		
		Project pro = new Project();
		pro.setId(1L);
		pro.setProjectName("Java");
		pro.setStudent(user);
		
		List<Project> listUser = new ArrayList<Project>();
		listUser.add(pro);
		Student user1 = new Student(4L,"John","huio","John@gmail.com",8899008899L,"stud3.png",listUser);
		return listUser;
	}
	

	
	@Test
    void getAllStudentsTest() throws Exception {         //get all user test method
		List<Student> lst = TestApp.setUp();
		String response = om.writeValueAsString(lst);
		Mockito.when(service.getUsers()).thenReturn(lst);
		MvcResult result = mockMvc.perform(get("/testStudent")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String userRes = result.getResponse().getContentAsString();
		assertEquals(response, userRes);
		
				
	}
	
	@Test
	void getStudentbyIdTest() throws Exception {
		String response=om.writeValueAsString(TestApp.setUp().get(0));
		Mockito.when(service.getUser(1L)).thenReturn(java.util.Optional.of(TestApp.setUp().get(0)));
		MvcResult result = mockMvc.perform(get("/testStudent/1").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		int response1 = result.getResponse().getStatus();
		assertEquals(200, response1);
		
	}
	
	@Test
	void deleteStudentTest() throws Exception {
		
		MvcResult result = mockMvc.perform(delete("/testStudent/1").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		int response = result.getResponse().getStatus();
		assertEquals(200, response);
	}
	
	@Test
	void addStudentTest() throws Exception {               //add user test method
		Mockito.when(service.addUser(TestApp.setUp().get(0))).thenReturn(1L);   // return particular value when particular method is called
		String payload = om.writeValueAsString(TestApp.setUp().get(0));        // Method that can be used to serialize any Java value asa String
		MvcResult result = mockMvc
				.perform(post("/studentsTest").content(payload).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated()).andReturn();
		int status = result.getResponse().getStatus();
		assertEquals(201, status);
		
	}
	
	@Test
	void addProjectTest() throws Exception {               //add user test method
		Mockito.when(service.addProject(TestApp.setUp1().get(0))).thenReturn(3L);   // return particular value when particular method is called
		String payload = om.writeValueAsString(TestApp.setUp1().get(0));        // Method that can be used to serialize any Java value asa String
		MvcResult result = mockMvc
				.perform(post("/projectTest").content(payload).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated()).andReturn();
		int status = result.getResponse().getStatus();
		assertEquals(201, status);
		
	}
	@Test
    void testEquals_Symmetric() {
	    Student st1 = new Student();
	    st1.setId(5L);
	    st1.setFname("Ghhjj");
	    st1.setLname("ghjj");
	    st1.setEmailId("gyjki@gmail.com");
	    st1.setContact(8888888888L);
	    st1.setPhoto("stu1.png");
	    Student st2 = new Student();
	    st2.setId(5L);
	    st2.setFname("Ghhjj");
	    st2.setLname("ghjj");
	    st2.setEmailId("gyjki@gmail.com");
	    st2.setContact(8888888888L);
	    st2.setPhoto("stu1.png");
	    Assert.assertTrue(st1.equals(st2) && st2.equals(st1));
	    Assert.assertTrue(st1.hashCode() == st2.hashCode());
	}
	
	@Test
    void testEquals1_Symmetric() {
	    Student st1 = new Student();
	    st1.setId(5L);
	    st1.setFname("Ghhjj");
	    st1.setLname("ghjj");
	    st1.setEmailId("gyjki@gmail.com");
	    st1.setContact(8888888888L);
	    st1.setPhoto("stu1.png");
	    Student st2 = new Student();
	    st2.setId(5L);
	    st2.setFname("Ghhjj");
	    st2.setLname("ghjj");
	    st2.setEmailId("gyjki@gmail.com");
	    st2.setContact(8888888888L);
	    st2.setPhoto("stu1.png");
	    Project p=new Project();
	    p.setId(1L);
		p.setProjectName("Java");
		p.setStudent(st1);
		Project p1=new Project();
	    p1.setId(1L);
		p1.setProjectName("Java");
		p1.setStudent(st2);
		List<Project> listUser = new ArrayList<Project>();
		listUser.add(p);
		listUser.add(p1);
		
	    
	    Assert.assertTrue(p.equals(p1) && p1.equals(p));
	    Assert.assertTrue(p.hashCode() == p1.hashCode());
	}
	
	  @Test
	      void testEqual2() {
		  Student st1 = new Student();
		    st1.setId(5L);
		    st1.setFname("Ghhjj");
		    st1.setLname("ghjj");
		    st1.setEmailId("gyjki@gmail.com");
		    st1.setContact(8888888888L);
		    st1.setPhoto("stu1.png");
		    Student st2 = new Student();
		    st2.setId(5L);
		    st2.setFname("Ghhjj");
		    st2.setLname("ghjj");
		    st2.setEmailId("gyjki@gmail.com");
		    st2.setContact(8888888888L);
		    st2.setPhoto("stu1.png");
	        assertEquals(true, st1.equals(st2));
	        
	    }
	
	
	
	
	
	

}
