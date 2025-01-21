package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;

import cat.institutmarianao.sailing.ws.validation.groups.OnUserCreate;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* JPA annotations */
@Entity
/* Mapping JPA Indexes */
@Table(name = "users", indexes = { @Index(name = "role", columnList = "role", unique = false),
		@Index(name = "full_name", columnList = "full_name", unique = false),
		@Index(name = "role_x_full_name", columnList = "role, full_name", unique = false) })
/* JPA Inheritance strategy is single table */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
/*
 * Maps different JPA objects depending on his role attribute (Employee,
 * Technician or Supervisor)
 */
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)

/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User implements Serializable {
	private static final long serialVersionUID = 1L;

	/* Values for role - MUST be constants (can not be enums) */
	public static final String ADMIN = "ADMIN";
	public static final String CLIENT = "CLIENT";

	public enum Role {
		ADMIN, CLIENT
	}

	public static final int MIN_USERNAME = 2;
	public static final int MAX_USERNAME = 25;
	public static final int MIN_PASSWORD = 10;

	/* Validation */
	@NotBlank
	@Size(min = MIN_USERNAME, max = MAX_USERNAME)
	/* JPA */
	@Id
	@Column(unique = true, nullable = false)
	/* Lombok */
	@EqualsAndHashCode.Include
	protected String username;

	/* Validation */
	@NotBlank(groups = OnUserCreate.class)
	@Size(min = MIN_PASSWORD)
	/* JPA */
	@Column(nullable = false)
	protected String password;

	/* Validation */
	@NotNull
	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	@Column(name = "role", insertable = false, updatable = false, nullable = false)
	protected Role role;
}
