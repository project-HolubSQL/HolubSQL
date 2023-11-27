package com.designpattern.database;

public interface TableVisitor {

    void visit(UnmodifiableTable table);

    void visit(ConcreteTable table);
}
