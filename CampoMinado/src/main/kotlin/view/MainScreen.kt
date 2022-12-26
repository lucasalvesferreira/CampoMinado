package view

import model.Board
import model.EventBoard
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main(args: Array<String>) {
    MainScreen()
}

class MainScreen :JFrame() {

    private val board = Board(qttLines = 16, qttColumn = 30,89);
    private val boardPanel = BoardPanel(board);

    init {
        board.onEvent (this::showResult);
        add(boardPanel);
        setSize(690,438);
        setLocationRelativeTo(null);
        defaultCloseOperation = EXIT_ON_CLOSE;
        title = "Mined Field";
        isVisible = true;
    }

    private fun showResult(eventBoard: EventBoard){
        SwingUtilities.invokeLater {
            val msg = when(eventBoard){
                EventBoard.VICTORY -> "You win!";
                EventBoard.DEFEAT -> "You loose... :P"
            }

            JOptionPane.showMessageDialog(this,msg);
            board.reset();

            boardPanel.repaint();
            boardPanel.validate();
        }
    }
}
