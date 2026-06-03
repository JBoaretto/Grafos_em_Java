import java.util.ArrayList;
import java.util.Collections;

public class GrafoPonderadoMatrizAdjacencia extends Grafo {
    private static final int AUSENCIA_ARESTA = -1;

    private final ArrayList<String> vertices;
    private int[][] matriz;

    public GrafoPonderadoMatrizAdjacencia() {
        this.vertices = new ArrayList<>();
        this.matriz = new int[0][0];
        preencherMatrizComAusencia(matriz);
    }

    @Override
    public void adicionarVertice(String vertice) {
        if (vertice == null) {
            return;
        }

        if (!existeVertice(vertice)) {
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

        for (int i = indice; i < qtdAntesRemocao - 1; i++) {
            for (int j = 0; j < qtdAntesRemocao; j++) {
                matriz[i][j] = matriz[i + 1][j];
            }
        }

        for (int j = indice; j < qtdAntesRemocao - 1; j++) {
            for (int i = 0; i < qtdAntesRemocao - 1; i++) {
                matriz[i][j] = matriz[i][j + 1];
            }
        }

        int ultimoIndiceUsado = qtdAntesRemocao - 1;
        for (int i = 0; i < qtdAntesRemocao; i++) {
            matriz[ultimoIndiceUsado][i] = AUSENCIA_ARESTA;
            matriz[i][ultimoIndiceUsado] = AUSENCIA_ARESTA;
        }
    }

    @Override
    public void adicionarAresta(String origem, String destino) {
        adicionarAresta(origem, destino, 1);
    }

    public void adicionarAresta(String origem, String destino, int peso) {
        if (origem == null || destino == null) {
            return;
        }

        adicionarVertice(origem);
        adicionarVertice(destino);

        int indiceOrigem = indiceDoVertice(origem);
        int indiceDestino = indiceDoVertice(destino);

        matriz[indiceOrigem][indiceDestino] = peso;
        matriz[indiceDestino][indiceOrigem] = peso;
    }

    @Override
    public void removerAresta(String origem, String destino) {
        int indiceOrigem = indiceDoVertice(origem);
        int indiceDestino = indiceDoVertice(destino);

        if (indiceOrigem == -1 || indiceDestino == -1) {
            return;
        }

        matriz[indiceOrigem][indiceDestino] = AUSENCIA_ARESTA;
        matriz[indiceDestino][indiceOrigem] = AUSENCIA_ARESTA;
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

        return matriz[indiceOrigem][indiceDestino] != AUSENCIA_ARESTA;
    }

    @Override
    public int grau(String vertice) {
        int indice = indiceDoVertice(vertice);

        if (indice == -1) {
            return 0;
        }

        int grau = 0;
        for (int j = 0; j < vertices.size(); j++) {
            if (matriz[indice][j] != AUSENCIA_ARESTA) {
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
        int quantidadeArestas = 0;

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i; j < vertices.size(); j++) {
                if (matriz[i][j] != AUSENCIA_ARESTA) {
                    quantidadeArestas++;
                }
            }
        }

        return quantidadeArestas;
    }

    @Override
    public String toString() {
        ArrayList<String> linhas = new ArrayList<>();
        ArrayList<String> verticesComAresta = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i; j < vertices.size(); j++) {
                if (matriz[i][j] != AUSENCIA_ARESTA) {
                    String origem = vertices.get(i);
                    String destino = vertices.get(j);
                    String primeiro = menor(origem, destino);
                    String segundo = maior(origem, destino);
                    int peso = matriz[i][j];

                    linhas.add(" \"" + primeiro + "\" -- \"" + segundo + "\" [label=\"" + peso + "\"];");
                    adicionarSeNaoExistir(verticesComAresta, origem);
                    adicionarSeNaoExistir(verticesComAresta, destino);
                }
            }
        }

        for (String vertice : vertices) {
            if (!verticesComAresta.contains(vertice)) {
                linhas.add(" \"" + vertice + "\";");
            }
        }

        Collections.sort(linhas);

        StringBuilder sb = new StringBuilder();
        sb.append("graph {");

        for (String linha : linhas) {
            sb.append(System.lineSeparator()).append(linha);
        }

        sb.append(System.lineSeparator()).append("}");
        return sb.toString();
    }

    private int indiceDoVertice(String vertice) {
        return vertices.indexOf(vertice);
    }

    private void garantirCapacidade(int capacidadeNecessaria) {
        if (capacidadeNecessaria <= matriz.length) {
            return;
        }

        int novaCapacidade = matriz.length * 2;
        if (novaCapacidade < capacidadeNecessaria) {
            novaCapacidade = capacidadeNecessaria;
        }

        int[][] novaMatriz = new int[novaCapacidade][novaCapacidade];
        preencherMatrizComAusencia(novaMatriz);

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                novaMatriz[i][j] = matriz[i][j];
            }
        }

        matriz = novaMatriz;
    }

    private void preencherMatrizComAusencia(int[][] matrizParaPreencher) {
        for (int i = 0; i < matrizParaPreencher.length; i++) {
            for (int j = 0; j < matrizParaPreencher[i].length; j++) {
                matrizParaPreencher[i][j] = AUSENCIA_ARESTA;
            }
        }
    }

    private void adicionarSeNaoExistir(ArrayList<String> lista, String valor) {
        if (!lista.contains(valor)) {
            lista.add(valor);
        }
    }

    private String menor(String a, String b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    private String maior(String a, String b) {
        return a.compareTo(b) <= 0 ? b : a;
    }
}
