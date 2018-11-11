/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchain;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Base64;

/**
 *
 * @author Tania
 */
public class Block implements Serializable{

    public final static String PRIMEIRO_HASH = "00";
    public final static String ALGORITMO_HASH = "SHA-256";
    private final Service dados;
    private final byte[] assinatura;
    private final String hash;
    private final String hashAntigo;
    private long nonce;
    private final String dificultyStartWith;
    private final int dificulty;
    private final String algHash;

    public Block(Service dados, byte[] assinatura, String hash, String hashAntigo, long nonce, String dificultyStartWith, int dificulty, String algHash) {
        this.dados = dados;
        this.assinatura = assinatura;
        this.hash = hash;
        this.hashAntigo = hashAntigo;
        this.nonce = nonce;
        this.dificultyStartWith = dificultyStartWith;
        this.dificulty = dificulty;
        this.algHash = algHash;
    }

    public static String calculateHash(MessageDigest md, byte[] dados, long nounce) {
        md.reset();
        md.update(dados);
        md.update(Utils.GenericUtils.longToBytes(nounce));
        return Base64.getEncoder().encodeToString(md.digest());
    }

    public static boolean testNonce(MessageDigest md, byte[] dados, long nounce, String dificStartString) {
        return Block.calculateHash(md, dados, nounce).startsWith(dificStartString);
    }

    private byte[] getBytes() {
        return Utils.GenericUtils.concatenarArray(
                Utils.GenericUtils.concatenarArray(this.dados.toBytes(), this.assinatura),
                Base64.getDecoder().decode(this.hashAntigo)
        );
    }

    public boolean testNonce(MessageDigest md, long nounce) {
        return Block.testNonce(md, this.getBytes(), nounce, this.dificultyStartWith);
    }
    
    public boolean testBlockInteg(MessageDigest md) {
        String temp = Block.calculateHash(md, this.getBytes(), this.nonce);
        return (temp.startsWith(this.dificultyStartWith) && temp.equals(this.hash));
    }
    
    public void breakInteg() {
        this.nonce=0;
    }
    

    //<editor-fold defaultstate="collapsed" desc="GETTERS">
    public Service getDados() {
        return dados;
    }

    /**
     * Devolve assinatura do serviço do bloco
     * @return assinatura do serviço do bloco
     */
    public byte[] getAssinatura() {
        return assinatura;
    }

    public String getHash() {
        return hash;
    }

    public String getHashAntigo() {
        return hashAntigo;
    }

    public long getNounce() {
        return nonce;
    }

    public String getDificultyStartWith() {
        return dificultyStartWith;
    }

    public int getDificulty() {
        return dificulty;
    }

    public String getAlgHash() {
        return algHash;

    }
    //</editor-fold>

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        
        stb.append("DADOS: ");
        stb.append(this.dados.toString());
        stb.append("\n");
        stb.append("ASSINATURA: ");
        stb.append(Base64.getEncoder().encodeToString(this.assinatura));
        stb.append("\n");
        stb.append("HASH: ");
        stb.append(this.hash);
        stb.append("\n");
        stb.append("HASH ANTIGO: ");
        stb.append(this.hashAntigo);
        stb.append("\n");
        
        return stb.toString();
    }

    
}
