package br.com.luciano.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import br.com.luciano.http.Pessoa;
import br.com.luciano.repository.PessoaRepository;
import br.com.luciano.repository.entity.PessoaEntity;

@Path("/service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceController {

	private final PessoaRepository repository = new PessoaRepository();

	@POST
	@Path("/inserir")
	public Response inserir(Pessoa pessoa) {
		PessoaEntity entity = new PessoaEntity();
		try {
			entity.setNome(pessoa.getNome());
			entity.setEmail(pessoa.getEmail());
			repository.salvar(entity);
			return Response.created(UriBuilder
					.fromResource(ServiceController.class)
					.path(String.valueOf(entity.getId())).build()).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path("/{id}")
	public Response alterar(@PathParam("id") Long id, PessoaEntity pessoaEntity) {
		if(pessoaEntity == null)
			return Response.status(Status.BAD_REQUEST).build();
		if(!id.equals(pessoaEntity.getId()))
			return Response.status(Status.CONFLICT).build();
		
		try {
			repository.alterar(pessoaEntity);
			return Response.ok().build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}	

	@GET
	@Path("/pessoa/{id}")
	public Pessoa buscarTodas(@PathParam("id") Long id) {
		PessoaEntity entity = repository.buscarPorId(id);
		if (entity != null)
			return new Pessoa(entity.getId(), entity.getNome(), entity.getEmail());
		return null;
	}

	@DELETE
	@Path("/excluir/{id}")
	public Response excluir(@PathParam("id") Long id) {	
		PessoaEntity pessoaEntity = this.repository.buscarPorId(id);
		if(pessoaEntity == null) {				
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			repository.excluir(pessoaEntity);
		} catch(Exception e) {
			return Response.status(Status.EXPECTATION_FAILED).build();
		}
		return Response.noContent().build();				
	}

	@GET
	@Path("/todas")
	public List<Pessoa> todas() {
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		List<PessoaEntity> listaEntityPessoas = repository.buscarTodos();
		
		for (PessoaEntity entity : listaEntityPessoas) {
			pessoas.add(new Pessoa(entity.getId(), entity.getNome(), entity.getEmail()));
		}
		
		return pessoas;
	}
}
