package com.prachi.springboot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
	
	@Autowired
	private StudentRepository repository;
	
	@Autowired
	private ProjectRepository prepository;
	
	public Student getStudentById(Long id) throws RecordNotFoundException 
	{
		Optional<Student> Student = repository.findById(id);
		
		if(Student.isPresent()) {
			return Student.get();
		} else {
			throw new RecordNotFoundException("No Student record exist for given id");
		}
	}
	
	public Student createOrUpdateUser(Student entity) 
	{
		if(entity.getId()  == 0) 
		{
			entity = repository.save(entity);
			
			return entity;
		} 
		else 
		{
			Optional<Student> User = repository.findById(entity.getId());
			
			if(User.isPresent()) 
			{
				Student newEntity = User.get();
				
				newEntity.setProjects(entity.getProjects());
				newEntity = repository.save(newEntity);
				
				return newEntity;
			} else {
				entity = repository.save(entity);
				
				return entity;
			}
		}
	} 
	
	public void deleteUserById(Long id) throws RecordNotFoundException 
	{
		Optional<Student> User = repository.findById(id);
		
		if(User.isPresent()) 
		{
			repository.deleteById(id);
		} else {
			throw new RecordNotFoundException("No User record exist for given id");
		}
	} 
	
	public List<Student> getUsers() {
		List<Student> users = new ArrayList<Student>();
		repository.findAll().forEach(users::add);
		return users;
	}
	
	 public Optional<Student> getUser(Long id) { return repository.findById(id); }
	 
	 public String deleteUser(Long id) {
			repository.deleteById(id);
			return "User deleted with id:"+id;
	 }
	 public Long addUser(Student user) {
		 repository.save(user);
		 return user.getId();
	}
	
	
	  public Long addProject(Project user) { prepository.save(user); return
	  user.getId(); }
	
	
	
	}


