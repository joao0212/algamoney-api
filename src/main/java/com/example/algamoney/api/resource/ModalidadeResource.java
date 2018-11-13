package com.example.algamoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.model.Modalidade;
import com.example.algamoney.api.repository.ModalidadeRepository;

@RestController
@RequestMapping("/modalidades")
public class ModalidadeResource {

	@Autowired
	private ModalidadeRepository modalidadeRepository;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Modalidade> listar() {
		return modalidadeRepository.findAll();
	}
}
