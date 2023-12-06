
package rr;
import java.util.Scanner;

import javax.swing.JOptionPane;

//import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import static rr.ConsoleColors.*;
public class Main {
 
    private final int QUANTUM;
    private final int TC;
    private boolean _swappingContext = false;
    private boolean _executing = false;
    private List<Processo> listaDeProcessos;

    public Main(int quantum, int trocaDeContexto, List<Processo> lista){
        QUANTUM = quantum;
        TC = trocaDeContexto;
        listaDeProcessos = lista.stream().sorted((h1,h2) -> h1.getIngresso().compareTo(h2.getIngresso())).toList();
    }

    public boolean isExecuting(){
        return _executing;
    }

    public void execOn() {
        _executing = true;
    }

    public void execOff() {
        _executing = false;
    }

    public void swappOn(){
        _swappingContext = true;
    }

    public void swappOff(){
        _swappingContext = false;
    }

    public boolean isSwappingContext(){
        return _swappingContext;
    }

    public void executar(){
        
        Scanner input = new Scanner(System.in);

        List<Processo> novos = new ArrayList<>(listaDeProcessos);
        List<Processo> esperando = new ArrayList<>();
        List<Processo> terminados = new ArrayList<>();
        
        Queue<Processo> fila = new LinkedList<>();

        int tempo = 0, contadorQuantum = QUANTUM, trocaDeContexto = TC;
        
        while(listaDeProcessos.size() > terminados.size()){
            
            System.out.println("=============================");

            List<Processo> aRemover = new ArrayList<>();

            for(Processo processo : novos){
                System.out.println("Entrou no ngc de jogar processos na espera...");

                if(processo.getIngresso() == tempo || processo.getIngresso() == tempo +1){//sim
                    esperando.add(processo);
                    if(fila.isEmpty()){
                        processo.on();
                        execOn();
                    }
                    fila.add(processo); 
                    aRemover.add(processo);
                }
                else{
                    break;
                }
            }
            for(Processo removido : aRemover){
                novos.remove(removido);
            }

            if(isSwappingContext()){
                System.out.println(BLUE_BOLD_BRIGHT+"Entrou na troca de contexto..."+RESET);
                input.nextLine();

                for(Processo processo : esperando)
                    processo.esperar();    
                
                if(trocaDeContexto-- <= 0){
                    swappOff();
                    execOn();
                    if(!fila.isEmpty()){
                        contadorQuantum = QUANTUM;
                        fila.peek().on();
                        continue;
                    }
                }
            }

            else if(isExecuting()){

                System.out.print(RED_BOLD_BRIGHT+"EXECUTANDO processo {"+GREEN_BOLD_BRIGHT);
                //input.nextLine();
                Processo finalizado = null;

                for(Processo processo : esperando){

                    if(processo.isOn()){
                        System.out.println(+processo.getNumero()+"}..."+RESET);

                        --contadorQuantum;
                        processo.executar();

                        if(contadorQuantum==0 && !processo.isFinished()){//era if(--contadorQuantum)
                            
                            processo.off();
                            execOff();
                            swappOn();
                            trocaDeContexto = TC;
                            fila.add(fila.poll());
                            System.out.println(GREEN_BOLD_BRIGHT+"O tempo de execução restante do processo é "+(processo.getDuracao()-processo.getExecucao())+RESET);
                        }

                        else if(processo.isFinished()){
                            
                            processo.off();
                            execOff();
                            swappOn();
                            trocaDeContexto = TC;

                            finalizado = processo;
                            finalizado.setTermino(tempo+1);//Todo: será é tempo + 1?

                            terminados.add(fila.poll());

                            System.out.println(YELLOW_BOLD_BRIGHT+"Já cabou..."+RESET);
                        }
                            
                        
                    }
                    else 
                        processo.esperar();   
                }
                if(Optional.ofNullable(finalizado).isPresent()){
                    esperando.remove(finalizado);
                }
                //acho q daria pra remover esse aqui e deixar só tempo + 1, mas fodasse KKKKKKK
                // for(Processo processo : novos){
                //     System.out.println("Entrou no ngc de jogar processos na espera...");

                //     if(processo.getIngresso() == tempo){//sim
                //         esperando.add(processo);
                //         if(fila.isEmpty()){
                //             processo.on();
                //             execOn();
                //         }
                //         fila.add(processo); 
                //         aRemover.add(processo);
                //     }
                //     else{
                //         break;
                //     }
                //     }
                //     for(Processo removido : aRemover){
                //         novos.remove(removido);
                // }
            }

            //tirar isso dps
            

            else if(!fila.isEmpty()){
                System.out.println("Entrou na fila está vazia...");
                //input.nextLine();

                fila.peek().on();
                execOn();
                contadorQuantum = QUANTUM;
            }

            else{
                System.out.println("algo deu muito errado");
                System.exit(1);
            }

            System.out.println("Já tô no tempo "+(++tempo)+"...");
            System.out.println("Quantum = "+contadorQuantum+"...");
            System.out.println("Troca de contexto = "+trocaDeContexto+"...");
            System.out.print("Fila: {");
            for(Processo p : fila){
                System.out.printf(" P%d -> ",p.getNumero());
            }
            System.out.println("}");
            System.out.println("==========================\n");

        };
        System.out.println("Tempo de execução: "+tempo);
        input.close();
        int esperaTotal = 0, vidaTotal = 0;
        // for(Processo processo: listaDeProcessos){
        //     esperaTotal += processo.getEspera();
        //     vidaTotal += processo.getVida();
        // }
        // }System.out.println(CYAN_BOLD_BRIGHT+"Tempo de espera total: "+esperaTotal);
        // System.out.println("Tempo de espera médio: "+(((double)esperaTotal))/listaDeProcessos.size());
        // System.out.println("============================");
        // System.out.println("Tempo de vida total: "+vidaTotal);
        // System.out.println("Tempo de vida médio: "+(((double)vidaTotal))/listaDeProcessos.size()+RESET);
        // for(int j = 0; j< listaDeProcessos.size(); j ++){
        //     Processo processo = listaDeProcessos.get(j);
        //     System.out.printf("%dº processo: Vida-> {%d}, Espera-> {%d}\n",j+1,processo.getVida(),processo.getEspera());
        // }
        listaDeProcessos = listaDeProcessos.stream().sorted((h1,h2) -> h1.getNumero().compareTo(h2.getNumero())).toList();
        esperaTotal = 0; vidaTotal = 0;
        for(int j = 0; j< listaDeProcessos.size(); j ++){
            Processo processo = listaDeProcessos.get(j);
            System.out.printf("%dº processo: Vida-> {%d}, Espera-> {%d}, Ingresso-> {%d}, Termino-> {%d}\n",processo.getNumero(),processo.getVida2(),processo.getEspera2(),processo.getIngresso(),processo.getTermino());
            vidaTotal += processo.getVida2();
            esperaTotal += processo.getEspera2();
        }


        System.out.println(GREEN_BOLD_BRIGHT+"\n\n--------2---------------------------------------");
        System.out.println(CYAN_BOLD_BRIGHT+"Tempo de espera total: "+esperaTotal);
        System.err.printf("Tempo de espera médio: %f\n",(((double)esperaTotal))/listaDeProcessos.size());
        System.out.println("============================");
        System.out.println("Tempo de vida total: "+vidaTotal);
        System.err.printf("Tempo de vida médio: %f",(((double)vidaTotal))/listaDeProcessos.size());
        System.out.println(RESET);

    }
   

    public static void main(String[] args) {

        int numeroDeProcessos, quantum, trocaDeContexto;
        List<Processo> lista = new ArrayList<>();

        // while(true){
            
        //     try{
        //         numeroDeProcessos = (int)validarValor(JOptionPane.showInputDialog(null,"Digite quantos processos haverão: ", "Inicio", JOptionPane.QUESTION_MESSAGE));
        //         quantum = (int)validarValor(JOptionPane.showInputDialog(null,"Digite a duração do quantum: ", "Inicio", JOptionPane.QUESTION_MESSAGE));
        //         trocaDeContexto = (int)validarValor(JOptionPane.showInputDialog(null,"Digite a duração da troca de contexto: ", "Inicio", JOptionPane.QUESTION_MESSAGE));
        //         break;
        //     }
        //     catch(NumberFormatException e){
        //         JOptionPane.showMessageDialog(null, "Tenha certeza de digitar um número!", "Erro",JOptionPane.ERROR_MESSAGE);
        //     }
        //     catch(IllegalArgumentException e){
        //         JOptionPane.showMessageDialog(null, "Tenha certeza de não deixar nenhum campo em branco!", "Erro",JOptionPane.ERROR_MESSAGE);
        //     }
        //     catch(ValorInvalidoException e){
        //         JOptionPane.showMessageDialog(null, "Só é possível passar valores positivos!", "Erro",JOptionPane.ERROR_MESSAGE);
        //     }
        // }

        // for(int i = 0; i < numeroDeProcessos; i++) {

        //     while(true){

        //         try{
        //             int tempoDeIngresso = (int)validarValor(JOptionPane.showInputDialog(null,String.format("Digite o tempo de ingresso do %dº processo: ",i+1), "Processos", JOptionPane.QUESTION_MESSAGE));
        //             int duracao = (int)validarValor(JOptionPane.showInputDialog(null,String.format("Digite a duração do %dº processo",i+1), "Processos", JOptionPane.QUESTION_MESSAGE));
        //             lista.add(new Processo(i+1,duracao,tempoDeIngresso));
        //             break;
        //         }
        //         catch(NumberFormatException e){
        //             JOptionPane.showMessageDialog(null, "Tenha certeza de digitar um número!", "Erro",JOptionPane.ERROR_MESSAGE);
        //         }
        //         catch(IllegalArgumentException e){
        //             JOptionPane.showMessageDialog(null, "Tenha certeza de não deixar nenhum campo em branco!", "Erro",JOptionPane.ERROR_MESSAGE);
        //         }
        //         catch(ValorInvalidoException e){
        //             JOptionPane.showMessageDialog(null, "Só é possível passar valores positivos!", "Erro",JOptionPane.ERROR_MESSAGE);
        //         }
        //     }
            
        // };
        
        // lista.add(new Processo(1,20,0));
        // lista.add(new Processo(2,20,2));
        // lista.add(new Processo(3,40,1));
        // lista.add(new Processo(1,40,4));
        // quantum = 20;
        // trocaDeContexto = 5;

        // quantum = 20;
        // trocaDeContexto = 5; //-> CASO DE TESTE
        // lista.add(new Processo(1,40,4));
        // lista.add(new Processo(2,20,1));
        // lista.add(new Processo(3,50,3));
        // lista.add(new Processo(4,30,0));

        quantum = 15;
        trocaDeContexto = 4; //-> CASO DE TESTE
        lista.add(new Processo(1,10,5));
        lista.add(new Processo(2,30,15));
        lista.add(new Processo(3,20,10));
        lista.add(new Processo(4,40,0));

        // quantum = 2;
        // trocaDeContexto = 1;
        // lista.add(new Processo(1,10,0));
        // lista.add(new Processo(2,4,0));
        // lista.add(new Processo(3,3,0));

        Main main = new Main(quantum, trocaDeContexto, lista);
        main.executar();
    }
   

    public static double validarValor(String entrada) throws IllegalArgumentException, NumberFormatException{
        if(Optional.ofNullable(entrada).isEmpty() ||  entrada.trim().isBlank())
            throw new IllegalArgumentException();
        return Double.parseDouble(entrada);
    }
    
    class ValorInvalidoException extends RuntimeException {
        @Override
        public String getMessage() {
            return "Só é possível passar valores positivos para a operação!";
        }
    }
}