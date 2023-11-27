package com.designpattern.database;

import java.util.*;

public class OrderByVisitor implements TableVisitor {
    private LinkedHashMap<String, Integer> orderingKeys;

    public OrderByVisitor(LinkedHashMap<String, Integer> orderingKeys) {
        this.orderingKeys = orderingKeys;
    }

    public void visit(ConcreteTable table) {
        LinkedList rows = table.getRowSet();

        rows.sort((o1, o2) -> {
            for (String key : orderingKeys.keySet()) {
                int diff;
                int i = table.indexOf(key);
                String a = ((Object[]) o1)[i].toString();
                String b = ((Object[]) o2)[i].toString();

                try {
                    diff = Integer.parseInt(a) - Integer.parseInt(b);
                } catch (Exception e) {
                    diff = a.compareTo(b);
                }

                if (diff != 0) {
                    return diff * orderingKeys.get(key);
                }
            }
            return 0;
        });
    }

    public void visit(UnmodifiableTable table) {
        visit((ConcreteTable) table.extract());
    }
}
