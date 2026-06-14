package br.com.avcar.service;

import br.com.avcar.model.BaseModel;
import br.com.avcar.repository.Repository;
import java.util.List;

/**
 * BaseService genérico: reutiliza o CRUD completo via Repository<T,ID>.
 * O hook validar() é onde cada domínio pluga sua estratégia de validação
 * (Strategy): no-op por padrão, sobrescrito nas subclasses que precisam.
 */
public abstract class BaseService<T extends BaseModel<ID>, ID> {

    protected final Repository<T, ID> repository;

    protected BaseService(Repository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> listar() {
        return repository.findAll();
    }

    public T buscarPorId(ID id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro não encontrado: id=" + id));
    }

    public T criar(T e) {
        validar(e);
        return repository.insert(e);
    }

    public void atualizar(T e) {
        validar(e);
        repository.update(e);
    }

    public void remover(ID id) {
        repository.deleteById(id);
    }

    protected void validar(T e) { }
}
