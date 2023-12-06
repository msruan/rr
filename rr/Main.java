
package rr;
import java.util.Scanner;

import javax.swing.JOptionPane;
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
        //             lista.add(new Processo(i+1,tempoDeIngresso,duracao));
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

        quantum = 15;
        trocaDeContexto = 4; //-> CASO DE TESTE
        lista.add(new Processo(1,5,10));
        lista.add(new Processo(2,15,30));
        lista.add(new Processo(3,20,20));
        lista.add(new Processo(4,0,40));

        Main main = new Main(quantum, trocaDeContexto, lista);
        main.executar();
    }

    public void executar(){
        
        Scanner input = new Scanner(System.in);

        List<Processo> novos = new ArrayList<>(listaDeProcessos);
        List<Processo> esperando = new ArrayList<>();
        List<Processo> terminados = new ArrayList<>();
        
        Queue<Processo> fila = new LinkedList<>();

        int tempo = 0, contadorQuantum = QUANTUM, trocaDeContexto = TC;
        
        while(listaDeProcessos.size() > terminados.size()){
            
            System.out.println("╔═════════════════════════════════════════");

            List<Processo> aRemover = new ArrayList<>();

            for(Processo processo : novos){
                //System.out.println("Entrou no ngc de jogar processos na espera...");

                if(processo.getIngresso() == tempo){//sim
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
                
                //input.nextLine();

                for(Processo processo : esperando)
                    processo.esperar();    

                
                if(trocaDeContexto-- <= 0){
                    input.nextLine();

                    swappOff();
                    execOn();
                    if(!fila.isEmpty()){
                        contadorQuantum = QUANTUM;
                        fila.peek().on();
                        continue;
                    }
                }
                System.out.println(BLUE_BOLD_BRIGHT+"║ TROCA DE CONTEXTO..."+RESET);
                System.out.println("║ T.C. = "+(1+trocaDeContexto)+"...");


            }

            else if(isExecuting()){

                System.out.print(RED_BOLD_BRIGHT+"║ EXECUTANDO processo {"+GREEN_BOLD_BRIGHT);
                Processo finalizado = null;

                for(Processo processo : esperando){

                    if(processo.isOn()){
                        System.out.println(+processo.getNumero()+RED_BOLD_BRIGHT+"}..."+RESET);

                        --contadorQuantum;
                        processo.executar();

                        if(contadorQuantum==0 && !processo.isFinished()){//era if(--contadorQuantum)
                            
                            processo.off();
                            execOff();
                            swappOn();
                            fila.add(fila.poll());
                            System.out.println(GREEN_BOLD_BRIGHT+"║ O tempo de execução restante do processo é "+YELLOW_BOLD_BRIGHT+(processo.getDuracao()-processo.getExecucao())+RESET);
                            trocaDeContexto = (fila.size() == 1 ? 0 : TC);

                        }

                        else if(processo.isFinished()){
                            
                            processo.off();
                            execOff();
                            swappOn();
                            trocaDeContexto = TC;

                            finalizado = processo;
                            finalizado.setTermino(tempo+1);//Todo: será é tempo + 1?

                            terminados.add(fila.poll());

                            System.out.println(YELLOW_BOLD_BRIGHT+"║ Já cabou..."+RESET);
                        }    
                    
                    }
                    else 
                        processo.esperar();   
                }
                if(Optional.ofNullable(finalizado).isPresent()){
                    esperando.remove(finalizado);
                }
                //acho q daria pra remover esse aqui e deixar só tempo + 1, mas fodasse KKKKKKK
                for(Processo processo : novos){
                    //System.out.println("Entrou no ngc de jogar processos na espera...");

                    if(processo.getIngresso() == tempo){//sim
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
            System.out.println("║ Quantum = "+(1+contadorQuantum)+"...");
            }

            else if(!fila.isEmpty()){
                System.out.println("║ Entrou na fila está vazia...");
                //input.nextLine();

                fila.peek().on();
                execOn();
                contadorQuantum = QUANTUM;
            }

            System.out.println("║ Já está no tempo "+(++tempo)+"...");
            System.out.print("║ Fila: {");
            int i = 0;
            for(Processo p : fila){
                i++;
                System.out.printf(" P%d "+(i==fila.size() ? "\0" : "->"),p.getNumero());
            }
            System.out.println("}");
            System.out.println("╚═════════════════════════════════════════\n");

        };
        System.out.format(" %50s","Tempo de execução: "+BLACK_BOLD+tempo+RESET);
        System.out.println("\n");
        input.close();
        int esperaTotal = 0, vidaTotal = 0;
        
        listaDeProcessos = listaDeProcessos.stream().sorted((h1,h2) -> h1.getNumero().compareTo(h2.getNumero())).toList();
        esperaTotal = 0; vidaTotal = 0;
        System.out.print("╔════════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
        System.out.format("║%28s %28s %28s %28s %28s %n", "", "", "", "", "");
        System.out.format("║%28s %28s %28s %28s %28s %n", "", "", "", "", "");

        System.out.format("║%28s %28s %28s %28s %28s %n", RED_BOLD_BRIGHT+"Processo"+RESET, YELLOW_BOLD+"Vida"+RESET, GREEN_BOLD+"Espera"+RESET, BLUE_BOLD+"Ingresso"+RESET, CYAN+"Término"+RESET);
        System.out.format("║%28s %28s %28s %28s %28s %n", "", "", "", "", "");

        for(int j = 0; j< listaDeProcessos.size(); j ++){

            Processo processo = listaDeProcessos.get(j);
            System.out.format("║%28s %28s %28s %28s %28s %n", "P"+RED_BOLD_BRIGHT+processo.getNumero()+RESET,YELLOW_BOLD+processo.getVida()+RESET,GREEN_BOLD+processo.getEspera()+RESET,BLUE_BOLD+processo.getIngresso()+RESET,CYAN+processo.getTermino()+RESET);
            System.out.format("║%28s %28s %28s %28s %28s %n", "", "", "", "", "");
            vidaTotal += processo.getVida();
            esperaTotal += processo.getEspera();
        }
        System.out.println("║\n║");
        System.out.format("║%28s %28s %28s %28s %28s %n║\n║", RED_BOLD_BRIGHT+"TOTAL"+RESET,YELLOW_BOLD+vidaTotal+RESET,GREEN_BOLD+esperaTotal+RESET,BLUE_BOLD+""+RESET,CYAN+""+RESET);
        System.out.format("%28s %28s %28s %28s %28s %n║\n║", RED_BOLD_BRIGHT+"MÉDIO"+RESET,YELLOW_BOLD+String.format("%.2f",(((float)vidaTotal))/listaDeProcessos.size())+RESET,GREEN_BOLD+        String.format("%.2f",(((float)esperaTotal))/listaDeProcessos.size())
+RESET,BLUE_BOLD+""+RESET,CYAN+""+RESET);
        System.out.println("\n╚════════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
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