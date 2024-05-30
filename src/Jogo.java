import java.io.*;
import java.net.*;

public class Jogo implements Runnable {
    private Socket jogador1Socket;
    private Socket jogador2Socket;
    private PrintWriter jogador1Out;
    private PrintWriter jogador2Out;
    private BufferedReader jogador1In;
    private BufferedReader jogador2In;

    public Jogo(Socket jogador1Socket, Socket jogador2Socket) {
        this.jogador1Socket = jogador1Socket;
        this.jogador2Socket = jogador2Socket;
    }

    @Override
    public void run() {
        try {
            jogador1In = new BufferedReader(new InputStreamReader(jogador1Socket.getInputStream()));
            jogador1Out = new PrintWriter(jogador1Socket.getOutputStream(), true);
            jogador2In = new BufferedReader(new InputStreamReader(jogador2Socket.getInputStream()));
            jogador2Out = new PrintWriter(jogador2Socket.getOutputStream(), true);

            while (true) {
                jogador1Out.println("Escolha: 1 - Pedra, 2 - Papel, 3 - Tesoura");
                jogador2Out.println("Escolha: 1 - Pedra, 2 - Papel, 3 - Tesoura");

                String escolhaJogador1 = jogador1In.readLine();
                String escolhaJogador2 = jogador2In.readLine();

                if (escolhaJogador1 == null || escolhaJogador2 == null) {
                    break;
                }

                int escolha1;
                int escolha2;

                try {
                    escolha1 = Integer.parseInt(escolhaJogador1);
                    escolha2 = Integer.parseInt(escolhaJogador2);
                } catch (NumberFormatException e) {
                    jogador1Out.println("Escolha inválida. Tente novamente.");
                    jogador2Out.println("Escolha inválida. Tente novamente.");
                    continue;
                }

                int resultado = determinarVencedor(escolha1, escolha2);

                if (resultado == 0) {
                    jogador1Out.println("Resultado: Empate!");
                    jogador2Out.println("Resultado: Empate!");
                } else if (resultado == 1) {
                    jogador1Out.println("Resultado: Você ganhou!");
                    jogador2Out.println("Resultado: Você perdeu!");
                } else {
                    jogador1Out.println("Resultado: Você perdeu!");
                    jogador2Out.println("Resultado: Você ganhou!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                jogador1Socket.close();
                jogador2Socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
