package derby;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name="college")
public class College implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
//	@Column(name="college_id")
    private int collegeId;	
	
	@Column(name="name")
	private String collegeName;
	
	@OneToMany(mappedBy="college", cascade=(CascadeType.ALL))
	private List<Student> students;

}
