package rr;
import java.util.Scanner;

import javax.swing.JOptionPane;

import java.io.InvalidClassException;
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

        int tempo = 0, contadorQuantum = 0, i = 0, trocaDeContexto = TC;
        
        do{

            List<Processo> aRemover = new ArrayList<>();
            for(Processo processo : novos){
                System.out.println("Entrou no ngc de jogar processos na espera...");
                //input.nextLine();

                if(processo.getIngresso() == tempo){//sim
                    esperando.add(processo);
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
                    //continue
                }
            }

            else if(isExecuting()){
                System.out.print(RED_BOLD_BRIGHT+"EXECUTANDO processo");
                //input.nextLine();
                Processo finalizado = null;

                for(Processo processo : esperando){

                    if(processo.isOn()){
                        System.out.println(+processo.getNumero()+"..."+RESET);

                        try{
                            processo.executar();

                            if(--contadorQuantum==0){

                                if(processo.isFinished())  
                                    throw new InvalidClassException(null);
                                else if(fila.size()==1){
                                    contadorQuantum = QUANTUM;
                                }
                                else {
                                    processo.off();
                                    fila.add(fila.poll());
                                    execOff();
                                    System.out.println(GREEN_BOLD_BRIGHT+"O tempo de execuaco restante do processo é "+(processo.getDuracao()-processo.getExecucao())+RESET);
                                    swappOn();
                                    trocaDeContexto = TC;
                                }

                                
                                
                            }
                        }
                        catch(InvalidClassException e){
                            finalizado = processo;
                            processo.off();
                            execOff();
                            swappOn();
                            trocaDeContexto = TC;
                            terminados.add(fila.poll());
                            System.out.println(YELLOW_BOLD_BRIGHT+"Já cabou..."+RESET);
                            // if(!fila.isEmpty()){
                            //     contadorQuantum = QUANTUM;
                            //         fila.peek().on();
                            //         continue;
                            // }
                        }
                    }
                    else 
                        processo.esperar();   
                }
                if(Optional.ofNullable(finalizado).isPresent()){
                    esperando.remove(finalizado);
                }
            }

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
            System.out.println("\n==========================");
            System.out.println("Já tô na "+i+" volta..."); i++;
            System.out.println("Já tô no tempo "+tempo+"...");
            System.out.println("Quantum = "+contadorQuantum+"...");
            System.out.println("Troca de contexto = "+trocaDeContexto+"...");
            System.out.println("==========================\n");
            tempo++;

        }while (listaDeProcessos.size() > terminados.size());
        System.out.println("Tempo de execução: "+tempo);
        input.close();
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
        //             lista.add(new Processo(duracao,tempoDeIngresso));
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
        quantum = 20;
        trocaDeContexto = 5;
        lista.add(new Processo(1,40,4));
        lista.add(new Processo(2,20,1));
        lista.add(new Processo(3,50,3));
        lista.add(new Processo(4,30,0));

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