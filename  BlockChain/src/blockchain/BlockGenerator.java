/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchain;

import blockchain.Exceptions.SwingWorkerNotReusableException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingWorker;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Tania
 */
public class BlockGenerator extends SwingWorker<Block, Long>{
    private final Service dados;
    private final byte[] assinatura;
    private final String hashAntigo;
    private long nounce;
    //----------------------
    private String hash;
    //----------------------
    private final String dificultyStartWith;
    private final int dificulty;
    private final String algHash;
    
    //swing worker
    
    private JTextComponent txtInfo;
    private BlockMiner[] mineiros;
    private boolean interrupted;
    
    

    public BlockGenerator(Service dados, byte[] assinatura, String hashAntigo, int dificulty,String algHash, JTextComponent txtInfo) {
        this(dados,assinatura,hashAntigo,dificulty,algHash);
        this.txtInfo = txtInfo;
    }
    
    
    public BlockGenerator(Service dados, byte[] assinatura, String hashAntigo, int dificulty,String algHash) {
        this.dados = dados;
        this.assinatura = assinatura;
        this.hashAntigo = hashAntigo;
        String tStrin="";
        for (int i = 0; i < dificulty; i++) {
            tStrin+="0";
        }
        this.dificultyStartWith=tStrin;
        this.dificulty = dificulty;
        this.algHash=algHash;
        
        this.hash="";
        this.nounce=0;
        this.txtInfo=null;
        this.mineiros=null;
    }
    
    private byte[] getBytes() {
        return
                Utils.GenericUtils.concatenateArray(Utils.GenericUtils.concatenateArray(this.dados.toBytes(), this.assinatura),
                        Base64.getDecoder().decode(this.hashAntigo)
                );
    }
    
    public boolean testNounce(MessageDigest md, long nounce) {
        return Block.testNonce(md, this.getBytes(), nounce,this.dificultyStartWith);
    }
   
    
    public boolean isMinerado(MessageDigest md) {
         String temp = Block.calculateHash(md, this.getBytes(), this.nounce);
         return (temp.startsWith(this.dificultyStartWith) && temp.equals(this.hash));
    }
    
    
    public synchronized void setNounce(MessageDigest md,long nounce) {
        if(!this.isMinerado(md)) {
            this.nounce=nounce;
            this.hash=Block.calculateHash(md, this.getBytes(), this.nounce);
        }
    }
    
    
    
    @Override
    protected Block doInBackground() throws Exception {
       if(this.mineiros!=null) throw new SwingWorkerNotReusableException();
       int numThrads = Runtime.getRuntime().availableProcessors();
       this.mineiros=new BlockMiner[numThrads];
       ExecutorService exe = Executors.newFixedThreadPool(numThrads);
        for (int i = 0; i < mineiros.length; i++) {
            mineiros[i]=new BlockMiner(this,MessageDigest.getInstance(this.algHash));
            exe.execute( mineiros[i]);
        }
        exe.shutdown();
        exe.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        
        
        if(this.isMinerado(MessageDigest.getInstance(this.algHash)))  return new Block(this.dados, this.assinatura, this.hash, this.hashAntigo, this.nounce, this.dificultyStartWith, this.dificulty,this.algHash);
        else throw new NullPointerException();
    }
    
    private void interrupt() {
        this.interrupted=true;
    }
    
    public boolean isInterrupted() {
        return this.interrupted;
    }

    @Override
    protected void done() {
        this.interrupt();
         if(txtInfo != null) {
            if(!this.isCancelled())txtInfo.setText("Mineração Concluida");
            else txtInfo.setText("Mineração cancelada");
        }
    }
    
    public void pub(long nounce) {
        publish(nounce);
    }

    @Override
    protected void process(List<Long> list) {
        if(txtInfo != null && !this.isInterrupted()) {
            txtInfo.setText(list.get(list.size()-1) + "");
        }
        list.clear();
    }
    
    
    
    
}
