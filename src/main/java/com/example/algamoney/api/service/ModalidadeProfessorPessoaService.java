package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.ModalidadeProfessorPessoa;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.model.Professor;
import com.example.algamoney.api.repository.ModalidadeProfessorPessoaRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.repository.ProfessorRepository;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class ModalidadeProfessorPessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private ModalidadeProfessorPessoaRepository modalidadeProfessorPessoaRepository;

	public ModalidadeProfessorPessoa salvar(ModalidadeProfessorPessoa modalidadeProfessorPessoa) {
		validarProfessor(modalidadeProfessorPessoa);
		validarPessoa(modalidadeProfessorPessoa);
		return modalidadeProfessorPessoaRepository.save(modalidadeProfessorPessoa);
	}

	public ModalidadeProfessorPessoa atualizar(Long codigo, ModalidadeProfessorPessoa modalidadeProfessorPessoa) {
		ModalidadeProfessorPessoa modalidadeProfessorPessoaSalvo = modalidadeProfessorPessoaRepository.findOne(codigo);
		BeanUtils.copyProperties(modalidadeProfessorPessoa, modalidadeProfessorPessoaSalvo, "codigo");
		return modalidadeProfessorPessoaRepository.save(modalidadeProfessorPessoaSalvo);
	}

	private void validarProfessor(ModalidadeProfessorPessoa modalidadeProfessorPessoa) {
		Professor professor = null;
		if(modalidadeProfessorPessoa.getProfessor().getCodigo() != null) {
			professor = professorRepository.findOne(modalidadeProfessorPessoa.getProfessor().getCodigo());
		}

		if(professor == null || professor.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}

	}

	private void validarPessoa(ModalidadeProfessorPessoa modalidadeProfessorPessoa) {
		Pessoa pessoa = null;
		if(modalidadeProfessorPessoa.getPessoa().getCodigo() != null) {
			pessoa = pessoaRepository.findOne(modalidadeProfessorPessoa.getPessoa().getCodigo());
		}

		if(pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}



}
