package br.com.curriculo.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.curriculo.arq.persistence.Persistent;
import lombok.Data;

@Data
@Entity
@Table(name = "Arquivo")
public class Arquivo implements Persistent , Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nome_aquivo", nullable = true)
    private String nome;

    @Lob
    @Basic(fetch = FetchType.LAZY, optional = false)
    @Column(name = "arquivo", nullable = true)
    private byte[] file;

	@Column(name = "content_type", nullable = true)    
    private String contentType;

    @Column(name = "size", nullable = true)
    private Integer size;

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
        return "Arquivo";
    }

    @Override
    public Arquivo clone() throws CloneNotSupportedException {
        return (Arquivo) super.clone();
    }
}
