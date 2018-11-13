package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Despesa;
import com.example.algamoney.api.repository.DespesaRepository;

@Service
public class DespesaService {

	@Autowired
	private DespesaRepository despesaRepository;

	public Despesa salvar(Despesa despesa) {
		return despesaRepository.save(despesa);
	}

	public Despesa atualizar(Long codigo, Despesa despesa) {
		Despesa despesaSalva = buscarDespesaExistente(codigo);
		BeanUtils.copyProperties(despesa, despesaSalva, "codigo");
		return despesaRepository.save(despesaSalva);
	}

	private Despesa buscarDespesaExistente(Long codigo) {
		Despesa despesaSalva = despesaRepository.findOne(codigo);
		if(despesaSalva == null) {
			throw new IllegalArgumentException();
		}
		return despesaSalva;
	}

}
