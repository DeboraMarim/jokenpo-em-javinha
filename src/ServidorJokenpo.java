import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.*;

public class ServidorJokenpo {
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static ServerSocket servidorSocket;

    public static void main(String[] args) {
        int porta = obterPorta();

        try {
            servidorSocket = new ServerSocket(porta);
            System.out.println("Servidor Jokenpô iniciado na porta " + porta);

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                executor.execute(new AtendeCliente(clienteSocket, servidorSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int obterPorta() {
        BufferedReader leitor = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.println("Qual porta deseja usar?");
                return Integer.parseInt(leitor.readLine());
            } catch (IOException | NumberFormatException e) {
                System.out.println("Porta inválida. Tente novamente.");
            }
        }
    }
}

class AtendeCliente implements Runnable {
    private Socket clienteSocket;
    private ServerSocket servidorSocket;

    public AtendeCliente(Socket clienteSocket, ServerSocket servidorSocket) {
        this.clienteSocket = clienteSocket;
        this.servidorSocket = servidorSocket;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
             PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true)) {

            saida.println("Escolha o modo de jogo: 1 - Jogador vs CPU, 2 - Jogador vs Jogador");
            String modo = entrada.readLine();

            if ("1".equals(modo)) {
                jogadorVsCPU(entrada, saida);
            } else if ("2".equals(modo)) {
                saida.println("Aguardando outro jogador...");
                Socket jogador2Socket = servidorSocket.accept();
                saida.println("Outro jogador conectado. Iniciando o jogo...");
                new Thread(new Jogo(clienteSocket, jogador2Socket)).start();
            } else {
                saida.println("Modo inválido.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void jogadorVsCPU(BufferedReader entrada, PrintWriter saida) throws IOException {
        int vitorias = 0;
        int derrotas = 0;
        int empates = 0;
        Random random = new Random();

        while (true) {
            saida.println("Escolha: 1 - Pedra, 2 - Papel, 3 - Tesoura (ou 'sair' para terminar)");
            String escolhaJogador = entrada.readLine();

            if ("sair".equalsIgnoreCase(escolhaJogador)) {
                break;
            }

            int escolhaJogadorInt = Integer.parseInt(escolhaJogador);
            int escolhaCPU = random.nextInt(3) + 1;
            String[] opcoes = {"Pedra", "Papel", "Tesoura"};

            saida.println("A CPU escolheu: " + opcoes[escolhaCPU - 1]);

            int resultado = determinarVencedor(escolhaJogadorInt, escolhaCPU);

            if (resultado == 0) {
                empates++;
                saida.println("Resultado: Empate!");
            } else if (resultado == 1) {
                vitorias++;
                saida.println("Resultado: Você ganhou!");
            } else {
                derrotas++;
                saida.println("Resultado: Você perdeu!");
            }
            saida.println("Vitórias: " + vitorias + " | Derrotas: " + derrotas + " | Empates: " + empates);
        }
    }

    private int determinarVencedor(int jogador1, int jogador2) {
        if (jogador1 == jogador2) {
            return 0; // Empate
        } else if ((jogador1 == 1 && jogador2 == 3) || 
                   (jogador1 == 2 && jogador2 == 1) || 
                   (jogador1 == 3 && jogador2 == 2)) {
            return 1; // Jogador 1 vence
        } else {
            return 2; // Jogador 2 vence
        }
    }
}