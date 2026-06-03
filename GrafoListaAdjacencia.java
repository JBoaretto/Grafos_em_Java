import java.util.ArrayList;
import java.util.Collections;

public class GrafoListaAdjacencia extends Grafo {
    
    // Vértices
    private final ArrayList<String> vertices;
    
    // Lista de Adjacência   
    private final ArrayList<ArrayList<String>> adjList;     

    // Construtor
    public GrafoListaAdjacencia() {
        this.vertices = new ArrayList<>();
        this.adjList = new ArrayList<>();
    }


    // Métodos Auxiliares 
    
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

    
    // Métodos Principais 

    @Override
    public void adicionarVertice(String vertice) {
        if (vertice == null) {
            return;
        }

        if (!existeVertice(vertice)) {
            vertices.add(vertice);
            adjList.add(new ArrayList<>());
        }
    }

    @Override
    public void removerVertice(String vertice) {
        int indice = indiceDoVertice(vertice);
        
        // Vértice não existe
        if (indice == -1) { 
            return;
        }

        // Remoção do vértice
        vertices.remove(indice);
        adjList.remove(indice);

        // Remoção dos acessos (arestas) ao vértice removido
        for (ArrayList<String> vizinhos : adjList) {
            vizinhos.remove(vertice);
        }
    }

    @Override
    public void adicionarAresta(String origem, String destino) {
        if (origem == null || destino == null) {
            return;
        }

        // Adição dos vértices caso não existam
        adicionarVertice(origem);
        adicionarVertice(destino);

        // Índices dos vértices
        int indiceOrigem = indiceDoVertice(origem);
        int indiceDestino = indiceDoVertice(destino);

        // Acesso às listas de adjacência dos vértices
        ArrayList<String> vizinhosOrigem = adjList.get(indiceOrigem);
        ArrayList<String> vizinhosDestino = adjList.get(indiceDestino);

        // Adição da aresta (origem <-> destino)
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

        // Se não existirem os vértices, não há aresta para remover.
        if (indiceOrigem == -1 || indiceDestino == -1) {
            return;
        }

        // Remoção da arestas (origem <-> destino)
        adjList.get(indiceOrigem).remove(destino);
        adjList.get(indiceDestino).remove(origem);
    }

    @Override
    public boolean existeVertice(String vertice) {
        return indiceDoVertice(vertice) != -1;
    }

    @Override
    public boolean existeAresta(String origem, String destino) {

        // Índices dos vértices
        int indiceOrigem = indiceDoVertice(origem);
        int indiceDestino = indiceDoVertice(destino);

        // Verificação da existência dos vértices
        if (indiceOrigem == -1 || indiceDestino == -1) {
            return false;
        }

        // Verificação da existência das arestas
        return adjList.get(indiceOrigem).contains(destino);
    }

    @Override
    public int grau(String vertice) {
        int indice = indiceDoVertice(vertice);

        // Não existe: grau 0
        if (indice == -1) {
            return 0;
        }

        // Existe: quantidade de vizinhos
        return adjList.get(indice).size();
    }

    @Override
    public int ordem() {
        return vertices.size();
    }

    @Override
    public int tamanho() {
        int somaDosGraus = 0;
        int lacos = 0;

        // Soma dos graus de todos os vértices
        for (int i = 0; i < adjList.size(); i++) {
            String vertice = vertices.get(i);
            ArrayList<String> vizinhos = adjList.get(i);

            // Contagem de arestas o vértice atual
            somaDosGraus += vizinhos.size();

            // O laço conta 2 vezes
            if (vizinhos.contains(vertice)) {
                lacos++;
            }
        }

        // A quantidade de arestas é a soma dos graus dividida por 2 (pois cada aresta é contada 2 vezes),
        // mais os laços (que foram contados 2 vezes na soma dos graus).
        return (somaDosGraus + lacos) / 2;
    }

    @Override
    public String toString() {

        // Esse vetor de Strings irá armazenar o output da função
        ArrayList<String> linhas = new ArrayList<>();

        // Lista de vértices com aresta
        ArrayList<String> verticesComAresta = new ArrayList<>();

        // Varreremos os vértices 
        for (int i = 0; i < vertices.size(); i++) {
            
            // Vértice Origem
            String origem = vertices.get(i);

            // Vizinhos do vértice origem
            ArrayList<String> vizinhos = adjList.get(i);

            // Vamos avaliar cada vértice vizinho
            for (String destino : vizinhos) {

                // Vamos ver que é lexicograficamente menor/maior: origem ou destino, para evitar duplicação de arestas
                String primeiro = menor(origem, destino);
                String segundo = maior(origem, destino);

                // Se a origem for o menor, adicionamos a aresta ao output
                if (origem.equals(primeiro)) {
                    linhas.add("    \"" + primeiro + "\" -- \"" + segundo + "\";");
                }

                // Senão, a aresta será adicionada quando processarmos o vértice destino, então não fazemos nada.
                adicionarSeNaoExistir(verticesComAresta, origem);
                adicionarSeNaoExistir(verticesComAresta, destino);
            }
        }

        // Adicionamos os vértices que não têm arestas
        ArrayList<String> verticesIsolados = new ArrayList<>();
        for (String vertice : vertices) {
            if (!verticesComAresta.contains(vertice)) {
                verticesIsolados.add("    \"" + vertice + "\";");
            }
        }

        // Ordenamos lexicograficamente as linhas para garantir uma saída consistente
        Collections.sort(linhas); 
        Collections.sort(verticesIsolados);

        // Construção do output final
        StringBuilder sb = new StringBuilder();
        sb.append("graph {");

        // Primeiro, adicionamos os vértices isolados ao output
        for (String isolado : verticesIsolados) {
            sb.append(System.lineSeparator()).append(isolado);
        }

        // Em seguida, adição de cada linha ao output ordenado
        for (String linha : linhas) {
            sb.append(System.lineSeparator()).append(linha);
        }

        // Fechamento do output
        sb.append(System.lineSeparator()).append("}");

        // Retorno do output final
        return sb.toString();
    }
}
