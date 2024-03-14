package transferenciadadostp;

public class Receptor {

    //mensagem recebida pelo transmissor
    private String mensagem;
    //Polinômio idêntico ao do transmissor
    private boolean[] polinomio = {true, false, false, true, true};

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

    private boolean decodificarDadoCRC(boolean bits[]) {

        boolean[] bitsDado = new boolean[8];
        for (int i = 0; i < bits.length - polinomio.length + 1; i++) {
            bitsDado[i] = bits[i];
        }
        //Divisão XOR
        for (int i = 0; i < bits.length - polinomio.length + 1; i++) {
            //Se o bit for 0 pule para o próximo.
            if (bits[i] == true) {
                int k = 0;
                for (int j = i; j < i + 5; j++) {
                    bits[j] = !(bits[j] == polinomio[k]);
                    k++;
                }

            }

        }
        //Para testar se o código crc no final é nulo ( 0 0 0 0 )
        boolean erroCRC = false;
        for (int i = bits.length - polinomio.length + 1; i < bits.length; i++) {
            if (bits[i] == true) {
                erroCRC = true;
                break;
            }
        }
        if (erroCRC == false) {
            return decodificarDado(bitsDado);
        } else {
            return false;
        }

    }

    //recebe os dados do transmissor
    public boolean receberDadoBits(boolean bits[]) {

        //aqui você deve trocar o médodo decofificarDado para decoficarDadoCRC (implemente!!)
        //será que sempre teremos sucesso nessa recepção
        return decodificarDadoCRC(bits);
    }
}
