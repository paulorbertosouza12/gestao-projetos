package br.com.paulobarros.repository;

import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.model.enums.StatusProjetoEnum;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long>, JpaSpecificationExecutor<Projeto> {

    @Query("""
        select count(p)
        from Projeto p
        join p.membros membros
        where membros.id = :membroId
        and p.status not in :statusIgnorados
    """)
    long countProjetosAtivosByMembroId(
            Long membroId,
            Collection<StatusProjetoEnum> statusIgnorados
    );

    default Specification<Projeto> filtro(ProjetoDTO filtro) {
        var filter = (filtro == null ? new ProjetoDTO() : filtro);

        Specification<Projeto> spec = (root, query, cb) -> cb.conjunction();

        if (filter.getNome() != null && !filter.getNome().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nome")), "%" + filter.getNome().toLowerCase() + "%"));
        }

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getInicio() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("inicio"), filter.getInicio()));
        }

        if (filter.getDataPrevisaoTermino() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("dataPrevisaoTermino"), filter.getDataPrevisaoTermino()));
        }

        if (filter.getIdGerenteResponsavel() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("idGerenteResponsavel"), filter.getIdGerenteResponsavel()));
        }

        return spec;
    }
}
