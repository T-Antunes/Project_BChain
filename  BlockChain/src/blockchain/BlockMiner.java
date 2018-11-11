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
        long  nounce;
        while(!this.gb.isMinerado(this.md) && !this.gb.isInterrupted()) {
            nounce=ThreadLocalRandom.current().nextLong();
            this.gb.pub(nounce);
            if(this.gb.testNounce(this.md, nounce)) {
                this.gb.setNounce(this.md, nounce);
            }
        }
    }    
}
