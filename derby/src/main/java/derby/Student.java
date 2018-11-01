package derby;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//@RequiredArgsConstructor
//@Getter
//@Setter
@Data
@RequiredArgsConstructor
@Entity
@Table(name="student")
public class Student implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="student_id")
	private int studentId;
	
	@Column(name="student_name")
	private String studentName;
	
//	@NotFound(action=NotFoundAction.IGNORE) 
	@ManyToOne(cascade=CascadeType.PERSIST)//,fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="college_id",referencedColumnName="COLLEGEID")
	private College college;
}
