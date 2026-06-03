import java.util.Scanner;

/* DUPLA
16876293 - João Pedro Boaretto
15512767 - José Fausto Vital Barbosa
*/ 

public class Main {
    public static void main(String[] args) {

        // Criando os grafos
        GrafoListaAdjacencia grafoLista = new GrafoListaAdjacencia();
        GrafoMatrizAdjacencia grafoMatriz = new GrafoMatrizAdjacencia();
        GrafoPonderadoMatrizAdjacencia grafoPonderado = new GrafoPonderadoMatrizAdjacencia();

        Scanner scanner = new Scanner(System.in);
        
        // Vai continuar lendo enquanto tiver linha
        while (scanner.hasNextLine()) {     
            
            // Lê a linha e remove espaços em branco no início e no fim, para evitar problemas de formatação.
            String linha = scanner.nextLine().trim();

            // Se for vazia, pula.
            if (linha.isEmpty()) {
                continue;
            }

            // Divide a linha em partes, usando espaço como separador.
            String[] partes = linha.split(" ");
            String comando = partes[0];

            // Verifica o comando e a quantidade de partes para determinar qual operação realizar.
            if (comando.equals("i") && partes.length == 4) {            // (i & 4) -> inserir

                String origem = partes[1];
                String destino = partes[2];

                int peso = Integer.parseInt(partes[3]);

                grafoLista.adicionarAresta(origem, destino);
                grafoMatriz.adicionarAresta(origem, destino);
                grafoPonderado.adicionarAresta(origem, destino, peso);

            } 
            
            else if (comando.equals("d") && partes.length == 3) {     // (d & 3) -> remover aresta
                String origem = partes[1];
                String destino = partes[2];

                grafoLista.removerAresta(origem, destino);
                grafoMatriz.removerAresta(origem, destino);
                grafoPonderado.removerAresta(origem, destino);
            } 
            
            else if (comando.equals("d") && partes.length == 2) {     // (d & 2) -> remover vértice
                String vertice = partes[1];

                grafoLista.removerVertice(vertice);
                grafoMatriz.removerVertice(vertice);
                grafoPonderado.removerVertice(vertice);
            } 
            
            else if (comando.equals("p") && partes.length == 1) {     // (p & 1) -> imprimir

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
