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
import com.example.algamoney.api.model.Despesa;
import com.example.algamoney.api.repository.DespesaRepository;
import com.example.algamoney.api.service.DespesaService;

@RestController
@RequestMapping("/despesas")
public class DespesaResource {

	@Autowired
	private DespesaRepository despesaRepository;

	@Autowired
	private DespesaService despesaService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<Despesa> pesquisar() {
		return despesaRepository.findAll();
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Despesa> buscarPeloCodigo(@PathVariable Long codigo) {
		Despesa despesa = despesaRepository.findOne(codigo);
		return despesa != null ? ResponseEntity.ok(despesa) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Despesa> criar(@Valid @RequestBody Despesa despesa, HttpServletResponse response) {
		Despesa despesaSalva = despesaService.salvar(despesa);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, despesaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(despesaSalva);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		despesaRepository.delete(codigo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<Despesa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Despesa despesa) {
		try {
			Despesa despesaSalva = despesaService.atualizar(codigo, despesa);
			return ResponseEntity.ok(despesaSalva);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
