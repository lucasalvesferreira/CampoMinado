package view

import model.Field
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

// Clasee que precisa que recebe duas funçoes
// btnEsquerdo e btnDireito -> e o campo que foi usado essas funçoes
class MouseCliqueListener (
    private val field: Field,
    private val onButtonLeft: (Field) -> Unit,
    private val onButtonRight: (Field) -> Unit
) : MouseListener{

    //    Ao clicar com botao direito ou com botao esquerdo
//    chama uma funçao que recebe um campo que sera usada em outra classe
    override fun mousePressed(e: MouseEvent?) {
        when(e?.button){
            MouseEvent.BUTTON1 -> onButtonLeft(field);
            MouseEvent.BUTTON2 -> onButtonRight(field); // ou botao2
        }
    }

    //    Metodos que sao forçados a serem implementados porem nao serao usados
    override fun mouseClicked(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}

}