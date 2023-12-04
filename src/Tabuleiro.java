import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class Tabuleiro {
    public Utilitarios util = new Utilitarios();
    public int[][] posicoes = new int[5][5];
    public Stack<Integer> baralho = new Stack<>();
    public int numJogadores;
    public Jogador[] jogadores;
    public int maoRodada[];
    public int maoOrdenada[];

    public void preencherBaralho() {
        for (int i = 1; i <= 109; i++) {
            baralho.push(i);
        }
    }

    public void iniciarTabuleiro() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o número de jogadores (3-6): ");
        numJogadores = scanner.nextInt();
        while (numJogadores > 6 || numJogadores < 3) {
            System.out.print("Quantidade de jogadores inválida. Digite o número de jogadores (3-6): ");
            numJogadores = scanner.nextInt();
        }
        jogadores = new Jogador[numJogadores];
        preencherBaralho();
        Collections.shuffle(baralho);

        for (int i = 0; i < 5; i++) {
            posicoes[i][0] = baralho.pop();
        }

        int cartasPorJogador = 12;
        for (int j = 0; j < numJogadores; j++) {
            jogadores[j] = new Jogador(); // Adicione esta linha para criar uma instância de Jogador.

            for (int i = 0; i < cartasPorJogador; i++) {
                int carta = baralho.pop();
                jogadores[j].cartas[i] = carta;
            }
        }
    }

    public void exibirTabuleiro() {
        System.out.println("Tabuleiro:");

        for (int i = 0; i < posicoes.length; i++) {
            for (int j = 0; j < posicoes[i].length; j++) {
                System.out.print("+--------"); // Linhas horizontais
            }
            System.out.println("+");

            for (int j = 0; j < posicoes[i].length; j++) {
                System.out.printf("|%7d ", posicoes[i][j]); // Conteúdo da célula
            }
            System.out.println("|");
        }

        for (int j = 0; j < posicoes[0].length; j++) {
            System.out.print("+--------"); // Linhas horizontais
        }
        System.out.println("+");
    }

    private boolean temDigitosRepetidos(int numero) {
        String numeroStr = Integer.toString(numero);
        return numeroStr.length() > numeroStr.chars().distinct().count();
    }

    public int calcularPontos(int carta) {
        if (carta == 0) {
            return carta;
        }
        int pontos = 1;

        if (carta % 10 == 5) {
            pontos += 1;
        }

        if (carta % 10 == 0) {
            pontos += 2;
        }

        if (temDigitosRepetidos(carta)) {
            pontos += 4;
        }

        return pontos;
    }

    public void limparLinha(int linha, Jogador jogador, int jogada) {
        int[] linhaTabuleiro = posicoes[linha];
        int pontos = 0;

        // Calcular pontos conforme as regras
        for (int carta : linhaTabuleiro) {
            pontos += calcularPontos(carta);
        }

        // Adicionar pontos ao jogador
        jogador.pontos = jogador.pontos + pontos;

        // Limpar a linha do tabuleiro
        Arrays.fill(linhaTabuleiro, 0);
        posicoes[linha][0] = jogada;
    }

    public static int encontrarIndex(int[] array, int item) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == item) {
                return i; // Retorna o índice se o item for encontrado
            }
        }
        return -1; // Retorna -1 se o item não for encontrado
    }

    public void exibirPontosJogadores() {
        for (int i = 0; i < numJogadores; i++) {
            Jogador jogador = jogadores[i];
            System.out.println("Pontos do Jogador " + i + ": " + jogador.pontos);
        }
    }

    public void Rodada() {
        maoRodada = new int[numJogadores];
        Scanner scanner = new Scanner(System.in);
        // Escolher cartas
        for (int i = 0; i < numJogadores; i++) {
            System.out.println("Jogador " + i);
            jogadores[i].exibeMao();
            maoRodada[i] = scanner.nextInt();
            while ((!jogadores[i].contem(maoRodada[i])) || maoRodada[i] == 0) {
                System.out.println("Jogada inválida, selecione outro valor: ");
                maoRodada[i] = scanner.nextInt();
            }
            jogadores[i].removeCarta(maoRodada[i]);
        }
        // Ordenar cartas:
        maoOrdenada = maoRodada.clone();
        Arrays.sort(maoOrdenada);

        // Ordenar jogadas:
        for (int a = 0; a < numJogadores; a++) {
            int v1 = 0, v2 = 0, dist = 200;
            // Pegar últimos valores da linha:
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 4; j++) {
                    if (posicoes[i][j + 1] == 0) {
                        if ((maoOrdenada[a] - posicoes[i][j]) < dist) {
                            if (maoOrdenada[a] - posicoes[i][j] > 0) {
                                dist = maoOrdenada[a] - posicoes[i][j];
                                v1 = i;
                                v2 = j;
                            }
                        }
                        break;
                    }
                }
                if (posicoes[i][4] != 0) {
                    if ((maoOrdenada[a] - posicoes[i][4]) < dist) {
                        if (maoOrdenada[a] - posicoes[i][4] > 0) {
                            dist = maoOrdenada[a] - posicoes[i][4];
                            v1 = i;
                            v2 = 4;
                        }
                    }
                }
            }
            // casos onde necessita somar ponto ao jogador:
            if (dist == 200 || v2 == 4) {
                int index = encontrarIndex(maoRodada, maoOrdenada[a]);
                if (v2 == 4) {
                    limparLinha(v1, jogadores[index], maoOrdenada[a]);
                } else {
                    if (dist == 200) {
                        int linhaParaLimpar = 0, maiorNum = posicoes[0][0];
                        for (int linha = 0; linha < 5; linha++) {
                            for (int col = 0; col < 5; col++) {
                                if (posicoes[linha][col] > maiorNum) {
                                    maiorNum = posicoes[linha][col];
                                    linhaParaLimpar = linha;
                                }
                            }
                        }
                        limparLinha(linhaParaLimpar, jogadores[index], maoOrdenada[a]);
                        // coletar todas as cartas da linha cujo último número seja o maior
                    }
                }

            } else {
                posicoes[v1][v2 + 1] = maoOrdenada[a];
            }
        }
    }

    public void exibirVencedor() {
        int menorPontuacao = Integer.MAX_VALUE;
        int vencedor = -1;
        boolean empate = false;

        for (int i = 0; i < numJogadores; i++) {
            Jogador jogador = jogadores[i];
            if (jogador.pontos < menorPontuacao) {
                menorPontuacao = jogador.pontos;
                vencedor = i;
                empate = false;
            } else if (jogador.pontos == menorPontuacao) {
                empate = true;
            }
        }

        if (empate) {
            System.out.println("O jogo terminou em empate!");
        } else {
            System.out.println("O jogador " + vencedor + " venceu o jogo com " + menorPontuacao + " pontos!");
        }
    }

}
