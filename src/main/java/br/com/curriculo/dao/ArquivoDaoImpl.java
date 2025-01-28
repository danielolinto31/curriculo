package br.com.curriculo.dao;

import org.springframework.stereotype.Repository;

import br.com.curriculo.arq.persistence.hibernate.HibernateTemplateCrudDao;
import br.com.curriculo.domain.Arquivo;


@Repository
public class ArquivoDaoImpl extends HibernateTemplateCrudDao<Arquivo> implements ArquivoDao {

    private static final long serialVersionUID = 1L;

}
