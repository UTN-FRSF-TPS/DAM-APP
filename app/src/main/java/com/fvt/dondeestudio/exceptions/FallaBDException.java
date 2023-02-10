package com.fvt.dondeestudio.exceptions;

public class FallaBDException extends Exception{
    public FallaBDException() {
        super("Ocurrio un error al realizar la operacion. Intente mas tarde");
    }
}
