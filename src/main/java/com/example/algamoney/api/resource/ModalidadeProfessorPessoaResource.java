package com.example.algamoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.ModalidadeProfessorPessoa;
import com.example.algamoney.api.repository.ModalidadeProfessorPessoaRepository;
import com.example.algamoney.api.service.ModalidadeProfessorPessoaService;

@RestController
@RequestMapping("/modprofaluno")
public class ModalidadeProfessorPessoaResource {

	@Autowired
	private ModalidadeProfessorPessoaRepository modalidadeProfessorPessoaRepository;

	@Autowired
	private ModalidadeProfessorPessoaService modalidadeProfessorPessoaService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<ModalidadeProfessorPessoa> listar(){
		return modalidadeProfessorPessoaRepository.findAll();
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<ModalidadeProfessorPessoa> buscarPeloCodigo(@PathVariable Long codigo) {
		ModalidadeProfessorPessoa modalidadeProfessorPessoa = modalidadeProfessorPessoaRepository.findOne(codigo);
		return modalidadeProfessorPessoa != null ? ResponseEntity.ok(modalidadeProfessorPessoa) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<ModalidadeProfessorPessoa> criar(@Valid @RequestBody ModalidadeProfessorPessoa modalidadeProfessorPessoa,
			HttpServletResponse response) {
		ModalidadeProfessorPessoa modalidadeProfessorPessoaSalva = modalidadeProfessorPessoaService.salvar(modalidadeProfessorPessoa);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, modalidadeProfessorPessoaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(modalidadeProfessorPessoaSalva);
	}

	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		modalidadeProfessorPessoaRepository.delete(codigo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<ModalidadeProfessorPessoa> atualizar(@PathVariable Long codigo, 
			@Valid @RequestBody ModalidadeProfessorPessoa modalidadeProfessorPessoa) {
		try {
			ModalidadeProfessorPessoa modalidadeProfessorPessoaSalva = modalidadeProfessorPessoaService.atualizar(codigo, modalidadeProfessorPessoa);
			return ResponseEntity.ok(modalidadeProfessorPessoaSalva);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
