package model

// Todos eventos possiveis que podem acontecer em cima de um campo
//      ABERTURA, MARCACAO, DESMARCACAO, EXPLOSAO, REINICIALIZACAO
enum class EventField { OPENING, MARKING, UNMARKING, EXPLOSION, RESET }

// Usamos dataClass pois em alguns momentos no codigo usamos o destructuring para extrair os atributos de uma classe tem que ser dataclass
// Recebe linha e coluna dentro do tabuleiro
data class Field(val line: Int,
                 val column: Int) {
    //    Lista de vizinhos
    private val neighbors = ArrayList<Field>();

    //    Lista de funçoes que sao disparadas sempre que um evento acontece
    //    Lista que recebe um campo e o evento e nao retorna nada
    private val callbacks = ArrayList<(Field, EventField) -> Unit>();

    //    Atributos
    var market: Boolean = false;
    var open: Boolean = false;
    var mined: Boolean = false;

    //    Somente leitura
    val unchecked: Boolean get() = !market;
    val close: Boolean get() = !open;
    val safe: Boolean get() = !mined;

    //    Atributos calculados
    val finishSuccessfully: Boolean get() = safe && open || mined && market;
    val qttMinedNeighbors: Int get() = neighbors.filter { it.mined }.size; //  Filtra todos os viznhos que tem mina e calcula a qtd de viznhos minados
    val safeNeighborhood: Boolean // Pega a lista de viznhos que e uma lista de campos, tranforma com o map para uma lista de valores booleanos e no reduce compara atributo com atributo (ex: true , true = true)
        get() = neighbors.map { it.safe }
            .reduce { result, safe -> result && safe }; // se qualquer um dos elementos for false da falso e se todos forem true da verdadeiro

    //    Add dentro da lista de vizinhos
    fun addNeighbors(neighbor: Field) {
        neighbors.add(neighbor);
    }

    //    Registra as funçoes callBacks na lista de callbacks
    //    Quando evento acontecer executa todas as fun que foram registradas
    fun onEvent(callback: (Field, EventField) -> Unit) {
        callbacks.add(callback);
    }

    //    Abre a mina se tiver fechada
    //    Faz uma validaçao para saber se esta minado, se estiver dispara um callback informando um evento de explosao
    //    Se nao tiver dispara um evento de abrir para todos os vizinhos que estao fechados, seguros e que a viznhança e segura
    fun open() {
        if (close) {
            open = true;
            if (mined) {
                callbacks.forEach { it(this, EventField.EXPLOSION) }
            } else {
                callbacks.forEach { it(this, EventField.OPENING) };
                neighbors.filter { it.close && it.safe && safeNeighborhood }.forEach { it.open() };
            }
        }
    }

    //    Altera a marcaçao colocada em cima de um campo de marcado para desmarcado e vise Versa
    fun changeMarking() {
        if (close) {
            market = !market;
            val evento = if (market) EventField.MARKING else EventField.UNMARKING;
            callbacks.forEach { it(this, evento) };
        }
    }

    //    Atibui a mina a o campo
    fun mine() {
        mined = true;
    }

    //    Reinicia o jogo marcando todos os atributos necessarios pra isso com false e envia um evento de reincializaçao
    fun reset() {
        open = false;
        mined = false;
        market = false;
        callbacks.forEach { callback ->
            callback(
                this,
                EventField.RESET
            )
        } //  ouuu Sem renomear o forEach callbacks.forEach { it (this, EventField.RESET) }
    }

}