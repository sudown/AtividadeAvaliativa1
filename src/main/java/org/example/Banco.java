package org.example;

import java.util.ArrayList;
import java.util.List;

public class Banco {
	private List<Cliente> listClientes = new ArrayList<>();
	private String nomeBanco;
	private int nrBanco;

	public Banco(String nomeBanco){
		this.setNomeBanco(nomeBanco);
	}

	public void criarConta(String nomeCliente, int cpfCliente, Conta conta) {
		Cliente clienteExistente = procurarContasPorCPF(cpfCliente, conta.getTipoConta());

		if (clienteExistente == null) {
			listClientes.add(new Cliente(nomeCliente, cpfCliente, conta));
			System.out.println(
					String.format("Conta %s de N° %d foi criada com sucesso.",
							conta.getTipoConta(),
							conta.getNumeroConta())
			);
		} else {
			Conta contaExistente = clienteExistente.getConta();
			System.out.println(
					String.format("Conta de N° %d já existe para o cliente %s.",
							contaExistente != null ? contaExistente.getNumeroConta() : -1, // Usa -1 como fallback
							clienteExistente.getNome())
			);
		}
	}

	public Cliente procurarContasPorCPF(int cpf, ContaTipo contaTipo){
		for (Cliente cliente : listClientes) {
			if(cliente.getCpf() == cpf && cliente.getConta().getTipoConta().equals(contaTipo)){
				if (cliente.getConta() != null) {
					//return cliente.getConta();
					return cliente;
				}
			}
		}
		return null;
	}

	public void creditarValor(int cpf, double valor, ContaTipo contaTipo){
		Cliente c = procurarContasPorCPF(cpf, contaTipo);		
		if(c!=null)
			c.getConta().creditar(valor);
		else
			System.out.println("Conta inexistente");
	}

	public void debitarValor(int cpf, double valor, ContaTipo contaTipo){
		Cliente c = procurarContasPorCPF(cpf, contaTipo);
		if(c!=null)
			c.getConta().debitar(valor);
		else
			System.out.println("Conta inexistente");
	}

	public void transferirValor(int cpfOrigem, ContaTipo contaTipoOrigem, double valor, int cpfDestino, ContaTipo contaTipoDestino) {
		Cliente cOrigem = procurarContasPorCPF(cpfOrigem, contaTipoOrigem);
		Cliente cDestino = procurarContasPorCPF(cpfDestino, contaTipoDestino);

		if (cOrigem == null || cDestino == null) {
			System.out.println("Conta de origem ou destino inexistente");
			return;
		}

		if (cOrigem.getConta().getSaldoConta() < valor) {
			System.out.println("Saldo insuficiente para realizar a transferência.");
			return;
		}

		cOrigem.getConta().debitar(valor);
		cDestino.getConta().creditar(valor);

		System.out.println(String.format("Transferência de R$ %.2f realizada com sucesso.", valor));
	}


	public void listarContasClientes() {
		for (Cliente c : listClientes) {
			System.out.println("#############################");
			System.out.println("# CPF: " + c.getCpf());
			System.out.println("# Nome: " + c.getNome()); 
			System.out.println("# Ag: " + c.getConta().getAgencia().getIdAgencia());
			System.out.println("# CC: " + c.getConta().getTipoConta()); 
			System.out.println("# Saldo: " + c.getConta().getSaldoConta()); 
			System.out.println("#############################"); 
		}
	}
	
	public Cliente procurarClientePorCPF(int cpf){
		for (Cliente cliente : listClientes) {
			if(cliente.getCpf() == cpf){
				if (cliente.getConta() != null) {
					return cliente;
				} else System.out.println("Cliente não localizado!");
			}
		}
		return null;
	}

	public double consultarSaldo(int cpf, ContaTipo contaTipo){
		return procurarContasPorCPF(cpf, contaTipo).getConta().getSaldoConta(); 
	}

	public int getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(int nrBanco) {
		this.nrBanco = nrBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}
}