package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Professor;
import com.example.algamoney.api.repository.ProfessorRepository;

@Service
public class ProfessorService {

	@Autowired
	ProfessorRepository professorRepository;

	public Professor atualizar(Long codigo, Professor professor) {
		Professor professorSalvo = buscarPeloCodigo(codigo);

		BeanUtils.copyProperties(professor, professorSalvo, "codigo");
		return professorRepository.save(professor);

	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Professor professorSalvo = buscarPeloCodigo(codigo);
		professorSalvo.setAtivo(ativo);
		professorRepository.save(professorSalvo);
	}

	private Professor buscarPeloCodigo(Long codigo) {
		Professor professorSalvo = professorRepository.findOne(codigo);
		if(professorSalvo == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return professorSalvo;
	}

}
