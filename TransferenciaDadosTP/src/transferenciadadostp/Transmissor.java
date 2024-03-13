package transferenciadadostp;

import java.util.Random;

public class Transmissor {

    private String mensagem;
    //Polinômio fixado
    private boolean[] polinomio = {true,false,false,true,true};
    
    public Transmissor(String mensagem) {
        this.mensagem = mensagem;
    }

    //convertendo um símbolo para "vetor" de boolean (bits)
    public boolean[] streamCaracter(char simbolo) {

        //cada símbolo da tabela ASCII é representado com 8 bits
        boolean bits[] = new boolean[8];

        //convertendo um char para int (encontramos o valor do mesmo na tabela ASCII)
        int valorSimbolo = (int) simbolo;
        int indice = 7;

        //convertendo cada "bits" do valor da tabela ASCII
        while (valorSimbolo >= 2) {
            int resto = valorSimbolo % 2;
            valorSimbolo /= 2;
            bits[indice] = (resto == 1);
            indice--;
        }
        bits[indice] = (valorSimbolo == 1);

        return bits;
    }

    //não modifique (seu objetivo é corrigir esse erro gerado no receptor)
    private void geradorRuido(boolean bits[]) {
        Random geradorAleatorio = new Random();

        //pode gerar um erro ou não..
        if (geradorAleatorio.nextInt(5) > 1) {
            int indice = geradorAleatorio.nextInt(8);
            bits[indice] = !bits[indice];
        }
    }

    //concatena o dado com 4 zeros
    private boolean[] dadoBitsCRC(boolean bits[]) {

        //Polinômio gerador 1 0 0 1 1
        //Novo dado (8 bits) com 0's adicionais
        boolean novoBits[] = new boolean[bits.length + polinomio.length - 1];
        //Completando o dado
        for (int i = 0; i < novoBits.length; i++) {
            //transfere o dado do parametro e add 4 zeros
            if (i < bits.length) {
                novoBits[i] = bits[i]; //(i<8)
            } else {
                novoBits[i] = false;
            }
        }

        return mensagemBitsCRC(novoBits);
    }

    //calcula o CRC e concatena com o dado
    private boolean[] mensagemBitsCRC(boolean bits[]) {
        //XOR
        boolean novosBits[] = bits.clone();

        //Divisão XOR para obter o codigo CRC
        for (int i = 0; i < bits.length - polinomio.length + 1; i++) {

            //Se o bit for 0 pule para o próximo.
            //Senao, faz o XOR com o polinomio
            if (bits[i] == true) {
                int k = 0;
                for (int j = i; j < (i + polinomio.length); j++) {
                    bits[j] = !(bits[j] == polinomio[k]);
                    k++;
                }
            }
        }
        //concatena o dado com o codigo CRC calculado
        for (int i = (bits.length - polinomio.length + 1); i < bits.length; i++) {
            novosBits[i] = bits[i];
        }

        return novosBits;
    }

    public void enviaDado(Receptor receptor) {

        for (int i = 0; i < this.mensagem.length(); i++) {
            boolean bits[] = streamCaracter(this.mensagem.charAt(i));

            boolean bitsCRC[] = dadoBitsCRC(bits);
            //Bits finais para o receptor
            geradorRuido(bitsCRC);

            //add ruidos na mensagem a ser enviada para o receptor
            //enviando a mensagem "pela rede" para o receptor (uma forma de testarmos esse método)

            if (receptor.receberDadoBits(bitsCRC) == false) {
                i = i - 1;
            }

            //o que faremos com o indicador quando houver algum erro? qual ação vamos tomar com o retorno do receptor
        }
    }

}
