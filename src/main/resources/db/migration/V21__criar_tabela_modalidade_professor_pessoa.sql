CREATE TABLE modalidade_professor_pessoa (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	codigo_pessoa BIGINT(20) NOT NULL,
	codigo_professor BIGINT(20) NOT NULL,
	codigo_modalidade BIGINT(20) NOT NULL,
	FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo),
	FOREIGN KEY (codigo_professor) REFERENCES professor(codigo),
	FOREIGN KEY (codigo_modalidade) REFERENCES modalidade(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;