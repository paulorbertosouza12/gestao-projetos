package br.com.paulobarros.repository;

import br.com.paulobarros.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {


}
