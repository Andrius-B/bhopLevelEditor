package com.bhop.editor.Operations;

/**
 * Created by Andrius on 6/25/2017.
 */

public abstract class Operation {
    /**
     * Class defines the basic substance of an operations these include:
     * Select
     * Add/Remove
     * Move
     * Scale
     * Modify(the parameters of an object)
     */


    /**
     * In principle three objects are stored
     * the stored is the originial's copy,
     * original is the reference to the object that is about to be changed
     * and the changed is the proposed changes
     */
    protected java.lang.Object stored;
    protected java.lang.Object original;
    protected java.lang.Object changed;

    public void setOperation(java.lang.Object original, java.lang.Object changed){
        this.stored = original;
        this.original = original;
        this.changed = changed;
    }
    abstract public void applyOperation();
    abstract public void undoOperation();
    public Object getOriginal(){
        return original;
    }

}
