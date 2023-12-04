public class App {
    public static void main(String[] args) throws Exception {
        Utilitarios util = new Utilitarios();
        Tabuleiro tab = new Tabuleiro();
        tab.iniciarTabuleiro();

        for (int i = 0; i < 12; i++) {
            util.limparTerminal();
            tab.exibirTabuleiro();
            tab.exibirPontosJogadores();
            tab.Rodada();
        }
        util.limparTerminal();
        tab.exibirTabuleiro();
        tab.exibirPontosJogadores();
        tab.exibirVencedor();

    }
}
