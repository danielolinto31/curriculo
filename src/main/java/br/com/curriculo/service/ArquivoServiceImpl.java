package br.com.curriculo.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.curriculo.arq.service.CrudServiceImpl;
import br.com.curriculo.dao.ArquivoDao;
import br.com.curriculo.domain.Arquivo;


@Service
public class ArquivoServiceImpl extends CrudServiceImpl<Arquivo, ArquivoDao> implements ArquivoService {

    private static final long serialVersionUID = 1L;

    @Inject
    public ArquivoServiceImpl(ArquivoDao dao) {
        super(dao);
    }

}
