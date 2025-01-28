package br.com.curriculo.dao;

import org.springframework.stereotype.Repository;

import br.com.curriculo.arq.persistence.hibernate.HibernateTemplateCrudDao;
import br.com.curriculo.domain.Curriculo;

@Repository
public class CurriculoDaoImpl extends HibernateTemplateCrudDao<Curriculo> implements CurriculoDao {

	private static final long serialVersionUID = 1L;
}