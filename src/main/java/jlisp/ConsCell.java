package jlisp;

public class ConsCell {
    Object car;
    Object cdr;

    ConsCell(Object car, Object cdr){
        this.car = car;
        this.cdr = cdr;
    }

    public String toString(){
        return("(" + Interpreter.stringify(car) + " . " + Interpreter.stringify(cdr) + ")");
    }
}