/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchain;

import java.security.MessageDigest;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author tania
 */
public class BlockMiner extends Thread{
    
    private final BlockGenerator gb;
    private final MessageDigest md;

    public BlockMiner(BlockGenerator gb,MessageDigest md) {
        this.gb = gb;
        this.md = md;
    }

    @Override
    public void run() {
        long  nonce;
        while(!this.gb.isMinerado(this.md) && !this.gb.isInterrupted()) {
            nonce=ThreadLocalRandom.current().nextLong();
            this.gb.pub(nonce);
            if(this.gb.testNounce(this.md, nonce)) {
                this.gb.setNounce(this.md, nonce);
            }
        }
    }    
}
