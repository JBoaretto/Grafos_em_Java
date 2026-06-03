public abstract class Grafo {

    /*
    Superclasse abstrata para representar um grafo. 
    Define os métodos que devem ser implementados por qualquer classe concreta de grafo.
     */
    public abstract void adicionarVertice(String vertice);

    public abstract void removerVertice(String vertice);

    public abstract void adicionarAresta(String origem, String destino);

    public abstract void removerAresta(String origem, String destino);

    public abstract boolean existeVertice(String vertice);

    public abstract boolean existeAresta(String origem, String destino);

    public abstract int grau(String vertice);

    public abstract int ordem();

    public abstract int tamanho();

    /*
    O Override do método toString() é declarado como abstrato, 
    o que significa que cada classe concreta de grafo deve fornecer 
    sua própria implementação para representar o grafo como uma string. 
    */
    @Override
    public abstract String toString();
}
