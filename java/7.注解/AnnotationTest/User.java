package AnnotationTest;


@Table(name="annotation_user")
public class User {

	@Column(typeName="int")
	@Constraint(isPrimaryKey=true)
	private String id;
	
	@Column(typeName="varchar(40)")
	@Constraint(notNull=true)
	private String name;
	
	@Column(typeName="varchar(40)")
	@Constraint(notNull=true)
	private String password;
	
	private int age;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password
				+ ", age=" + age + "]";
	}
	
	
}
