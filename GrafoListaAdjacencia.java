import java.util.ArrayList;
import java.util.Collections;

public class GrafoListaAdjacencia extends Grafo {
    private final ArrayList<String> vertices;
    private final ArrayList<ArrayList<String>> adjacencias;     // lista de adjacência

    public GrafoListaAdjacencia() {
        this.vertices = new ArrayList<>();
        this.adjacencias = new ArrayList<>();
    }

    @Override
    public void adicionarVertice(String vertice) {
        if (vertice == null) {
            return;
        }

        if (!existeVertice(vertice)) {
            vertices.add(vertice);
            adjacencias.add(new ArrayList<>());
        }
    }

    @Override
    public void removerVertice(String vertice) {
        int indice = indiceDoVertice(vertice);

        if (indice == -1) { // vértice não existe
            return;
        }

        vertices.remove(indice);
        adjacencias.remove(indice);

        for (ArrayList<String> vizinhos : adjacencias) {
            vizinhos.remove(vertice);
        }
    }

    @Override
    public void adicionarAresta(String origem, String destino) {
        if (origem == null || destino == null) {
            return;
        }

        adicionarVertice(origem);
        adicionarVertice(destino);

        int indiceOrigem = indiceDoVertice(origem);
        int indiceDestino = indiceDoVertice(destino);

        ArrayList<String> vizinhosOrigem = adjacencias.get(indiceOrigem);
        ArrayList<String> vizinhosDestino = adjacencias.get(indiceDestino);

        if (!vizinhosOrigem.contains(destino)) {
            vizinhosOrigem.add(destino);
        }

        if (!vizinhosDestino.contains(origem)) {
            vizinhosDestino.add(origem);
        }
    }

    @Override
    public void removerAresta(String origem, String destino) {
        int indiceOrigem = indiceDoVertice(origem);
        int indiceDestino = indiceDoVertice(destino);

        if (indiceOrigem == -1 || indiceDestino == -1) {
            return;
        }

        adjacencias.get(indiceOrigem).remove(destino);
        adjacencias.get(indiceDestino).remove(origem);
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

        return adjacencias.get(indiceOrigem).contains(destino);
    }

    @Override
    public int grau(String vertice) {
        int indice = indiceDoVertice(vertice);

        if (indice == -1) {
            return 0;
        }

        return adjacencias.get(indice).size();
    }

    @Override
    public int ordem() {
        return vertices.size();
    }

    @Override
    public int tamanho() {
        int somaDosGraus = 0;
        int lacos = 0;

        for (int i = 0; i < adjacencias.size(); i++) {
            String vertice = vertices.get(i);
            ArrayList<String> vizinhos = adjacencias.get(i);

            somaDosGraus += vizinhos.size();

            if (vizinhos.contains(vertice)) {
                lacos++;
            }
        }

        return (somaDosGraus + lacos) / 2;
    }

    @Override
    public String toString() {
        ArrayList<String> linhas = new ArrayList<>();
        ArrayList<String> verticesComAresta = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            String origem = vertices.get(i);
            ArrayList<String> vizinhos = adjacencias.get(i);

            for (String destino : vizinhos) {
                String primeiro = menor(origem, destino);
                String segundo = maior(origem, destino);

                if (origem.equals(primeiro)) {
                    linhas.add(" \"" + primeiro + "\" -- \"" + segundo + "\";");
                }

                adicionarSeNaoExistir(verticesComAresta, origem);
                adicionarSeNaoExistir(verticesComAresta, destino);
            }
        }

        for (String vertice : vertices) {
            if (!verticesComAresta.contains(vertice)) {
                linhas.add(" \"" + vertice + "\";");
            }
        }

        Collections.sort(linhas); // ordenação lexicográfica

        StringBuilder sb = new StringBuilder();
        sb.append("graph {");

        for (String linha : linhas) {
            sb.append(System.lineSeparator()).append(linha);
        }

        sb.append(System.lineSeparator()).append("}");
        return sb.toString();
    }

    // Métodos auxiliares:
    private int indiceDoVertice(String vertice) {
        return vertices.indexOf(vertice);
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
