import java.io.*;
import java.net.*;

public class ClienteJokenpo {
    public static void main(String[] args) {
        String enderecoServidor = obterEnderecoServidor();
        int portaServidor = obterPortaServidor();

        try (Socket socket = new Socket(enderecoServidor, portaServidor);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader leitorConsole = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println(entrada.readLine());
            System.out.print("Escolha o modo de jogo: ");
            String modo = leitorConsole.readLine();
            saida.println(modo);

            if ("1".equals(modo)) {
                jogarContraCPU(entrada, saida, leitorConsole);
            } else if ("2".equals(modo)) {
                jogarContraJogador(entrada, saida, leitorConsole);
            } else {
                System.out.println("Modo inválido.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void jogarContraCPU(BufferedReader entrada, PrintWriter saida, BufferedReader leitorConsole) throws IOException {
        while (true) {
            System.out.println(entrada.readLine());
            String escolha = leitorConsole.readLine();
            if ("sair".equalsIgnoreCase(escolha)) {
                break;
            }
            saida.println(escolha);

            System.out.println(entrada.readLine());
            System.out.println(entrada.readLine());
            System.out.println(entrada.readLine());
        }
    }

    private static void jogarContraJogador(BufferedReader entrada, PrintWriter saida, BufferedReader leitorConsole) throws IOException {
        while (true) {
            System.out.println(entrada.readLine());
            String escolha = leitorConsole.readLine();
            saida.println(escolha);

            String resultado = entrada.readLine();
            if (resultado == null) {
                break;
            }
            System.out.println(resultado);
        }
    }

    private static String obterEnderecoServidor() {
        BufferedReader leitor = new BufferedReader(new InputStreamReader(System.in));
        String endereco = "";
        boolean enderecoValido = false;

        while (!enderecoValido) {
            try {
                System.out.println("Qual é o endereço IP do servidor?");
                endereco = leitor.readLine();
                enderecoValido = validarEnderecoIP(endereco);
                if (!enderecoValido) {
                    System.out.println("Endereço IP inválido. Tente novamente.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return endereco;
    }

    private static boolean validarEnderecoIP(String endereco) {
        try {
            if (endereco.equalsIgnoreCase("localhost")) {
                return true;
            }
            InetAddress.getByName(endereco);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private static int obterPortaServidor() {
        BufferedReader leitor = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.println("Qual é a porta do servidor?");
                return Integer.parseInt(leitor.readLine());
            } catch (IOException | NumberFormatException e) {
                System.out.println("Porta inválida. Tente novamente.");
            }
        }
    }
}