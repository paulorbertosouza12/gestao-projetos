package br.com.paulobarros.repository;

import br.com.paulobarros.model.Membro;
import br.com.paulobarros.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembroRepository extends JpaRepository<Membro, Long> {

}
