package br.com.curriculo.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.curriculo.arq.service.CrudServiceImpl;
import br.com.curriculo.dao.CurriculoDao;
import br.com.curriculo.domain.Curriculo;

@Service
public class CurriculoServiceImpl extends CrudServiceImpl<Curriculo, CurriculoDao> implements CurriculoService {

	private static final long serialVersionUID = 1L;

	@Inject
	public CurriculoServiceImpl(CurriculoDao dao) {
		super(dao);
	}

//	@Override
//	public void validate(Curriculo entity, boolean isInsert, boolean isUpdate, boolean isDelete)
//			throws ServiceBusinessException {
//		if ((isInsert) || (isUpdate)) {
//			try {
//				Utils.validateUniqueness(entity, this, "descricao", "sigla");
//			} catch (ServiceBusinessException e) {
//				log.debug("ERRO", e);
//				throw new ServiceBusinessException(
//						"A operação não pode ser realizada por violar a integridade de dados");
//			}
//
//		}
//		super.validate(entity, isInsert, isUpdate, isDelete);
//	}

}
