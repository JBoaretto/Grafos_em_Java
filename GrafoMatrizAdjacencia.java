import java.util.ArrayList;
import java.util.Collections;

public class GrafoMatrizAdjacencia extends Grafo {

    // Vértices e matriz de adjacência
    private final ArrayList<String> vertices;
    private boolean[][] matriz;

    // Construtor
    public GrafoMatrizAdjacencia() {
        this.vertices = new ArrayList<>();
        this.matriz = new boolean[0][0];
    }

    // Métodos Auxiliares
    private int indiceDoVertice(String vertice) {
        return vertices.indexOf(vertice);
    }

    // Dobra o tamanho da matriz quando necessário para acomodar novos vértices.
    private void garantirCapacidade(int capacidadeNecessaria) {
        if (capacidadeNecessaria <= matriz.length) {
            return;
        }

        int novaCapacidade = matriz.length * 2;
        if (novaCapacidade < capacidadeNecessaria) {
            novaCapacidade = capacidadeNecessaria;
        }

        boolean[][] novaMatriz = new boolean[novaCapacidade][novaCapacidade];

        for (int i = 0; i < vertices.size(); i++) {
            System.arraycopy(matriz[i], 0, novaMatriz[i], 0, vertices.size());
        }

        matriz = novaMatriz;
    }

    private void adicionarSeNaoExistir(ArrayList<String> lista, String valor) {
        if (!lista.contains(valor)) {
            lista.add(valor);
        }
    }

    // Retorna o vértice que vem primeiro em ordem lexicográfica.
    private String menor(String a, String b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    // Retorna o vértice que vem depois em ordem lexicográfica.
    private String maior(String a, String b) {
        return a.compareTo(b) <= 0 ? b : a;
    }

    // Métodos Principais

    @Override
    public void adicionarVertice(String vertice) {
        if (vertice == null) {
            return;
        }

        if (!existeVertice(vertice)) {

            // Aumenta o tamanho da matriz se necessário.
            garantirCapacidade(vertices.size() + 1);
            vertices.add(vertice);
        }
    }

    @Override
    public void removerVertice(String vertice) {
        int indice = indiceDoVertice(vertice);

        if (indice == -1) {
            return;
        }

        int qtdAntesRemocao = vertices.size();
        vertices.remove(indice);

        // Vamos sobrescrever a linha do vértice removido
        for (int i = indice; i < qtdAntesRemocao - 1; i++) {

            //A função abaixo copia a linha i+1 para a linha i, sobrescrevendo a linha do vértice removido.
            //source, sourceCol, dest, destCol, length
            System.arraycopy(matriz[i + 1], 0, matriz[i], 0, qtdAntesRemocao);
        }

        // Vamos sobrescrever a coluna do vértice removido
        for (int j = indice; j < qtdAntesRemocao - 1; j++) {
            for (int i = 0; i < qtdAntesRemocao - 1; i++) {
                matriz[i][j] = matriz[i][j + 1];
            }
        }

        // Vamos limpar a última linha e coluna, para não guardarem lixo
        int ultimoIndiceUsado = qtdAntesRemocao - 1;
        for (int i = 0; i < qtdAntesRemocao; i++) {
            matriz[ultimoIndiceUsado][i] = false;
            matriz[i][ultimoIndiceUsado] = false;
        }
    }

    @Override
    public void adicionarAresta(String origem, String destino) {
        if (origem == null || destino == null) {
            return;
        }

        // Adição de novos vértices caso não existam
        adicionarVertice(origem);
        adicionarVertice(destino);

        // Obtenção dos índices dos vértices
        int indiceOrigem = indiceDoVertice(origem);
        int indiceDestino = indiceDoVertice(destino);

        // Inserção da Aresta
        matriz[indiceOrigem][indiceDestino] = true;
        matriz[indiceDestino][indiceOrigem] = true;
    }

    @Override
    public void removerAresta(String origem, String destino) {
        int indiceOrigem = indiceDoVertice(origem);
        int indiceDestino = indiceDoVertice(destino);

        if (indiceOrigem == -1 || indiceDestino == -1) {
            return;
        }

        // Remoção se os vértices existirem
        matriz[indiceOrigem][indiceDestino] = false;
        matriz[indiceDestino][indiceOrigem] = false;
    }

    @Override
    public boolean existeVertice(String vertice) {
        return indiceDoVertice(vertice) != -1;
    }

    @Override
    public boolean existeAresta(String origem, String destino) {
        int indiceOrigem = indiceDoVertice(origem);
        int indiceDestino = indiceDoVertice(destino);

        if (indiceOrigem == -1 || indiceDestino == -1) {
            return false;
        }

        // Verifica existência se os vértices existirem
        return matriz[indiceOrigem][indiceDestino];
    }

    @Override
    public int grau(String vertice) {
        int indice = indiceDoVertice(vertice);

        if (indice == -1) {
            return 0;
        }

        // Retorna a quantidade de arestas que existem na linha do vértice, que é o seu grau.
        int grau = 0;
        for (int j = 0; j < vertices.size(); j++) {
            if (matriz[indice][j]) {
                grau++;
            }
        }

        return grau;
    }

    @Override
    public int ordem() {
        return vertices.size();
    }

    @Override
    public int tamanho() {
        int qtdArestas = 0;

        // Conta as arestas existentes, percorrendo apenas metade da matriz
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i; j < vertices.size(); j++) {
                if (matriz[i][j]) {
                    qtdArestas++;
                }
            }
        }

        return qtdArestas;
    }

    @Override
    public String toString() {
        
        // Mesma ideia da Lista de Adjacência: 
        // vamos construir uma lista de linhas para o output,
        // e uma lista auxiliar para rastrear quais vértices têm arestas,
        // para depois adicionar os vértices isolados.

        ArrayList<String> linhas = new ArrayList<>();
        ArrayList<String> verticesComAresta = new ArrayList<>();

        // Para cada vértice
        for (int i = 0; i < vertices.size(); i++) {

            // Vemos apenas a metade da matriz, para evitar duplicação de arestas
            for (int j = i; j < vertices.size(); j++) {

                if (matriz[i][j]) {
                    String origem = vertices.get(i);
                    String destino = vertices.get(j);
                    String primeiro = menor(origem, destino);
                    String segundo = maior(origem, destino);

                    // Adicionamos o menor lexicograficamente como origem, para evitar duplicação de arestas
                    linhas.add("    \"" + primeiro + "\" -- \"" + segundo + "\";");

                    // Registramos que ambos os vértices têm arestas, para depois podermos adicionar os isolados
                    adicionarSeNaoExistir(verticesComAresta, origem);
                    adicionarSeNaoExistir(verticesComAresta, destino);
                }
            }
        }

        // Adicionamos os vértices que não têm arestas
        ArrayList<String> verticesIsolados = new ArrayList<>();
        for (String vertice : vertices) {
            if (!verticesComAresta.contains(vertice)) {
                verticesIsolados.add("    \"" + vertice + "\";");
            }
        }

        Collections.sort(linhas);
        Collections.sort(verticesIsolados);

        StringBuilder sb = new StringBuilder();
        sb.append("graph {");

        for (String linha : verticesIsolados) {
            sb.append(System.lineSeparator()).append(linha);
        }

        for (String linha : linhas) {
            sb.append(System.lineSeparator()).append(linha);
        }

        sb.append(System.lineSeparator()).append("}");
        return sb.toString();
    }
}
