/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchain;

import blockchain.Exceptions.NotFirstBlockInChainException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Tania
 */
public abstract class  BlockChain implements Serializable{
    private CopyOnWriteArrayList<Block> blocks;
    private String algHash;

    public BlockChain(Block block) throws NotFirstBlockInChainException {
        if(!block.getHashAntigo().equals(Block.FIRST_HASH)) throw new NotFirstBlockInChainException();
        this.algHash=block.getAlgHash();
        this.blocks=new CopyOnWriteArrayList();
        this.blocks.add(block);
    }
    
    protected final void add(Block bl) {
        this.blocks.add(bl);
    }
    
    protected final int size() {
        return this.blocks.size();
    }
    
    protected final Block get(int index) {
        return this.blocks.get(index);
    }
    
    public final String getLastHash() {
        return this.blocks.get(this.blocks.size()-1).getHash();
    }
    
    protected final CopyOnWriteArrayList getListaClone() {
        return (CopyOnWriteArrayList) this.blocks.clone();
    }
    
    public abstract void adicionaBlock(Block bloco) throws Exception;
    
    public boolean verificaBlockChain() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(this.algHash);
        String hashAntigo=Block.FIRST_HASH;
        for (int i = 0; i < this.blocks.size(); i++) {
            Block blocK=this.blocks.get(i);
            if(!blocK.getHashAntigo().equals(hashAntigo)) return false;
            if(!blocK.testBlockInteg(md)) return false;
            hashAntigo=blocK.getHash();
        }
        return true;
    }
    
    public void repararInteg() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(this.algHash);
        String hashAntigo=Block.FIRST_HASH;
        CopyOnWriteArrayList arr = new CopyOnWriteArrayList();
        for (int i = 0; i < this.blocks.size(); i++) {
            Block bloco=this.blocks.get(i);
            if(!bloco.getHashAntigo().equals(hashAntigo) || !bloco.testBlockInteg(md)) {
                break;
            }
            arr.add(bloco);
            hashAntigo=bloco.getHash();
        }
        this.blocks=arr;
    }
    
    public boolean breakInteg(int index) {
        if(index < 1 || index >= this.blocks.size()) return false;
        this.blocks.get(index).breakInteg();
        return true;
    }
    
    @Override
    public String toString( ) {
        StringBuilder stb  = new StringBuilder();
        for (int i = 0; i < blocks.size(); i++) {
            stb.append("ID: ")
                .append(i)
                .append("\n")
                .append(blocks.get(i).toString())
                .append("------------------------------------------------------------------\n");
        }
        return stb.toString();
    }
    
    public void saveBlockChain() throws IOException {
        File f = new File("teste.1");
        if (!f.exists()) {
            f.createNewFile();
        }
        try (FileOutputStream fs = new FileOutputStream(f); ObjectOutputStream out = new ObjectOutputStream(fs)) {
            out.writeObject(this);
        }
    }

    public static BlockChain loadBlockChain() throws IOException, ClassNotFoundException {
        File f = new File("teste.1");
        if (!f.exists()) {
            return null;
        }
        BlockChain obj;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            obj = (BlockChain) in.readObject();
        }
        return obj;
    }
}
