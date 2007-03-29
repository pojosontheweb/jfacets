package woko.examples.library;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import static com.mongus.beans.validation.BeanValidator.validate;

/**
 * Author of books and articles
 */
@Searchable
@Entity
public class Author {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@SearchableId
	private Long id;
	
	@SearchableProperty
	@NotNull
	@Length(max=40)
	private String firstName;
	
	@SearchableProperty
	@NotNull
	@Length(max=40)
	private String lastName;

	@SearchableProperty
	@Length(max=20)
	private String nickName;

	@SearchableProperty
	@Length(min=5, max=40)
	private String email;
	
	@SearchableProperty
	private Date birthDate;
	
	@ManyToMany(mappedBy = "authors")
	private Set<BaseItem> items;

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		validate(firstName);
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		validate(lastName);
		this.lastName = lastName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		validate(nickName);
		this.nickName = nickName;		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		validate(email);
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<BaseItem> getItems() {
		return items;
	}

	public void setItems(Set<BaseItem> items) {
		this.items = items;
	}
	
	/**
	 * Example of read-only property.
	 */
	public Integer getAge() {
		if (getBirthDate()!=null) {
			long time = System.currentTimeMillis();
			long birthTime = getBirthDate().getTime();
			float age = time - birthTime;
			age = (age / (1000 * 60 * 60 * 24 * 365));
			return new Integer(Math.round(age));
		} else
			return null;
	}
	

}
