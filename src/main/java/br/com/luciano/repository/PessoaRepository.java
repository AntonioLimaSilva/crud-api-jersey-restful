package br.com.luciano.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import br.com.luciano.repository.entity.PessoaEntity;

public class PessoaRepository {

	private final EntityManagerFactory factory;
	
	private final EntityManager manager;
	
	public PessoaRepository(){
		this.factory = Persistence.createEntityManagerFactory("crudPU");	
		this.manager = this.factory.createEntityManager();
	}
	
	public void salvar(PessoaEntity pessoaEntity){		
		this.manager.getTransaction().begin();
		this.manager.persist(pessoaEntity);
		this.manager.getTransaction().commit();
	}
	
	public void alterar(PessoaEntity pessoaEntity){		
		this.manager.getTransaction().begin();
		this.manager.merge(pessoaEntity);
		this.manager.getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<PessoaEntity> buscarTodos(){		
		return this.manager.createQuery("SELECT p FROM PessoaEntity p ORDER BY p.nome").getResultList();
	}
	
	public PessoaEntity buscarPorId(Long id){		
		return this.manager.find(PessoaEntity.class, id);
	}
	
	public void excluir(PessoaEntity pessoaEntity){		
		try {
			this.manager.getTransaction().begin();
			this.manager.remove(pessoaEntity);
			this.manager.flush();
			this.manager.getTransaction().commit();
		} catch(PersistenceException e) {
			e.printStackTrace();
		}
		
	}
}
