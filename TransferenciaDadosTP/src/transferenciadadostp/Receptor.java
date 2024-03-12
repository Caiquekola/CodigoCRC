package transferenciadadostp;

public class Receptor {

    //mensagem recebida pelo transmissor
    private String mensagem;

    public Receptor() {
        //mensagem vazia no inicio da execução
        this.mensagem = "";
    }

    public String getMensagem() {
        return mensagem;
    }

    private boolean decodificarDado(boolean bits[]) {

        int codigoAscii = 0;
        int expoente = bits.length - 1;

        //converntendo os "bits" para valor inteiro para então encontrar o valor tabela ASCII
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                codigoAscii += Math.pow(2, expoente);
            }
            expoente--;
        }

        //concatenando cada simbolo na mensagem original
        this.mensagem += (char) codigoAscii;

        //esse retorno precisa ser pensado... será que o dado sempre chega sem ruído???
        return true;
    }

    private boolean decodificarDadoCRC(boolean bits[], boolean polinomio[]) {
        boolean[] bitsDado = new boolean[8];

        boolean novosBits[] = new boolean[bits.length];
        for (int i = 0; i < bits.length; i++) {
            novosBits[i] = bits[i];
            if (bits.length < 8) {
                bitsDado[i] = bits[i];
            }
        }

        //Divisão XOR
        for (int i = 0; i < bits.length - 4; i++) {

            //Se o bit for 0 pule para o próximo.
            if (bits[i] == false) {
                continue;

            } else {
                int k = 0;
                for (int j = i; j < i + 5; j++) {
                    // 0 1 1 1 1 1 1 0 0 0 0 0
                    //   1 0 0 1 1 
                    // 0 0 1 1 0 0 1
                    bits[j] = !(bits[j] == polinomio[k]);
                    k++;
                }

            }
            for (int j = 0; j < bits.length; j++) {
                System.out.print((bits[j] == true) ? "1 " : "0 ");
            }
            System.out.println("");

        }
        System.out.println("Teste 2");
        boolean logico = true;
        for (int i = bits.length - 4; i < novosBits.length; i++) {
            novosBits[i] = bits[i];
            System.out.print(((novosBits[i] == true) ? "1 " : "0 "));
            if (novosBits[i] == true) {
                logico = false;
            }
        }
        System.out.println("");
        for (int j = 0; j < bits.length; j++) {
            System.out.print((novosBits[j] == true) ? "1 " : "0 ");
        }

        if (logico = true) {
            return decodificarDado(bitsDado);
        } else {
            return false;
        }

    }

    //recebe os dados do transmissor
    public boolean receberDadoBits(boolean bits[], boolean polinomio[]) {

        //aqui você deve trocar o médodo decofificarDado para decoficarDadoCRC (implemente!!)
        decodificarDadoCRC(bits, polinomio);
        

        //será que sempre teremos sucesso nessa recepção
        return true;
    }
}
