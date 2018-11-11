/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchain.Exceptions;

/**
 * Swing Worker sem possibilidade de ser reutilizada
 * @author Tania
 */
public class SwingWorkerNotReusableException extends Exception {

    /**
     * Creates a new instance of <code>SwingWorkerNotReusableException</code>
     * without detail message.
     */
    public SwingWorkerNotReusableException() {
    }

    /**
     * Constructs an instance of <code>SwingWorkerNotReusableException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SwingWorkerNotReusableException(String msg) {
        super(msg);
    }
}
