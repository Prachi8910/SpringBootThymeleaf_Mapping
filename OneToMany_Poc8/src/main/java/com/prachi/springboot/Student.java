package com.prachi.springboot;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student_poc8")
public class Student{
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "fname")
	    private String fname;

	    @Column(name = "lname")
	    private String lname;
	    
	    @Column(name = "emailId")
	    private String emailId;
	    
	    @Column(name = "contact")
	    private long contact;
	    
	    @Column(name = "photo")
	    private String photo;
	    
	    @OneToMany(cascade = CascadeType.ALL,mappedBy = "student", fetch = FetchType.LAZY)
	  //  @JoinColumn(name = "project_id", referencedColumnName = "id")
	    List < Project > projects = new ArrayList < > ();

		public void addProject(String projectName) {
			this.projects.add(new Project(projectName,this));
		}
	    
		 @Transient
		    public String getPhotosImagePath() {
		        if (photo == null || id == null) return null;
		         
		        return "/student-photos/" + id + "/" + photo;
		    }


		


		
		  



	   
	   
	

}
