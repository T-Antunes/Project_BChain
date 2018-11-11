/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchain.Exceptions;

/**
 *
 * @author Tania
 */
public class NotFirstBlockInChainException extends Exception {

    /**
     * Creates a new instance of <code>NotFirstBlockInChainException</code>
     * without detail message.
     */
    public NotFirstBlockInChainException() {
    }

    /**
     * Constructs an instance of <code>NotFirstBlockInChainException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public NotFirstBlockInChainException(String msg) {
        super(msg);
    }
}
