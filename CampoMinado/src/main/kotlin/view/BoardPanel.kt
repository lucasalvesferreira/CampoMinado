package view

import model.Board
import java.awt.GridLayout
import javax.swing.JPanel

//  Recebe uma classe tabuleiro
class BoardPanel(board: Board) : JPanel() {

//    Definindo um layout especifico que nesse caso e composto por linhas e colunas
//    Passa por todos os campos add criando e adicionando o botao nesse painel
    init {
        layout = GridLayout(board.qttLines, board.qttColumn);
        board.forEachFields { field ->
            val button = ButtonField(field);
            add(button);
        }
    }


}