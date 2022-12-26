package model

import java.util.Random

// Todos eventos possiveis que podem acontecer
// VITORIA E DERROTA
enum class EventBoard { VICTORY, DEFEAT }

//  Recebe quantidade de linhas, colunas e minas
class Board(
    val qttLines: Int,
    val qttColumn: Int,
    private val qttMine: Int
) {

    private val filds = ArrayList<ArrayList<Field>>(); //  Campos do tabuleiro (ArrayList de ArrayList)
    private val callbacks = ArrayList<(EventBoard) -> Unit>(); // Chama evento de vitoria e derrota

    init {
        generateFields();
        associateNeighbors();
        raffleMines();
    }

    //    Gera os campos do tabuleiro
//    percorre do zero ate a quantidade de linhas ou colunas que foi passada mas nao contando a quantidade de linhas ou seja qtdLinhas -1 ou para colunas
    private fun generateFields() {
        for (line in 0 until qttLines) {
            filds.add(ArrayList());
            for (column in 0 until qttColumn) {
                val newFild = Field(line, column);
                newFild.onEvent(this::verifyVictoryOrDefeat)
                filds[line].add(newFild)
            }
        }
    }

    // Percorre os todos os campos do tabuleiro inicializados a cima( generateFields) e chama o metodo associar viznhos passando como parametro o campo como parametro
    private fun associateNeighbors() {
        forEachFields { associateNeighbors(it) }
    }

// Apartir da linhas ou colunas eu posso ter :
//              viznhos em coluna +1 ou viznhos em coluna -1
//              viznhos em linhas +1 ou viznhos em linhas -1

    private fun associateNeighbors(field: Field) {
        val (line, column) = field;
        val lines = arrayOf(line - 1, line, line + 1);
        val columns = arrayOf(column - 1, column, column + 1);

        lines.forEach { l ->
            columns.forEach { c ->
                val current = filds.getOrNull(l)?.getOrNull(c);
                current?.takeIf { field != it }?.let { field.addNeighbors(it) }
            }
        }
    }

    //    Sorteia minas enquanto a quantidade de minas atual for menor que a passada verificando se o campo ja nao tem alguma mina
    private fun raffleMines() {
        val generator = Random()

        var randomLine = -1;
        var randomColumn = -1;
        var amountOfCurrentMines = 0;

        while (amountOfCurrentMines < this.qttMine) {
            randomLine = generator.nextInt(qttLines);
            randomColumn = generator.nextInt(qttColumn);

            val randomField = filds[randomLine][randomColumn];
            if (randomField.safe) {
                randomField.mine();
                amountOfCurrentMines++;
            }
        }
    }

    //    Passa por todos os campos verificando se o objetivo foi alcançado ou seja se terminou com sucesso
//    Se todos os campos forem validos retorna true
    private fun goalAchieved(): Boolean {
        var playerWin = true;
        forEachFields { if (!it.finishSuccessfully) playerWin = false };
        return playerWin;
    }

    //    Apos qualquer evento verifica se ele ganhou ou perdeu
//    Se ouver uma explosao dispara um evento de derrota
//    Se nao verifica se o objetivo foi alcançado e se for dispara vitoria
    private fun verifyVictoryOrDefeat(field: Field, event: EventField) {
        if (event == EventField.EXPLOSION) {
            callbacks.forEach { it(EventBoard.DEFEAT) };
        } else if (goalAchieved()) {
            callbacks.forEach { it(EventBoard.VICTORY) };
        }
    }

    //  Como os campos e um atributo private existe uma necessidade de percorrer os campos para aleterar, limpar ,etc
//    Imitaçao do forEach mas percorre todos os campos
    fun forEachFields(callback: (Field) -> Unit) {
        filds.forEach { line -> line.forEach(callback) };
    }

    //    Adiciona na lista de chamados do tabulieros (callbacks) para que alguem fique escutando se ganhou ou perdeu
    fun onEvent(callback: (EventBoard) -> Unit) {
        callbacks.add(callback);
    }

    //    Reinicia todos os campos
//    e sorteia as minas novamente
    fun reset() {
        forEachFields { it.reset() };
        raffleMines();
    }
}