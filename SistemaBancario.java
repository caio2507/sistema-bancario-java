
import java.util.Scanner;

public class SistemaBancario {

    static int[] numeros = new int[100];
    static String[] titulares = new String[100];
    static double[] saldos = new double[100];

    static String[][] extratos = new String[100][200];
    static int[] qtdTransacoes = new int[100];

    static int totalContas = 0;
    static int proximoNumero = 1001;

    static Scanner sc = new Scanner(System.in);

    static void cadastrarConta() {
        if (totalContas >= 100) {
            System.out.println("Limite de contas atingido!");
            return;
        }

        System.out.print("Nome do titular: ");
        String nome = sc.nextLine();

        System.out.print("Depósito inicial: R$ ");
        double deposito = sc.nextDouble();
        sc.nextLine();

        if (deposito < 0) {
            System.out.println("Valor inválido. Conta não criada.");
            return;
        }

        int i = totalContas;
        numeros[i] = proximoNumero++;
        titulares[i] = nome;
        saldos[i] = deposito;
        qtdTransacoes[i] = 0;

        if (deposito > 0) {
            registrarTransacao(i, "Depósito inicial: R$ " + String.format("%.2f", deposito));
        }

        totalContas++;

        System.out.println("Conta criada com sucesso");
        System.out.println("  Número da conta: " + numeros[i]);
        System.out.println("  Titular: " + titulares[i]);
        System.out.println("  Saldo inicial: R$ " + String.format("%.2f", saldos[i]));
    }

    static void depositar() {
        int idx = buscarConta("depositar");
        if (idx == -1) return;

        System.out.print("Valor do depósito: R$ ");
        double valor = sc.nextDouble();
        sc.nextLine();

        if (valor <= 0) {
            System.out.println("Valor inválido.");
            return;
        }

        saldos[idx] += valor;
        registrarTransacao(idx, "Depósito: +R$ " + String.format("%.2f", valor)
                + " | Saldo: R$ " + String.format("%.2f", saldos[idx]));

        System.out.println(" Depósito realizado! Saldo atual: R$ " + String.format("%.2f", saldos[idx]));
    }

    static void sacar() {
        int idx = buscarConta("sacar");
        if (idx == -1) return;

        System.out.print("Valor do saque: R$ ");
        double valor = sc.nextDouble();
        sc.nextLine();

        if (valor <= 0) {
            System.out.println("Valor inválido.");
            return;
        }

        if (valor > saldos[idx]) {
            System.out.println(" Saldo insuficiente! Saldo disponível: R$ " + String.format("%.2f", saldos[idx]));
            return;
        }

        saldos[idx] -= valor;
        registrarTransacao(idx, "Saque: -R$ " + String.format("%.2f", valor)
                + " | Saldo: R$ " + String.format("%.2f", saldos[idx]));

        System.out.println(" Saque realizado! Saldo atual: R$ " + String.format("%.2f", saldos[idx]));
    }

    static void transferir() {
        System.out.println("--- Conta de ORIGEM ---");
        int origem = buscarConta("transferir");
        if (origem == -1) return;

        System.out.println("--- Conta de DESTINO ---");
        int destino = buscarConta("receber transferência");
        if (destino == -1) return;

        if (origem == destino) {
            System.out.println("Origem e destino não podem ser a mesma conta.");
            return;
        }

        System.out.print("Valor da transferência: R$ ");
        double valor = sc.nextDouble();
        sc.nextLine();

        if (valor <= 0) {
            System.out.println("Valor inválido.");
            return;
        }

        if (valor > saldos[origem]) {
            System.out.println("✘ Saldo insuficiente!");
            return;
        }

        saldos[origem] -= valor;
        saldos[destino] += valor;

        registrarTransacao(origem, "Transferência enviada para conta " + numeros[destino]
                + ": -R$ " + String.format("%.2f", valor)
                + " | Saldo: R$ " + String.format("%.2f", saldos[origem]));

        registrarTransacao(destino, "Transferência recebida da conta " + numeros[origem]
                + ": +R$ " + String.format("%.2f", valor)
                + " | Saldo: R$ " + String.format("%.2f", saldos[destino]));

        System.out.println("✔ Transferência realizada com sucesso!");
    }

    static void verExtrato() {
        int idx = buscarConta("ver extrato");
        if (idx == -1) return;

        System.out.println("\n=========================");
        System.out.println("  EXTRATO - Conta " + numeros[idx] + " | " + titulares[idx]);
        System.out.println("\n=========================");

        if (qtdTransacoes[idx] == 0) {
            System.out.println("  Nenhuma transação registrada.");
        } else {
            for (int i = 0; i < qtdTransacoes[idx]; i++) {
                System.out.println("  " + (i + 1) + ". " + extratos[idx][i]);
            }
        }

        System.out.println("--------------------------------------");
        System.out.println("  Saldo atual: R$ " + String.format("%.2f", saldos[idx]));
        System.out.println("\n=========================");
    }

    static void verSaldo() {
        int idx = buscarConta("consultar saldo");
        if (idx == -1) return;

        System.out.println("Saldo de " + titulares[idx] + " (conta " + numeros[idx] + "): R$ "
                + String.format("%.2f", saldos[idx]));
    }

    static void listarContas() {
        if (totalContas == 0) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }

        System.out.println("\n=========================");
        System.out.println("  CONTAS CADASTRADAS");
        System.out.println("=========================");
        for (int i = 0; i < totalContas; i++) {
            System.out.printf("  Conta: %d | Titular: %-20s | Saldo: R$ %.2f%n",
                    numeros[i], titulares[i], saldos[i]);
        }
        System.out.println("\n=========================");
    }

    static int buscarConta(String operacao) {
        if (totalContas == 0) {
            System.out.println("Nenhuma conta cadastrada!");
            return -1;
        }

        System.out.print("Número da conta para " + operacao + ": ");
        int numero = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < totalContas; i++) {
            if (numeros[i] == numero) return i;
        }

        System.out.println("✘ Conta " + numero + " não encontrada.");
        return -1;
    }

    static void registrarTransacao(int idx, String descricao) {
        if (qtdTransacoes[idx] < 200) {
            extratos[idx][qtdTransacoes[idx]] = descricao;
            qtdTransacoes[idx]++;
        }
    }

    public static void main(String[] args) {
        int opcao;

        do {
            System.out.println("  === SISTEMA BANCÁRIO ===  ");
            System.out.println("  1. Cadastrar conta          ");
            System.out.println("  2. Depositar                ");
            System.out.println("  3. Sacar                    ");
            System.out.println("  4. Transferir               ");
            System.out.println("  5. Ver extrato              ");
            System.out.println("  6. Ver saldo                ");
            System.out.println("  7. Listar contas            ");
            System.out.println("  0. Sair                     ");

            System.out.print("Opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1: cadastrarConta(); break;
                case 2: depositar(); break;
                case 3: sacar(); break;
                case 4: transferir(); break;
                case 5: verExtrato(); break;
                case 6: verSaldo(); break;
                case 7: listarContas(); break;
                case 0: System.out.println("Encerrando sistema. Até logo!"); break;
                default: System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }
}