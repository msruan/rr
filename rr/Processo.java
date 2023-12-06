package rr;

public class Processo {
    private int tempoDeEspera;
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
    public int getTermino(){
        return _horaDoTermino;
    }

    //Todo: ver se isso aqui não tá lançando nenhuma exceção e tals
    public int getVida2(){
        return _horaDoTermino - tempoDeIngresso;
    }
    public int getEspera2(){
        return getVida2() - duracao;
    }
    public void setTermino(int horaDoTermino){
        this._horaDoTermino = horaDoTermino;
    }


    public Integer getNumero(){
        return numero;
    }
    public int getDuracao(){
        return duracao;
    }

    public void executar( ){
        if(isFinished()){
            System.out.println("tentou executar quadno nao devia...");
            System.out.flush();
            System.exit(1);
        }
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