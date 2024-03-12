package transferenciadadostp;

import java.util.Random;

public class Transmissor {
    private String mensagem;

    public Transmissor(String mensagem) {
        this.mensagem = mensagem;
    }
    
    //convertendo um símbolo para "vetor" de boolean (bits)
    public boolean[] streamCaracter(char simbolo){
        
        //cada símbolo da tabela ASCII é representado com 8 bits
        boolean bits[] = new boolean[8];
        
        //convertendo um char para int (encontramos o valor do mesmo na tabela ASCII)
        int valorSimbolo = (int) simbolo;
        int indice = 7;
        
        //convertendo cada "bits" do valor da tabela ASCII
        while(valorSimbolo >= 2){
            int resto = valorSimbolo % 2;
            valorSimbolo /= 2;
            bits[indice] = (resto == 1);
            indice--;
        }
        bits[indice] = (valorSimbolo == 1);
        
        return bits;
    } 
    
    //não modifique (seu objetivo é corrigir esse erro gerado no receptor)
    private void geradorRuido(boolean bits[]){
        Random geradorAleatorio = new Random();
        
        //pode gerar um erro ou não..
        if(geradorAleatorio.nextInt(5) > 1){
            int indice = geradorAleatorio.nextInt(8);
            bits[indice] = !bits[indice];
        }
    }
    
    private boolean[] dadoBitsCRC(boolean bits[]){
        
        //Polinômio gerador 1 0 0 1 1
        boolean[] polinomio = {true,false,false,true,true};
        //Novo bit com 0's adicionais
        boolean novoBits[] = new boolean[bits.length+4];
        //Completando o bit
        for (int i = 0; i < novoBits.length; i++) {
            
            if(i<bits.length){
                novoBits[i] = bits[i];    
            }else{
                novoBits[i] = false;
            }
            System.out.print((novoBits[i]==true)?"1 ":"0 ");
            
        }
        System.out.println("");
        
        
        
        return mensagemBitsCRC(novoBits,polinomio);
    }
    private boolean[] mensagemBitsCRC(boolean bits[],boolean polinomio[]){
        //XOR
        boolean novosBits[] = new boolean[bits.length];
        for (int i = 0; i < bits.length; i++) {
            novosBits[i] = bits[i];
        }
        
        //Divisão XOR
        for (int i = 0; i < bits.length-4; i++) {
            
            //Se o bit for 0 pule para o próximo.
            if(bits[i]==false){
                continue;
                
            }else{
                int k = 0;
                for (int j = i; j < i+5; j++) {
                    // 0 1 1 1 1 1 1 0 0 0 0 0
                    //   1 0 0 1 1 
                    // 0 0 1 1 0 0 1
                    bits[j] = !(bits[j]==polinomio[k]);
                    k++;
                }
                
                
            }
            for (int j = 0; j < bits.length; j++) {
                System.out.print((bits[j]==true)?"1 ":"0 ");
            }
                        System.out.println("");

        }
        System.out.println("Teste");
        for (int i = bits.length-4; i < novosBits.length; i++) {
            novosBits[i] = bits[i];
            System.out.print(((novosBits[i]==true)?"1 ":"0 "));
        }
        System.out.println("");
        for (int j = 0; j < bits.length; j++) {
                System.out.print((novosBits[j]==true)?"1 ":"0 ");
            }
        
        return novosBits;
        
    }
    
    
    public void enviaDado(Receptor receptor){
        for(int i = 0; i < this.mensagem.length();i++){
            boolean bits[] = streamCaracter(this.mensagem.charAt(i));
            
            /*-------AQUI você deve adicionar os bits do códico CRC para contornar os problemas de ruidos
                        você pode modificar o método anterior também
                boolean bitsCRC[] = dadoBitsCRC(bits);
            */
            boolean bitsCRC[] = dadoBitsCRC(bits);
            //Bits finais para o receptor
//            geradorRuido(bits);

            //add ruidos na mensagem a ser enviada para o receptor
            
            //enviando a mensagem "pela rede" para o receptor (uma forma de testarmos esse método)
            boolean[] polinomio = {true,false,false,true,true};
            boolean indicadorCRC = receptor.receberDadoBits(bitsCRC,polinomio);
            //o que faremos com o indicador quando houver algum erro? qual ação vamos tomar com o retorno do receptor
            
            
        }
    }
}
