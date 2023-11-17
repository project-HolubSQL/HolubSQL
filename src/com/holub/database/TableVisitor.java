package com.holub.database;

public interface TableVisitor {

    void visit(UnmodifiableTable table);

    void visit(ConcreteTable table);
}
