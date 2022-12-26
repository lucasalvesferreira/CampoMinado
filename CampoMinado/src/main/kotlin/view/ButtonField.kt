package view

import model.EventField
import model.Field
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingUtilities

private val COR_BG_NORMAL = Color(184, 184, 184);
private val COR_BG_MARCACAO = Color(8, 179, 247);
private val COR_BG_EXPLOSAO = Color(189, 66, 68);
private val COR_TXT_GREEN = Color(0, 100, 0);

//  Classe de alterar estado do botao
// Recebe um campo e executa uma açao em cima dele
class ButtonField(private val field: Field) : JButton() {

    //    Inicializa a classe apllicando as funçoes da classe MouseCliqueListener e setando algumas config padrao
    init {
        font = font.deriveFont(Font.BOLD);
        background = COR_BG_NORMAL;
        isOpaque = true;
        border = BorderFactory.createBevelBorder(0);
        addMouseListener(MouseCliqueListener(field, { it.open() }, { it.changeMarking() }));

//  Sempre que um evento acontecer no campo ele chama o metodo para aplicar o estilo correto
        field.onEvent(this::applyStyle);
    }

    //  Um when onde para cada um dos eventos tem um estilo especifico
    private fun applyStyle(field: Field, event: EventField) {
        when (event) {
            EventField.EXPLOSION -> applyStyleExplosion(); // Aplica estilo de explosao
            EventField.OPENING -> applyStyleOpen();       // Aplica estilo de abertura do campo
            EventField.MARKING -> applyStyleMarket();    // Aplica estilo de marcado ao campo
            else -> applyNormalStyle();                 // Aplica estilo padrao ao campo
        }

//    Para evitar bugs na alteraçao dos estilos chamamos o metodo abaixo que pinta o botao novamente
        SwingUtilities.invokeLater {
            repaint();
            validate();
        }
    }

    // Aplica estilo de explosao
    private fun applyStyleExplosion() {
        background = COR_BG_EXPLOSAO;
        text = "X";
    }

    // Aplica estilo de abertura do campo
    private fun applyStyleOpen() {
        background = COR_BG_NORMAL;
        border = BorderFactory.createLineBorder(Color.GRAY);

        foreground = when (field.qttMinedNeighbors) {
            1 -> COR_TXT_GREEN;
            2 -> Color.BLUE;
            3 -> Color.YELLOW;
            4, 5, 6 -> Color.RED;
            else -> Color.PINK;
        }

        text = if (field.qttMinedNeighbors > 0) field.qttMinedNeighbors.toString() else "";
    }

    // Aplica estilo de marcado ao campo
    private fun applyStyleMarket() {
        background = COR_BG_MARCACAO;
        foreground = Color.BLACK;
        text = "M"
    }

    // Aplica estilo padrao ao campo
    private fun applyNormalStyle() {
        background = COR_BG_NORMAL;
        border = BorderFactory.createBevelBorder(0);
        text = "";
    }

}