import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Criando os grafos
        GrafoListaAdjacencia grafoLista = new GrafoListaAdjacencia();
        GrafoMatrizAdjacencia grafoMatriz = new GrafoMatrizAdjacencia();
        GrafoPonderadoMatrizAdjacencia grafoPonderado = new GrafoPonderadoMatrizAdjacencia();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {     // vai continuar lendo enquanto tiver linha
            String linha = scanner.nextLine().trim();

            if (linha.isEmpty()) {
                continue;
            }

            String[] partes = linha.split(" ");
            String comando = partes[0];

            if (comando.equals("i") && partes.length == 4) {            // (i & 4) -> inserir
                String origem = partes[1];
                String destino = partes[2];
                int peso = Integer.parseInt(partes[3]);

                grafoLista.adicionarAresta(origem, destino);
                grafoMatriz.adicionarAresta(origem, destino);
                grafoPonderado.adicionarAresta(origem, destino, peso);
            } else if (comando.equals("d") && partes.length == 3) {     // (d & 3) -> remover aresta
                String origem = partes[1];
                String destino = partes[2];

                grafoLista.removerAresta(origem, destino);
                grafoMatriz.removerAresta(origem, destino);
                grafoPonderado.removerAresta(origem, destino);
            } else if (comando.equals("d") && partes.length == 2) {     // (d & 2) -> remover vértice
                String vertice = partes[1];

                grafoLista.removerVertice(vertice);
                grafoMatriz.removerVertice(vertice);
                grafoPonderado.removerVertice(vertice);
            } else if (comando.equals("p") && partes.length == 1) {     // (p & 1) -> imprimir
                System.out.println("Lista de Adjacencia");
                System.out.println(grafoLista);
                System.out.println("Matriz de Adjacencia");
                System.out.println(grafoMatriz);
                System.out.println("Ponderado - Matriz de Adjacencia");
                System.out.println(grafoPonderado);
            }
        }

        scanner.close();
    }
}
