public class Jogador {
    public int pontos;
    public int cartas[] = new int[12];

    public void exibeMao() {
        System.out.print("Escolha uma carta: ");
        for (int i = 0; i < 12; i++) {
            System.out.print(cartas[i] + " ");
        }
        System.out.println(" ");
    }

    public void removeCarta(int valor) {
        for (int i = 0; i < cartas.length; i++) {
            if (cartas[i] == valor) {
                // Mover as cartas restantes para preencher o espaço vazio
                for (int j = i; j < cartas.length - 1; j++) {
                    cartas[j] = cartas[j + 1];
                }
                cartas[cartas.length - 1] = 0; // Última posição agora é zero
                return; // Sair do método após encontrar e remover a carta
            }
        }
    }

    public boolean contem(int a) {
        for (int i = 0; i < 12; i++) {
            if (a == cartas[i]) {
                return true;
            }
        }
        return false;
    }
}
