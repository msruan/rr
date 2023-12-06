package rr;

public class Processo {

    private int _horaDoTermino;
    private int numero;
    private int duracao;
    private int tempoDeExecucao = 0;
    private boolean isOn;
    private Integer tempoDeIngresso; 

    public Processo(int numero , int tempoDeIngresso, int duracao){
        
        this.numero = numero;
        this.duracao = duracao;
        this.tempoDeIngresso = tempoDeIngresso;
        this.isOn = false;
    }

    public Integer getIngresso(){
        return tempoDeIngresso;
    }

    public int getExecucao(){
        return tempoDeExecucao;
    }

    public int getTermino(){
        return _horaDoTermino;
    }

    public int getVida(){
        return _horaDoTermino - tempoDeIngresso;
    }

    public int getEspera(){
        return getVida() - duracao;
    }

    public Integer getNumero(){
        return numero;
    }

    public int getDuracao(){
        return duracao;
    }

    public void setTermino(int horaDoTermino){
        this._horaDoTermino = horaDoTermino;
    }

    public boolean isOn(){
        return isOn;
    }
    
    public boolean isFinished(){
        return tempoDeExecucao == duracao;
    }

    public void off(){
        isOn = false;
    }

    public void on(){
        isOn = true;
    }

    public void executar(){

        if(isFinished()){
            System.out.println("Tentou executar quando não deveria...");
            System.out.flush();
            System.exit(1);
        }
        tempoDeExecucao++;
    }

    public void esperar(){
    }
}
