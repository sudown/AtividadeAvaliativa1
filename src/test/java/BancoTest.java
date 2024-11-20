import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BancoTest {

    private Banco banco;

    @BeforeEach
    void setup() {
        banco = new Banco("Banco Central");
    }

    @Test
    void testCriarContaSucesso() {
        Conta conta = new Conta(1, ContaTipo.CORRENTE);
        banco.criarConta("Netinho", 123456789, conta);

        Cliente cliente = banco.procurarClientePorCPF(123456789);
        assertNotNull(cliente, "O cliente deve ser encontrado.");
        assertEquals("Netinho", cliente.getNome(), "O nome do cliente deve corresponder.");
        assertEquals(ContaTipo.CORRENTE, cliente.getConta().getTipoConta(), "O tipo da conta deve ser CORRENTE.");
    }

    @Test
    void testCriarContaJaExistente() {
        Conta conta = new Conta(1, ContaTipo.CORRENTE);
        banco.criarConta("Bosquinho", 123456789, conta);

        Conta novaConta = new Conta(1, ContaTipo.CORRENTE);
        banco.criarConta("Bosquinho", 123456789, novaConta);

        Cliente cliente = banco.procurarClientePorCPF(123456789);
        assertNotNull(cliente, "Cliente deveria existir.");
        assertEquals(conta.getNumeroConta(), cliente.getConta().getNumeroConta(),
                "A conta existente deve permanecer inalterada.");
    }

    @Test
    void testCreditarValor() {
        Conta conta = new Conta(1, ContaTipo.CORRENTE);
        banco.criarConta("Deivinho", 123456789, conta);

        banco.creditarValor(123456789, 500.0, ContaTipo.CORRENTE);
        assertEquals(500.0, banco.consultarSaldo(123456789, ContaTipo.CORRENTE), "O saldo deve ser 500.");
    }

    @Test
    void testDebitarValor() {
        Conta conta = new Conta(1, ContaTipo.CORRENTE);
        banco.criarConta("Shalashaska", 123456789, conta);

        banco.creditarValor(123456789, 500.0, ContaTipo.CORRENTE);
        banco.debitarValor(123456789, 200.0, ContaTipo.CORRENTE);

        assertEquals(300.0, banco.consultarSaldo(123456789, ContaTipo.CORRENTE), "O saldo deve ser 300.");
    }

    @Test
    void testTransferirValor() {
        Conta conta1 = new Conta(1, ContaTipo.CORRENTE);
        Conta conta2 = new Conta(1, ContaTipo.POUPANCA);

        banco.criarConta("Naked Snake", 123456789, conta1);
        banco.criarConta("The Boss", 987654321, conta2);

        banco.creditarValor(123456789, 500.0, ContaTipo.CORRENTE);
        banco.transferirValor(123456789, ContaTipo.CORRENTE, 200, 987654321, ContaTipo.POUPANCA);

        assertEquals(300.0, banco.consultarSaldo(123456789, ContaTipo.CORRENTE), "O saldo de João deve ser 300.");
        assertEquals(200.0, banco.consultarSaldo(987654321, ContaTipo.POUPANCA), "O saldo de Maria deve ser 200.");
    }

    @Test
    void testTransferirValorSaldoInsuficiente() {
        Conta contaOrigem = new Conta(1, ContaTipo.CORRENTE);
        Conta contaDestino = new Conta(1, ContaTipo.POUPANCA);

        banco.criarConta("Naked Snake", 123456789, contaOrigem);
        banco.criarConta("The Boss", 987654321, contaDestino);

        banco.creditarValor(123456789, 50.0, ContaTipo.CORRENTE);

        banco.transferirValor(123456789, ContaTipo.CORRENTE, 200, 987654321, ContaTipo.POUPANCA);

        assertEquals(50.0, banco.consultarSaldo(123456789, ContaTipo.CORRENTE),
                "O saldo da conta de origem deve permanecer inalterado.");
        assertEquals(0.0, banco.consultarSaldo(987654321, ContaTipo.POUPANCA),
                "O saldo da conta de destino deve permanecer inalterado.");
    }


    @Test
    void testConsultarSaldo() {
        Conta conta = new Conta(1, ContaTipo.CORRENTE);
        banco.criarConta("Link", 123456789, conta);

        banco.creditarValor(123456789, 100.0, ContaTipo.CORRENTE);

        double saldo = banco.consultarSaldo(123456789, ContaTipo.CORRENTE);
        assertEquals(100.0, saldo, "O saldo deve ser 100.");
    }

    @Test
    void testProcurarContasPorCPF() {
        Conta conta = new Conta(1, ContaTipo.CORRENTE);
        banco.criarConta("Zelda", 123456789, conta);

        Cliente cliente = banco.procurarContasPorCPF(123456789, ContaTipo.CORRENTE);
        assertNotNull(cliente, "O cliente deve ser encontrado.");
        assertEquals(conta.getNumeroConta(), cliente.getConta().getNumeroConta(), "O número da conta deve corresponder.");
    }

    @Test
    void testProcurarClientePorCPF() {
        Conta conta = new Conta(1, ContaTipo.CORRENTE);
        banco.criarConta("Arthur Morgan", 123456789, conta);

        Cliente cliente = banco.procurarClientePorCPF(123456789);
        assertNotNull(cliente, "O cliente deve ser encontrado.");
        assertEquals("Arthur Morgan", cliente.getNome(), "O nome do cliente deve corresponder.");
    }
}