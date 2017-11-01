package AnnotationTest;

@Table
public class Book {

	@Column(columnName="book_id",typeName="int")
	@Constraint(isPrimaryKey=true)
	private int id;
	
	@Column(columnName="book_name",typeName="varchar(40)")
	private String name;
	
	@Column(columnName="book_author",typeName="varchar(200)")
	private String author;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", author=" + author + "]";
	}
	
	
	
}
