package br.com.curriculo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import br.com.curriculo.arq.persistence.Persistent;
import br.com.curriculo.arq.persistence.UniqueAttributes;
import lombok.Data;

@Data
@Entity
@Table(name = "Curriculo")
@UniqueAttributes("email")
public class Curriculo implements Persistent {

    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Integer id;

	@NotNull(message = "O campo \"nome\" é obrigatório")
	@Length(min = 2, max = 100, message = "O campo \"nome\" deve conter entre {min} e {max} caracteres.")
	@Column(name = "nome", nullable = false)
	private String nome;

	@NotNull(message = "O campo \"e-mail\" é obrigatório")
	@Email
	@Column(name = "email", nullable = false)
	private String email;

	@NotNull(message = "O campo \"telefone\" é obrigatório")
	@Column(name = "telefone", nullable = false)
	private String telefone;

	@NotNull(message = "O campo \"cargo desejado\" é obrigatório")
	@Length(min = 2, max = 100, message = "O campo \"cargo desejado\" deve conter entre {min} e {max} caracteres.")
	@Column(name = "cargo_desejado", nullable = false)
	private String cargoDesejado;

	@NotNull(message = "O campo \"escolaridade\" é obrigatório")
	@Enumerated(value = EnumType.STRING)
	@Column(name = "escolaridade", nullable = false)
	private Escolaridade escolaridade;

	@Column(name = "observacoes")
	private String observacoes;

	@Column(name = "ip", nullable = true)
	private String ip;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_envio", nullable = true)
	private Date dataEnvio;

	@ManyToOne
	@NotNull(message = "O campo \"arquivo\" é obrigatório")
    @JoinColumn(name = "arquivo_id", nullable = false)
    private Arquivo arquivo;

	@Override
    public Integer getId() {
        return id;
    }

	@Override
    @Transient
    public String getLabel() {
        return null;
    }

    @Override
    @Transient
    public String getEntityLabel() {
        return "Curriculo";
    }
}
