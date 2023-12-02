package rr;
import java.io.InvalidClassException;

public class Processo {
    private int numero;
    private int duracao;
    private int tempoDeExecucao = 0;
    private int tempoDeEspera = 0;
    private boolean isOn;
    private Integer tempoDeIngresso; 

    public Processo(int numero ,int duracao, int tempoDeIngresso){
        this.numero = numero;
        this.duracao = duracao;
        this.tempoDeIngresso = tempoDeIngresso;
        this.isOn = false;
    }
    public int getNumero(){
        return numero;
    }
    public int getDuracao(){
        return duracao;
    }

    public void executar( )throws InvalidClassException{
        if(isFinished())
            throw new InvalidClassException("Processo já encerrou!!!");
        tempoDeExecucao++;
    }

    public int getExecucao(){
        return tempoDeExecucao;
    }

    public void esperar(){
        tempoDeEspera++;
    }

    public boolean isFinished(){
        return tempoDeExecucao == duracao;
    }
        
    public int getEspera(){
        return tempoDeEspera;
    }

    public Integer getIngresso(){
        return tempoDeIngresso;
    }

    public void off(){
        isOn = false;
    }

    public void on(){
        isOn = true;
    }

    public boolean isOn(){
        return isOn;
    }
}